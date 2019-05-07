package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.Fn1;

/**
 * The contravariant functor (or "co-functor"); that is, a functor that maps contravariantly (<code>A &lt;- B</code>)
 * over its parameter.
 * Contravariant functors are not necessarily {@link Functor}s.
 * <p>
 * For more information, read about
 * <a href="https://hackage.haskell.org/package/contravariant-1.4/docs/Data-Functor-Contravariant.html"
 * target="_top">Contravariant Functors</a>.
 *
 * @param <A> the type of the parameter
 * @param <C> the unification parameter
 * @see Profunctor
 */
public interface Contravariant<A, C extends Contravariant<?, C>> {

    /**
     * Contravariantly map <code>A &lt;- B</code>.
     *
     * @param fn  the mapping function
     * @param <B> the new parameter type
     * @return the mapped Contravariant functor instance
     */
    <B> Contravariant<B, C> contraMap(Fn1<? super B, ? extends A> fn);
}
