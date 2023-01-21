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
                    try {
                        WaitForAsyncUtils.waitFor(500L, TimeUnit.MILLISECONDS, () -> floorlist.listView.currentElevatorProperty.get() != null);
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                });

        for (int floorId = 0; floorId < app.FLOOR_COUNT; floorId++) {
            ElevatorFloorManagerListView.FloorLabel lbl = robot.lookup(String.format(fmtFloorQ, floorId)).queryAs(ElevatorFloorManagerListView.FloorLabel.class);
            robot.rightClickOn(String.format(fmtFloorQ, floorId))
                // wait for correct selected floor
                .interact(() -> {
                    try {
                        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> lbl.equals(floorlist.selectedFloorProperty.get()));
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                })
                // wait for menu to show up
                .interact(() -> {
                    try {
                        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, menu.showingProperty());
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                })
                // close menu again
                .interact(() -> {
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
                        try {
                            WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> floorlist.listView.currentElevatorProperty.get() != null);
                        } catch (TimeoutException e) {
                            throw new RuntimeException(e);
                        }
                    });

            for (int floorId = 0; floorId < app.FLOOR_COUNT; floorId++) {
                final ElevatorFloorManagerListView.FloorLabel lbl = getFloorLabel(robot, floorId);
                robot.rightClickOn(lbl)
                    // wait for correct selected floor
                    .interact(() -> {
                        try {
                            WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> floorlist.selectedFloorProperty.get() == lbl);
                        } catch (TimeoutException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    // wait for menu to show up
                    .interact(() -> {
                        try {
                            WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, menu.showingProperty());
                        } catch (TimeoutException e) {
                            throw new RuntimeException(e);
                        }
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
                try {
                    WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> floorlist.listView.currentElevatorProperty.get() != null);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            })
            .rightClickOn(floorLbl)
                    .interact(() -> {
                        try {
                            WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, menu.showingProperty());
                        } catch (TimeoutException e) {
                            throw new RuntimeException(e);
                        }
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
                    try {
                        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> floorlist.listView.currentElevatorProperty.get() != null);
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                })
                .rightClickOn(floorLbl)
                .interact(() -> {
                    try {
                        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, menu.showingProperty());
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                })
                .interact(() -> {
                    assertFalse(menu.underService.isSelected());
                    assertTrue(floorLbl.isDisable());
                })
                .clickOn(menu.underService.getStyleableNode())
                .interact(() -> {
                    try {
                        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, menu.underService.selectedProperty());
                        WaitForAsyncUtils.waitFor(defaultTimeout, TimeUnit.MILLISECONDS, () -> !floorLbl.disabledProperty().get());
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    void testApplicationDoesNotCrashWhenNoElevatorSelected(FxRobot robot) {
        var floorLabel = getFloorLabel(robot, 0);

        robot.rightClickOn(floorLabel);
    }
}
