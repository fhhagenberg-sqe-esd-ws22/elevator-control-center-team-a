package at.fhhagenberg.sqe.sqelevator.mock;

import at.fhhagenberg.sqe.App;
import at.fhhagenberg.sqe.ui.ElevatorParams;
import at.fhhagenberg.sqe.ui.ParamUtils;
import sqelevator.IElevator;

public class MockApp extends App {
    private IElevator control = null;

    @Override
    protected synchronized IElevator getControl() {

        Parameters params = getParameters();

        ElevatorParams elevatorParams = ParamUtils.parseParams(params).orElseThrow(()
                -> new IllegalArgumentException("Usage: PROGRAM host:port [-bn BIND_NAME]"));

        if (control == null) {
            control = new MockElevatorControl(3 ,5);
        }
        return control;
    }
}
