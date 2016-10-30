package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;

/**
 * Given an <code>Iterable&lt;A&gt;</code> <code>as</code> and a disjoint mapping function <code>a -> Either&lt;L,
 * R&gt;</code>, return a {@link Tuple2} over the lazily unwrapped left <code>L</code> and right <code>R</code> values
 * in the first and second slots, respectively. Note that while the tuple must be constructed eagerly, the left and
 * right iterables contained therein are both lazy, so comprehension over infinite iterables is supported.
 *
 * @param <A> A type contravariant to the input Iterable element type
 * @param <B> The output left Iterable element type, as well as the Either L type
 * @param <C> The output right Iterable element type, as well as the Either R type
 * @see Either
 */
public final class Partition<A, B, C> implements Fn2<Function<? super A, ? extends Either<B, C>>, Iterable<A>, Tuple2<Iterable<B>, Iterable<C>>> {

    private Partition() {
    }

    @Override
    public Tuple2<Iterable<B>, Iterable<C>> apply(Function<? super A, ? extends Either<B, C>> function,
                                                  Iterable<A> as) {
        Iterable<Either<B, C>> eithers = map(function, as);

        Iterable<B> lefts = unwrapRight(map(Either::invert, eithers));
        Iterable<C> rights = unwrapRight(map(id(), eithers));

        return tuple(lefts, rights);
    }

    private <X> Iterable<X> unwrapRight(Iterable<Either<?, X>> eithers) {
        return map(Optional::get, filter(Optional::isPresent, map(Either::toOptional, eithers)));
    }

    public static <A, B, C> Partition<A, B, C> partition() {
        return new Partition<>();
    }

    public static <A, B, C> Fn1<Iterable<A>, Tuple2<Iterable<B>, Iterable<C>>> partition(
            Function<? super A, ? extends Either<B, C>> function) {
        return Partition.<A, B, C>partition().apply(function);
    }

    public static <A, B, C> Tuple2<Iterable<B>, Iterable<C>> partition(
            Function<? super A, ? extends Either<B, C>> function,
            Iterable<A> as) {
        return Partition.<A, B, C>partition(function).apply(as);
    }
}
