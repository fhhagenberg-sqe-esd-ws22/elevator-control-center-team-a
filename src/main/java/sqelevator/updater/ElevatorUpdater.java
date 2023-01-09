package sqelevator.updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqelevator.Elevator;
import sqelevator.IElevator;
import sqelevator.util.Direction;
import sqelevator.util.DoorStatus;

import java.rmi.RemoteException;
import java.sql.Date;
import java.time.Instant;
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
    public void Update() throws RemoteException {
        final long updateTick = control.getClockTick();
        LOG.debug("Staring update@{}", Date.from(Instant.ofEpochSecond(updateTick)));
        // VisitorPattern draus machen?
        elevator.acceleration.set(control.getElevatorAccel(elevator.elevatorNumber));
        elevator.targetFloor.set(control.getTarget(elevator.elevatorNumber));
        elevator.committedDirection.set(Direction.valueOf(control.getCommittedDirection(elevator.elevatorNumber)));
        elevator.door.set(DoorStatus.valueOf(control.getElevatorDoorStatus(elevator.elevatorNumber)));

        //
        elevator.currentSpeed.set(control.getElevatorSpeed(elevator.elevatorNumber));
        elevator.currentCapacity.set(control.getElevatorCapacity(elevator.elevatorNumber));
        elevator.currentWeight.set(control.getElevatorWeight(elevator.elevatorNumber));
        elevator.feetFromBase.set(control.getElevatorPosition(elevator.elevatorNumber));
        elevator.currentFloor.set(control.getElevatorFloor(elevator.elevatorNumber));

        //
        final int floorCount = control.getFloorNum();
        Set<Integer> serviceFloors = new HashSet<>();
        boolean[] buttonUp = new boolean[floorCount];
        boolean[] buttonDown = new boolean[floorCount];
        boolean[] buttonReq = new boolean[floorCount];
        for(int floor = 0; floor < floorCount; floor++) {
            if (control.getServicesFloors(elevator.elevatorNumber, floor)) {
                serviceFloors.add(floor);
            }

            buttonUp[floor] = control.getFloorButtonUp(floor);
            buttonDown[floor] = control.getFloorButtonDown(floor);
            buttonReq[floor] = control.getElevatorButton(elevator.elevatorNumber, floor);
        }

        elevator.serviceableFloors.set(serviceFloors);

        elevator.buttonUp.set(buttonUp);
        elevator.buttonDown.set(buttonDown);
        elevator.buttonReq.set(buttonReq);

        elevator.lastUpdate.setValue(updateTick);
        LOG.debug("Ended update@{}", Date.from(Instant.ofEpochSecond(control.getClockTick())));
    }
}
