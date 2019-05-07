package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.Collection;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;

/**
 * The {@link Monoid} instance formed under mutative concatenation for an arbitrary {@link Collection}. The collection
 * subtype (<code>C</code>) must support {@link Collection#addAll(Collection)}.
 * <p>
 * Note that the result is a new collection, and the inputs to this monoid are left unmodified.
 *
 * @see Monoid
 */
public final class AddAll<A, C extends Collection<A>> implements MonoidFactory<Fn0<C>, C> {

    private static final AddAll<?, ?> INSTANCE = new AddAll<>();

    private AddAll() {
    }

    @Override
    public Monoid<C> checkedApply(Fn0<C> cFn0) {
        return new Monoid<C>() {
            @Override
            public C identity() {
                return cFn0.apply();
            }

            @Override
            public C checkedApply(C xs, C ys) {
                C c = identity();
                c.addAll(xs);
                c.addAll(ys);
                return c;
            }

            @Override
            public <B> C foldMap(Fn1<? super B, ? extends C> fn, Iterable<B> bs) {
                return FoldLeft.foldLeft((x, y) -> {
                    x.addAll(y);
                    return x;
                }, identity(), map(fn, bs));
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <A, C extends Collection<A>> AddAll<A, C> addAll() {
        return (AddAll<A, C>) INSTANCE;
    }

    public static <A, C extends Collection<A>> Monoid<C> addAll(Fn0<C> collectionFn0) {
        return AddAll.<A, C>addAll().apply(collectionFn0);
    }

    public static <A, C extends Collection<A>> Fn1<C, C> addAll(Fn0<C> collectionFn0, C xs) {
        return addAll(collectionFn0).apply(xs);
    }

    public static <A, C extends Collection<A>> C addAll(Fn0<C> collectionFn0, C xs, C ys) {
        return addAll(collectionFn0, xs).apply(ys);
    }
}
