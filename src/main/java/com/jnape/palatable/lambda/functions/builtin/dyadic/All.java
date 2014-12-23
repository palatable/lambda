package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

public final class All<A> implements DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Boolean> {

    @Override
    public final Boolean apply(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        for (A a : as)
            if (!predicate.apply(a))
                return false;

        return true;
    }

    public static <A> All<A> all() {
        return new All<>();
    }

    public static <A> MonadicFunction<Iterable<A>, Boolean> all(MonadicFunction<? super A, Boolean> predicate) {
        return All.<A>all().apply(predicate);
    }

    public static <A> boolean all(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        return all(predicate).apply(as);
    }
}
