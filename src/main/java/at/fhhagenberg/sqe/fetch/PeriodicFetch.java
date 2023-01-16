package at.fhhagenberg.sqe.fetch;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicFetch {
    private final ScheduledExecutorService scheduler;

    public PeriodicFetch(int threadPoolSize, Runnable fn) {
        scheduler = Executors.newScheduledThreadPool(threadPoolSize);

        scheduler.scheduleAtFixedRate(fn, 500, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Shutsdown the periodic fetch task
     * @return true if we shut down successfully, false if we ran into a timeout during awaiting termination
     * @throws InterruptedException if we are interrupted during waiting
     */
    public boolean shutdown() throws InterruptedException {
        scheduler.shutdown();
        return scheduler.awaitTermination(5, TimeUnit.SECONDS);
    }
}
