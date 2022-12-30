package sqelevator;

import javafx.beans.property.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqelevator.exceptions.NonExistentElevatorException;
import sqelevator.util.Direction;
import sqelevator.util.DoorStatus;

import java.rmi.RemoteException;
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
    private static long elevatorIdCounter = 0;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public Elevator(IElevator control) throws RemoteException {
        synchronized (Elevator.class) {
            this.elevatorId = Elevator.elevatorIdCounter++;
        }
        final int maxElevatorCount = control.getElevatorNum();
        if (maxElevatorCount >= elevatorId) {
            throw new NonExistentElevatorException("Tried to create elevator#%d, but there are only %d.", elevatorId, maxElevatorCount);
        }
    }

    public void servicesFloor(int floor, boolean service) {
        Set<Integer> val = serviceableFloors.getValue();
        if (service) {
            val.add(floor);
        } else {
            val.remove(floor);
        }
        serviceableFloors.setValue(val);
        LOG.debug("Elevator#{} | updated serviceFloors {}={}", elevatorId, floor, service);
    }
}
