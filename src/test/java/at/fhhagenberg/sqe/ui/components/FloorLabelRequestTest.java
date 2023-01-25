package at.fhhagenberg.sqe.ui.components;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class FloorLabelRequestTest {
    MockApp app = null;
    final long defaultTimeout = 5000L;

    @Start
    void SetUp(Stage stage) throws NotBoundException, RemoteException {
        app = new MockApp();
        app.start(stage);
    }

    @BeforeEach
    void CleanUp() {
        app.control.reset();
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
    void testFloorLabelRequestUp(FxRobot robot) throws InterruptedException {
        final var textChanged = new SimpleStringProperty();
        try {
            final var floorlabel = getFloorLabel(robot, 4);
            final var elevatorlabel = getElevatorLabel(robot, 1);

            final var latch = new CountDownLatch(2); // twice to account for 1. binding and 2. the test update
            textChanged.addListener((obs, oVal, nVal) -> latch.countDown());

            robot.clickOn(elevatorlabel)
                    .interact(() -> {
                        textChanged.bind(floorlabel.textProperty());

                        assertEquals("Floor 5          ", floorlabel.getText());
                        app.control.setFloorButtonUp(floorlabel.f.floorId);
                    });

            assertTrue(latch.await(defaultTimeout, TimeUnit.MILLISECONDS));
            assertEquals("Floor 5     ^    ", floorlabel.getText());
        } finally {
            textChanged.unbind();
        }
    }

    @Test
    void testFloorLabelRequestDown(FxRobot robot) throws InterruptedException {
        final var textChanged = new SimpleStringProperty();
        try {
            final var floorlabel = getFloorLabel(robot, 2);
            final var elevatorlabel = getElevatorLabel(robot, 0);

            final var latch = new CountDownLatch(2); // twice to account for 1. binding and 2. the test update
            textChanged.addListener((obs, oVal, nVal) -> latch.countDown());

            robot.clickOn(elevatorlabel)
                    .interact(() -> {
                        textChanged.bind(floorlabel.textProperty());

                        assertEquals("Floor 3          ", floorlabel.getText());
                        app.control.setFloorButtonDown(floorlabel.f.floorId);
                    });

            assertTrue(latch.await(defaultTimeout, TimeUnit.MILLISECONDS));
            assertEquals("Floor 3       v  ", floorlabel.getText());
        } finally {
            textChanged.unbind();
        }
    }

    @Test
    void testsAreNotRunningAsJavaFXThread() {
        assertFalse(Platform.isFxApplicationThread());
    }

    @Test
    void testsAreNotRunningAsJavaFXThread(FxRobot robot) {
        assertFalse(Platform.isFxApplicationThread());
        robot.interact(() -> assertTrue(Platform.isFxApplicationThread()));
    }

    @Test
    void testFloorLabelRequestStop(FxRobot robot) throws InterruptedException {
        final var textChanged = new SimpleStringProperty();
        try {
            final var floorlabel = getFloorLabel(robot, 0);
            final var elevatorlabel = getElevatorLabel(robot, 2);

            final var latch = new CountDownLatch(2); // twice to account for 1. binding and 2. the test update
            textChanged.addListener((obs, oVal, nVal) -> latch.countDown());

            robot.clickOn(elevatorlabel)
                    .interact(() -> {
                        textChanged.bind(floorlabel.textProperty());

                        assertEquals("Floor 1          ", floorlabel.getText());
                        app.control.setElevatorButton(elevatorlabel.e.elevatorNumber, floorlabel.f.floorId);
                    });

            assertTrue(latch.await(defaultTimeout, TimeUnit.MILLISECONDS));
            assertEquals("Floor 1         o", floorlabel.getText());
        } finally {
            textChanged.unbind();
        }
    }
}
