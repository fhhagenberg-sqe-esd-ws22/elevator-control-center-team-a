package at.fhhagenberg.sqe.ui.components;

import javafx.beans.property.SimpleBooleanProperty;

class Floor {
    public final int floorId;
    public final SimpleBooleanProperty underserviceProperty = new SimpleBooleanProperty(true);

    public Floor(ElevatorListView listview, int id)
    {
        floorId = id;

        listview.currentElevatorProperty.addListener((observable, oldValue, newValue) -> underserviceProperty.set(newValue.e.serviceableFloors.get().contains(floorId)));
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