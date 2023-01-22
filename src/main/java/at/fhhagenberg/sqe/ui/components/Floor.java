package at.fhhagenberg.sqe.ui.components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Floor {
    public final int floorId;
    public final SimpleBooleanProperty underserviceProperty = new SimpleBooleanProperty(true);
    public final SimpleObjectProperty<ElevatorListView.ElevatorListItem> selectedElevator = new SimpleObjectProperty<>();

    public Floor(ElevatorListView listview, int id)
    {
        floorId = id;
        selectedElevator.bind(listview.currentElevatorProperty);
        selectedElevator.addListener((observable, oldValue, newValue) -> underserviceProperty.set(newValue.e.serviceableFloors.get().contains(floorId)));
    }
    @Override
    public String toString()
    {
        return String.format("Floor#%d", floorId);
    }

    public String displayText() {
        return "Floor " + (floorId + 1);
    }
}