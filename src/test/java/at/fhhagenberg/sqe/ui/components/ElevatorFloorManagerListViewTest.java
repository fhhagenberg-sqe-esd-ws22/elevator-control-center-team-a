package at.fhhagenberg.sqe.ui.components;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class ElevatorFloorManagerListViewTest {
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
    void testRightClickOpensContextMenu(FxRobot robot) {
        String elevatorQ = "#elevatorlist #elevator_0";
        final String fmtFloorQ = "#floorlist #floorlabel_%d";

        final ElevatorFloorManagerListView floorlist = getFloorMainPanel(robot);
        final FloorDetailContextMenu menu = floorlist.floorContextMenu;

        robot.clickOn(elevatorQ)
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> floorlist.listView.currentElevatorProperty.get() != null);
                });

        for (int floorId = 0; floorId < app.FLOOR_COUNT; floorId++) {
            ElevatorFloorManagerListView.FloorLabel lbl = robot.lookup(String.format(fmtFloorQ, floorId)).queryAs(ElevatorFloorManagerListView.FloorLabel.class);
            robot.rightClickOn(String.format(fmtFloorQ, floorId));

            WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> lbl.equals(floorlist.selectedFloorProperty.get()));
            WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> menu.showingProperty().get());
            // wait for correct selected floor
            // wait for menu to show up
            robot.interact(() -> {
                    assertTrue(menu.isShowing());

                    // close menu again
                    robot.clickOn(elevatorQ);
                });
        }
    }

    @Test
    void testDisabledFloorShowsUpCorrectly(FxRobot robot) throws RemoteException {
        final int elevatorId = 2;
        final int floorId = 3;
        app.control.setServicesFloors(elevatorId, floorId, false);
        final ElevatorFloorManagerListView floorlist = getFloorMainPanel(robot);
        final FloorDetailContextMenu menu = floorlist.floorContextMenu;

        var elevatorLbl = getElevatorLabel(robot, elevatorId);
        var floorLbl = getFloorLabel(robot, floorId);

        robot.clickOn(elevatorLbl)
            .interact(() -> {
                WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> floorlist.listView.currentElevatorProperty.get() != null);
            })
            .rightClickOn(floorLbl);

            WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> menu.showingProperty().get());

            robot.interact(() -> {
                assertFalse(menu.underService.isSelected());
                assertTrue(floorLbl.isDisable());
            });
    }

    @Test
    void testReenabledFloorShowsUpCorrectly(FxRobot robot) throws RemoteException {
        final int elevatorId = 2;
        final int floorId = 3;

        app.control.setServicesFloors(elevatorId, floorId, false);
        final ElevatorFloorManagerListView floorlist = getFloorMainPanel(robot);
        final FloorDetailContextMenu menu = floorlist.floorContextMenu;

        var elevatorLbl = getElevatorLabel(robot, elevatorId);
        var floorLbl = getFloorLabel(robot, floorId);

        robot.clickOn(elevatorLbl)
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> floorlist.listView.currentElevatorProperty.get() != null);
                })
                .rightClickOn(floorLbl);

        WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> menu.showingProperty().get());
        robot.interact(() -> {
            assertFalse(menu.underService.isSelected());
            assertTrue(floorLbl.isDisable());
            }).clickOn(menu.underService.getStyleableNode());

        WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> menu.underService.selectedProperty().get());
        WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> !floorLbl.disabledProperty().get());

        assertTrue(menu.underService.selectedProperty().get());
        assertFalse(floorLbl.disabledProperty().get());
    }

    @Test
    void testApplicationDoesNotCrashWhenNoElevatorSelected(FxRobot robot) {
        var floorLabel = getFloorLabel(robot, 0);
        final ElevatorFloorManagerListView floorlist = getFloorMainPanel(robot);
        final FloorDetailContextMenu menu = floorlist.floorContextMenu;

        robot.rightClickOn(floorLabel)
                .interact(() -> waitFor())
                .interact(() -> assertTrue(menu.isShowing()));
    }

    @Test
    void testSendToFloor(FxRobot robot) throws TimeoutException {
        final var floorLabel = getFloorLabel(robot, 1);
        final var elevatorLabel = getElevatorLabel(robot, 2);
        final var floorpanel = getFloorMainPanel(robot);
        final var elevatorpanel = getElevatorPanel(robot);

        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> app.control.getServicesFloors(elevatorLabel.e.elevatorNumber, floorLabel.f.floorId));
        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> elevatorLabel.e.serviceableFloors.get().contains(floorLabel.f.floorId));

        robot.clickOn(elevatorLabel);
        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> !floorLabel.isDisable());
        robot.rightClickOn(floorLabel);

        WaitForAsyncUtils.waitForAsync(500L, () -> elevatorLabel.equals(elevatorpanel.currentElevatorProperty.get()));
        WaitForAsyncUtils.waitForAsync(500L, floorpanel.floorContextMenu::isShowing);

        robot.clickOn(floorpanel.floorContextMenu.sendToThisFloor.getStyleableNode());

        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> floorLabel.f.floorId == app.control.getTarget(elevatorLabel.e.elevatorNumber));
        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> floorLabel.f.floorId == elevatorLabel.e.currentFloor.get());

        assertEquals(floorLabel.f.floorId, elevatorLabel.e.currentFloor.get());
    }

    @Test
    void testSendToFloorNoElevatorSelected(FxRobot robot) {
        final var floorLabel = getFloorLabel(robot, 1);
        final var floorpanel = getFloorMainPanel(robot);

        final var currentFloorValue = robot.lookup("#detaillist #currentfloorval").queryAs(Label.class);

        robot.rightClickOn(floorLabel)
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(500L, floorpanel.floorContextMenu::isShowing);
                })
                .clickOn(floorpanel.floorContextMenu.sendToThisFloor.getStyleableNode())
                .interact(() -> {
                    assertTrue(currentFloorValue.getText().isEmpty());
                });
    }

    @Test
    void testFloorLabelIsUpdatedIfElevatorIsChanged(FxRobot robot) throws RemoteException {
        final var elevator_0 = getElevatorLabel(robot, 0);
        final var elevator_1 = getElevatorLabel(robot, 1);
        final var floorlabel = getFloorLabel(robot, 3);
        app.control.setServicesFloors(elevator_0.e.elevatorNumber, floorlabel.f.floorId, false);
        final ElevatorFloorManagerListView floorlist = getFloorMainPanel(robot);

        robot.clickOn(elevator_0);

        WaitForAsyncUtils.waitForAsync(500L, () -> elevator_0.equals(floorlist.listView.currentElevatorProperty.get()));
        assertFalse(floorlabel.f.underserviceProperty.get());

        robot.clickOn(elevator_1);
        WaitForAsyncUtils.waitForAsync(500L, () -> elevator_1.equals(floorlist.listView.currentElevatorProperty.get()));
        waitFor(floorlabel.f.underserviceProperty::get);
        assertTrue(floorlabel.f.underserviceProperty.get());

        robot.clickOn(elevator_0);
        WaitForAsyncUtils.waitForAsync(500L ,() -> elevator_0.equals(floorlist.listView.currentElevatorProperty.get()));
        waitFor(() -> !floorlabel.f.underserviceProperty.get());
        assertFalse(floorlabel.f.underserviceProperty.get());
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
}
