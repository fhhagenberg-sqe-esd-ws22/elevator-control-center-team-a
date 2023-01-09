package sqelevator.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import sqelevator.IElevator;
import sqelevator.exceptions.ParseInvalidDirectionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DirectionTest {
    @Test
    void test_valueOf() {
        assertEquals(Direction.UP, Direction.valueOf(IElevator.ELEVATOR_DIRECTION_UP));
        assertEquals(Direction.DOWN, Direction.valueOf(IElevator.ELEVATOR_DIRECTION_DOWN));
        assertEquals(Direction.UNCOMMITTED, Direction.valueOf(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED));
    }

    @ParameterizedTest()
    @CsvFileSource(resources = "DirectionTest.csv", numLinesToSkip = 1)
    void test_valueOf_throws(int value) {
        assertThrows(ParseInvalidDirectionException.class, () -> Direction.valueOf(value));
    }
}
