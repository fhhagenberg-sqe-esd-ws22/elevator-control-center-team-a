package at.fhhagenberg.sqe.ui.components;

import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import javafx.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqelevator.Elevator;
import sqelevator.IElevator;

import java.rmi.RemoteException;


public class ElevatorFloorManagerListView extends HBox {

    final private static Logger log = LoggerFactory.getLogger("EventHandlerLogging");
    private ListView<Floor> floorList;
    private FloorDetailContextMenu floorContextMenu;

    private final ElevatorListView listView;
    private final IElevator control;

    public ElevatorFloorManagerListView(IElevator control, ElevatorListView elevatorList, int numOfFloors)
    {
        this.control = control;
        listView = elevatorList;
        floorList = new ListView<>();
        floorContextMenu = new FloorDetailContextMenu();
        for (var i = 0; i < numOfFloors; ++i)
        {
            floorList.getItems().add(new Floor(i));
        }
        getChildren().add(floorList);
        floorList.setContextMenu(floorContextMenu);
        floorContextMenu.sendToThisFloor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Floor f = floorList.getSelectionModel().getSelectedItem();
                Elevator e = elevatorList.getSelectedElevator();
                try {
                    control.setTarget(e.elevatorNumber, f.number);
                    log.info("Set target {} for elevator {}", f.number, e.elevatorNumber);
                } catch (RemoteException ex) {
                    log.error("Failed to set target for elevator {}.", e.elevatorNumber);
                }
            }
        });
    }

}
