package at.fhhagenberg.sqe.params;


import javafx.application.Application;
import java.util.Optional;

public class ParamUtils {

    private ParamUtils() { /* hide default public c'tor */}

    public static Optional<ElevatorParams> parseParams(Application.Parameters params) {
        if (params == null || params.getRaw().isEmpty()) {
            return Optional.empty();
        }

        String[] hostPort = params.getRaw().get(0).split(":");

        ElevatorParams elevatorParams;

        if (hostPort.length == 1 && params.getRaw().size() == 1) {
            elevatorParams = new ElevatorParams(hostPort[0]);
        } else if (params.getRaw().contains("-bn") && hostPort.length == 1) {
            elevatorParams = new ElevatorParams(hostPort[0], params.getRaw().get(params.getRaw().indexOf("-bn") + 1));
        } else if (params.getRaw().contains("-bn") && hostPort.length == 2) {
            elevatorParams = new ElevatorParams(hostPort[0], params.getRaw().get(params.getRaw().indexOf("-bn") + 1));
        } else if (!params.getRaw().contains("-bn") && hostPort.length == 2) {
            elevatorParams = new ElevatorParams(hostPort[0], Integer.parseInt(hostPort[1]));
        } else {
            return Optional.empty();
        }

        return Optional.of(elevatorParams);
    }

}
