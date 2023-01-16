package at.fhhagenberg.sqe.ui.components;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
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
        getChildren().add(floorListView);
        var selectedElevator = elevatorList.getSelectedElevator();

        floorContextMenu = new FloorDetailContextMenu();

        floorListView.setContextMenu(floorContextMenu);
        floorContextMenu.setOnShowing(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e) {
                var selectedFloor = floorListView.getSelectionModel().getSelectedItem();
                try {
                    floorContextMenu.underService.setSelected(selectedFloor.isUnderService());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        floorContextMenu.underService.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                var selectedFloor = floorListView.getSelectionModel().getSelectedItem();
                try {
                    if(floorContextMenu.underService.isSelected())
                    {
                        selectedFloor.setUnderService();
                    }
                    else
                    {
                        selectedFloor.unsetUnderService();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        floorContextMenu.sendToThisFloor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.printf("Send to floor %s%n", floorListView.getSelectionModel().getSelectedItem());
            }
        });

    }
}
