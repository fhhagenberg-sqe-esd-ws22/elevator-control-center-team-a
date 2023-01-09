package sqelevator;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import sqelevator.util.Direction;
import sqelevator.util.DoorStatus;

import java.util.HashSet;
import java.util.Set;

public class Elevator {
    public final SimpleIntegerProperty lastUpdate = new SimpleIntegerProperty();
    public final SimpleObjectProperty<Direction> committedDirection = new SimpleObjectProperty<>();
    public final SimpleObjectProperty<DoorStatus> door = new SimpleObjectProperty<>();
    public final SimpleIntegerProperty acceleration = new SimpleIntegerProperty();
    public final SimpleObjectProperty<boolean[]> buttonUp = new SimpleObjectProperty<>();
    public final SimpleObjectProperty<boolean[]> buttonDown = new SimpleObjectProperty<>();
    public final SimpleObjectProperty<boolean[]> buttonReq = new SimpleObjectProperty<>();
    public final SimpleIntegerProperty feetFromBase = new SimpleIntegerProperty();
    public final SimpleIntegerProperty currentSpeed = new SimpleIntegerProperty();
    public final SimpleIntegerProperty currentCapacity = new SimpleIntegerProperty();
    public final SimpleIntegerProperty currentWeight = new SimpleIntegerProperty();
    public final SimpleIntegerProperty targetFloor = new SimpleIntegerProperty();
    public final SimpleObjectProperty<Set<Integer>> serviceableFloors = new SimpleObjectProperty<>(new HashSet<>());
    public final long elevatorId;
    public final int elevatorNumber;
    private static long elevatorIdCounter = 0;

    public Elevator(int elevatorNumber) {
        this.elevatorNumber = elevatorNumber;
        synchronized (Elevator.class) {
            this.elevatorId = Elevator.elevatorIdCounter++;
        }
    }
}
