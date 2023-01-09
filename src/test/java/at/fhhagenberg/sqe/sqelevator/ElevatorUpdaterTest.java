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
    public void test2() throws RemoteException {
        Elevator e = new Elevator(0);

        when(control.getElevatorCapacity(anyInt())).thenReturn(17);
        when(control.getElevatorDoorStatus(anyInt())).thenReturn(DoorStatus.Closed.val);

        ElevatorUpdater updater = new ElevatorUpdater(e, control);
        Assertions.assertEquals(0, e.currentCapacity.getValue());
        updater.Update();
        Assertions.assertEquals(17, e.currentCapacity.getValue());
    }
}
