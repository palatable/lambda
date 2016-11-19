package com.jnape.palatable.lambda.monoid;

import com.jnape.palatable.lambda.functions.builtin.fn2.Map;
import com.jnape.palatable.lambda.functions.builtin.fn2.ReduceLeft;
import com.jnape.palatable.lambda.functions.builtin.fn2.ReduceRight;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;

/**
 * A {@link Monoid} is the pairing of a {@link Semigroup} with an identity element.
 *
 * @param <A> the element type this Monoid is formed over
 */
public interface Monoid<A> extends Semigroup<A> {

    /**
     * The identity element of this monoid.
     *
     * @return the identity
     */
    A identity();

    /**
     * Catamorphism under this monoid using {@link ReduceLeft}, where the result is the reduction, or, if empty, the
     * identity of this monoid.
     *
     * @param as the elements to reduce
     * @return the reduction, or {@link Monoid#identity} if empty
     * @see ReduceLeft
     */
    default A reduceLeft(Iterable<A> as) {
        return ReduceLeft.reduceLeft(toBiFunction(), as).orElse(identity());
    }

    /**
     * Catamorphism under this monoid using {@link ReduceRight}, where the result is the reduction, or, if empty, the
     * identity of this monoid.
     *
     * @param as an Iterable of elements in this monoid
     * @return the reduction, or {@link Monoid#identity} if empty
     * @see ReduceRight
     */
    default A reduceRight(Iterable<A> as) {
        return ReduceRight.reduceRight(toBiFunction(), as).orElse(identity());
    }

    /**
     * Homomorphism combined with catamorphism. Convert an <code>Iterable&lt;B&gt;</code> to an
     * <code>Iterable&lt;A&gt;</code> (that is, an <code>Iterable</code> of elements this monoid is formed over), then
     * reduce the result from left to right. Under algebraic data types, this is isomorphic to a flatMap.
     *
     * @param fn  the mapping function from A to B
     * @param bs  the Iterable of Bs
     * @param <B> the input Iterable element type
     * @return the folded result under this Monoid
     * @see Map
     * @see Monoid#reduceLeft(Iterable)
     */
    default <B> A foldMap(Function<? super B, ? extends A> fn, Iterable<B> bs) {
        return reduceLeft(map(fn, bs));
    }

    /**
     * Promote a {@link Semigroup} to a {@link Monoid} by supplying an identity element.
     *
     * @param semigroup the semigroup
     * @param identity  the identity element
     * @param <A>       the element type of this monoid
     * @return the monoid
     */
    static <A> Monoid<A> monoid(Semigroup<A> semigroup, A identity) {
        return new Monoid<A>() {
            @Override
            public A identity() {
                return identity;
            }

            @Override
            public A apply(A x, A y) {
                return semigroup.apply(x, y);
            }
        };
    }
}
