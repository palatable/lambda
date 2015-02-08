package com.jnape.palatable.lambda.continuation;

import org.junit.Test;
import testsupport.concurrent.Turnstile;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.continuation.Memo.memoize;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MemoTest {

    @Test
    public void computesValueOnlyOnce() {
        AtomicInteger counter = new AtomicInteger(0);
        Memo<Integer> memo = memoize(counter::incrementAndGet);

        assertThat(memo.get(), is(1));
        assertThat(memo.get(), is(1));
        assertThat(memo.get(), is(1));
        assertThat(memo.get(), is(1));
        assertThat(memo.get(), is(1));
    }

    @Test
    public void memoizesValuesToo() {
        Memo<Integer> memo = memoize(2);
        assertThat(memo.get(), is(2));
    }

    @Test
    public void onlyMemoizesValueOfWinningThreadInMultiThreadedRaceSituation() throws InterruptedException {
        Turnstile turnstile = new Turnstile(2);

        Supplier<Integer> randomIntSupplier = () -> {
            int i = new Random().nextInt();
            turnstile.arrive();
            return i;
        };

        Memo<Integer> memo = memoize(randomIntSupplier);

        AtomicInteger thread1Value = new AtomicInteger();
        AtomicInteger thread2Value = new AtomicInteger();

        Thread thread1 = new Thread(() -> thread1Value.set(memo.get()));
        Thread thread2 = new Thread(() -> thread2Value.set(memo.get()));

        thread1.start();
        sleep(100);
        thread2.start();

        thread2.join();

        assertThat(memo.get(), is(thread1Value.get()));
    }
}