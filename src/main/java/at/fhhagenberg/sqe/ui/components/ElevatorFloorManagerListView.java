package at.fhhagenberg.sqe.ui.components;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


public class ElevatorFloorManagerListView extends HBox {
    private ListView<String> floorList;
    private FloorDetailContextMenu floorContextMenu;

    private final ElevatorListView listView;

    public ElevatorFloorManagerListView(ElevatorListView elevatorList, int numOfFloors)
    {
        listView = elevatorList;
        var selectedElevator = elevatorList.getSelectedElevator();
        floorList = new ListView<>();
        floorContextMenu = new FloorDetailContextMenu();
        for (var i = 0; i < numOfFloors; ++i)
        {
            floorList.getItems().add(String.format("Floor %d", i));
        }
        getChildren().add(floorList);
        floorList.setContextMenu(floorContextMenu);
        floorContextMenu.sendToThisFloor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.printf("Send to floor %s%n", floorList.getSelectionModel().getSelectedItem());
            }
        });
    }

}
