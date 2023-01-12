package at.fhhagenberg.sqe.ui.view;

import at.fhhagenberg.sqe.ui.components.ElevatorDetailList;
import at.fhhagenberg.sqe.ui.components.ElevatorFloorManagerListView;
import at.fhhagenberg.sqe.ui.components.ElevatorListView;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import sqelevator.Elevator;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ElevatorControlUI extends HBox{
    List<Elevator> elevators = new ArrayList<>();
    public ElevatorControlUI(IElevator control) throws RemoteException {

        var numOfElevators = control.getElevatorNum();
        for(var i = 0; i < numOfElevators; ++i)
        {
            elevators.add(new Elevator(i));
        }

        var elevatorList = new ElevatorListView(elevators);
        var floorList = new ElevatorFloorManagerListView(elevatorList, control.getFloorNum());
        var detail = new ElevatorDetailList(elevatorList);
        getChildren().add(elevatorList);
        getChildren().add(floorList);
        getChildren().add(detail);

    }
}