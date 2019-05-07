package com.jnape.palatable.lambda.functions.builtin.fn4;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SideEffect;
import com.jnape.palatable.lambda.iteration.IterationInterruptedException;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.time.InstantRecordingClock;
import testsupport.traits.Deforesting;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import static com.jnape.palatable.lambda.adt.Try.trying;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn4.RateLimit.rateLimit;
import static com.jnape.palatable.lambda.functions.specialized.SideEffect.sideEffect;
import static java.time.Clock.systemUTC;
import static java.time.Duration.ZERO;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;
import static testsupport.matchers.RateLimitedIterationMatcher.iteratesAccordingToRateLimit;

@RunWith(Traits.class)
public class RateLimitTest {

    private InstantRecordingClock clock;

    @Before
    public void setUp() throws Exception {
        clock = new InstantRecordingClock(systemUTC());
    }

    @TestTraits({Laziness.class, InfiniteIterableSupport.class, EmptyIterableSupport.class, Deforesting.class})
    public Fn1<Iterable<Object>, Iterable<Object>> testSubject() {
        return rateLimit(systemUTC()::instant, 1L, ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void lessThanOneLimitIsInvalid() {
        rateLimit(clock::instant, 0L, ZERO, emptyList());
    }

    @Test
    public void zeroDurationJustIteratesElements() {
        assertThat(rateLimit(clock::instant, 1L, ZERO, asList(1, 2, 3)), iterates(1, 2, 3));
    }

    @Test
    public void limitPerDurationIsHonoredAccordingToClock() {
        Duration duration = Duration.ofMillis(10);
        long     limit    = 2L;
        assertThat(rateLimit(clock::instant, limit, duration, asList(1, 2, 3, 4)),
                   iteratesAccordingToRateLimit(limit, duration, asList(1, 2, 3, 4), clock));
    }

    @Test(timeout = 100, expected = IterationInterruptedException.class)
    public void rateLimitingDelayIsInterruptible() throws InterruptedException {
        Thread         testThread = Thread.currentThread();
        CountDownLatch latch      = new CountDownLatch(1);
        new Thread(() -> {
            trying(sideEffect(latch::await)).orThrow();
            testThread.interrupt();
        }) {{
            start();
        }};

        rateLimit(clock::instant, 1L, Duration.ofSeconds(1), repeat(1)).forEach(xs -> latch.countDown());
    }
}