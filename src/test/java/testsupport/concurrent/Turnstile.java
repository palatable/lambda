package testsupport.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.locks.LockSupport.park;
import static java.util.concurrent.locks.LockSupport.unpark;
import static java.util.stream.StreamSupport.stream;

public final class Turnstile {

    private final int                   parties;
    private final BlockingQueue<Thread> arrivals;

    public Turnstile(int parties) {
        this.parties = parties;
        arrivals = new ArrayBlockingQueue<>(parties);
    }

    public void arrive() {
        synchronized (this) {
            arrivals.add(currentThread());
            if (arrivals.size() == parties) {
                allowThrough(parties);
                return;
            }
        }
        park();
    }

    public void allowAllThrough() {
        allowThrough(arrivals.size());
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private void allowThrough(int parked) {
        Thread currentThread = currentThread();
        Collection<Thread> allowedThrough = new ArrayList<>();
        arrivals.drainTo(allowedThrough, parked);
        stream(allowedThrough.spliterator(), false)
                .filter(t -> !t.equals(currentThread))
                .forEach(t -> {
                    unpark(t);
                    try {
                        t.join();
                    } catch (InterruptedException ignored) {
                    }
                });
    }
}
