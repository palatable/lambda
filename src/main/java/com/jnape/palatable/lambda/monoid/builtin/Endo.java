package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.monoid.Monoid;

import static com.jnape.palatable.lambda.functions.Fn2.fn2;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * A {@link Monoid} formed by {@link Fn1} under composition.
 *
 * @param <A> the input/output type to the {@link Fn1}
 */
public final class Endo<A> implements Monoid<Fn1<A, A>> {

    private static final Endo<?> INSTANCE = new Endo<>();

    private Endo() {
    }

    @Override
    public Fn1<A, A> identity() {
        return id();
    }

    public A apply(Fn1<A, A> f, Fn1<A, A> g, A a) {
        return apply(f, g).apply(a);
    }

    @Override
    public Fn1<A, A> apply(Fn1<A, A> f, Fn1<A, A> g) {
        return f.fmap(g);
    }

    @Override
    public Fn2<Fn1<A, A>, A, A> apply(Fn1<A, A> f) {
        return fn2(Monoid.super.apply(f));
    }

    @SuppressWarnings("unchecked")
    public static <A> Endo<A> endo() {
        return (Endo<A>) INSTANCE;
    }

    public static <A> Fn2<Fn1<A, A>, A, A> endo(Fn1<A, A> f) {
        return Endo.<A>endo().apply(f);
    }

    public static <A> Fn1<A, A> endo(Fn1<A, A> f, Fn1<A, A> g) {
        return endo(f).apply(g);
    }

    public static <A> A endo(Fn1<A, A> f, Fn1<A, A> g, A a) {
        return endo(f, g).apply(a);
    }
}
