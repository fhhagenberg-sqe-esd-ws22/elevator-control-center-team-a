package at.fhhagenberg.sqe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandlineTest {

    @Test
    void testDebugPresent() {
        String[] input = new String[]{"a", "b", "d", "-d"};
        Assertions.assertTrue(App.hasDebug(input));
    }

    @Test
    void testNoDebugOptionPresent() {
        String[] input = new String[]{"a", "b", "d", "-", "--d"};
        Assertions.assertFalse(App.hasDebug(input));
    }
}
