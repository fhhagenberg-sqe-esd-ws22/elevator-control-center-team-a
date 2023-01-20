package sqelevator.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseInvalidDirectionExceptionTest {

    @Test
    void testParseInvalidDirectionExceptionText() {
        var e = new ParseInvalidDirectionException(7);

        assertEquals("Encountered direction value '7'", e.getMessage());
    }
}
