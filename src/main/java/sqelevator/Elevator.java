package sqelevator;

import javafx.beans.property.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqelevator.util.Direction;
import sqelevator.util.DoorStatus;

import java.util.HashSet;
import java.util.Set;

public class Elevator {
    public final SimpleObjectProperty<Direction> committedDirection = new SimpleObjectProperty<>();
    public final SimpleObjectProperty<DoorStatus> door = new SimpleObjectProperty<>();
    public final SimpleIntegerProperty acceleration = new SimpleIntegerProperty();
    public final SimpleBooleanProperty button = new SimpleBooleanProperty();
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
