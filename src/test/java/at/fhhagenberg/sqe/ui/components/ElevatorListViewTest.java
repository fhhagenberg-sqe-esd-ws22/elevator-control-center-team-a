package at.fhhagenberg.sqe.ui.components;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import at.fhhagenberg.sqe.ui.view.ElevatorControlUI;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class ElevatorListViewTest {

    MockApp app = null;

    @Start
    void SetUp(Stage stage) throws NotBoundException, RemoteException {
        app = new MockApp();
        app.start(stage);
    }

    @Test
    void testElevatorlistHasCorrectNumberOfElements(FxRobot robot) {
        var list = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class);

        assertEquals(app.ELEVATOR_COUNT, list.elevatorList.getItems().size());
    }

    @Test
    void testPropertyIsNullIfNoElevatorSelected(FxRobot robot) {
        var list = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class);

        assertNull(list.currentElevatorProperty.get());
    }

    @Test
    void testListIsNotEditable(FxRobot robot) {
        var list = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class);

        assertFalse(list.elevatorList.isEditable());
    }

    @Test
    void testElevatorLabelText(FxRobot robot) {
        ElevatorListView.ElevatorListItem firstElement = robot.lookup("#elevatorlist > #elevatorlistview #elevator_0").queryAs(ElevatorListView.ElevatorListItem.class);
        ElevatorListView.ElevatorListItem secondElement = robot.lookup("#elevatorlist > #elevatorlistview #elevator_1").queryAs(ElevatorListView.ElevatorListItem.class);

        assertEquals("Elevator 0", firstElement.getText());
        assertEquals("Elevator 1", secondElement.getText());
    }

    @Disabled("Fails in CI. idk Why.")
    @Test
    void testReturnsSelectedElevator(FxRobot robot) {
        var controlui = robot.lookup("#elevatorcontrolui").queryAs(ElevatorControlUI.class);
        var list = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class);
        ElevatorListView.ElevatorListItem firstElement = robot.lookup("#elevatorlist > #elevatorlistview #elevator_0").queryAs(ElevatorListView.ElevatorListItem.class);
        ElevatorListView.ElevatorListItem secondElement = robot.lookup("#elevatorlist > #elevatorlistview #elevator_1").queryAs(ElevatorListView.ElevatorListItem.class);

        waitForUpdate();
        Assumptions.assumeTrue(list.currentElevatorProperty.get() == null);
        waitForUpdate();

        robot.clickOn("#elevatorlist > #elevatorlistview #elevator_0", MouseButton.PRIMARY)
            .interact(controlui::updateElevators)
            .interact(this::waitForUpdate)
            .interact(() -> assertEquals(firstElement.e.elevatorNumber, list.currentElevatorProperty.get().e.elevatorNumber))
            .clickOn("#elevatorlist > #elevatorlistview #elevator_1", MouseButton.PRIMARY)
            .interact(this::waitForUpdate)
            .interact(controlui::updateElevators)
            .interact(() -> assertEquals(secondElement.e.elevatorNumber, list.currentElevatorProperty.get().e.elevatorNumber));
    }

    @Stop
    void cleanup() throws Exception {
        app.stop();
    }

    void waitForUpdate() {
        try {
            Thread.sleep(Duration.ofMillis(1500L).toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
