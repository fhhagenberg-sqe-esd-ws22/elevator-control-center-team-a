package at.fhhagenberg.sqe;

import at.fhhagenberg.sqe.params.ElevatorParams;
import at.fhhagenberg.sqe.params.ParamUtils;
import javafx.application.Application;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sqelevator.exceptions.InvalidProgramArgumentsException;

import java.rmi.UnknownHostException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppLaunchBehaviourTest {

    App app = null;

    @BeforeEach
    public void SetUp() {
        app = new App();
    }

    @Test
    void throwsExceptionWhenNoArgsProvided() {
        Assertions.assertThrows(InvalidProgramArgumentsException.class, () -> {
            app.getControl();
        }, "Usage: PROGRAM host:port [-bn BIND_NAME]");
    }

    @Test
    void throwsExceptionWhenInvalidhostname() {
        final List<String> args = List.of("weirdHostname", "-bn", "weird@bindname");
        Application.Parameters params = mock(Application.Parameters.class);
        when(params.getRaw()).thenReturn(args);
        Optional<ElevatorParams> maybeEparams = ParamUtils.parseParams(params);

        assertTrue(maybeEparams.isPresent());

        Assertions.assertThrows(UnknownHostException.class, () -> {
            app.aquireRemoteObject(maybeEparams.get());
        });
    }
}
