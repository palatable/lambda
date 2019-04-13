package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;

/**
 * The {@link Monoid} instance formed under mutative concatenation for an arbitrary {@link Collection}. The collection
 * subtype (<code>C</code>) must support {@link Collection#addAll(Collection)}.
 * <p>
 * Note that the result is a new collection, and the inputs to this monoid are left unmodified.
 *
 * @see Monoid
 */
public final class AddAll<A, C extends Collection<A>> implements MonoidFactory<Supplier<C>, C> {

    private static final AddAll<?,?> INSTANCE = new AddAll<>();

    private AddAll() {
    }

    @Override
    public Monoid<C> apply(Supplier<C> cSupplier) {
        return new Monoid<C>() {
            @Override
            public C identity() {
                return cSupplier.get();
            }

            @Override
            public C apply(C xs, C ys) {
                C c = identity();
                c.addAll(xs);
                c.addAll(ys);
                return c;
            }

            @Override
            public <B> C foldMap(Function<? super B, ? extends C> fn, Iterable<B> bs) {
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

    public static <A, C extends Collection<A>> Monoid<C> addAll(Supplier<C> collectionSupplier) {
        return AddAll.<A, C>addAll().apply(collectionSupplier);
    }

    public static <A, C extends Collection<A>> Fn1<C, C> addAll(Supplier<C> collectionSupplier, C xs) {
        return addAll(collectionSupplier).apply(xs);
    }

    public static <A, C extends Collection<A>> C addAll(Supplier<C> collectionSupplier, C xs, C ys) {
        return addAll(collectionSupplier, xs).apply(ys);
    }
}
