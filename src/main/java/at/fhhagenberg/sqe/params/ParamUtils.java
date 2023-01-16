package at.fhhagenberg.sqe.params;


import inet.ipaddr.HostName;
import javafx.application.Application;

import java.util.Collection;
import java.util.Optional;

public class ParamUtils {

    private ParamUtils() { /* hide default public c'tor */}

    private static Optional<String> extractHostname(String in) {
        HostName hostName = new HostName(in);
        if (hostName.isValid() && !in.isEmpty()) {
            return Optional.of(in);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> extractPort(String in) {
        Integer port = null;
        try {
            port = Integer.parseInt(in);
        } catch (Exception e) {
            return Optional.empty();
        }
        if (0 < port && port <= 65535) {
            return Optional.of(port);
        }
        return Optional.empty();
    }

    private static Optional<String> extractBindname(Collection<String> in) {
        var iter = in.iterator();
        // we skip the hostname:port
        iter.next();
        String val = null;
        while(iter.hasNext()) {
            val = iter.next();
            if ("-bn".equals(val) && iter.hasNext()) {
                return Optional.of(iter.next());
            }
        }
        return Optional.empty();
    }

    public static Optional<ElevatorParams> parseParams(Application.Parameters params) {
        if (params == null || params.getRaw().isEmpty()) {
            return Optional.empty();
        }

        String[] hostPort = params.getRaw().get(0).split(":");

        Optional<String> hostname = extractHostname(hostPort[0]);
        Optional<Integer> port = Optional.empty();
        if (hostPort.length > 1) {
            port = extractPort(hostPort[1]);
        }
        Optional<String> bindname = extractBindname(params.getRaw());

        ElevatorParams elevatorParams = null;

        if (hostname.isEmpty()) return Optional.empty();

        if (port.isPresent() && bindname.isEmpty()) {
            elevatorParams = new ElevatorParams(hostname.get(), port.get());
        }

        if (port.isPresent() && bindname.isPresent()) {
            elevatorParams = new ElevatorParams(hostname.get(), port.get(), bindname.get());
        }

        if (port.isEmpty() && bindname.isEmpty()) {
            elevatorParams = new ElevatorParams(hostname.get());
        }

        if (port.isEmpty() && bindname.isPresent()) {
            elevatorParams = new ElevatorParams(hostname.get(), bindname.get());
        }

        return Optional.of(elevatorParams);
    }

}
