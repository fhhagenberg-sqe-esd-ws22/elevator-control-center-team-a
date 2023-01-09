package at.fhhagenberg.sqe.ui.components;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.Collection;
import java.util.List;

public class ElevatorListView extends HBox {
    private ListView<String> elevatorList;
    public ElevatorListView(Collection<String> elevators) //TODO: change String to Elevator Type
    {
        elevatorList = new ListView<>();
        elevatorList.getItems().addAll(elevators);
        getChildren().add(elevatorList);
    }

    public static String getSelectedElevator(){ //TODO: change String to Elevator Type
        return "Elevator 1";
    }
}
