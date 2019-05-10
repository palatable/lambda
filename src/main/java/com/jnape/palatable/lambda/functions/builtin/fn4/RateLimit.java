package com.jnape.palatable.lambda.functions.builtin.fn4;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.internal.iteration.IterationInterruptedException;
import com.jnape.palatable.lambda.internal.iteration.RateLimitingIterable;

import java.time.Duration;
import java.time.Instant;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static java.util.Collections.singleton;

/**
 * Given an {@link Fn0} of {@link Instant Instants} (presumably backed by a clock), a <code>limit</code>, a
 * {@link Duration}, and an {@link Iterable} <code>as</code>, return an {@link Iterable} that iterates <code>as</code>
 * according to the threshold specified by the limit per duration, using the {@link Fn0} to advance time.
 * <p>
 * As an example, the following will print at most 10 elements per second:
 * <pre><code>
 * rateLimit(Clock.systemUTC()::instant, 10L, Duration.ofSeconds(1), iterate(x -&gt; x + 1, 1))
 *     .forEach(System.out::println);
 * </code></pre>
 * Currying allows different rate limits to be combined naturally:
 * <pre><code>
 * Iterable&lt;Integer&gt; elements = iterate(x -&gt; x + 1, 1);
 *
 * Supplier&lt;Instant&gt; instantFn0 = Clock.systemUTC()::instant;
 * Fn1&lt;Iterable&lt;Integer&gt;, Iterable&lt;Integer&gt;&gt; tenPerSecond =
 *     rateLimit(instantFn0, 10L, Duration.ofSeconds(1));
 * Fn1&lt;Iterable&lt;Integer&gt;, Iterable&lt;Integer&gt;&gt; oneHundredEveryTwoMinutes =
 *     rateLimit(instantFn0, 100L, Duration.ofMinutes(2));
 *
 * tenPerSecond.fmap(oneHundredEveryTwoMinutes).apply(elements).forEach(System.out::println);
 * </code></pre>
 * In the preceding example, the elements will be printed at most 10 elements per second and 100 elements per 120
 * seconds.
 * <p>
 * If the host {@link Thread} is {@link Thread#interrupt() interrupted} while the returned {@link Iterable} is waiting
 * for the next available time slice, an {@link IterationInterruptedException} will immediately be thrown.
 * <p>
 * Note that the returned {@link Iterable} will never iterate faster than the specified rate limit, but the earliest
 * the next element is available will be dependent on the precision of the underlying instant supplier as well as any
 * overhead involved in producing the element from the original {@link Iterable}.
 *
 * @param <A> the {@link Iterable} element type
 */
public final class RateLimit<A> implements Fn4<Fn0<Instant>, Long, Duration, Iterable<A>, Iterable<A>> {

    private static final RateLimit<?> INSTANCE = new RateLimit<>();

    private RateLimit() {
    }

    @Override
    public Iterable<A> checkedApply(Fn0<Instant> instantFn0, Long limit, Duration duration, Iterable<A> as) {
        if (limit < 1)
            throw new IllegalArgumentException("Limit must be greater than 0: " + limit);

        return new RateLimitingIterable<>(as, singleton(tuple(limit, duration, instantFn0)));
    }

    @SuppressWarnings("unchecked")
    public static <A> RateLimit<A> rateLimit() {
        return (RateLimit<A>) INSTANCE;
    }

    public static <A> Fn3<Long, Duration, Iterable<A>, Iterable<A>> rateLimit(Fn0<Instant> instantFn0) {
        return RateLimit.<A>rateLimit().apply(instantFn0);
    }

    public static <A> Fn2<Duration, Iterable<A>, Iterable<A>> rateLimit(Fn0<Instant> instantFn0, Long limit) {
        return RateLimit.<A>rateLimit(instantFn0).apply(limit);
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> rateLimit(Fn0<Instant> instantFn0, Long limit, Duration duration) {
        return RateLimit.<A>rateLimit(instantFn0, limit).apply(duration);
    }

    public static <A> Iterable<A> rateLimit(Fn0<Instant> instantFn0, Long limit, Duration duration, Iterable<A> as) {
        return RateLimit.<A>rateLimit(instantFn0, limit, duration).apply(as);
    }
}
