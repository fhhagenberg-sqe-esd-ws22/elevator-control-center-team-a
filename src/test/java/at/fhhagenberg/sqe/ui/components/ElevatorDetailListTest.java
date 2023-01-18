package at.fhhagenberg.sqe.ui.components;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
    void testFreshDetailViewSpeed(FxRobot robot) {
        final String test = "speed";
        var box = getBox(robot, test);
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals(2, box.getChildren().size());
        assertEquals("Speed:  ", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewPayload(FxRobot robot) {
        final String test = "payload";
        var box = getBox(robot, test);
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals(2, box.getChildren().size());
        assertEquals("Payload:  ", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewDoorStatus(FxRobot robot) {
        final String test = "doorstatus";
        var box = getBox(robot, test);
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals(2, box.getChildren().size());
        assertEquals("Door status:  ", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewFloorpos(FxRobot robot) {
        final String test = "floorpos";
        var box = getBox(robot, test);
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals(2, box.getChildren().size());
        assertEquals("Current floor:  ", label.getText());
        assertEquals("", val.getText());
    }

    @Test
    void testFreshDetailViewTargetfloor(FxRobot robot) {
        final String test = "targetfloor";
        var box = getBox(robot, test);
        var val = getVal(robot, test);
        var label = getLabel(robot, test);

        assertEquals(2, box.getChildren().size());
        assertEquals("Target floor:  ", label.getText());
        assertEquals("", val.getText());
    }

    protected Label getLabel(FxRobot robot, String val) {
        String q = String.format("#detaillist > #%sbox #%slabel", val, val);
        return robot.lookup(q).queryAs(Label.class);
    }

    protected HBox getBox(FxRobot robot, String val) {
        String q = String.format("#detaillist > #%sbox", val);
        return robot.lookup(q).queryAs(HBox.class);
    }

    protected Label getVal(FxRobot robot, String val) {
        String q = String.format("#detaillist > #%sbox #%sval", val, val);
        return robot.lookup(q).queryAs(Label.class);
    }
}
