package at.fhhagenberg.sqe.ui.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sqelevator.Elevator;

public class ElevatorDetailList extends VBox{
    private ElevatorListView listView;
    private Label speedVal      = new Label();
    private Label payloadVal    = new Label();
    private Label floorPosVal = new Label();
    private Label doorStatusVal = new Label();
    private Label targetFloorVal = new Label();
    private CheckBox autoManual;


    public ElevatorDetailList(ElevatorListView elevatorList){
        listView = elevatorList;
        var selectedElevator = listView.getSelectedElevator();

        Label speedLbl      = new Label("Speed:  ");
        HBox speedBox = new HBox(speedLbl, speedVal);
        Label payloadLbl    = new Label("Payload:  ");
        HBox payloadBox = new HBox(payloadLbl, payloadVal);
        Label doorStatusLbl = new Label("Door status:  ");
        HBox doorStatusBox = new HBox(doorStatusLbl, doorStatusVal);
        Label floorPosLbl = new Label("Current floor:  ");
        HBox floorPosBox = new HBox(floorPosLbl, floorPosVal);
        Label targetFloorLbl = new Label("Target floor:  ");
        HBox targetFloorBox = new HBox(targetFloorLbl, targetFloorVal);

        autoManual = new CheckBox("Automatic mode"); //TODO: implement bind in ToggleSwitch

        getChildren().addAll(autoManual, speedBox, payloadBox, doorStatusBox, floorPosBox, targetFloorBox);
        listView.setOnChangedFunction(this::updateBindings);
    }

    public Void updateBindings(Elevator elevator)
    {
        speedVal.textProperty().bind(Bindings.convert(elevator.currentSpeed));
        payloadVal.textProperty().bind(Bindings.convert(elevator.currentWeight));
        doorStatusVal.textProperty().bind(Bindings.convert(elevator.door));
        floorPosVal.textProperty().bind(Bindings.convert(elevator.currentFloor));
        targetFloorVal.textProperty().bind((Bindings.convert(elevator.targetFloor)));
        return null;
    }
}
