package at.fhhagenberg.sqe.ui.components;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import sqelevator.Elevator;

import java.util.Collection;

public class ElevatorListView extends HBox {

    private final ListView<Elevator> elevatorList;
    public ElevatorListView(Collection<Elevator> elevators)
    {
        elevatorList = new ListView<>();
        elevatorList.getItems().addAll(elevators);

        getChildren().add(elevatorList);
    }

    public Elevator getSelectedElevator() {
        Elevator selected = elevatorList.getSelectionModel().getSelectedItem();
        if (selected != null) return selected;
        return elevatorList.getItems().get(0);
    }
}
