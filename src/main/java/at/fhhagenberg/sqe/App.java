package at.fhhagenberg.sqe;

import at.fhhagenberg.sqe.ui.view.ElevatorControlUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqelevator.IElevator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * JavaFX App
 */
public class App extends Application {

    final Logger log = LoggerFactory.getLogger("ElevatorControlCenter");

    @Override
    public void start(Stage stage) throws RemoteException, NotBoundException {

        var elevatorUI = new ElevatorControlUI(getControl());
        var mainLayout = new StackPane();
        mainLayout.getChildren().add(elevatorUI);
        var scene = new Scene(mainLayout, 800, 600);


        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    protected synchronized IElevator getControl() throws NotBoundException, RemoteException {
        final String BIND_NAME = "TeamA"; // TODO(cn): Make BIND_NAME an argv with a default

        Parameters params = getParameters();

        try {
            // TODO(cn): Handle argv parameters better ([host+port, host] being the only options)
            Registry reg = LocateRegistry.getRegistry(params.getRaw().get(0));
            log.info("Connected to server.");
            final IElevator control = (IElevator) reg.lookup(BIND_NAME);
            log.info("Bound local object to remote object via '{}'", BIND_NAME);
            return control;
        } catch (NotBoundException e) {
            log.error("Failed during lookup of remote object '{}'", BIND_NAME);
            throw e;
        } catch (RemoteException e) {
            log.error("Failed to connect to remote machine.\n{}", e.getMessage());
            throw e;
        }
    }

}