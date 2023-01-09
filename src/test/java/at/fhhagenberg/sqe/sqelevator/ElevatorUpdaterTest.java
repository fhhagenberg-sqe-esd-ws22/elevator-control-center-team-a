package at.fhhagenberg.sqe.sqelevator;

import org.junit.jupiter.api.Test;
import sqelevator.Elevator;
import sqelevator.IElevator;
import sqelevator.updater.ElevatorUpdater;

public class ElevatorUpdaterTest {

    @Test
    public void test() {
        Elevator elevator = new Elevator(0);

        IElevator elevatorService = new MockElevatorService();

        ElevatorUpdater elevatorUpdater = new ElevatorUpdater(elevator, elevatorService);
    }
}
