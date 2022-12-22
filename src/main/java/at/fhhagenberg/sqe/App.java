package at.fhhagenberg.sqe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var layout = new BorderPane(label);
        var button = new Button("Click me!");
        button.setOnAction(evt -> button.setText("Clicked!"));
        layout.setBottom(button);

        var scene = new Scene(layout, 640, 480);

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
        if (args.length < 2) {
            log.info("Usage {} host:port [-d]", args[0]);
            return;
        }
        log.info("Test");
        launch();
    }

}