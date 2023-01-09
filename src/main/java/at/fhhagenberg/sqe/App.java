package at.fhhagenberg.sqe;

import at.fhhagenberg.sqe.ui.view.ElevatorControlUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {

        var elevatorUI = new ElevatorControlUI();
        var mainLayout = new StackPane();
        mainLayout.getChildren().add(elevatorUI);
        var scene = new Scene(mainLayout, 800, 600);


        stage.setScene(scene);
        stage.show();
    }

    public static boolean hasDebug(String[] args) {
        for(String s : args) {
            if ("-d".equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger("Test");
        if (args.length < 2 && args.length > 0) {
            log.info("Usage {} host:port [-d]", args[0]);
            return;
        }
        log.info("Test");
        launch();
    }

}