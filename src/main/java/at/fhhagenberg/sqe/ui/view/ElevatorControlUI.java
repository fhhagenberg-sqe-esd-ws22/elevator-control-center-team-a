package at.fhhagenberg.sqe.ui.view;

import at.fhhagenberg.sqe.fetch.PeriodicFetch;
import at.fhhagenberg.sqe.ui.components.ElevatorDetailList;
import at.fhhagenberg.sqe.ui.components.ElevatorFloorManagerListView;
import at.fhhagenberg.sqe.ui.components.ElevatorListView;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqelevator.Elevator;
import sqelevator.IElevator;
import sqelevator.updater.ElevatorUpdater;
import sqelevator.updater.Updater;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ElevatorControlUI extends HBox {
    private final List<Elevator> elevators = new ArrayList<>();
    public final PeriodicFetch updater;
    private final IElevator control;

    private final static Logger LOG = LoggerFactory.getLogger("UpdateElevator");

    public ElevatorControlUI(IElevator control) throws RemoteException {

        var numOfElevators = control.getElevatorNum();
        if (numOfElevators == 0) {
            throw new IllegalArgumentException("There must always be an elevator."); // TODO(cn): Make this neater
        }
        for(var i = 0; i < numOfElevators; ++i)
        {
            elevators.add(new Elevator(i));
        }

        this.control = control;
        updater = new PeriodicFetch(numOfElevators / 2 + 1, this::updateElevators);

        var elevatorList = new ElevatorListView(elevators);
        var floorList = new ElevatorFloorManagerListView(elevatorList, control);
        var detail = new ElevatorDetailList(elevatorList);
        getChildren().add(elevatorList);
        getChildren().add(floorList);
        getChildren().add(detail);
    }

    private void updateElevators() {
        for (Elevator e : elevators) {
            Updater u = new ElevatorUpdater(e, control);
            try {
                u.update();
            } catch (RemoteException err) {
                LOG.error("Failed to update elevator {}", e.elevatorNumber);
            }
        }
    }
}
