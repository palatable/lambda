package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Not.not;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Find.find;

/**
 * A {@link Monoid} instance formed by <code>Boolean</code>. Equivalent to logical <code>&amp;&amp;</code>.
 *
 * @see Or
 * @see Monoid
 */
public final class And implements Monoid<Boolean>, BiPredicate<Boolean, Boolean> {

    private static final And INSTANCE = new And();

    private And() {
    }

    @Override
    public Boolean identity() {
        return true;
    }

    @Override
    public Boolean apply(Boolean x, Boolean y) {
        return x && y;
    }

    @Override
    public <B> Boolean foldMap(Function<? super B, ? extends Boolean> fn, Iterable<B> bs) {
        return find(not(fn), bs).fmap(constantly(false)).orElse(true);
    }

    @Override
    public boolean test(Boolean x, Boolean y) {
        return apply(x, y);
    }

    @Override
    public And flip() {
        return this;
    }

    public static And and() {
        return INSTANCE;
    }

    public static Fn1<Boolean, Boolean> and(Boolean x) {
        return and().apply(x);
    }

    public static Boolean and(Boolean x, Boolean y) {
        return and(x).apply(y);
    }
}
