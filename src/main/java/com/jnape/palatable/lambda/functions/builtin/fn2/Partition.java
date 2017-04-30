package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;

/**
 * Given an <code>Iterable&lt;A&gt;</code> <code>as</code> and a disjoint mapping function <code>a -&gt;
 * CoProduct2&lt;A, B&gt;</code>, return a {@link Tuple2} over the lazily unwrapped left <code>A</code> and right
 * <code>B</code> values in the first and second slots, respectively. Note that while the tuple must be constructed
 * eagerly, the left and right iterables contained therein are both lazy, so comprehension over infinite iterables is
 * supported.
 *
 * @param <A> A type contravariant to the input Iterable element type
 * @param <B> The output left Iterable element type, as well as the CoProduct2 A type
 * @param <C> The output right Iterable element type, as well as the CoProduct2 B type
 * @see CoProduct2
 */
public final class Partition<A, B, C> implements Fn2<Function<? super A, ? extends CoProduct2<B, C, ?>>, Iterable<A>, Tuple2<Iterable<B>, Iterable<C>>> {

    private static final Partition INSTANCE = new Partition();

    private Partition() {
    }

    @Override
    public Tuple2<Iterable<B>, Iterable<C>> apply(Function<? super A, ? extends CoProduct2<B, C, ?>> function,
                                                  Iterable<A> as) {
        Iterable<CoProduct2<B, C, ?>> coproducts = map(function, as);

        Iterable<B> lefts = map(Optional::get, filter(Optional::isPresent, map(CoProduct2::projectA, coproducts)));
        Iterable<C> rights = map(Optional::get, filter(Optional::isPresent, map(CoProduct2::projectB, coproducts)));

        return tuple(lefts, rights);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C> Partition<A, B, C> partition() {
        return INSTANCE;
    }

    public static <A, B, C> Fn1<Iterable<A>, Tuple2<Iterable<B>, Iterable<C>>> partition(
            Function<? super A, ? extends CoProduct2<B, C, ?>> function) {
        return Partition.<A, B, C>partition().apply(function);
    }

    public static <A, B, C> Tuple2<Iterable<B>, Iterable<C>> partition(
            Function<? super A, ? extends CoProduct2<B, C, ?>> function,
            Iterable<A> as) {
        return Partition.<A, B, C>partition(function).apply(as);
    }
}
