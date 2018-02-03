package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.structural.Matcher.$;

public final class Matchers {

    private Matchers() {
    }

    public static <L, R, R2> Matcher<Either<L, R>, R2> $right(Matcher<R, R2> matcher) {
        return either -> either.toMaybe().flatMap(matcher::match);
    }

    public static <L, R> Matcher<Either<L, R>, R> $right(Function<? super R, Boolean> predicate) {
        return $right($(predicate));
    }

    public static <L, R> Matcher<Either<L, R>, R> $right(R r) {
        return $right(eq(r));
    }

    public static <L, R> Matcher<Either<L, R>, R> $right() {
        return $right($());
    }

    public static <A> Matcher<Maybe<A>, A> $just(A a) {
        return $just(eq(a));
    }

    public static <A> Matcher<Maybe<A>, A> $just(Function<? super A, Boolean> predicate) {
        return $just($(predicate));
    }

    public static <A, B> Matcher<Maybe<A>, B> $just(Matcher<A, B> matcher) {
        return maybeA -> maybeA.flatMap(matcher::match);
    }

    public static <A> Matcher<Maybe<A>, A> $just() {
        return $just($());
    }

    public static <A> Matcher<Maybe<A>, Maybe<A>> $nothing() {
        return x -> x.fmap(Maybe::just);
    }
}
