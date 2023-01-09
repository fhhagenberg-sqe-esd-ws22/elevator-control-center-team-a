package at.fhhagenberg.sqe.ui.components;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ElevatorDetailList extends VBox{
    private ToggleSwitch m_auto_manual;
    private Label m_speed;
    private Label m_payload;
    private Label m_door_status;

    public ElevatorDetailList(){
        m_auto_manual = new ToggleSwitch();
        m_speed = new Label("speed: ");
        m_payload = new Label("payload: ");
        m_door_status = new Label("door status: ");
        getChildren().addAll(m_auto_manual, m_speed, m_payload, m_door_status);
    }
}
