package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

public final class Any<A> extends DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Boolean> {

    @Override
    public final Boolean apply(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        for (A a : as)
            if (predicate.apply(a))
                return true;

        return false;
    }

    public static <A> Any<A> any() {
        return new Any<A>();
    }

    public static <A> MonadicFunction<Iterable<A>, Boolean> any(MonadicFunction<? super A, Boolean> predicate) {
        return Any.<A>any().partial(predicate);
    }

    public static <A> Boolean any(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        return any(predicate).apply(as);
    }
}
