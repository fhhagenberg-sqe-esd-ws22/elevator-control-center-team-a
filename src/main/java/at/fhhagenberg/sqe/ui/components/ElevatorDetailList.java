package at.fhhagenberg.sqe.ui.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ElevatorDetailList extends VBox{
    ElevatorListView listView;
    private ToggleSwitch autoManual;
    private Label speed;
    private Label payload;
    private Label doorStatus;

    public ElevatorDetailList(ElevatorListView elevatorList){
        listView = elevatorList;
        var selectedElevator = listView.getSelectedElevator();
        autoManual = new ToggleSwitch(); //TODO: implement bind in ToggleSwitch
        speed = new Label("speed: ");
        payload = new Label("payload: ");
        doorStatus = new Label("door status: ");
        speed.textProperty().bind(Bindings.convert(selectedElevator.currentSpeed));
        payload.textProperty().bind(Bindings.convert(selectedElevator.currentWeight));
        doorStatus.textProperty().bind(Bindings.convert(selectedElevator.door));
        getChildren().addAll(autoManual, speed, payload, doorStatus);
    }
}
