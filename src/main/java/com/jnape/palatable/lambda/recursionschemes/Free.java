package com.jnape.palatable.lambda.recursionschemes;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Anamorphism.ana;

/**
 * The free monad. <code>Free</code> can be thought of as adding the capabilities of a pattern functor to a functor
 * that has no zero construction -- that is, a functor that has no constructions for which <code>f.fmap(fn) ==
 * f</code>.
 * <p>
 * Consider the example where some functor <code>f</code> has no zero construction; fixing <code>f</code> is rendered
 * impossible by construction, since there can be no terminating expression. Conversely, {@link Free#pure} provides an
 * escape-hatch to terminate the expression recursion, allowing construction of fix-like expressions for functors that
 * otherwise would not support this.
 *
 * @param <A>     the type of the expression-terminating pure value
 * @param <F>     the functor unification type
 * @param <FreeF> the type corresponding to the functor that wraps Free
 */
public interface Free<A, F extends Functor, FreeF extends Functor<?, F>> extends CoProduct2<A, FreeF> {

    /**
     * Given a {@link Coalgebra}, recursively fold this <code>Free</code> into a {@link Fix}. Note that this is only
     * achievable for pattern functors, as the pure anamorphism will only terminate once a zero construction is reached.
     *
     * @param coalgebra the coalgebra to use to convert a pure value into a Fix
     * @param <FA>      the functor type the coalgebra unwraps the value from
     * @return the fix version of this free
     */
    @SuppressWarnings("unchecked")
    default <FA extends Functor<A, F>> Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fix(
            Coalgebra<A, FA> coalgebra) {
        return match(a -> ana(coalgebra, a),
                     freeF -> Fix.fix(freeF.fmap(x1 -> ((Free<A, F, FreeF>) x1).fix(coalgebra))));
    }

    /**
     * The recursive expression in free construction, using a functor.
     *
     * @param f       the functor
     * @param <A>     the pure parameter type
     * @param <F>     the functor unification type
     * @param <FreeF> the type corresponding to the functor that wraps Free
     * @return an instance of Free over this functor
     */
    static <A, F extends Functor, FreeF extends Functor<? extends Free<A, F, ?>, F>> Free<A, F, FreeF> free(
            FreeF f) {
        return new Free<A, F, FreeF>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super FreeF, ? extends R> bFn) {
                return bFn.apply(f);
            }

            @Override
            public boolean equals(Object other) {
                return other instanceof Free && ((Free<?, ?, ?>) other).match(constantly(false), f::equals);
            }

            @Override
            public int hashCode() {
                return 31 + f.hashCode();
            }
        };
    }

    /**
     * The terminating expression in free construction, using a value.
     *
     * @param a       the pure value
     * @param <A>     the pure parameter type
     * @param <F>     the functor unification type
     * @param <FreeF> the type corresponding to the functor that wraps Free
     * @return an instance of Free over this value
     */
    static <A, F extends Functor, FreeF extends Functor<?, F>> Free<A, F, FreeF> pure(A a) {
        return new Free<A, F, FreeF>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super FreeF, ? extends R> bFn) {
                return aFn.apply(a);
            }

            @Override
            public boolean equals(Object other) {
                return other instanceof Free && ((Free<?, ?, ?>) other).match(a::equals, constantly(false));
            }

            @Override
            public int hashCode() {
                return 31 + a.hashCode();
            }
        };

    }
}
