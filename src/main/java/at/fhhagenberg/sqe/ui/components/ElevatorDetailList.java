package at.fhhagenberg.sqe.ui.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ElevatorDetailList extends VBox{
    ElevatorListView listView;
    private CheckBox autoManual;
    private Label speed;
    private Label payload;
    private Label doorStatus;

    public ElevatorDetailList(ElevatorListView elevatorList){
        listView = elevatorList;
        var selectedElevator = listView.getSelectedElevator();

        Label speedLbl      = new Label("Speed:  ");
        Label speedVal      = new Label();
        HBox speedBox = new HBox(speedLbl, speedVal);
        Label payloadLbl    = new Label("Payload:  ");
        Label payloadVal    = new Label();
        HBox payloadBox = new HBox(payloadLbl, payloadVal);
        Label doorStatusLbl = new Label("Door status:  ");
        Label doorStatusVal = new Label();
        HBox doorStatusBox = new HBox(doorStatusLbl, doorStatusVal);

        autoManual = new CheckBox("Automatic mode"); //TODO: implement bind in ToggleSwitch


        speedVal.textProperty().bind(Bindings.convert(selectedElevator.currentSpeed));
        payloadVal.textProperty().bind(Bindings.convert(selectedElevator.currentWeight));
        doorStatusVal.textProperty().bind(Bindings.convert(selectedElevator.door));
        getChildren().addAll(autoManual, speedBox, payloadBox, doorStatusBox);
    }
}
