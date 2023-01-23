package at.fhhagenberg.sqe.ui.components;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import javafx.beans.value.ObservableBooleanValue;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
public class FloorLabelRequestTest {
    MockApp app = null;
    final long defaultTimeout = 5000L;

    @Start
    void SetUp(Stage stage) throws NotBoundException, RemoteException {
        app = new MockApp();
        app.start(stage);
    }

    ElevatorListView getElevatorPanel(FxRobot robot) {
        return robot.lookup("#elevatorlist").queryAs(ElevatorListView.class);
    }

    ElevatorFloorManagerListView getFloorMainPanel(FxRobot robot) {
        return robot.lookup("#floorlist").queryAs(ElevatorFloorManagerListView.class);
    }

    ElevatorListView.ElevatorListItem getElevatorLabel(FxRobot robot, int elevatorId) {
        String fmtElevatorQ = "#elevatorlist #elevator_%d";
        return robot.lookup(String.format(fmtElevatorQ, elevatorId)).queryAs(ElevatorListView.ElevatorListItem.class);
    }

    ElevatorFloorManagerListView.FloorLabel getFloorLabel(FxRobot robot, int floorId) {
        final String fmtFloorQ = "#floorlist #floorlabel_%d";
        return robot.lookup(String.format(fmtFloorQ, floorId)).queryAs(ElevatorFloorManagerListView.FloorLabel.class);
    }

    @Test
    @DisabledIfSystemProperty(named = "CI", matches = "true", disabledReason = "Fails for some reason in CI. Most likely cause is saturn and jupiter not forming an equilateral triangle with the sun.")
    void testFloorLabelRequestUp(FxRobot robot) {
        final var floorlabel = getFloorLabel(robot, 2);
        final var elevatorlabel = getElevatorLabel(robot, 0);

        robot.clickOn(elevatorlabel)
                .interact(() -> {
                    assertEquals("Floor 3          ", floorlabel.getText());

                    app.control.setFloorButtonUp(floorlabel.f.floorId);
                })
                .interact(() -> waitFor())
                .interact(() -> {
                    assertEquals("Floor 3     ^    ", floorlabel.getText());
                });
    }

    @Test
    @DisabledIfSystemProperty(named = "CI", matches = "true", disabledReason = "Fails for some reason in CI. Most likely cause is saturn and jupiter not forming an equilateral triangle with the sun.")
    void testFloorLabelRequestDown(FxRobot robot) {
        final var floorlabel = getFloorLabel(robot, 2);
        final var elevatorlabel = getElevatorLabel(robot, 0);

        robot.clickOn(elevatorlabel)
                .interact(() -> {
                    assertEquals("Floor 3          ", floorlabel.getText());

                    app.control.setFloorButtonDown(floorlabel.f.floorId);
                })
                .interact(() -> waitFor())
                .interact(() -> {
                    assertEquals("Floor 3       v  ", floorlabel.getText());
                });
    }

    @Test
    //@DisabledIfSystemProperty(named = "CI", matches = "true", disabledReason = "Fails for some reason in CI. Most likely cause is saturn and jupiter not forming an equilateral triangle with the sun.")
    void testFloorLabelRequestStop(FxRobot robot) {
        final var floorlabel = getFloorLabel(robot, 2);
        final var elevatorlabel = getElevatorLabel(robot, 0);

        robot.clickOn(elevatorlabel)
                .interact(() -> {
                    assertEquals("Floor 3          ", floorlabel.getText());

                    app.control.setElevatorButton(elevatorlabel.e.elevatorNumber, floorlabel.f.floorId);
                    try {
                        var a = app.control.getElevatorButton(elevatorlabel.e.elevatorNumber, floorlabel.f.floorId);
                        assertTrue(a);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                })
                .interact(() -> waitFor())
                .interact(() -> {
                    assertEquals("Floor 3         o", floorlabel.getText());
                });
    }

    void waitFor() {
        WaitForAsyncUtils.waitForFxEvents(3);
    }

    void waitFor(Callable<Boolean> fn) {
        try {
            WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, fn);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    void waitFor(ObservableBooleanValue prop) {
        try {
            WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, prop::get);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
