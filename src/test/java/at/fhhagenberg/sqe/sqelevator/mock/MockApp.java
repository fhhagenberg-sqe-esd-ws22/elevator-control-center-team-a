package at.fhhagenberg.sqe.sqelevator.mock;

import at.fhhagenberg.sqe.App;
import sqelevator.IElevator;

public class MockApp extends App {
    private IElevator control = null;

    @Override
    protected synchronized IElevator getControl() {

        if (control == null) {
            control = new MockElevatorControl(3 ,5);
        }
        return control;
    }
}
