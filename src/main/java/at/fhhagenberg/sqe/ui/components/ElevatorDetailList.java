package at.fhhagenberg.sqe.ui.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sqelevator.Elevator;

public class ElevatorDetailList extends VBox{
    private ElevatorListView listView;
    private final Label speedVal = new Label();
    private final Label payloadVal = new Label();
    private final Label floorPosVal = new Label();
    private final Label doorStatusVal = new Label();
    private final Label targetFloorVal = new Label();
    private final CheckBox autoManual;


    public ElevatorDetailList(ElevatorListView elevatorList){
        listView = elevatorList;

        Label speedLbl = new Label("Speed:  ");
        speedLbl.setId("speedlabel");
        speedVal.setId("speedval");
        HBox speedBox = new HBox(speedLbl, speedVal);
        speedBox.setId("speedbox");

        Label payloadLbl = new Label("Payload:  ");
        payloadLbl.setId("payloadlabel");
        payloadVal.setId("payloadval");
        HBox payloadBox = new HBox(payloadLbl, payloadVal);
        payloadBox.setId("payloadbox");

        Label doorStatusLbl = new Label("Door status:  ");
        doorStatusLbl.setId("doorstatuslabel");
        doorStatusVal.setId("doorstatusval");
        HBox doorStatusBox = new HBox(doorStatusLbl, doorStatusVal);
        doorStatusBox.setId("doorstatusbox");

        Label floorPosLbl = new Label("Current floor:  ");
        floorPosLbl.setId("floorposlabel");
        floorPosVal.setId("floorposval");
        HBox floorPosBox = new HBox(floorPosLbl, floorPosVal);
        floorPosBox.setId("floorposbox");

        Label targetFloorLbl = new Label("Target floor:  ");
        targetFloorLbl.setId("targetfloorlabel");
        targetFloorVal.setId("targetfloorval");
        HBox targetFloorBox = new HBox(targetFloorLbl, targetFloorVal);
        targetFloorBox.setId("targetfloorbox");

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
