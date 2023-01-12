package at.fhhagenberg.sqe.sqelevator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import sqelevator.Elevator;
import sqelevator.IElevator;
import sqelevator.updater.ElevatorUpdater;
import sqelevator.util.Direction;
import sqelevator.util.DoorStatus;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ElevatorUpdaterTest {

    @Mock
    IElevator control = null;

    final int mFloorCount = 5;

    @BeforeEach
    public void Setup() throws RemoteException {
        control = mock(IElevator.class);
        when(control.getFloorNum()).thenReturn(mFloorCount);
    }

    @Test
    void testCommittedDirectionUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getCommittedDirection(anyInt())).thenReturn(0).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.committedDirection.getValue());
        updater.update();
        Assertions.assertEquals(sqelevator.util.Direction.UP, e.committedDirection.getValue());
        updater.update();
        Assertions.assertEquals(sqelevator.util.Direction.DOWN, e.committedDirection.getValue());
        updater.update();
        Assertions.assertEquals(Direction.UNCOMMITTED, e.committedDirection.getValue());
    }

    @Test
    void testElevatorAccelUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorAccel(anyInt())).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.acceleration.getValue());
        updater.update();
        Assertions.assertEquals(1, e.acceleration.getValue());
        updater.update();
        Assertions.assertEquals(2, e.acceleration.getValue());
    }

    @Test
    void testElevatorButton() throws RemoteException {
        Elevator e = new Elevator(0);

        boolean firstReturn[] = new boolean[]{false, true, false, false, false};

        boolean secondReturn[] = new boolean[]{true, false, false, false, false};

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorButton(anyInt(), anyInt())).thenReturn(false).thenReturn(true).thenReturn(false);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.buttonReq.getValue());
        updater.update();
        Assertions.assertArrayEquals(firstReturn, e.buttonReq.getValue());
        when(control.getElevatorButton(anyInt(), anyInt())).thenReturn(true).thenReturn(false);
        updater.update();
        Assertions.assertArrayEquals(secondReturn, e.buttonReq.getValue());
    }

    @Test
    void testElevatorDoorStatus() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val).thenReturn(DoorStatus.OPEN.val).thenReturn(DoorStatus.OPENING.val).thenReturn(DoorStatus.CLOSING.val);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.door.getValue());
        updater.update();
        Assertions.assertEquals(DoorStatus.CLOSED, e.door.getValue());
        updater.update();
        Assertions.assertEquals(DoorStatus.OPEN, e.door.getValue());
        updater.update();
        Assertions.assertEquals(DoorStatus.OPENING, e.door.getValue());
        updater.update();
        Assertions.assertEquals(DoorStatus.CLOSING, e.door.getValue());
    }

    @Test
    void testElevatorFloor() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorFloor(anyInt())).thenReturn(0).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentFloor.getValue());
        updater.update();
        Assertions.assertEquals(0, e.currentFloor.getValue());
        updater.update();
        Assertions.assertEquals(1, e.currentFloor.getValue());
        updater.update();
        Assertions.assertEquals(2, e.currentFloor.getValue());
    }

    @Test
    void testElevatorPosition() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorPosition(anyInt())).thenReturn(0).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.feetFromBase.getValue());
        updater.update();
        Assertions.assertEquals(0, e.feetFromBase.getValue());
        updater.update();
        Assertions.assertEquals(1, e.feetFromBase.getValue());
        updater.update();
        Assertions.assertEquals(2, e.feetFromBase.getValue());
    }

    @Test
    void testElevatorSpeed() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorSpeed(anyInt())).thenReturn(0).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentSpeed.getValue());
        updater.update();
        Assertions.assertEquals(0, e.currentSpeed.getValue());
        updater.update();
        Assertions.assertEquals(1, e.currentSpeed.getValue());
        updater.update();
        Assertions.assertEquals(2, e.currentSpeed.getValue());
    }

    @Test
    void testElevatorWeight() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorWeight(anyInt())).thenReturn(0).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentWeight.getValue());
        updater.update();
        Assertions.assertEquals(0, e.currentWeight.getValue());
        updater.update();
        Assertions.assertEquals(1, e.currentWeight.getValue());
        updater.update();
        Assertions.assertEquals(2, e.currentWeight.getValue());
    }

    @Test
    void testElevatorCapacity() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorCapacity(anyInt())).thenReturn(17).thenReturn(18).thenReturn(19);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentCapacity.getValue());
        updater.update();
        Assertions.assertEquals(17, e.currentCapacity.getValue());
        updater.update();
        Assertions.assertEquals(18, e.currentCapacity.getValue());
        updater.update();
        Assertions.assertEquals(19, e.currentCapacity.getValue());
    }

    @Test
    void testFloorButtonDown() throws RemoteException {
        Elevator e = new Elevator(0);

        boolean firstReturn[] = new boolean[]{false, true, false, false, false};

        boolean secondReturn[] = new boolean[]{true, false, false, false, false};

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getFloorButtonDown(anyInt())).thenReturn(false).thenReturn(true).thenReturn(false);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.buttonDown.getValue());
        updater.update();
        Assertions.assertArrayEquals(firstReturn, e.buttonDown.getValue());
        when(control.getFloorButtonDown(anyInt())).thenReturn(true).thenReturn(false);
        updater.update();
        Assertions.assertArrayEquals(secondReturn, e.buttonDown.getValue());
    }

    @Test
    void testFloorButtonUp() throws RemoteException {
        Elevator e = new Elevator(0);

        boolean firstReturn[] = new boolean[]{false, true, false, false, false};

        boolean secondReturn[] = new boolean[]{true, false, false, false, false};

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getFloorButtonUp(anyInt())).thenReturn(false).thenReturn(true).thenReturn(false);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.buttonUp.getValue());
        updater.update();
        Assertions.assertArrayEquals(firstReturn, e.buttonUp.getValue());
        when(control.getFloorButtonUp(anyInt())).thenReturn(true).thenReturn(false);
        updater.update();
        Assertions.assertArrayEquals(secondReturn, e.buttonUp.getValue());
    }

    @Test
    void testFloorHeight() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getFloorHeight()).thenReturn(0).thenReturn(1).thenReturn(2);

        //ElevatorUpdater updater = new ElevatorUpdater(e, control);
        //Assertions.assertEquals(0, e.floorHeight);
        //updater.update();
        //Assertions.assertEquals(1, e.floorHeight);
        //updater.update();
        //Assertions.assertEquals(2, e.floorHeight);
        //updater.update();
        //Assertions.assertEquals(2, e.floorHeight);
    }

    @Test
    void testServicesFloor() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getServicesFloors(anyInt(), anyInt())).thenReturn(true).thenReturn(false);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(true, e.serviceableFloors.getValue().isEmpty());
        updater.update();
        Assertions.assertEquals(true, e.serviceableFloors.getValue().contains(0));
        Assertions.assertEquals(false, e.serviceableFloors.getValue().contains(1));
        when(control.getServicesFloors(anyInt(), anyInt())).thenReturn(false).thenReturn(false).thenReturn(true);
        updater.update();
        Assertions.assertEquals(true, e.serviceableFloors.getValue().contains(2));
        when(control.getServicesFloors(anyInt(), anyInt())).thenReturn(true).thenReturn(false).thenReturn(true);
        updater.update();
        Assertions.assertEquals(true, e.serviceableFloors.getValue().contains(0));
        Assertions.assertEquals(false, e.serviceableFloors.getValue().contains(1));
        Assertions.assertEquals(true, e.serviceableFloors.getValue().contains(2));
    }

    @Test
    void testTarget() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getTarget(anyInt())).thenReturn(0).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.targetFloor.getValue());
        updater.update();
        Assertions.assertEquals(0, e.targetFloor.getValue());
        updater.update();
        Assertions.assertEquals(1, e.targetFloor.getValue());
        updater.update();
        Assertions.assertEquals(2, e.targetFloor.getValue());
    }

    @Test
    void testSetCommitedDirection() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getCommittedDirection(anyInt()))
                .thenReturn(IElevator.ELEVATOR_DIRECTION_UP)
                .thenReturn(IElevator.ELEVATOR_DIRECTION_DOWN)
                .thenReturn(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.committedDirection.getValue());
        updater.update();
        Assertions.assertEquals(Direction.UP, e.committedDirection.getValue());
        updater.update();
        Assertions.assertEquals(Direction.DOWN, e.committedDirection.getValue());
        updater.update();
        Assertions.assertEquals(Direction.UNCOMMITTED, e.committedDirection.getValue());
    }

    @Test
    void testSetServicesFloors() throws RemoteException {
        Elevator e = new Elevator(0);

        Set<Integer> emptySet = new HashSet<>();
        Set<Integer> servicedFloors = new HashSet<>();
        servicedFloors.add(2);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getServicesFloors(anyInt(), anyInt())).thenReturn(true).thenReturn(false);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(true, e.serviceableFloors.getValue().isEmpty());
        updater.update();
        Assertions.assertEquals(true, e.serviceableFloors.getValue().contains(0));
        updater.update();
        Assertions.assertEquals(false, e.serviceableFloors.getValue().contains(6));
        when(control.getServicesFloors(anyInt(), anyInt())).thenReturn(false).thenReturn(true).thenReturn(false);
        updater.update();
        Assertions.assertEquals(true, e.serviceableFloors.getValue().contains(1));
    }

    @Test
    void testSetTarget() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getTarget(anyInt())).thenReturn(0).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.targetFloor.getValue());
        updater.update();
        Assertions.assertEquals(0, e.targetFloor.getValue());
        updater.update();
        Assertions.assertEquals(1, e.targetFloor.getValue());
        updater.update();
        Assertions.assertEquals(2, e.targetFloor.getValue());
    }

    void testFloorButtonUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorButton(anyInt(), anyInt())).thenReturn(true).thenReturn(false);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.buttonReq.getValue());
        updater.update();
        Assertions.assertEquals(true, e.buttonReq.getValue()[0]);
        updater.update();
        Assertions.assertEquals(false, e.buttonReq.getValue()[0]);
    }

    @Test
    void testDoorStatusUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.OPEN.val).thenReturn(DoorStatus.CLOSED.val);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.door.getValue());
        updater.update();
        Assertions.assertEquals(DoorStatus.OPEN, e.door.getValue());
        updater.update();
        Assertions.assertEquals(DoorStatus.CLOSED, e.door.getValue());
    }

    @Test
    void testCapacityUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentCapacity.getValue());
        updater.update();
        Assertions.assertEquals(17, e.currentCapacity.getValue());
    }

    @Test
    void testTargetFloorUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getTarget(anyInt())).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.targetFloor.getValue());
        updater.update();
        Assertions.assertEquals(1, e.targetFloor.getValue());
        updater.update();
        Assertions.assertEquals(2, e.targetFloor.getValue());
    }

    @Test
    void testCurrentFloorUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorFloor(anyInt())).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentFloor.getValue());
        updater.update();
        Assertions.assertEquals(1, e.currentFloor.getValue());
        updater.update();
        Assertions.assertEquals(2, e.currentFloor.getValue());
    }

    @Test
    void testSpeedUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorSpeed(anyInt())).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentSpeed.getValue());
        updater.update();
        Assertions.assertEquals(1, e.currentSpeed.getValue());
        updater.update();
        Assertions.assertEquals(2, e.currentSpeed.getValue());
    }

    @Test
    void testWeightUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorWeight(anyInt())).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentWeight.getValue());
        updater.update();
        Assertions.assertEquals(1, e.currentWeight.getValue());
        updater.update();
        Assertions.assertEquals(2, e.currentWeight.getValue());
    }
}
