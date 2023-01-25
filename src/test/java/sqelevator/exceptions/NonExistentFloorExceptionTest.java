package sqelevator.exceptions;

import org.junit.jupiter.api.Test;

import java.util.MissingFormatArgumentException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NonExistentFloorExceptionTest {

    @Test
    void testNonExistentElevatorExceptionTextNoArgs() {
        String msg = "null";
        var e = new NonExistentFloorException(msg);

        assertEquals(msg, e.getMessage());
    }

    @Test
    void testNonExistentElevatorExceptionTextWithArgs() {
        String fmString = "a %s %d";
        var e = new NonExistentFloorException(fmString, "valid", 6);

        assertEquals("a valid 6", e.getMessage());
    }

    @Test
    void testNonExistentElevatorExceptionTextWithNotEnoughArgs() {
        String fmtString = "a %s %d";
        assertThrows(MissingFormatArgumentException.class, () -> {
            @SuppressWarnings("unused")
            NonExistentFloorException _unused = new NonExistentFloorException(fmtString, "valid");
        });
    }

    @Test
    void testNonExistentElevatorExceptionTextWithTooManyArgs() {
        String fmtString = "a %s %d";

        var e = new NonExistentFloorException(fmtString, "valid", 7, "one too much");

        assertEquals("a valid 7", e.getMessage());
    }
}
