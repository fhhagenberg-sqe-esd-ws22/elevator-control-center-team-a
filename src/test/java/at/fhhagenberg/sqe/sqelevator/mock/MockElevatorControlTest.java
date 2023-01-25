package at.fhhagenberg.sqe.sqelevator.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class MockElevatorControlTest {

    @Mock
    MockElevatorControl control = null;
    private final int ELEVATOR_COUNT = 3;
    private final int FLOOR_COUNT = 5;

    @BeforeEach
    public void SetUp() {
        control = new MockElevatorControl(ELEVATOR_COUNT, FLOOR_COUNT);
    }

    @Test
    void test() throws RemoteException {
        assertEquals(0L, control.getClockTick());
        assertEquals(1L, control.getClockTick());
    }

    @Test
    void testDefaults() throws RemoteException {
        assertEquals(ELEVATOR_COUNT, control.getElevatorNum());
        assertEquals(FLOOR_COUNT, control.getFloorNum());
    }

    @Test
    void testReachesTargetDestination() throws  RemoteException {
        int elevatorNumber = 0;
        int floorTarget = 3;

        control.setTarget(elevatorNumber, floorTarget);

        assertEquals(floorTarget, control.getTarget(elevatorNumber));
    }

    @Test
    void testServicesFloor() throws RemoteException {
        int elevatorNumber = 0;
        int floorTarget = 3;

        assertTrue(control.getServicesFloors(elevatorNumber, floorTarget));

        control.setServicesFloors(elevatorNumber, floorTarget, false);

        assertFalse(control.getServicesFloors(elevatorNumber, floorTarget));
    }

    @Test
    void testFloorHeightMath() throws RemoteException {
        int elevatorNumber = 0;
        int floorTarget = 3;

        assertEquals(0, control.getElevatorPosition(0));

        control.setTarget(elevatorNumber, floorTarget);
        assertEquals(21, control.getElevatorPosition(0));
    }
}
