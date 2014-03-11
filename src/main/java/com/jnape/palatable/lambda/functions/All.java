package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;

public final class All<A> extends DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Boolean> {

    @Override
    public final Boolean apply(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        for (A a : as)
            if (!predicate.apply(a))
                return false;

        return true;
    }

    public static <A> All<A> all() {
        return new All<A>();
    }

    public static <A> MonadicFunction<Iterable<A>, Boolean> all(MonadicFunction<? super A, Boolean> predicate) {
        return All.<A>all().partial(predicate);
    }

    public static <A> boolean all(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        return all(predicate).apply(as);
    }
}
