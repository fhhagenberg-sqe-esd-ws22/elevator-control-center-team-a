package at.fhhagenberg.sqe.fetch;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PeriodicFetchTest {

    @Test
    void testExecutionOfFunction() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        PeriodicFetch fetch = new PeriodicFetch(1, latch::countDown);

        assertTrue(latch.await(5_000, TimeUnit.MILLISECONDS));

        fetch.shutdown();

        assertEquals(0L, latch.getCount());
    }

    @Test
    void testGracefulshutdown() {
        PeriodicFetch fetch = new PeriodicFetch(1, () -> { /* nothing */});

        assertDoesNotThrow(() -> assertTrue(fetch.shutdown()));
    }
}
