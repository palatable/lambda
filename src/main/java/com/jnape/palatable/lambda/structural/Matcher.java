package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;

@FunctionalInterface
public interface Matcher<A, B> extends Fn1<A, Maybe<B>> {

    static <A> Matcher<A, A> $(Function<? super A, Boolean> predicate) {
        return a -> just(a).filter(predicate);
    }

    static <A> Matcher<A, A> $(A a) {
        return $(eq(a));
    }

    static <A> Matcher<A, A> identity() {
        return Maybe::just;
    }
}
