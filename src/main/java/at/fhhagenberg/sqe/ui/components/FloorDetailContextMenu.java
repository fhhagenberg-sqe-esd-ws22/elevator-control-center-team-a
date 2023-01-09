package at.fhhagenberg.sqe.ui.components;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class FloorDetailContextMenu extends ContextMenu {
    public final CheckMenuItem underService;
    public final MenuItem sendToThisFloor;
    public FloorDetailContextMenu()
    {
        underService = new CheckMenuItem("\"under service\"");
        sendToThisFloor = new MenuItem("send elevator to this floor");
        getItems().addAll(underService, sendToThisFloor);
    }
}
