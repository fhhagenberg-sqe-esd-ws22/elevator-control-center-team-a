package at.fhhagenberg.sqe.ui.components;

import javafx.beans.property.SimpleObjectProperty;
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

    public static class FloorLabel extends Label {

        public final Floor f;
        FloorLabel(Floor f) {
            super(f.displayText());
            this.f = f;

            f.underserviceProperty.addListener((obs, oVal, nVal) -> disableProperty().set(!nVal));

            f.selectedElevator.addListener((obs, oVal, nVal) -> disableProperty().set(!f.underserviceProperty.get()));
        }

        @Override
        public String toString() {
            return f.displayText();
        }
    }
    private static final Logger log = LoggerFactory.getLogger("EventHandlerLogging");
    public final List<FloorLabel> floorList;
    public final ListView<FloorLabel> floorListView;
    public final FloorDetailContextMenu floorContextMenu;
    public final ElevatorListView listView;
    public final SimpleObjectProperty<FloorLabel> selectedFloorProperty = new SimpleObjectProperty<>();

    public ElevatorFloorManagerListView(ElevatorListView elevatorList, IElevator control) throws RemoteException {
        listView = elevatorList;

        floorList = new ArrayList<>();
        for(var i = 0; i < control.getFloorNum(); ++i)
        {
            Floor f = new Floor(listView, i);
            FloorLabel fl = new FloorLabel(f);
            fl.setId(String.format("floorlabel_%d", i));
            floorList.add(fl);
        }

        floorListView = new ListView<>();
        selectedFloorProperty.bind(floorListView.getSelectionModel().selectedItemProperty());
        floorListView.getItems().addAll(floorList);
        getChildren().add(floorListView);

        floorContextMenu = new FloorDetailContextMenu();

        floorListView.setContextMenu(floorContextMenu);
        floorContextMenu.setOnShowing(e -> {
            var selectedFloor = floorListView.getSelectionModel().getSelectedItem();
            if (selectedFloor == null) return; // TODO(cn): Show "No floor/elevator selected" to user
            floorContextMenu.underService.setSelected(selectedFloor.f.underserviceProperty.getValue());
            floorContextMenu.sendToThisFloor.setDisable(!selectedFloor.f.underserviceProperty.getValue());
        });

        floorContextMenu.underService.selectedProperty().addListener((observable, oldVal, newVal) -> {
            var selectedFloor = floorListView.getSelectionModel().getSelectedItem();
            var selectedElevator = elevatorList.currentElevatorProperty.get();
            if (selectedElevator == null) return; // TODO(cn): Show "No floor/elevator selected" to user
            if (selectedFloor == null) return;

            Elevator e = selectedElevator.e;
            try {
                selectedFloor.f.underserviceProperty.set(newVal);
                control.setServicesFloors(e.elevatorNumber, selectedFloor.f.floorId, newVal);
                log.debug("Updated service property for elevator={} and floor={} to val={}", e, selectedFloor.f, newVal);
            } catch (RemoteException ex) {
                log.error("Failed to update service property for elevator={} and floor={}\n{}", e, selectedFloor.f, ex.getMessage());
            }
        });
        floorContextMenu.sendToThisFloor.setOnAction(event -> {
            var selectedFloor = floorListView.getSelectionModel().getSelectedItem();
            var selectedElevator = elevatorList.currentElevatorProperty.get();
            if (selectedElevator == null) return; // TODO(cn): Show "No floor/elevator selected" to user
            if (selectedFloor == null) return;

            Elevator e = selectedElevator.e;
            Floor f = selectedFloor.f;
            if (e == null) return;
            try {
                control.setTarget(e.elevatorNumber, f.floorId);
                log.debug("Set target {} for elevator {}", f.floorId, e.elevatorNumber);
            } catch (RemoteException ex) {
                log.error("Failed to set target for elevator {}\n{}.", e.elevatorNumber, ex.getMessage()); // TODO show error msg to user
            }
        });

    }
}
