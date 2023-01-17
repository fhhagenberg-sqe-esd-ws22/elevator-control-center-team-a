package at.fhhagenberg.sqe.params;

import java.util.Optional;

public class ElevatorParams {
    public final String host;
    public final Optional<Integer> port;
    public final String bindName;

    public ElevatorParams(String host) {
        this.host = host;
        this.port = Optional.empty();
        this.bindName = "Team A";
    }

    public ElevatorParams(String host, Integer port) {
        this.host = host;
        this.port = Optional.of(port);
        this.bindName = "Team A";
    }

    public ElevatorParams(String host, String bindName) {
        this.host = host;
        this.port = Optional.empty();
        this.bindName = bindName;
    }

    public ElevatorParams(String host, Integer port, String bindName) {
        this.host = host;
        this.port = Optional.of(port);
        this.bindName = bindName;
    }
}
