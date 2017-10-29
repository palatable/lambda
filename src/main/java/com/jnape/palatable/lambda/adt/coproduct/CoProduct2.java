package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A generalization of the coproduct of two types <code>A</code> and <code>B</code>. Coproducts represent the disjoint
 * union of two or more distinct types, and provides an interface for specifying morphisms from those types to a common
 * result type.
 * <p>
 * Learn more about <a href="https://en.wikipedia.org/wiki/Coproduct">Coproducts</a>.
 *
 * @param <A> a type parameter representing the first possible type of this coproduct
 * @param <B> a type parameter representing the second possible type of this coproduct
 * @see Choice2
 * @see Either
 */
@FunctionalInterface
public interface CoProduct2<A, B, CP2 extends CoProduct2<A, B, ?>> {

    /**
     * Type-safe convergence requiring a match against all potential types.
     *
     * @param aFn morphism <code>A -&gt; R</code>
     * @param bFn morphism <code>B -&gt; R</code>
     * @param <R> result type
     * @return the result of applying the appropriate morphism to this coproduct's unwrapped value
     */
    <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn);

    /**
     * Diverge this coproduct by introducing another possible type that it could represent. As no morphisms can be
     * provided mapping current types to the new type, this operation merely acts as a convenience method to allow the
     * use of a more convergent coproduct with a more divergent one; that is, if a <code>CoProduct3&lt;String, Integer,
     * Boolean&gt;</code> is expected, a <code>CoProduct2&lt;String, Integer&gt;</code> should suffice.
     * <p>
     * Generally, we use inheritance to make this a non-issue; however, with coproducts of differing magnitudes, we
     * cannot guarantee variance compatibility in one direction conveniently at construction time, and in the other
     * direction, at all. A {@link CoProduct2} could not be a {@link CoProduct3} without specifying all type parameters
     * that are possible for a {@link CoProduct3} - more specifically, the third possible type - which is not
     * necessarily known at construction time, or even useful if never used in the context of a {@link CoProduct3}. The
     * inverse inheritance relationship - {@link CoProduct3} &lt; {@link CoProduct2} - is inherently unsound, as a
     * {@link CoProduct3} cannot correctly implement {@link CoProduct2#match}, given that the third type <code>C</code>
     * is always possible.
     * <p>
     * For this reason, there is a <code>diverge</code> method supported between all <code>CoProduct</code> types of
     * single magnitude difference.
     *
     * @param <C> the additional possible type of this coproduct
     * @return a coproduct of the initial types plus the new type
     */
    default <C> CoProduct3<A, B, C, ? extends CoProduct3<A, B, C, ?>> diverge() {
        return new CoProduct3<A, B, C, CoProduct3<A, B, C, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn) {
                return CoProduct2.this.match(aFn, bFn);
            }
        };
    }

    /**
     * Project this coproduct onto a tuple, such that the slot in the tuple that corresponds to this coproduct's value
     * is present, while the other slots are absent.
     *
     * @return a tuple of the coproduct projection
     */
    default Tuple2<Maybe<A>, Maybe<B>> project() {
        return match(a -> tuple(just(a), nothing()),
                     b -> tuple(nothing(), just(b)));
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the first slot value.
     *
     * @return an optional value representing the projection of the "a" type index
     */
    default Maybe<A> projectA() {
        return project()._1();
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the second slot value.
     *
     * @return an optional value representing the projection of the "b" type index
     */
    default Maybe<B> projectB() {
        return project()._2();
    }

    /**
     * Swap the type parameters.
     *
     * @return The inverted coproduct
     */
    default CoProduct2<B, A, ? extends CoProduct2<B, A, ?>> invert() {
        return new CoProduct2<B, A, CoProduct2<B, A, ?>>() {
            @Override
            public <R> R match(Function<? super B, ? extends R> aFn, Function<? super A, ? extends R> bFn) {
                return CoProduct2.this.match(bFn, aFn);
            }
        };
    }

    /**
     * Embed this coproduct inside another value; that is, given morphisms from this coproduct to <code>R</code>, apply
     * the appropriate morphism to this coproduct as a whole. Like {@link CoProduct2#match}, but without unwrapping the
     * value.
     *
     * @param aFn morphism <code>A v B -&gt; R</code>, applied in the <code>A</code> case
     * @param bFn morphism <code>A v B -&gt; R</code>, applied in the <code>B</code> case
     * @param <R> result type
     * @return the result of applying the appropriate morphism to this coproduct
     */
    @SuppressWarnings("unchecked")
    default <R> R embed(Function<? super CP2, ? extends R> aFn,
                        Function<? super CP2, ? extends R> bFn) {
        return this.<Fn1<CP2, R>>match(constantly(fn1(aFn)),
                                       constantly(fn1(bFn)))
                .apply((CP2) this);
    }
}
