package at.fhhagenberg.sqe.ui.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sqelevator.Elevator;

public class ElevatorDetailList extends VBox{
    private final Label speedVal = new Label();
    private final Label payloadVal = new Label();
    private final Label doorStatusVal = new Label();
    private final Label targetFloorVal = new Label();
    private final Label directionVal = new Label();
    private final Label accelerationVal = new Label();
    private final Label feetFromBaseVal = new Label();
    private final Label currentCapacityVal = new Label();
    private final Label currentWeightVal = new Label();
    private final Label currentFloorVal = new Label();


    public ElevatorDetailList(ElevatorListView elevatorList){

        Label speedLbl = new Label("Speed:");
        speedLbl.setId("speedlabel");
        speedVal.setId("speedval");

        Label payloadLbl = new Label("Payload:");
        payloadLbl.setId("payloadlabel");
        payloadVal.setId("payloadval");

        Label doorStatusLbl = new Label("Door status:");
        doorStatusLbl.setId("doorstatuslabel");
        doorStatusVal.setId("doorstatusval");

        Label targetFloorLbl = new Label("Target floor:");
        targetFloorLbl.setId("targetfloorlabel");
        targetFloorVal.setId("targetfloorval");

        Label directionLbl = new Label("Committed direction:");
        directionLbl.setId("directionlabel");
        directionVal.setId("directionval");

        Label accelerationLbl = new Label("Acceleration:");
        accelerationLbl.setId("accelerationlabel");
        accelerationVal.setId("accelerationval");

        Label feetFromBaseLbl = new Label("Feet from base:");
        feetFromBaseLbl.setId("feetfrombaselabel");
        feetFromBaseVal.setId("feetfrombaseval");

        Label currentCapacityLbl = new Label("Current capacity:");
        currentCapacityLbl.setId("currentcapacitylabel");
        currentCapacityVal.setId("currentcapacityval");

        Label currentWeightLbl = new Label("Current weight:");
        currentWeightLbl.setId("currentweightlabel");
        currentWeightVal.setId("currentweightval");

        Label currentFloorLbl = new Label("Current floor:");
        currentFloorLbl.setId("currentfloorlabel");
        currentFloorVal.setId("currentfloorval");

        Label spaceHolder = new Label(" ");
        VBox leftBox = new VBox(
                speedLbl,
                payloadLbl,
                doorStatusLbl,
                targetFloorLbl,
                directionLbl,
                accelerationLbl,
                feetFromBaseLbl,
                currentCapacityLbl,
                currentWeightLbl,
                currentFloorLbl);
        leftBox.setId("leftbox");
        VBox rightBox = new VBox(
                speedVal,
                payloadVal,
                doorStatusVal,
                targetFloorVal,
                directionVal,
                accelerationVal,
                feetFromBaseVal,
                currentCapacityVal,
                currentWeightVal,
                currentFloorVal);
        rightBox.setId("rightbox");
        HBox detailBox = new HBox(leftBox, rightBox);
        detailBox.setId("detailbox");
        detailBox.setSpacing(20);


        CheckBox autoManual = new CheckBox("Automatic mode"); //TODO: implement bind in ToggleSwitch

        this.getChildren().addAll(autoManual, spaceHolder, detailBox);
        elevatorList.setOnChangedFunction(this::updateBindings);
    }

    public Void updateBindings(Elevator elevator)
    {
        speedVal.textProperty().bind(Bindings.convert(elevator.currentSpeed));
        payloadVal.textProperty().bind(Bindings.convert(elevator.currentWeight));
        doorStatusVal.textProperty().bind(Bindings.convert(elevator.door));
        targetFloorVal.textProperty().bind((Bindings.convert(elevator.targetFloor)));
        directionVal.textProperty().bind(Bindings.convert(elevator.committedDirection));
        accelerationVal.textProperty().bind(Bindings.convert(elevator.acceleration));
        feetFromBaseVal.textProperty().bind(Bindings.convert(elevator.feetFromBase));
        currentCapacityVal.textProperty().bind(Bindings.convert(elevator.currentCapacity));
        currentWeightVal.textProperty().bind(Bindings.convert(elevator.currentWeight));
        currentFloorVal.textProperty().bind(Bindings.convert(elevator.currentFloor));

        return null;
    }
}
