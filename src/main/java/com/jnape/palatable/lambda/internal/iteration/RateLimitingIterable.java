package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn0;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class RateLimitingIterable<A> implements Iterable<A> {
    private final Iterable<A>                               as;
    private final Set<Tuple3<Long, Duration, Fn0<Instant>>> rateLimits;

    public RateLimitingIterable(Iterable<A> as, Set<Tuple3<Long, Duration, Fn0<Instant>>> rateLimits) {
        Set<Tuple3<Long, Duration, Fn0<Instant>>> combinedRateLimits = new HashSet<>(rateLimits);
        if (as instanceof RateLimitingIterable<A> inner) {
            combinedRateLimits.addAll(inner.rateLimits);
            as = inner.as;
        }
        this.rateLimits = combinedRateLimits;
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        return new RateLimitingIterator<>(as.iterator(), rateLimits);
    }
}
