package at.fhhagenberg.sqe;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import at.fhhagenberg.sqe.ui.components.ElevatorFloorManagerListView;
import at.fhhagenberg.sqe.ui.components.ElevatorListView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.stage.Stage;
import sqelevator.Elevator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.hamcrest.CoreMatchers.notNullValue;

@ExtendWith(ApplicationExtension.class)
class AppTest {
    MockApp app = null;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    void start(Stage stage) throws NotBoundException, RemoteException {
        app = new MockApp();
        app.start(stage);
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testElevatorListHasCorrectCountOfElements(FxRobot robot) {
        FxAssert.verifyThat("#elevatorlist", notNullValue());
        var elevators = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class);

        Assertions.assertEquals(app.ELEVATOR_COUNT, elevators.elevatorList.getItems().size());
    }

    @Test
    void testFloorListHasCorrectCountOfElements(FxRobot robot) {
        FxAssert.verifyThat("#floorlist", notNullValue());
        var floors = robot.lookup("#floorlist").queryAs(ElevatorFloorManagerListView.class);

        Assertions.assertEquals(app.FLOOR_COUNT, floors.floorList.size());
    }

    @Test
    void testElevatorText(FxRobot robot) {
        var elevatorList = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class);

        Elevator e = elevatorList.elevatorList.getItems().get(0).e;

        Assertions.assertEquals(String.format("Elevator#%d {%d}", e.elevatorNumber, e.elevatorId), e.toString());
        Assertions.assertEquals("Elevator 1", e.displayText());
    }

    @Test
    void testFloorListLabelText(FxRobot robot) {
        FxAssert.verifyThat("#floorlist", notNullValue());
        var floorLabel = robot.lookup("#floorlist #floorlabel_0").queryAs(ElevatorFloorManagerListView.FloorLabel.class);

        Assertions.assertEquals("Floor 1          ", floorLabel.toString());
        Assertions.assertEquals("Floor 1          ", floorLabel.f.displayTextProperty.getValue());
        Assertions.assertEquals("Floor#0", floorLabel.f.toString());
    }
}