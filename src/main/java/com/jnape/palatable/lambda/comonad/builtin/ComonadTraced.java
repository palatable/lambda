package com.jnape.palatable.lambda.comonad.builtin;

import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.functor.builtin.Traced;
import com.jnape.palatable.lambda.monoid.Monoid;

/**
 * An interface for an Traced {@link Comonad}, which monoidally combines input to a trace function through a {@link Monoid} instance.
 * A concrete class is provided in {@link Traced}.
 *
 * @param <A> the type of the input values to the trace function
 * @param <B> the type of the output from the trace function
 */
public interface ComonadTraced<A, M extends Monoid<A>, B, W extends Comonad<?, W>> extends Comonad<B, W> {
    /**
     * Run a provided trace function.
     *
     * @param a  the value to provide to the tracer.
     * @return   the value output by the trace function
     */
    B runTrace(A a);

    /**
     * Retrieve the {@link Monoid} instance for the input type A.
     *
     * @return  a {@link Monoid} instance for A
     */
    Monoid<A> getMonoid();

    /**
     * {@inheritDoc}
     */
    @Override
    default B extract() {
        return runTrace(getMonoid().identity());
    }
}
