package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.specialized.checked.Runtime;
import com.jnape.palatable.lambda.functor.Applicative;

import static com.jnape.palatable.lambda.functions.Fn6.fn6;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A function taking five arguments. Defined in terms of {@link Fn4}, so similarly auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The third argument type
 * @param <D> The fourth argument type
 * @param <E> The fifth argument type
 * @param <F> The return type
 * @see Fn4
 */
@FunctionalInterface
public interface Fn5<A, B, C, D, E, F> extends Fn4<A, B, C, D, Fn1<E, F>> {

    F checkedApply(A a, B b, C c, D d, E e) throws Throwable;

    /**
     * Invoke this function with the given arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @param e the fifth argument
     * @return the result of the function application
     */
    default F apply(A a, B b, C c, D d, E e) {
        try {
            return checkedApply(a, b, c, d, e);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Fn1<E, F> checkedApply(A a, B b, C c, D d) throws Throwable {
        return e -> checkedApply(a, b, c, d, e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Fn6<Z, A, B, C, D, E, F> widen() {
        return fn6(constantly(this));
    }

    /**
     * Partially apply this function by taking its first argument.
     *
     * @param a the first argument
     * @return an {@link Fn5} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn4<B, C, D, E, F> apply(A a) {
        return (b, c, d, e) -> apply(a, b, c, d, e);
    }

    /**
     * Partially apply this function by taking its first two arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @return an {@link Fn3} that takes the remaining arguments and returns the result
     */
    @Override
    default Fn3<C, D, E, F> apply(A a, B b) {
        return (c, d, e) -> apply(a, b, c, d, e);
    }

    /**
     * Partially apply this function by taking its first three arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @return an {@link Fn2} that takes remaining arguments and returns the result
     */
    @Override
    default Fn2<D, E, F> apply(A a, B b, C c) {
        return (d, e) -> apply(a, b, c, d, e);
    }

    /**
     * Partially apply this function by taking its first four arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @return an {@link Fn1} that takes the remaining argument and returns the result
     */
    @Override
    default Fn1<E, F> apply(A a, B b, C c, D d) {
        return (e) -> apply(a, b, c, d, e);
    }

    /**
     * Flip the order of the first two arguments.
     *
     * @return an {@link Fn5} that takes the first and second arguments in reversed order
     */
    @Override
    default Fn5<B, A, C, D, E, F> flip() {
        return (b, a, c, d, e) -> apply(a, b, c, d, e);
    }

    /**
     * Returns an {@link Fn4} that takes the first two arguments as a <code>{@link Product2}&lt;A, B&gt;</code> and the
     * remaining arguments.
     *
     * @return an {@link Fn4} taking a {@link Product2} and the remaining arguments
     */
    @Override
    default Fn4<? super Product2<? extends A, ? extends B>, C, D, E, F> uncurry() {
        return (ab, c, d, e) -> apply(ab._1(), ab._2(), c, d, e);
    }

    @Override
    default <G> Fn5<A, B, C, D, E, F> discardR(Applicative<G, Fn1<A, ?>> appB) {
        return fn5(Fn4.super.discardR(appB));
    }

    @Override
    default <Z> Fn5<Z, B, C, D, E, F> diMapL(Fn1<? super Z, ? extends A> fn) {
        return fn5(Fn4.super.diMapL(fn));
    }

    @Override
    default <Z> Fn5<Z, B, C, D, E, F> contraMap(Fn1<? super Z, ? extends A> fn) {
        return fn5(Fn4.super.contraMap(fn));
    }

    @Override
    default <Y, Z> Fn6<Y, Z, B, C, D, E, F> compose(Fn2<? super Y, ? super Z, ? extends A> before) {
        return fn6(Fn4.super.compose(before));
    }

    /**
     * Static factory method for wrapping a curried {@link Fn1} in an {@link Fn5}.
     *
     * @param curriedFn1 the curried fn1 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the output type
     * @return the {@link Fn5}
     */
    static <A, B, C, D, E, F> Fn5<A, B, C, D, E, F> fn5(Fn1<A, Fn4<B, C, D, E, F>> curriedFn1) {
        return (a, b, c, d, e) -> curriedFn1.apply(a).apply(b, c, d, e);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn2} in an {@link Fn5}.
     *
     * @param curriedFn2 the curried fn2 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the output type
     * @return the {@link Fn5}
     */
    static <A, B, C, D, E, F> Fn5<A, B, C, D, E, F> fn5(Fn2<A, B, Fn3<C, D, E, F>> curriedFn2) {
        return (a, b, c, d, e) -> curriedFn2.apply(a, b).apply(c, d, e);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn3} in an {@link Fn5}.
     *
     * @param curriedFn3 the curried fn3 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the output type
     * @return the {@link Fn5}
     */
    static <A, B, C, D, E, F> Fn5<A, B, C, D, E, F> fn5(Fn3<A, B, C, Fn2<D, E, F>> curriedFn3) {
        return (a, b, c, d, e) -> curriedFn3.apply(a, b, c).apply(d, e);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn4} in an {@link Fn5}.
     *
     * @param curriedFn4 the curried fn4 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the fifth input argument type
     * @param <F>        the output type
     * @return the {@link Fn5}
     */
    static <A, B, C, D, E, F> Fn5<A, B, C, D, E, F> fn5(Fn4<A, B, C, D, Fn1<E, F>> curriedFn4) {
        return (a, b, c, d, e) -> curriedFn4.apply(a, b, c, d).apply(e);
    }

    /**
     * Static factory method for coercing a lambda to an {@link Fn5}.
     *
     * @param fn  the lambda to coerce
     * @param <A> the first input argument type
     * @param <B> the second input argument type
     * @param <C> the third input argument type
     * @param <D> the fourth input argument type
     * @param <E> the fifth input argument type
     * @param <F> the output type
     * @return the {@link Fn5}
     */
    static <A, B, C, D, E, F> Fn5<A, B, C, D, E, F> fn5(Fn5<A, B, C, D, E, F> fn) {
        return fn;
    }
}
