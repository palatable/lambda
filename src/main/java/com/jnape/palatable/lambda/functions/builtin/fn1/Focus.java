package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.traversable.Traversable;
import com.jnape.palatable.lambda.zipper.Zipper;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

public final class Focus<A, TA extends Traversable<A, ?>> implements Fn1<Zipper<A, TA>, Maybe<A>> {

    private static final Focus INSTANCE = new Focus();

    private Focus() {
    }

    @Override
    public Maybe<A> apply(Zipper<A, TA> zipper) {
        return zipper.match(constantly(nothing()), t -> just(t._1()));
    }

    @SuppressWarnings("unchecked")
    public static <A, TA extends Traversable<A, ?>> Focus<A, TA> focus() {
        return INSTANCE;
    }

    public static <A, TA extends Traversable<A, ?>> Maybe<A> focus(Zipper<A, TA> zipper) {
        return Focus.<A, TA>focus().apply(zipper);
    }
}
