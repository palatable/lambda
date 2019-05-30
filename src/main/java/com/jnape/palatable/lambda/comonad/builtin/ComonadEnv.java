package com.jnape.palatable.lambda.comonad.builtin;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Env;

/**
 * An interface for an Env {@link Comonad}, which provides some additional context through {@link ComonadEnv#ask}.
 * In the simplest case, for {@link Env}, this is a {@link Product2} with the additional semantics that {@link Product2#_1}
 * is the environment, and {@link Product2#_2} the value extracted from the Comonad.
 *
 * @param <E> the environment type
 * @param <A> the value type
 */
public interface ComonadEnv<E, A, W extends Comonad<?, W>> extends Comonad<A, W>, Product2<E, A> {
    /**
     * Retrieve the environment.
     *
     * @return the environment of type E
     */
    E ask();

    /**
     * Retrieve a mapped environment.
     *
     * @param <R> the return type of the environment
     * @param f   the mapping function over the environment
     * @return    the new environment R
     */
    default <R> R asks(Fn1<? super E, ? extends R> f) {
        return f.apply(this.ask());
    }

    /**
     * Map the environment type of a ComonadEnv.
     *
     * @param f   the mapping function of the environment
     * @param <R> the new environment type
     * @return    a new instance of a ComonadEnv with environment R and the same value A as previously
     */
    <R> ComonadEnv<R, A, ?> mapEnv(Fn1<? super E, ? extends R> f);

    /**
     * Retrieve the environment as the first element.
     */
    default E _1() {
        return ask();
    }

    /**
     * Retrieve the comonadic extraction value as the second element.
     *
     * @return the second element
     */
    default A _2() {
        return extract();
    }
}
