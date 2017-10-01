package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Profunctor;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.Fn2.fn2;

/**
 * A function taking a single argument. This is the core function type that all other function types extend and
 * auto-curry with.
 *
 * @param <A> The argument type
 * @param <B> The result type
 */
@FunctionalInterface
public interface Fn1<A, B> extends Applicative<B, Fn1<A, ?>>, Profunctor<A, B, Fn1>, Function<A, B> {

    /**
     * Invoke this function with the given argument.
     *
     * @param a the argument
     * @return the result of the function application
     */
    B apply(A a);

    /**
     * Also left-to-right composition (<a href="http://jnape.com/the-perils-of-implementing-functor-in-java/">sadly</a>).
     *
     * @param <C> the return type of the next function to invoke
     * @param f   the function to invoke with this function's return value
     * @return a function representing the composition of this function and f
     */
    @Override
    default <C> Fn1<A, C> fmap(Function<? super B, ? extends C> f) {
        return Applicative.super.<C>fmap(f).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, C> pure(C c) {
        return __ -> c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, C> zip(Applicative<Function<? super B, ? extends C>, Fn1<A, ?>> appFn) {
        return a -> appFn.<Fn1<A, Function<? super B, ? extends C>>>coerce().apply(a).apply(apply(a));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    default <C> Fn1<A, C> zip(Fn2<A, B, C> appFn) {
        return zip((Fn1<A, Function<? super B, ? extends C>>) (Object) appFn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, C> discardL(Applicative<C, Fn1<A, ?>> appB) {
        return Applicative.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Fn1<A, B> discardR(Applicative<C, Fn1<A, ?>> appB) {
        return Applicative.super.discardR(appB).coerce();
    }

    /**
     * Contravariantly map over the argument to this function, producing a function that takes the new argument type,
     * and produces the same result.
     *
     * @param <Z> the new argument type
     * @param fn  the contravariant argument mapping function
     * @return a new function from Z (the new argument type) to B (the same result)
     */
    @Override
    default <Z> Fn1<Z, B> diMapL(Function<Z, A> fn) {
        return (Fn1<Z, B>) Profunctor.super.diMapL(fn);
    }

    /**
     * Covariantly map over the return value of this function, producing a function that takes the same argument, and
     * produces the new result type.
     *
     * @param <C> the new result type
     * @param fn  the covariant result mapping function
     * @return a new function from A (the same argument type) to C (the new result type)
     */
    @Override
    default <C> Fn1<A, C> diMapR(Function<B, C> fn) {
        return (Fn1<A, C>) Profunctor.super.diMapR(fn);
    }

    /**
     * Exercise both <code>diMapL</code> and <code>diMapR</code> over this function in the same invocation.
     *
     * @param <Z> the new argument type
     * @param <C> the new result type
     * @param lFn the contravariant argument mapping function
     * @param rFn the covariant result mapping function
     * @return a new function from Z (the new argument type) to C (the new result type)
     */
    @Override
    default <Z, C> Fn1<Z, C> diMap(Function<Z, A> lFn, Function<B, C> rFn) {
        return lFn.andThen(this).andThen(rFn)::apply;
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
     * Right-to-left composition between different arity functions. Preserves highest arity in the return type,
     * specialized to lambda types (in this case, {@link BiFunction} -&gt; {@link Fn2}).
     *
     * @param before the function to pass its return value to this function's input
     * @param <Y>    the resulting function's first argument type
     * @param <Z>    the resulting function's second argument type
     * @return a new function from Y x Z to B
     */
    default <Y, Z> Fn2<Y, Z, B> compose(BiFunction<? super Y, ? super Z, ? extends A> before) {
        return compose(fn2(before));
    }

    /**
     * Right-to-left composition between different arity functions. Preserves highest arity in the return type.
     *
     * @param before the function to pass its return value to this function's input
     * @param <Y>    the resulting function's first argument type
     * @param <Z>    the resulting function's second argument type
     * @return a new function from Y x Z to B
     */
    default <Y, Z> Fn2<Y, Z, B> compose(Fn2<? super Y, ? super Z, ? extends A> before) {
        return fn2(before.fmap(this::compose))::apply;
    }

    /**
     * Left-to-right composition between different arity functions. Preserves highest arity in the return type,
     * specialized to lambda types (in this case, {@link BiFunction} -&gt; {@link Fn2}).
     *
     * @param after the function to invoke on this function's return value
     * @param <C>   the resulting function's second argument type
     * @param <D>   the resulting function's return type
     * @return a new function from A x C to D
     */
    default <C, D> Fn2<A, C, D> andThen(BiFunction<? super B, ? super C, ? extends D> after) {
        return (a, c) -> after.apply(apply(a), c);
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

    /**
     * Static factory method for wrapping a {@link Function} in an {@link Fn1}. Useful for avoid explicit casting when
     * using method references as {@link Fn1}s.
     *
     * @param function the function to adapt
     * @param <A>      the input argument type
     * @param <B>      the output type
     * @return the Fn1
     */
    static <A, B> Fn1<A, B> fn1(Function<? super A, ? extends B> function) {
        return function::apply;
    }
}
