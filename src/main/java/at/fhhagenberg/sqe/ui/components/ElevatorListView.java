package at.fhhagenberg.sqe.ui.components;

import javafx.beans.property.SimpleObjectProperty;
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
    public final SimpleObjectProperty<ElevatorListItem> currentElevatorProperty = new SimpleObjectProperty<>();
    public ElevatorListView(Collection<Elevator> elevators)
    {
        elevatorList = new ListView<>();
        elevatorList.setId("elevatorlistview");
        for (Elevator e : elevators) {
            ElevatorListItem item = new ElevatorListItem(e);
            item.setId(String.format("elevator_%d", e.elevatorNumber));
            elevatorList.getItems().addAll(item);
        }
        currentElevatorProperty.bind(elevatorList.getSelectionModel().selectedItemProperty());
        getChildren().add(elevatorList);
    }
}
