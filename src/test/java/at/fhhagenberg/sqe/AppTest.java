package at.fhhagenberg.sqe;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import at.fhhagenberg.sqe.ui.components.ElevatorFloorManagerListView;
import at.fhhagenberg.sqe.ui.components.ElevatorListView;
import org.junit.jupiter.api.Assertions;
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

import static org.hamcrest.CoreMatchers.notNullValue;

@ExtendWith(ApplicationExtension.class)
public class AppTest {
    MockApp app = null;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    public void start(Stage stage) throws NotBoundException, RemoteException {
        app = new MockApp();
        app.start(stage);
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    public void testElevatorListHasCorrectCountOfElements(FxRobot robot) {
        FxAssert.verifyThat("#elevatorlist", notNullValue());
        var elevators = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class);

        Assertions.assertEquals(app.ELEVATOR_COUNT, elevators.elevatorList.getItems().size());
    }

    @Test
    public void testFloorListHasCorrectCountOfElements(FxRobot robot) {
        FxAssert.verifyThat("#floorlist", notNullValue());
        var floors = robot.lookup("#floorlist").queryAs(ElevatorFloorManagerListView.class);

        Assertions.assertEquals(app.FLOOR_COUNT, floors.floorList.size());
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    @Disabled
    public void testButtonClick(FxRobot robot) {
        // when:
        robot.clickOn(".button");

        // or (lookup by css class):
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("Clicked!"));
    }
}