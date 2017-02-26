package com.jnape.palatable.lambda.recursionschemes;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.builtin.Histomorphism;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

/**
 * The combination of the least fixed point of a given functor and a value annotation. Useful for adding additional
 * information to individual levels of nesting for memoization.
 *
 * @param <F>       the functor unification type
 * @param <A>       the annotation value type
 * @param <CofreeF> the type corresponding to the unfixed functor
 * @see Histomorphism
 */
public interface Cofree<F extends Functor, A, CofreeF extends Functor<?, F>> {

    /**
     * Produce the internal structure represented by this cofree.
     *
     * @return a tuple of the functor and the value
     */
    Tuple2<CofreeF, A> uncofree();

    /**
     * Convenience method for extracting the annotation value from teh internal structure of this cofree.
     *
     * @return the annotation value
     */
    default A attr() {
        return uncofree()._2();
    }

    /**
     * Fix and attach an annotation value to a {@link Functor}.
     *
     * @param f         the unfixed functor
     * @param a         the annotation value
     * @param <A>       the annotation value type
     * @param <F>       the functor unification tyep
     * @param <CofreeF> the type corresponding to the unfixed functor
     * @return a cofree joining the fixed functor to the annotation value
     */
    static <A, F extends Functor, CofreeF extends Functor<? extends Cofree<F, A, ?>, F>> Cofree<F, A, CofreeF> cofree(
            CofreeF f, A a) {
        return () -> tuple(f, a);
    }
}
