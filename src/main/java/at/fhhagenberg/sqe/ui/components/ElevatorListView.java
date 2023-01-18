package at.fhhagenberg.sqe.ui.components;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import sqelevator.Elevator;

import java.util.Collection;
import java.util.function.Function;

public class ElevatorListView extends HBox {

    static class ElevatorListItem extends Label {
        public final Elevator e;
        public ElevatorListItem(Elevator e) {
            super(e.toString());
            this.e = e;
        }
    }

    public final ListView<ElevatorListItem> elevatorList;
    public ElevatorListView(Collection<Elevator> elevators)
    {
        elevatorList = new ListView<>();
        elevatorList.setId("elevatorlistview");
        for (Elevator e : elevators) {
            ElevatorListItem item = new ElevatorListItem(e);
            item.setId(String.format("elevator_%d", e.elevatorNumber));
            elevatorList.getItems().addAll(item);
        }

        getChildren().add(elevatorList);
    }

    public Elevator getSelectedElevator() {
        ElevatorListItem selected = elevatorList.getSelectionModel().getSelectedItem();
        if (selected != null) return selected.e;
        return elevatorList.getItems().get(0).e;
    }

    public void setOnChangedFunction(Function<Elevator, Void> func)
    {
        elevatorList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> func.apply(newVal.e));
    }
}
