package at.fhhagenberg.sqe.ui.components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqelevator.Elevator;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorFloorManagerListView extends HBox {
    private static final Logger log = LoggerFactory.getLogger("EventHandlerLogging");
    public final List<Floor> floorList;
    private final FloorDetailContextMenu floorContextMenu;
    public final ElevatorListView listView;
    public final SimpleObjectProperty<Floor> selectedFloorProperty = new SimpleObjectProperty<>();

    public ElevatorFloorManagerListView(ElevatorListView elevatorList, IElevator control) throws RemoteException {
        listView = elevatorList;

        floorList = new ArrayList<>();
        for(var i = 0; i < control.getFloorNum(); ++i)
        {
            floorList.add(new Floor(listView, control, i));
        }

        ListView<Floor> floorListView = new ListView<>();
        selectedFloorProperty.bind(floorListView.getSelectionModel().selectedItemProperty());
        floorListView.getItems().addAll(floorList);
        getChildren().add(floorListView);

        floorContextMenu = new FloorDetailContextMenu();

        floorListView.setContextMenu(floorContextMenu);
        floorContextMenu.setOnShowing(e -> {
            var selectedFloor = floorListView.getSelectionModel().getSelectedItem();
            try {
                floorContextMenu.underService.setSelected(selectedFloor.isUnderService());
            } catch (RemoteException ex) {
                log.error("Failed to update \"is serviced\" flagFloor#{}\n{}", selectedFloor.floorId, ex.getMessage());
            }
        });

        floorContextMenu.underService.setOnAction(actionEvent -> {
            var selectedFloor = floorListView.getSelectionModel().getSelectedItem();
            try {
                if(floorContextMenu.underService.isSelected())
                {
                    selectedFloor.setUnderService();
                }
                else
                {
                    selectedFloor.unsetUnderService();
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        floorContextMenu.sendToThisFloor.setOnAction(event -> {
            Floor f = floorListView.getSelectionModel().getSelectedItem();
            Elevator e = elevatorList.getSelectedElevator();
            try {
                control.setTarget(e.elevatorNumber, f.floorId);
                log.debug("Set target {} for elevator {}", f.floorId, e.elevatorNumber);
            } catch (RemoteException ex) {
                log.error("Failed to set target for elevator {}\n{}.", e.elevatorNumber, ex.getMessage()); // TODO show error msg to user
            }
        });

    }
}
