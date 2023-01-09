package sqelevator.updater;

import sqelevator.Elevator;
import sqelevator.IElevator;
import sqelevator.util.Direction;
import sqelevator.util.DoorStatus;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class ElevatorUpdater implements Updater {

    private final Elevator elevator;
    private final IElevator control;

    public ElevatorUpdater(Elevator elevator, IElevator control) {
        this.elevator = elevator;
        this.control = control;
    }
    @Override
    public void Update() throws RemoteException {
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

        //
        final int floorcount = control.getFloorNum();
        Set<Integer> serviceFloors = new HashSet<>();
        boolean[] buttonUp = new boolean[floorcount];
        boolean[] buttonDown = new boolean[floorcount];
        boolean[] buttonReq = new boolean[floorcount];
        for(int floor = 0; floor < floorcount; floor++) {
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
    }
}
