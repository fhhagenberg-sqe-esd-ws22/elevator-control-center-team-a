package sqelevator.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import sqelevator.IElevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DirectionTest {
    @Test
    public void test_valueOf() {
        assertEquals(Direction.Up, Direction.valueOf(IElevator.ELEVATOR_DIRECTION_UP));
        assertEquals(Direction.Down, Direction.valueOf(IElevator.ELEVATOR_DIRECTION_DOWN));
        assertEquals(Direction.Uncommitted, Direction.valueOf(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED));
    }

    @ParameterizedTest()
    @CsvFileSource(resources = "DirectionTest.csv", numLinesToSkip = 1)
    public void test_valueOf_throws(int value) {
        assertThrows(RuntimeException.class, () -> Direction.valueOf(value));
    }
}
