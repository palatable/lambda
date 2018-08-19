package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.CatMaybes.catMaybes;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;

/**
 * A {@link Monoid} instance formed by <code>{@link Maybe}&lt;A&gt;</code>. The application to two {@link Maybe} values
 * produces the first non-empty value, or {@link Maybe#nothing()} if all values are empty.
 *
 * @param <A> the Maybe value parameter type
 * @see Last
 * @see Present
 * @see Monoid
 * @see Maybe
 */
public final class First<A> implements Monoid<Maybe<A>> {

    private static final First INSTANCE = new First();

    private First() {
    }

    @Override
    public Maybe<A> identity() {
        return nothing();
    }

    @Override
    public Maybe<A> apply(Maybe<A> x, Maybe<A> y) {
        return x.fmap(Maybe::just).orElse(y);
    }

    @Override
    public <B> Maybe<A> foldMap(Function<? super B, ? extends Maybe<A>> fn, Iterable<B> bs) {
        return head(catMaybes(map(fn, bs)));
    }

    @SuppressWarnings("unchecked")
    public static <A> First<A> first() {
        return INSTANCE;
    }

    public static <A> Fn1<Maybe<A>, Maybe<A>> first(Maybe<A> x) {
        return First.<A>first().apply(x);
    }

    public static <A> Maybe<A> first(Maybe<A> x, Maybe<A> y) {
        return first(x).apply(y);
    }
}
