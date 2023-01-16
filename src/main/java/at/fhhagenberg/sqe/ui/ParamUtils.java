package at.fhhagenberg.sqe.ui;


import javafx.application.Application;
import java.util.Optional;
import at.fhhagenberg.sqe.ui.ElevatorParams;

public class ParamUtils {

    public static Optional<ElevatorParams> parseParams(Application.Parameters params) {
        if (params.getRaw().size() < 1) {
            return Optional.empty();
        }
        String[] hostPort = params.getRaw().get(0).split(":");

        ElevatorParams elevatorParams = new ElevatorParams();
        elevatorParams.host = hostPort[0];

        if (hostPort.length > 1) {
            elevatorParams.port = Optional.of(Integer.parseInt(hostPort[1]));
        }

        if (params.getRaw().size() > 2 && params.getRaw().get(1).equals("-bn")) {
            elevatorParams.bindName = params.getRaw().get(2);
        }
        return Optional.of(elevatorParams);
    }

}
