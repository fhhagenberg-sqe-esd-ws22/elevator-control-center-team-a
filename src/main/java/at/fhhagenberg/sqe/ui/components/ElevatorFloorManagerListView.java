package at.fhhagenberg.sqe.ui.components;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import javafx.event.EventHandler;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ElevatorFloorManagerListView extends HBox {
    private ArrayList<Floor> floorList;
    private FloorDetailContextMenu floorContextMenu;
    private final ElevatorListView listView;
    private final IElevator elevatorControl;

    public ElevatorFloorManagerListView(ElevatorListView elevatorList, IElevator control) throws RemoteException {
        listView = elevatorList;
        elevatorControl = control;
        floorList = new ArrayList<Floor>();
        for(var i = 0; i < elevatorControl.getFloorNum(); ++i)
        {
            floorList.add(new Floor(listView, elevatorControl, i));
        }

        var floorListView = new ListView<Floor>();
        floorListView.getItems().addAll(floorList);

        var selectedElevator = elevatorList.getSelectedElevator();

        floorContextMenu = new FloorDetailContextMenu();

        floorListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + floorListView.getSelectionModel().getSelectedItem());
            }
        });


        /*
        floorListView.setContextMenu(floorContextMenu);
        floorContextMenu.sendToThisFloor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.printf("Send to floor %s%n", floorListView.getSelectionModel().getSelectedItem());
            }
        });
            */
    }
}
