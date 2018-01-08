package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.adt.Maybe;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;

public final class Matchers {

    private Matchers() {
    }


    public static <A> Matcher<Maybe<A>, A> $just(A a) {
        return $just(eq(a));
    }

    public static <A> Matcher<Maybe<A>, A> $just(Function<? super A, Boolean> predicate) {
        return $just(Matcher.$(predicate));
    }

    public static <A, B> Matcher<Maybe<A>, B> $just(Matcher<A, B> matcher) {
        return maybeA -> maybeA.flatMap(matcher);
    }

    public static <A> Matcher<Maybe<A>, A> $just() {
        return $just(Any.$__());
    }

    public static final class Any<A> implements Matcher<A, A> {

        public static final Any INSTANCE = new Any<>();

        private Any() {
        }

        @Override
        public Maybe<A> apply(A a) {
            return just(a);
        }


        @SuppressWarnings("unchecked")
        public static <A> Any<A> $__() {
            return INSTANCE;
        }
    }
}
