package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.traversable.Traversable;
import com.jnape.palatable.lambda.zipper.Zipper;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;

public final class Next<A, TA extends Traversable<A, ?>> implements Fn1<Zipper<A, TA>, Zipper<A, TA>> {

    private static final Next INSTANCE = new Next();

    private Next() {
    }

    @Override
    public Zipper<A, TA> apply(Zipper<A, TA> zipper) {
        return zipper.match(Zipper::top, n -> n._2().apply(nothing()));
    }

    @SuppressWarnings("unchecked")
    public static <A, TA extends Traversable<A, ?>> Next<A, TA> next() {
        return INSTANCE;
    }

    public static <A, TA extends Traversable<A, ?>> Zipper<A, TA> next(Zipper<A, TA> zipper) {
        return Next.<A, TA>next().apply(zipper);
    }
}
