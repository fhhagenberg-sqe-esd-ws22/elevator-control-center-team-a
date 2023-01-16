package at.fhhagenberg.sqe.sqelevator;

import at.fhhagenberg.sqe.params.ElevatorParams;
import at.fhhagenberg.sqe.params.ParamUtils;
import javafx.application.Application.Parameters;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ParamUtilsTest {

    @Test
    void testParseParamsWithPortWithBindName() {
        List<String> args = List.of("localhost:8080", "-bn", "Test 1");

        Parameters params = mock(Parameters.class);
        when(params.getRaw()).thenReturn(args);

        ElevatorParams elevatorParams = ParamUtils.parseParams(params).get();

        if (elevatorParams.port.isPresent()) {
            Assertions.assertEquals("localhost", elevatorParams.host);
            Assertions.assertEquals(8080, elevatorParams.port.get());
            Assertions.assertEquals("Test 1", elevatorParams.bindName);
        }
    }

    @Test
    void testParseParamsWithoutPortWithBindName() {
        List<String> args = List.of("localhost", "-bn", "Test 2");

        Parameters params = mock(Parameters.class);
        when(params.getRaw()).thenReturn(args);

        ElevatorParams elevatorParams = ParamUtils.parseParams(params).get();

        Assertions.assertEquals("localhost", elevatorParams.host);
        Assertions.assertEquals("Test 2", elevatorParams.bindName);
        Assertions.assertFalse(elevatorParams.port.isPresent());
    }

    @Test
    void testParseParamsWithoutPortWithoutBindName() {
        List<String> args = List.of("localhost");

        Parameters params = mock(Parameters.class);
        when(params.getRaw()).thenReturn(args);

        ElevatorParams elevatorParams = ParamUtils.parseParams(params).get();

        Assertions.assertEquals("localhost", elevatorParams.host);
        Assertions.assertFalse(elevatorParams.port.isPresent());
        Assertions.assertEquals("Team A", elevatorParams.bindName);
    }

    @Test
    void testParseParamsWithPortWithoutBindName() {
        List<String> args = List.of("localhost:8080");

        Parameters params = mock(Parameters.class);
        when(params.getRaw()).thenReturn(args);

        ElevatorParams elevatorParams = ParamUtils.parseParams(params).get();

        if (elevatorParams.port.isPresent()) {
            Assertions.assertEquals("localhost", elevatorParams.host);
            Assertions.assertEquals(8080, elevatorParams.port.get());
            Assertions.assertEquals("Team A", elevatorParams.bindName);
        }
    }

    @Test
    void testParseParamsWithoutParams() {
        List<String> args = List.of();

        Parameters params = mock(Parameters.class);
        when(params.getRaw()).thenReturn(args);

        Assertions.assertFalse(ParamUtils.parseParams(params).isPresent());
    }


}
