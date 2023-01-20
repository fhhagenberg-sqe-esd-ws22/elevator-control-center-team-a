package sqelevator.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidProgramArgumentsExceptionTest {

    @Test
    void testInvalidArgumentExceptionsText() {
        String msg = "a message";
        var e = new InvalidProgramArgumentsException(msg);

        assertEquals(msg, e.getMessage());
    }
}
