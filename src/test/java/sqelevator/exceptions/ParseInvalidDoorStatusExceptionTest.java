package sqelevator.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParseInvalidDoorStatusExceptionTest {

    @Test
    void testParseInvalidDoorStatusExceptionText() {
        var e = new ParseInvalidDoorStatusException(7);

        assertEquals("Encountered door status value '7'", e.getMessage());
    }
}
