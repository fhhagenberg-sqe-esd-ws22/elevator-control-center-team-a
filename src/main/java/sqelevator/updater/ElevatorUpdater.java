package sqelevator.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqelevator.Elevator;
import sqelevator.IElevator;
import sqelevator.util.Direction;
import sqelevator.util.DoorStatus;

import java.rmi.RemoteException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ElevatorUpdater implements Updater {
    private static final Logger LOG = LoggerFactory.getLogger(ElevatorUpdater.class);

    private final Elevator elevator;
    private final IElevator control;

    public ElevatorUpdater(Elevator elevator, IElevator control) {
        this.elevator = elevator;
        this.control = control;
    }
    @Override
    public void update() throws RemoteException {
        final long updateTick = control.getClockTick();
        LOG.debug("Staring update@{}", Date.from(Instant.ofEpochSecond(updateTick)));
        // VisitorPattern draus machen?

        final var acceleration = control.getElevatorAccel(elevator.elevatorNumber);
        if (acceleration != elevator.acceleration.get()) {
            elevator.acceleration.set(acceleration);
        }

        final var targetFloor = control.getTarget(elevator.elevatorNumber);
        if (targetFloor != elevator.targetFloor.get()) {
            elevator.targetFloor.set(targetFloor);
        }

        final var commitedDirection = Direction.valueOf(control.getCommittedDirection(elevator.elevatorNumber));
        if (!commitedDirection.equals(elevator.committedDirection.get())) {
            elevator.committedDirection.set(commitedDirection);
        }

        final var door = DoorStatus.valueOf(control.getElevatorDoorStatus(elevator.elevatorNumber));
        if (!door.equals(elevator.door.get())) {
            elevator.door.set(door);
        }

        //
        elevator.currentSpeed.set(control.getElevatorSpeed(elevator.elevatorNumber));
        elevator.currentCapacity.set(control.getElevatorCapacity(elevator.elevatorNumber));
        elevator.currentWeight.set(control.getElevatorWeight(elevator.elevatorNumber));
        elevator.feetFromBase.set(control.getElevatorPosition(elevator.elevatorNumber));
        elevator.currentFloor.set(control.getElevatorFloor(elevator.elevatorNumber));

        //
        final int floorCount = control.getFloorNum();
        final Set<Integer> serviceFloors = new HashSet<>();
        final boolean[] buttonUp = new boolean[floorCount];
        final boolean[] buttonDown = new boolean[floorCount];
        final boolean[] buttonReq = new boolean[floorCount];
        for(int floor = 0; floor < floorCount; floor++) {
            if (control.getServicesFloors(elevator.elevatorNumber, floor)) {
                serviceFloors.add(floor);
            }

            buttonUp[floor] = control.getFloorButtonUp(floor);
            buttonDown[floor] = control.getFloorButtonDown(floor);
            buttonReq[floor] = control.getElevatorButton(elevator.elevatorNumber, floor);
        }

        if (!serviceFloors.equals(elevator.serviceableFloors.get())) {
            elevator.serviceableFloors.set(serviceFloors);
        }

        if (!Arrays.equals(buttonUp, elevator.buttonUp.get())) {
            elevator.buttonUp.set(buttonUp);
        }

        if (!Arrays.equals(buttonDown, elevator.buttonDown.get())) {
            elevator.buttonDown.set(buttonDown);
        }

        if (!Arrays.equals(buttonReq, elevator.buttonReq.get())) {
            elevator.buttonReq.set(buttonReq);
        }

        if (updateTick != elevator.lastUpdate.get()) {
            elevator.lastUpdate.setValue(updateTick);
        }
        LOG.debug("Ended update@{}", Date.from(Instant.ofEpochSecond(control.getClockTick())));
    }
}
