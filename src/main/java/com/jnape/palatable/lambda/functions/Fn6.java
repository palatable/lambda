package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.Fn7.fn7;

/**
 * A function taking six arguments. Defined in terms of {@link Fn5}, so similarly auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The third argument type
 * @param <D> The fourth argument type
 * @param <E> The fifth argument type
 * @param <F> The sixth argument type
 * @param <G> The return type
 * @see Fn5
 */
@FunctionalInterface
public interface Fn6<A, B, C, D, E, F, G> extends Fn5<A, B, C, D, E, Fn1<F, G>> {

    /**
     * Invoke this function with the given arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @param e the fifth argument
     * @param f the sixth argument
     * @return the result of the function application
     */
    G apply(A a, B b, C c, D d, E e, F f);

    /**
     * Partially apply this function by taking its first argument.
     *
     * @param a the first argument
     * @return an {@link Fn5} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn5<B, C, D, E, F, G> apply(A a) {
        return (b, c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    /**
     * Partially apply this function by taking its first two arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @return an {@link Fn4} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn4<C, D, E, F, G> apply(A a, B b) {
        return (c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    /**
     * Partially apply this function by taking its first three arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @return an {@link Fn3} that takes remaining arguments and returns the result
     */
    @Override
    default Fn3<D, E, F, G> apply(A a, B b, C c) {
        return (d, e, f) -> apply(a, b, c, d, e, f);
    }

    /**
     * Partially apply this function by taking its first four arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @return an {@link Fn2} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn2<E, F, G> apply(A a, B b, C c, D d) {
        return (e, f) -> apply(a, b, c, d, e, f);
    }

    /**
     * Partially apply this function by taking its first five arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @param e the fifth argument
     * @return an {@link Fn1} that takes the remaining argument and returns the result
     */
    @Override
    default Fn1<F, G> apply(A a, B b, C c, D d, E e) {
        return (f) -> apply(a, b, c, d, e, f);
    }

    /**
     * Flip the order of the first two arguments.
     *
     * @return an {@link Fn6} that takes the first and second arguments in reversed order
     */
    @Override
    default Fn6<B, A, C, D, E, F, G> flip() {
        return (b, a, c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    /**
     * Returns an {@link Fn5} that takes the first two arguments as a <code>{@link Product2}&lt;A, B&gt;</code> and the
     * remaining arguments.
     *
     * @return an {@link Fn5} taking a {@link Product2} and the remaining arguments
     */
    @Override
    default Fn5<? super Product2<? extends A, ? extends B>, C, D, E, F, G> uncurry() {
        return (ab, c, d, e, f) -> apply(ab._1(), ab._2(), c, d, e, f);
    }

    @Override
    default <H> Fn6<A, B, C, D, E, F, G> discardR(Applicative<H, Fn1<A, ?>> appB) {
        return fn6(Fn5.super.discardR(appB));
    }

    @Override
    default <Z> Fn6<Z, B, C, D, E, F, G> diMapL(Function<? super Z, ? extends A> fn) {
        return fn6(Fn5.super.diMapL(fn));
    }

    @Override
    default <Z> Fn6<Z, B, C, D, E, F, G> contraMap(Function<? super Z, ? extends A> fn) {
        return fn6(Fn5.super.contraMap(fn));
    }

    @Override
    default <Y, Z> Fn7<Y, Z, B, C, D, E, F, G> compose(BiFunction<? super Y, ? super Z, ? extends A> before) {
        return fn7(Fn5.super.compose(before));
    }

    @Override
    default <Y, Z> Fn7<Y, Z, B, C, D, E, F, G> compose(Fn2<? super Y, ? super Z, ? extends A> before) {
        return fn7(Fn5.super.compose(before));
    }

    /**
     * Static factory method for wrapping a curried {@link Fn1} in an {@link Fn6}.
     *
     * @param curriedFn1 the curried fn1 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the output type
     * @return the {@link Fn6}
     */
    static <A, B, C, D, E, F, G> Fn6<A, B, C, D, E, F, G> fn6(Fn1<A, Fn5<B, C, D, E, F, G>> curriedFn1) {
        return (a, b, c, d, e, f) -> curriedFn1.apply(a).apply(b, c, d, e, f);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn2} in an {@link Fn6}.
     *
     * @param curriedFn2 the curried fn2 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the output type
     * @return the {@link Fn6}
     */
    static <A, B, C, D, E, F, G> Fn6<A, B, C, D, E, F, G> fn6(Fn2<A, B, Fn4<C, D, E, F, G>> curriedFn2) {
        return (a, b, c, d, e, f) -> curriedFn2.apply(a, b).apply(c, d, e, f);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn3} in an {@link Fn6}.
     *
     * @param curriedFn3 the curried fn3 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the output type
     * @return the {@link Fn6}
     */
    static <A, B, C, D, E, F, G> Fn6<A, B, C, D, E, F, G> fn6(Fn3<A, B, C, Fn3<D, E, F, G>> curriedFn3) {
        return (a, b, c, d, e, f) -> curriedFn3.apply(a, b, c).apply(d, e, f);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn4} in an {@link Fn6}.
     *
     * @param curriedFn4 the curried fn4 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the output type
     * @return the {@link Fn6}
     */
    static <A, B, C, D, E, F, G> Fn6<A, B, C, D, E, F, G> fn6(Fn4<A, B, C, D, Fn2<E, F, G>> curriedFn4) {
        return (a, b, c, d, e, f) -> curriedFn4.apply(a, b, c, d).apply(e, f);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn5} in an {@link Fn6}.
     *
     * @param curriedFn5 the curried fn4 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the output type
     * @return the {@link Fn6}
     */
    static <A, B, C, D, E, F, G> Fn6<A, B, C, D, E, F, G> fn6(Fn5<A, B, C, D, E, Fn1<F, G>> curriedFn5) {
        return (a, b, c, d, e, f) -> curriedFn5.apply(a, b, c, d, e).apply(f);
    }

    /**
     * Static factory method for coercing a lambda to an {@link Fn6}.
     *
     * @param fn  the lambda to coerce
     * @param <A> the first input argument type
     * @param <B> the second input argument type
     * @param <C> the third input argument type
     * @param <D> the fourth input argument type
     * @param <E> the fifth input argument type
     * @param <F> the sixth input argument type
     * @param <G> the output type
     * @return the {@link Fn6}
     */
    static <A, B, C, D, E, F, G> Fn6<A, B, C, D, E, F, G> fn6(Fn6<A, B, C, D, E, F, G> fn) {
        return fn;
    }
}
