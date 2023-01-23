package at.fhhagenberg.sqe.ui.components;

import javafx.beans.property.*;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;
import javafx.beans.value.ObservableValue;
import sqelevator.Elevator;

public class Floor {
    public final int floorId;
    public final SimpleBooleanProperty underserviceProperty = new SimpleBooleanProperty(true);
    public final SimpleObjectProperty<ElevatorListView.ElevatorListItem> selectedElevator = new SimpleObjectProperty<>();
    public final SimpleStringProperty displayTextProperty = new SimpleStringProperty("");
    public final ObjectProperty<boolean[]> buttonUpReq = new SimpleObjectProperty<>();
    public final ObjectProperty<boolean[]> buttonDownReq = new SimpleObjectProperty<>();
    public final ObjectProperty<boolean[]> stopReq = new SimpleObjectProperty<>();

    private static String buildDisplayString(Floor f) {
        final boolean upReq = f.buttonUpReq.get() != null && f.buttonUpReq.get()[f.floorId];
        final boolean downReq = f.buttonDownReq.get() != null && f.buttonDownReq.get()[f.floorId];
        final boolean floorReq = f.stopReq.get() != null && f.stopReq.get()[f.floorId];

        return String.format("Floor %d     %s %s %s",
                f.floorId + 1,
                (upReq ? "^" : " "),
                (downReq ? "v" : " "),
                (floorReq ? "o" : " ")
                );
    }

    public Floor(ElevatorListView listview, int id)
    {
        floorId = id;
        selectedElevator.bind(listview.currentElevatorProperty);
        selectedElevator.addListener((observable, oldValue, newValue) -> {
            buttonUpReq.unbind();
            buttonDownReq.unbind();
            stopReq.unbind();

            buttonUpReq.bind(newValue.e.buttonUp);
            buttonDownReq.bind(newValue.e.buttonDown);
            stopReq.bind(newValue.e.buttonReq);

            underserviceProperty.set(newValue.e.serviceableFloors.get().contains(floorId));

            buttonDownReq.addListener(this::updateDisplayString);
            buttonUpReq.addListener(this::updateDisplayString);
            stopReq.addListener(this::updateDisplayString);

            //
            displayTextProperty.setValue(buildDisplayString(this));
        });
        displayTextProperty.setValue(buildDisplayString(this));
    }

    private void updateDisplayString(ObservableValue<? extends boolean[]> observable, boolean[] oldValue, boolean[] newValue) {
        displayTextProperty.setValue(buildDisplayString(this));
    }
    @Override
    public String toString()
    {
        return String.format("Floor#%d", floorId);
    }
}