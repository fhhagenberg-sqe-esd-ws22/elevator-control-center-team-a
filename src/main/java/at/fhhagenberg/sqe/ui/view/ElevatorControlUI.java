package at.fhhagenberg.sqe.ui.view;

import at.fhhagenberg.sqe.ui.components.ElevatorDetailList;
import at.fhhagenberg.sqe.ui.components.ElevatorFloorManagerListView;
import at.fhhagenberg.sqe.ui.components.ElevatorListView;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class ElevatorControlUI extends HBox{
    public ElevatorControlUI()
    {
        List<String> elevators = new ArrayList<>();
        elevators.add("Elevator 1");
        elevators.add("Elevator 2");
        elevators.add("Elevator 3");

        var elevatorList = new ElevatorListView(elevators);
        var floorList = new ElevatorFloorManagerListView();
        var detail = new ElevatorDetailList();
        getChildren().add(elevatorList);
        getChildren().add(floorList);
        getChildren().add(detail);

    }
}
