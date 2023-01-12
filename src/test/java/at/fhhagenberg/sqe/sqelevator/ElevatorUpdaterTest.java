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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ElevatorUpdaterTest {

    @Mock
    IElevator control = null;

    @BeforeEach
    public void Setup() throws RemoteException {
        control = mock(IElevator.class);
        when(control.getFloorNum()).thenReturn(5);
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
    void testDoorStatusUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.OPEN.val).thenReturn(DoorStatus.CLOSED.val);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.door.getValue());
        updater.update();
        Assertions.assertEquals(DoorStatus.OPEN, e.door.getValue());
        updater.update();
        Assertions.assertEquals(DoorStatus.CLOSED, e.door.getValue());
    }

    @Test
    void testTargetFloorUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
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
    void testCommittedDirectionUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
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
    void testCurrentFloorUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
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

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
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

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorWeight(anyInt())).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentWeight.getValue());
        updater.update();
        Assertions.assertEquals(1, e.currentWeight.getValue());
        updater.update();
        Assertions.assertEquals(2, e.currentWeight.getValue());
    }

    @Test
    void testFloorButtonUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.CLOSED.val);
        when(control.getElevatorButton(anyInt(), anyInt())).thenReturn(true).thenReturn(false);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.buttonReq.getValue());
        updater.update();
        Assertions.assertEquals(true, e.buttonReq.getValue()[0]);
        updater.update();
        Assertions.assertEquals(false, e.buttonReq.getValue()[0]);
    }
}
