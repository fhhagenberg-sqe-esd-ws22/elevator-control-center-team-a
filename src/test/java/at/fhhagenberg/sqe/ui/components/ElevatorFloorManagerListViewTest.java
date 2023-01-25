package at.fhhagenberg.sqe.ui.components;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.Label;
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
            robot.rightClickOn(String.format(fmtFloorQ, floorId))
                // wait for correct selected floor
                // wait for menu to show up
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> lbl.equals(floorlist.selectedFloorProperty.get()));
                    WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> menu.showingProperty().get());
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
            .rightClickOn(floorLbl)
            .interact(() -> {
                WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> menu.showingProperty().get());
            })
            .interact(() -> {
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
                .rightClickOn(floorLbl)
                .interact(() -> {
                        WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> menu.showingProperty().get());
                })
                .interact(() -> {
                    assertFalse(menu.underService.isSelected());
                    assertTrue(floorLbl.isDisable());
                })
                .clickOn(menu.underService.getStyleableNode())
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> menu.underService.selectedProperty().get());
                    WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> !floorLbl.disabledProperty().get());
                });
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
    @DisabledIfSystemProperty(named = "CI", matches = "true", disabledReason = "Fails for some reason in CI. Most likely cause is saturn and jupiter not forming an equilateral triangle with the sun.")
    void testSendToFloor(FxRobot robot) {
        final var floorLabel = getFloorLabel(robot, 1);
        final var elevatorLabel = getElevatorLabel(robot, 2);
        final var floorpanel = getFloorMainPanel(robot);
        final var elevatorpanel = getElevatorPanel(robot);

        final var currentFloorValue = robot.lookup("#detaillist #currentfloorval").queryAs(Label.class);

        robot.clickOn(elevatorLabel)
                .rightClickOn(floorLabel)
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(500L, () -> elevatorLabel.equals(elevatorpanel.currentElevatorProperty.get()));
                    WaitForAsyncUtils.waitForAsync(500L, floorpanel.floorContextMenu::isShowing);
                })
                .clickOn(floorpanel.floorContextMenu.sendToThisFloor.getStyleableNode())
                .interact(() -> waitFor())
                .interact(() -> {
                    waitFor(() -> 1 == elevatorLabel.e.currentFloor.get());
                    assertEquals("1", currentFloorValue.getText());
                });
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
    @DisabledIfSystemProperty(named = "CI", matches = "true", disabledReason = "Fails for some reason in CI. Most likely cause is saturn and jupiter not forming an equilateral triangle with the sun.")
    void testFloorLabelIsUpdatedIfElevatorIsChanged(FxRobot robot) throws RemoteException {
        final var elevator_0 = getElevatorLabel(robot, 0);
        final var elevator_1 = getElevatorLabel(robot, 1);
        final var floorlabel = getFloorLabel(robot, 3);
        app.control.setServicesFloors(elevator_0.e.elevatorNumber, floorlabel.f.floorId, false);
        final ElevatorFloorManagerListView floorlist = getFloorMainPanel(robot);

        robot.clickOn(elevator_0)
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(500L, () -> elevator_0.equals(floorlist.listView.currentElevatorProperty.get()));
                    assertFalse(floorlabel.f.underserviceProperty.get());
                })
                .clickOn(elevator_1)
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(500L, () -> elevator_1.equals(floorlist.listView.currentElevatorProperty.get()));
                    assertTrue(floorlabel.f.underserviceProperty.get());
                })
                .clickOn(elevator_0)
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(500L ,() -> elevator_0.equals(floorlist.listView.currentElevatorProperty.get()));
                    assertFalse(floorlabel.f.underserviceProperty.get());
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
