package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;

import java.util.function.Function;

/**
 * A function taking a single argument. This is the core function type that all other function types extend and
 * auto-curry with.
 *
 * @param <A> The argument type
 * @param <B> The result type
 */
@FunctionalInterface
public interface Fn1<A, B> extends Functor<B>, Profunctor<A, B>, Function<A, B> {

    /**
     * Invoke this function with the given argument.
     *
     * @param a the argument
     * @return the result of the function application
     */
    B apply(A a);

    /**
     * Left-to-right composition, such that <code>g.then(f).apply(x)</code> is equivalent to
     * <code>f.apply(g.apply(x))</code>.
     *
     * @param f   the function to invoke with this function's return value
     * @param <C> the return type of the next function to invoke
     * @return a function representing the composition of this function and f
     */
    default <C> Fn1<A, C> then(Fn1<? super B, ? extends C> f) {
        return fmap(f);
    }

    /**
     * Also left-to-right composition (<a href="http://jnape.com/the-perils-of-implementing-functor-in-java/">sadly</a>).
     *
     * @param f   the function to invoke with this function's return value
     * @param <C> the return type of the next function to invoke
     * @return a function representing the composition of this function and f
     * @see Fn1#then(Fn1)
     */
    @Override
    default <C> Fn1<A, C> fmap(Fn1<? super B, ? extends C> f) {
        return a -> f.apply(apply(a));
    }

    /**
     * Contravariantly map over the argument to this function, producing a function that takes the new argument type,
     * and produces the same result.
     *
     * @param fn  the contravariant argument mapping function
     * @param <Z> the new argument type
     * @return a new function from Z (the new argument type) to B (the same result)
     */
    @Override
    default <Z> Fn1<Z, B> diMapL(Fn1<Z, A> fn) {
        return (Fn1<Z, B>) Profunctor.super.diMapL(fn);
    }

    /**
     * Covariantly map over the return value of this function, producing a function that takes the same argument, and
     * produces the new result type.
     *
     * @param fn  the covariant result mapping function
     * @param <C> the new result type
     * @return a new function from A (the same argument type) to C (the new result type)
     */
    @Override
    default <C> Fn1<A, C> diMapR(Fn1<B, C> fn) {
        return (Fn1<A, C>) Profunctor.super.diMapR(fn);
    }

    /**
     * Exercise both <code>diMapL</code> and <code>diMapR</code> over this function in the same invocation.
     *
     * @param lFn the contravariant argument mapping function
     * @param rFn the covariant result mapping function
     * @param <Z> the new argument type
     * @param <C> the new result type
     * @return a new function from Z (the new argument type) to C (the new result type)
     */
    @Override
    default <Z, C> Fn1<Z, C> diMap(Fn1<Z, A> lFn, Fn1<B, C> rFn) {
        return lFn.andThen(this).andThen(rFn);
    }

    /**
     * Override of {@link Function#compose(Function)}, returning an instance of <code>Fn1</code> for compatibility.
     * Right-to-left composition.
     *
     * @param before the function who's return value is this function's argument
     * @param <Z>    the new argument type
     * @return a new function from Z (the new argument type) to B (the same result type)
     */
    @Override
    default <Z> Fn1<Z, B> compose(Function<? super Z, ? extends A> before) {
        return z -> apply(before.apply(z));
    }

    /**
     * Override of {@link Function#andThen(Function)}, returning an instance of <code>Fn1</code> for compatibility.
     * Left-to-right composition.
     *
     * @param after the function to invoke on this function's return value
     * @param <C>   the new result type
     * @return a new function from A (the same argument type) to C (the new result type)
     */
    @Override
    default <C> Fn1<A, C> andThen(Function<? super B, ? extends C> after) {
        return a -> after.apply(apply(a));
    }
}
