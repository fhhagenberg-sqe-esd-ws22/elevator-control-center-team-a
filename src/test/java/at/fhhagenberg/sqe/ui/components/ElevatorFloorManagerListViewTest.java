package at.fhhagenberg.sqe.ui.components;

import at.fhhagenberg.sqe.sqelevator.mock.MockApp;
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
    @DisabledIfSystemProperty(named = "CI", matches = "true", disabledReason = "Fails for some reason in CI. Most likely cause is saturn and jupiter not forming an equilateral triangle with the sun.")
    void testAllFloorsAreServicedByAllElevatorsOnStartup(FxRobot robot) {
        final ElevatorFloorManagerListView floorlist = getFloorMainPanel(robot);
        final FloorDetailContextMenu menu = floorlist.floorContextMenu;

        for (int elevatorId = 0; elevatorId < app.ELEVATOR_COUNT; elevatorId++) {
            final ElevatorListView.ElevatorListItem elbl = getElevatorLabel(robot, elevatorId);
            robot.clickOn(elbl)
                .interact(() -> {
                    WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> floorlist.listView.currentElevatorProperty.get() != null);
                });

            for (int floorId = 0; floorId < app.FLOOR_COUNT; floorId++) {
                final ElevatorFloorManagerListView.FloorLabel lbl = getFloorLabel(robot, floorId);
                robot.rightClickOn(lbl)
                    // wait for correct selected floor
                    // wait for menu to show up
                    .interact(() -> {
                        WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> floorlist.selectedFloorProperty.get() == lbl);
                        WaitForAsyncUtils.waitForAsync(defaultTimeout, () -> menu.showingProperty().get());
                    })
                    // check under service
                    .interact(() -> {
                        assertTrue(menu.underService.isSelected());
                        assertFalse(lbl.disableProperty().get());
                    })
                    // close menu again
                    .interact(() -> {
                        robot.clickOn(elbl);
                    });
            }
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

        robot.rightClickOn(floorLabel);

        assertTrue(menu.isShowing());
    }
}