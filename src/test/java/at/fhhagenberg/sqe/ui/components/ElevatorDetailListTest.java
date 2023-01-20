package at.fhhagenberg.sqe.ui.components;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class ElevatorDetailListTest {
    MockApp app = null;

    @Start
    void SetUp(Stage stage) throws NotBoundException, RemoteException {
        app = new MockApp();
        app.start(stage);
    }

    @Test
    void testLeftAndRightBox(FxRobot robot) {
        final String test = "speed";
        var lbox = getLeftBox(robot, test);
        var rbox = getLeftBox(robot, test);
        assertEquals(10, lbox.getChildren().size());
        assertEquals(10, rbox.getChildren().size());
    }

    @Test
    void testFreshDetailViewSpeed(FxRobot robot) {
        final String test = "speed";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Speed:", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewPayload(FxRobot robot) {
        final String test = "payload";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Payload:", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewDoorStatus(FxRobot robot) {
        final String test = "doorstatus";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Door status:", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewFloorpos(FxRobot robot) {
        final String test = "currentfloor";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Current floor:", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewTargetfloor(FxRobot robot) {
        final String test = "targetfloor";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Target floor:", label.getText());
        assertEquals("", val.getText());
    }

    @Test
void testFreshDetailViewCommittedDirection(FxRobot robot) {
        final String test = "direction";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Committed direction:", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewAccel(FxRobot robot) {
        final String test = "acceleration";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Acceleration:", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewFeetFromBase(FxRobot robot) {
        final String test = "feetfrombase";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Feet from base:", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewWeight(FxRobot robot) {
        final String test = "currentweight";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Current weight:", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewCapacity(FxRobot robot) {
        final String test = "currentcapacity";
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals("Current capacity:", label.getText());
        assertEquals("", val.getText());
    }


    protected Label getLabel(FxRobot robot, String val) {
        String q = String.format("#detaillist > #detailbox > #leftbox > #%slabel", val, val);
        return robot.lookup(q).queryAs(Label.class);
    }

    protected VBox getLeftBox(FxRobot robot, String val) {
        String q = String.format("#detaillist > #detailbox > #leftbox", val);
        return robot.lookup(q).queryAs(VBox.class);
    }

    protected VBox getRightBox(FxRobot robot, String val) {
        String q = String.format("#detaillist > #detailbox > #rightbox", val);
        return robot.lookup(q).queryAs(VBox.class);
    }

    protected Label getVal(FxRobot robot, String val) {
        String q = String.format("#detaillist > #detailbox > #rightbox > #%sval", val, val);
        return robot.lookup(q).queryAs(Label.class);
    }
}
