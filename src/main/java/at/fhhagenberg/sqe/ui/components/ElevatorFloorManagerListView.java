package at.fhhagenberg.sqe.ui.components;

import javafx.scene.control.Label;
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

    static class FloorLabel extends Label {

        public final Floor f;
        FloorLabel(Floor f) {
            super(f.displayText());
            this.f = f;
        }

        @Override
        public String toString() {
            return f.displayText();
        }
    }
    private static final Logger log = LoggerFactory.getLogger("EventHandlerLogging");
    public final List<FloorLabel> floorList;
    private final FloorDetailContextMenu floorContextMenu;
    public final ElevatorListView listView;
    private final IElevator elevatorControl;

    public ElevatorFloorManagerListView(ElevatorListView elevatorList, IElevator control) throws RemoteException {
        listView = elevatorList;
        elevatorControl = control;
        floorList = new ArrayList<>();
        for(var i = 0; i < elevatorControl.getFloorNum(); ++i)
        {
            Floor f = new Floor(listView, elevatorControl, i);
            FloorLabel fl = new FloorLabel(f);
            fl.setId(String.format("floorlabel_%d", i));
            floorList.add(fl);
        }

        ListView<FloorLabel> floorListView = new ListView<>();
        floorListView.getItems().addAll(floorList);
        getChildren().add(floorListView);

        floorContextMenu = new FloorDetailContextMenu();

        floorListView.setContextMenu(floorContextMenu);
        floorContextMenu.setOnShowing(e -> {
            var selectedFloor = floorListView.getSelectionModel().getSelectedItem();
            try {
                floorContextMenu.underService.setSelected(selectedFloor.f.isUnderService());
            } catch (RemoteException ex) {
                log.error("Failed to update \"is serviced\" flagFloor#{}\n{}", selectedFloor.f.floorId, ex.getMessage());
            }
        });

        floorContextMenu.underService.setOnAction(actionEvent -> {
            var selectedFloor = floorListView.getSelectionModel().getSelectedItem();
            try {
                if(floorContextMenu.underService.isSelected())
                {
                    selectedFloor.f.setUnderService();
                }
                else
                {
                    selectedFloor.f.unsetUnderService();
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        floorContextMenu.sendToThisFloor.setOnAction(event -> {
            Floor f = floorListView.getSelectionModel().getSelectedItem().f;
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
