package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.adt.Try;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn0;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.jnape.palatable.lambda.adt.Try.failure;
import static com.jnape.palatable.lambda.adt.Try.trying;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Size.size;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GTE.gte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static com.jnape.palatable.lambda.monad.Monad.join;
import static com.jnape.palatable.lambda.semigroup.builtin.Max.max;
import static java.lang.Thread.sleep;
import static java.util.Collections.emptyList;

public final class RateLimitingIterator<A> implements Iterator<A> {
    private final Iterator<A>                                              asIterator;
    private final Set<Tuple3<Long, Duration, Fn0<Instant>>>                rateLimits;
    private final Map<Tuple3<Long, Duration, Fn0<Instant>>, List<Instant>> timeSlicesByRateLimit;

    public RateLimitingIterator(Iterator<A> asIterator, Set<Tuple3<Long, Duration, Fn0<Instant>>> rateLimits) {
        this.asIterator = asIterator;
        this.rateLimits = rateLimits;
        timeSlicesByRateLimit = new HashMap<>();
    }

    @Override
    public boolean hasNext() {
        return asIterator.hasNext();
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();
        awaitNextTimeSlice();
        return asIterator.next();
    }

    private void awaitNextTimeSlice() {
        rateLimits.forEach(rateLimit -> {
            awaitNextTimeSliceForRateLimit(rateLimit);
            List<Instant> timeSlicesForRateLimit = timeSlicesByRateLimit.getOrDefault(rateLimit, new ArrayList<>());
            timeSlicesForRateLimit.add(rateLimit._3().apply());
            timeSlicesByRateLimit.put(rateLimit, timeSlicesForRateLimit);
        });
    }

    private void awaitNextTimeSliceForRateLimit(Tuple3<Long, Duration, Fn0<Instant>> rateLimit) {
        while (rateLimitExhaustedInTimeSlice(rateLimit)) {
            join(trying(() -> sleep(0))
                         .fmap(Try::success)
                         .catching(InterruptedException.class, t -> failure(new IterationInterruptedException(t))))
                    .orThrow();
        }
    }

    private boolean rateLimitExhaustedInTimeSlice(Tuple3<Long, Duration, Fn0<Instant>> rateLimit) {
        List<Instant> timeSlicesForRateLimit = timeSlicesByRateLimit.getOrDefault(rateLimit, emptyList());
        return rateLimit.into((limit, duration, instantSupplier) -> {
            Instant timeSliceEnd         = instantSupplier.apply();
            Instant previousTimeSliceEnd = timeSliceEnd.minus(duration);
            timeSlicesForRateLimit.removeIf(lt(previousTimeSliceEnd).toPredicate());
            return max(0L, limit - size(filter(mark -> lte(mark, previousTimeSliceEnd) && gte(mark, timeSliceEnd), timeSlicesForRateLimit))) == 0;
        });
    }

}
