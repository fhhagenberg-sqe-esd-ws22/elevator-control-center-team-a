package at.fhhagenberg.sqe.ui.components;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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
    void testReturnsFirstElevatorIfNoneSelected(FxRobot robot) {
        var list = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class);
        ElevatorListView.ElevatorListItem firstElement = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class).elevatorList.getItems().get(0);
        ElevatorListView.ElevatorListItem secondElement = robot.lookup("#elevatorlist").queryAs(ElevatorListView.class).elevatorList.getItems().get(1);

        assertNull(list.currentElevatorProperty.get());

        robot.clickOn("#elevatorlist > #elevatorlistview #elevator_0", MouseButton.PRIMARY);
        assertEquals(firstElement.e, list.currentElevatorProperty.get().e);

        assertEquals("Elevator 0", firstElement.getText());
        assertEquals("Elevator 1", secondElement.getText());

        robot.clickOn("#elevatorlist > #elevatorlistview #elevator_1", MouseButton.PRIMARY);
        assertFalse(list.elevatorList.isEditable());
        assertEquals(secondElement.e, list.currentElevatorProperty.get().e);
    }
}
