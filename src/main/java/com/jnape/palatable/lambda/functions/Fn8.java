package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.internal.Runtime;

/**
 * A function taking eight arguments. Defined in terms of {@link Fn7}, so similarly auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The third argument type
 * @param <D> The fourth argument type
 * @param <E> The fifth argument type
 * @param <F> The sixth argument type
 * @param <G> The seventh argument type
 * @param <H> The eighth argument type
 * @param <I> The return type
 * @see Fn7
 */
@FunctionalInterface
public interface Fn8<A, B, C, D, E, F, G, H, I> extends Fn7<A, B, C, D, E, F, G, Fn1<H, I>> {

    I checkedApply(A a, B b, C c, D d, E e, F f, G g, H h) throws Throwable;

    /**
     * Invoke this function with the given arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @param e the fifth argument
     * @param f the sixth argument
     * @param g the seventh argument
     * @param h the eighth argument
     * @return the result of the function application
     */
    default I apply(A a, B b, C c, D d, E e, F f, G g, H h) {
        try {
            return checkedApply(a, b, c, d, e, f, g, h);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Fn1<H, I> checkedApply(A a, B b, C c, D d, E e, F f, G g) throws Throwable {
        return h -> checkedApply(a, b, c, d, e, f, g, h);
    }

    /**
     * Partially apply this function by taking its first argument.
     *
     * @param a the first argument
     * @return an {@link Fn7} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn7<B, C, D, E, F, G, H, I> apply(A a) {
        return (b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    /**
     * Partially apply this function by taking its first two arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @return an {@link Fn6} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn6<C, D, E, F, G, H, I> apply(A a, B b) {
        return (c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    /**
     * Partially apply this function by taking its first three arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @return an {@link Fn5} that takes remaining arguments and returns the result
     */
    @Override
    default Fn5<D, E, F, G, H, I> apply(A a, B b, C c) {
        return (d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    /**
     * Partially apply this function by taking its first four arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @return an {@link Fn4} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn4<E, F, G, H, I> apply(A a, B b, C c, D d) {
        return (e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    /**
     * Partially apply this function by taking its first five arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @param e the fifth argument
     * @return an {@link Fn3} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn3<F, G, H, I> apply(A a, B b, C c, D d, E e) {
        return (f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    /**
     * Partially apply this function by taking its first six arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @param e the fifth argument
     * @param f the sixth argument
     * @return an {@link Fn2} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn2<G, H, I> apply(A a, B b, C c, D d, E e, F f) {
        return (g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    /**
     * Partially apply this function by taking its first seven arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @param e the fifth argument
     * @param f the sixth argument
     * @param g the seventh argument
     * @return an {@link Fn1} that takes the remaining argument and returns the result
     */
    @Override
    default Fn1<H, I> apply(A a, B b, C c, D d, E e, F f, G g) {
        return (h) -> apply(a, b, c, d, e, f, g, h);
    }

    /**
     * Flip the order of the first two arguments.
     *
     * @return an {@link Fn8} that takes the first and second arguments in reversed order
     */
    @Override
    default Fn8<B, A, C, D, E, F, G, H, I> flip() {
        return (b, a, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    /**
     * Returns an {@link Fn7} that takes the first two arguments as a <code>{@link Product2}&lt;A, B&gt;</code> and the
     * remaining arguments.
     *
     * @return an {@link Fn7} taking a {@link Product2} and the remaining arguments
     */
    @Override
    default Fn7<? super Product2<? extends A, ? extends B>, C, D, E, F, G, H, I> uncurry() {
        return (ab, c, d, e, f, g, h) -> apply(ab._1(), ab._2(), c, d, e, f, g, h);
    }

    @Override
    default <J> Fn8<A, B, C, D, E, F, G, H, I> discardR(Applicative<J, Fn1<A, ?>> appB) {
        return fn8(Fn7.super.discardR(appB));
    }

    @Override
    default <Z> Fn8<Z, B, C, D, E, F, G, H, I> diMapL(Fn1<? super Z, ? extends A> fn) {
        return fn8(Fn7.super.diMapL(fn));
    }

    @Override
    default <Z> Fn8<Z, B, C, D, E, F, G, H, I> contraMap(Fn1<? super Z, ? extends A> fn) {
        return fn8(Fn7.super.contraMap(fn));
    }

    @Override
    default <Y, Z> Fn8<Y, Z, B, C, D, E, F, G, Fn1<H, I>> compose(Fn2<? super Y, ? super Z, ? extends A> before) {
        return Fn7.super.compose(before);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn1} in an {@link Fn8}.
     *
     * @param curriedFn1 the curried fn1 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the eighth input argument type
     * @param <I>        the output type
     * @return the {@link Fn8}
     */
    static <A, B, C, D, E, F, G, H, I> Fn8<A, B, C, D, E, F, G, H, I> fn8(
            Fn1<A, Fn7<B, C, D, E, F, G, H, I>> curriedFn1) {
        return (a, b, c, d, e, f, g, h) -> curriedFn1.apply(a).apply(b, c, d, e, f, g, h);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn2} in an {@link Fn8}.
     *
     * @param curriedFn2 the curried fn2 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the eighth input argument type
     * @param <I>        the output type
     * @return the {@link Fn8}
     */
    static <A, B, C, D, E, F, G, H, I> Fn8<A, B, C, D, E, F, G, H, I> fn8(
            Fn2<A, B, Fn6<C, D, E, F, G, H, I>> curriedFn2) {
        return (a, b, c, d, e, f, g, h) -> curriedFn2.apply(a, b).apply(c, d, e, f, g, h);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn3} in an {@link Fn8}.
     *
     * @param curriedFn3 the curried fn3 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the eighth input argument type
     * @param <I>        the output type
     * @return the {@link Fn8}
     */
    static <A, B, C, D, E, F, G, H, I> Fn8<A, B, C, D, E, F, G, H, I> fn8(
            Fn3<A, B, C, Fn5<D, E, F, G, H, I>> curriedFn3) {
        return (a, b, c, d, e, f, g, h) -> curriedFn3.apply(a, b, c).apply(d, e, f, g, h);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn4} in an {@link Fn8}.
     *
     * @param curriedFn4 the curried fn4 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the eighth input argument type
     * @param <I>        the output type
     * @return the {@link Fn8}
     */
    static <A, B, C, D, E, F, G, H, I> Fn8<A, B, C, D, E, F, G, H, I> fn8(
            Fn4<A, B, C, D, Fn4<E, F, G, H, I>> curriedFn4) {
        return (a, b, c, d, e, f, g, h) -> curriedFn4.apply(a, b, c, d).apply(e, f, g, h);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn5} in an {@link Fn8}.
     *
     * @param curriedFn5 the curried fn5 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the eighth input argument type
     * @param <I>        the output type
     * @return the {@link Fn8}
     */
    static <A, B, C, D, E, F, G, H, I> Fn8<A, B, C, D, E, F, G, H, I> fn8(
            Fn5<A, B, C, D, E, Fn3<F, G, H, I>> curriedFn5) {
        return (a, b, c, d, e, f, g, h) -> curriedFn5.apply(a, b, c, d, e).apply(f, g, h);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn6} in an {@link Fn8}.
     *
     * @param curriedFn6 the curried fn6 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the eighth input argument type
     * @param <I>        the output type
     * @return the {@link Fn8}
     */
    static <A, B, C, D, E, F, G, H, I> Fn8<A, B, C, D, E, F, G, H, I> fn8(
            Fn6<A, B, C, D, E, F, Fn2<G, H, I>> curriedFn6) {
        return (a, b, c, d, e, f, g, h) -> curriedFn6.apply(a, b, c, d, e, f).apply(g, h);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn7} in an {@link Fn8}.
     *
     * @param curriedFn7 the curried fn7 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the eighth input argument type
     * @param <I>        the output type
     * @return the {@link Fn8}
     */
    static <A, B, C, D, E, F, G, H, I> Fn8<A, B, C, D, E, F, G, H, I> fn8(
            Fn7<A, B, C, D, E, F, G, Fn1<H, I>> curriedFn7) {
        return (a, b, c, d, e, f, g, h) -> curriedFn7.apply(a, b, c, d, e, f, g).apply(h);
    }

    /**
     * Static factory method for coercing a lambda to an {@link Fn8}.
     *
     * @param fn  the lambda to coerce
     * @param <A> the first input argument type
     * @param <B> the second input argument type
     * @param <C> the third input argument type
     * @param <D> the fourth input argument type
     * @param <E> the fifth input argument type
     * @param <F> the sixth input argument type
     * @param <G> the seventh input argument type
     * @param <H> the eighth input argument type
     * @param <I> the output type
     * @return the {@link Fn8}
     */
    static <A, B, C, D, E, F, G, H, I> Fn8<A, B, C, D, E, F, G, H, I> fn8(Fn8<A, B, C, D, E, F, G, H, I> fn) {
        return fn;
    }
}
