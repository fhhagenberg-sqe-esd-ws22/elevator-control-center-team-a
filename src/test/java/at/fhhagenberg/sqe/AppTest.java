package at.fhhagenberg.sqe;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@Disabled
@ExtendWith(ApplicationExtension.class)
public class AppTest {
    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    public void start(Stage stage) throws NotBoundException, RemoteException {
        var app = new MockApp();
        app.start(stage);
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    public void testButtonWithText(FxRobot robot) {
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("Click me!"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    public void testButtonClick(FxRobot robot) {
        // when:
        robot.clickOn(".button");

        // or (lookup by css class):
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("Clicked!"));
    }
}