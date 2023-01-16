package at.fhhagenberg.sqe.ui.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import sqelevator.Elevator;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.Function;

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

    public void setOnChangedFunction(Function<Elevator, Void> func)
    {
        elevatorList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Elevator>() {
            @Override
            public void changed(ObservableValue<? extends Elevator> observableValue, Elevator oldVal, Elevator newVal) {
                func.apply(newVal);
            }
        });
    }
}
