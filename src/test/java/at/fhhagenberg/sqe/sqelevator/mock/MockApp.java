package at.fhhagenberg.sqe.sqelevator.mock;

import at.fhhagenberg.sqe.App;
import sqelevator.IElevator;

public class MockApp extends App {
    public IElevator control = null;
    public final int ELEVATOR_COUNT = 3;
    public final int FLOOR_COUNT = 5;

    @Override
    protected synchronized IElevator getControl() {

        if (control == null) {
            control = new MockElevatorControl(ELEVATOR_COUNT ,FLOOR_COUNT);
        }
        return control;
    }
}
