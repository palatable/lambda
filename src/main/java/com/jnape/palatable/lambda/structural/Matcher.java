package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.adt.Maybe;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;

@FunctionalInterface
public interface Matcher<A, B> {

    Maybe<B> match(A a);

    static <A> Matcher<A, A> $(Function<? super A, Boolean> predicate) {
        return a -> just(a).filter(predicate);
    }

    @SuppressWarnings("unchecked")
    static <A> Any<A> $() {
        return Any.INSTANCE;
    }

    static <A> Matcher<A, A> $(A a) {
        return $(eq(a));
    }

    static <A> Matcher<A, A> identity() {
        return Maybe::just;
    }

    public static final class Any<A> implements Matcher<A, A> {

        private static final Any INSTANCE = new Any();

        private Any() {
        }

        @Override
        public Maybe<A> match(A a) {
            return just(a);
        }
    }
}
