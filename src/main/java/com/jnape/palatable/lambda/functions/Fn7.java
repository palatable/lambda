package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.Fn8.fn8;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A function taking six arguments. Defined in terms of {@link Fn6}, so similarly auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The third argument type
 * @param <D> The fourth argument type
 * @param <E> The fifth argument type
 * @param <F> The sixth argument type
 * @param <G> The seventh argument type
 * @param <H> The return type
 * @see Fn6
 */
@FunctionalInterface
public interface Fn7<A, B, C, D, E, F, G, H> extends Fn6<A, B, C, D, E, F, Fn1<G, H>> {

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
     * @return the result of the function application
     */
    H apply(A a, B b, C c, D d, E e, F f, G g);

    /**
     * @inheritDoc
     */
    @Override
    default <Z> Fn8<Z, A, B, C, D, E, F, G, H> widen() {
        return fn8(constantly(this));
    }

    /**
     * Partially apply this function by taking its first argument.
     *
     * @param a the first argument
     * @return an {@link Fn6} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn6<B, C, D, E, F, G, H> apply(A a) {
        return (b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    /**
     * Partially apply this function by taking its first two arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @return an {@link Fn5} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn5<C, D, E, F, G, H> apply(A a, B b) {
        return (c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    /**
     * Partially apply this function by taking its first three arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @return an {@link Fn4} that takes remaining arguments and returns the result
     */
    @Override
    default Fn4<D, E, F, G, H> apply(A a, B b, C c) {
        return (d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    /**
     * Partially apply this function by taking its first four arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @return an {@link Fn3} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn3<E, F, G, H> apply(A a, B b, C c, D d) {
        return (e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    /**
     * Partially apply this function by taking its first five arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @param e the fifth argument
     * @return an {@link Fn2} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn2<F, G, H> apply(A a, B b, C c, D d, E e) {
        return (f, g) -> apply(a, b, c, d, e, f, g);
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
     * @return an {@link Fn1} that takes the remaining argument and returns the result
     */
    @Override
    default Fn1<G, H> apply(A a, B b, C c, D d, E e, F f) {
        return (g) -> apply(a, b, c, d, e, f, g);
    }

    /**
     * Flip the order of the first two arguments.
     *
     * @return an {@link Fn7} that takes the first and second arguments in reversed order
     */
    @Override
    default Fn7<B, A, C, D, E, F, G, H> flip() {
        return (b, a, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    /**
     * Returns an {@link Fn6} that takes the first two arguments as a <code>{@link Product2}&lt;A, B&gt;</code> and the
     * remaining arguments.
     *
     * @return an {@link Fn6} taking a {@link Product2} and the remaining arguments
     */
    @Override
    default Fn6<? super Product2<? extends A, ? extends B>, C, D, E, F, G, H> uncurry() {
        return (ab, c, d, e, f, g) -> apply(ab._1(), ab._2(), c, d, e, f, g);
    }

    @Override
    default <I> Fn7<A, B, C, D, E, F, G, H> discardR(Applicative<I, Fn1<A, ?>> appB) {
        return fn7(Fn6.super.discardR(appB));
    }

    @Override
    default <Z> Fn7<Z, B, C, D, E, F, G, H> diMapL(Function<? super Z, ? extends A> fn) {
        return fn7(Fn6.super.diMapL(fn));
    }

    @Override
    default <Z> Fn7<Z, B, C, D, E, F, G, H> contraMap(Function<? super Z, ? extends A> fn) {
        return fn7(Fn6.super.contraMap(fn));
    }

    @Override
    default <Y, Z> Fn8<Y, Z, B, C, D, E, F, G, H> compose(BiFunction<? super Y, ? super Z, ? extends A> before) {
        return fn8(Fn6.super.compose(before));
    }

    @Override
    default <Y, Z> Fn8<Y, Z, B, C, D, E, F, G, H> compose(Fn2<? super Y, ? super Z, ? extends A> before) {
        return fn8(Fn6.super.compose(before));
    }

    /**
     * Static factory method for wrapping a curried {@link Fn1} in an {@link Fn7}.
     *
     * @param curriedFn1 the curried fn1 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the output type
     * @return the {@link Fn7}
     */
    static <A, B, C, D, E, F, G, H> Fn7<A, B, C, D, E, F, G, H> fn7(Fn1<A, Fn6<B, C, D, E, F, G, H>> curriedFn1) {
        return (a, b, c, d, e, f, g) -> curriedFn1.apply(a).apply(b, c, d, e, f, g);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn2} in an {@link Fn7}.
     *
     * @param curriedFn2 the curried fn2 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the output type
     * @return the {@link Fn7}
     */
    static <A, B, C, D, E, F, G, H> Fn7<A, B, C, D, E, F, G, H> fn7(Fn2<A, B, Fn5<C, D, E, F, G, H>> curriedFn2) {
        return (a, b, c, d, e, f, g) -> curriedFn2.apply(a, b).apply(c, d, e, f, g);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn3} in an {@link Fn7}.
     *
     * @param curriedFn3 the curried fn3 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the output type
     * @return the {@link Fn7}
     */
    static <A, B, C, D, E, F, G, H> Fn7<A, B, C, D, E, F, G, H> fn7(Fn3<A, B, C, Fn4<D, E, F, G, H>> curriedFn3) {
        return (a, b, c, d, e, f, g) -> curriedFn3.apply(a, b, c).apply(d, e, f, g);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn4} in an {@link Fn7}.
     *
     * @param curriedFn4 the curried fn4 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the output type
     * @return the {@link Fn7}
     */
    static <A, B, C, D, E, F, G, H> Fn7<A, B, C, D, E, F, G, H> fn7(Fn4<A, B, C, D, Fn3<E, F, G, H>> curriedFn4) {
        return (a, b, c, d, e, f, g) -> curriedFn4.apply(a, b, c, d).apply(e, f, g);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn5} in an {@link Fn7}.
     *
     * @param curriedFn5 the curried fn4 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the output type
     * @return the {@link Fn7}
     */
    static <A, B, C, D, E, F, G, H> Fn7<A, B, C, D, E, F, G, H> fn7(Fn5<A, B, C, D, E, Fn2<F, G, H>> curriedFn5) {
        return (a, b, c, d, e, f, g) -> curriedFn5.apply(a, b, c, d, e).apply(f, g);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn6} in an {@link Fn7}.
     *
     * @param curriedFn6 the curried fn4 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the sixth input argument type
     * @param <G>        the seventh input argument type
     * @param <H>        the output type
     * @return the {@link Fn7}
     */
    static <A, B, C, D, E, F, G, H> Fn7<A, B, C, D, E, F, G, H> fn7(Fn6<A, B, C, D, E, F, Fn1<G, H>> curriedFn6) {
        return (a, b, c, d, e, f, g) -> curriedFn6.apply(a, b, c, d, e, f).apply(g);
    }

    /**
     * Static factory method for coercing a lambda to an {@link Fn7}.
     *
     * @param fn  the lambda to coerce
     * @param <A> the first input argument type
     * @param <B> the second input argument type
     * @param <C> the third input argument type
     * @param <D> the fourth input argument type
     * @param <E> the fifth input argument type
     * @param <F> the sixth input argument type
     * @param <G> the seventh input argument type
     * @param <H> the output type
     * @return the {@link Fn7}
     */
    static <A, B, C, D, E, F, G, H> Fn7<A, B, C, D, E, F, G, H> fn7(Fn7<A, B, C, D, E, F, G, H> fn) {
        return fn;
    }
}
