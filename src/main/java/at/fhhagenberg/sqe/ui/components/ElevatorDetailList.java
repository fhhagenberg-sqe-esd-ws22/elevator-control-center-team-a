package at.fhhagenberg.sqe.ui.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class ElevatorDetailList extends VBox{
    ElevatorListView listView;
    private ToggleSwitch mAutoManual;
    private Label m_speed;
    private Label m_payload;
    private Label m_door_status;

    public ElevatorDetailList(ElevatorListView elevatorList){
        listView = elevatorList;
        var selectedElevator = listView.getSelectedElevator();
        mAutoManual = new ToggleSwitch(); //TODO: implement bind in ToggleSwitch
        m_speed = new Label("speed: ");

        m_speed.textProperty().bind(Bindings.convert(selectedElevator.currentSpeed));
        m_payload = new Label("payload: ");
        m_door_status = new Label("door status: ");

        getChildren().addAll(mAutoManual, m_speed, m_payload, m_door_status);
    }
}
