package at.fhhagenberg.sqe.sqelevator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import sqelevator.Elevator;
import sqelevator.IElevator;
import sqelevator.updater.ElevatorUpdater;
import sqelevator.util.DoorStatus;

import java.rmi.RemoteException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ElevatorUpdaterTest {

    @Test
    public void test() {
        Elevator elevator = new Elevator(0);

        IElevator elevatorService = new MockElevatorService();

        ElevatorUpdater elevatorUpdater = new ElevatorUpdater(elevator, elevatorService);
    }

    @Mock
    IElevator control = null;

    @BeforeEach
    public void Setup() {
        control = mock(MockElevatorService.class);
    }

    @Test
    public void testCapacityUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.Closed.val);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentCapacity.getValue());
        updater.Update();
        Assertions.assertEquals(17, e.currentCapacity.getValue());
    }

    @Test
    public void testDoorStatusUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.Open.val).thenReturn(DoorStatus.Closed.val);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.door.getValue());
        updater.Update();
        Assertions.assertEquals(DoorStatus.Open, e.door.getValue());
        updater.Update();
        Assertions.assertEquals(DoorStatus.Closed, e.door.getValue());
    }

    @Test
    public void testTargetFloorUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.Closed.val);
        when(control.getTarget(anyInt())).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.targetFloor.getValue());
        updater.Update();
        Assertions.assertEquals(1, e.targetFloor.getValue());
        updater.Update();
        Assertions.assertEquals(2, e.targetFloor.getValue());
    }

    @Test
    public void testCommittedDirectionUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.Closed.val);
        when(control.getCommittedDirection(anyInt())).thenReturn(0).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(null, e.committedDirection.getValue());
        updater.Update();
        Assertions.assertEquals(sqelevator.util.Direction.Up, e.committedDirection.getValue());
        updater.Update();
        Assertions.assertEquals(sqelevator.util.Direction.Down, e.committedDirection.getValue());
        updater.Update();
        Assertions.assertEquals(sqelevator.util.Direction.Uncommitted, e.committedDirection.getValue());
    }

    @Test
    public void testPositionUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.Closed.val);
        when(control.getElevatorPosition(anyInt())).thenReturn(0).thenReturn(1).thenReturn(2);

        //ElevatorUpdater updater = new ElevatorUpdater(e, control);
        //Assertions.assertEquals(0, e.position.getValue());
        //updater.Update();
        //Assertions.assertEquals(1, e.position.getValue());
        //updater.Update();
        //Assertions.assertEquals(2, e.position.getValue());
    }

    @Test
    public void testSpeedUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.Closed.val);
        when(control.getElevatorSpeed(anyInt())).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentSpeed.getValue());
        updater.Update();
        Assertions.assertEquals(1, e.currentSpeed.getValue());
        updater.Update();
        Assertions.assertEquals(2, e.currentSpeed.getValue());
    }

    @Test
    public void testWeightUpdate() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.Closed.val);
        when(control.getElevatorWeight(anyInt())).thenReturn(1).thenReturn(2);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentWeight.getValue());
        updater.Update();
        Assertions.assertEquals(1, e.currentWeight.getValue());
        updater.Update();
        Assertions.assertEquals(2, e.currentWeight.getValue());
    }
}
