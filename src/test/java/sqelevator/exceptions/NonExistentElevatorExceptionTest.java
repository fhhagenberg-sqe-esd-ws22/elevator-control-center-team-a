package sqelevator.exceptions;

import org.junit.jupiter.api.Test;

import java.util.MissingFormatArgumentException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NonExistentElevatorExceptionTest {

    @Test
    void testNonExistentElevatorExceptionTextNoArgs() {
        String msg = "null";
        var e = new NonExistentElevatorException(msg);

        assertEquals(msg, e.getMessage());
    }

    @Test
    void testNonExistentElevatorExceptionTextWithArgs() {
        String fmString = "a %s %d";
        var e = new NonExistentElevatorException(fmString, "valid", 6);

        assertEquals("a valid 6", e.getMessage());
    }

    @Test
    void testNonExistentElevatorExceptionTextWithNotEnoughArgs() {
        String fmtString = "a %s %d";
        assertThrows(MissingFormatArgumentException.class, () -> {
            @SuppressWarnings("unused")
            NonExistentElevatorException _unused = new NonExistentElevatorException(fmtString, "valid");
        });
    }

    @Test
    void testNonExistentElevatorExceptionTextWithTooManyArgs() {
        String fmtString = "a %s %d";

        var e = new NonExistentElevatorException(fmtString, "valid", 7, "one too much");
        
        assertEquals("a valid 7", e.getMessage());
    }
}
