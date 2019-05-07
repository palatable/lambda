package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

/**
 * Negate a predicate function.
 *
 * @param <A> the input argument type
 */
public final class Not<A> implements BiPredicate<Fn1<? super A, ? extends Boolean>, A> {
    private static final Not<?> INSTANCE = new Not<>();

    private Not() {
    }

    @Override
    public Boolean checkedApply(Fn1<? super A, ? extends Boolean> pred, A a) {
        return !pred.apply(a);
    }

    @SuppressWarnings("unchecked")
    public static <A> Not<A> not() {
        return (Not<A>) INSTANCE;
    }

    public static <A> Predicate<A> not(Fn1<? super A, ? extends Boolean> pred) {
        return Not.<A>not().apply(pred);
    }

    public static <A> Boolean not(Fn1<? super A, ? extends Boolean> pred, A a) {
        return not(pred).apply(a);
    }
}
