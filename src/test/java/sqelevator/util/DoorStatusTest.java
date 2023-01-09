package sqelevator.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import sqelevator.IElevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DoorStatusTest {

    @Test
    public void test_valueOf() {
        assertEquals(DoorStatus.Closed, DoorStatus.valueOf(IElevator.ELEVATOR_DOORS_CLOSED));
        assertEquals(DoorStatus.Closing, DoorStatus.valueOf(IElevator.ELEVATOR_DOORS_CLOSING));
        assertEquals(DoorStatus.Open, DoorStatus.valueOf(IElevator.ELEVATOR_DOORS_OPEN));
        assertEquals(DoorStatus.Opening, DoorStatus.valueOf(IElevator.ELEVATOR_DOORS_OPENING));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "DoorStatusTest.csv", numLinesToSkip = 1)
    public void test_valueOf_throws(int value) {
        assertThrows(RuntimeException.class, () -> DoorStatus.valueOf(value));
    }
}
