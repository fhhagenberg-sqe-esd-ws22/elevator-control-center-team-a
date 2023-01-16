package at.fhhagenberg.sqe;

import at.fhhagenberg.sqe.ui.ElevatorParams;
import at.fhhagenberg.sqe.ui.ParamUtils;
import at.fhhagenberg.sqe.ui.view.ElevatorControlUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqelevator.IElevator;

import javax.swing.event.HyperlinkEvent;
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

        stage.setOnCloseRequest(val -> {
            if (val != null) {
                boolean successfullShutdown = false;
                int tryCounter = 0;
                final int maxTries = 5;
                while (!successfullShutdown) {
                    tryCounter++;
                    try {
                        elevatorUI.updater.shutdown();
                        successfullShutdown = true;
                    } catch (InterruptedException e) {
                        log.error("Interrupted during shutdown. ({}/{})\n{}", tryCounter, maxTries, e.getMessage());
                    }
                }
                log.debug("Shut down after {}/{} tries.", tryCounter, maxTries);
            }
        });
    }

    protected synchronized IElevator getControl() throws NotBoundException, RemoteException {
        Parameters params = getParameters();

        ElevatorParams elevatorParams = ParamUtils.parseParams(params).orElseThrow(()
                -> new IllegalArgumentException("Usage: PROGRAM host:port [-bn BIND_NAME]"));

        try {

            Registry reg;

            if (elevatorParams.port.isPresent()) {
                reg = LocateRegistry.getRegistry(elevatorParams.host, elevatorParams.port.get());
            } else {
                reg = LocateRegistry.getRegistry(elevatorParams.host);
            }

            log.info("Connected to server.");
            final IElevator control = (IElevator) reg.lookup(elevatorParams.bindName);
            log.info("Bound local object to remote object via '{}'", elevatorParams.bindName);
            return control;
        } catch (NotBoundException e) {
            log.error("Failed during lookup of remote object '{}'", elevatorParams.bindName);
            throw e;
        } catch (RemoteException e) {
            log.error("Failed to connect to remote machine.\n{}", e.getMessage());
            throw e;
        }
    }

}