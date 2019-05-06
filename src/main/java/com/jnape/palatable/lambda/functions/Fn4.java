package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.specialized.checked.Runtime;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.Fn5.fn5;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A function taking four arguments. Defined in terms of {@link Fn3}, so similarly auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The third argument type
 * @param <D> The fourth argument type
 * @param <E> The return type
 * @see Fn3
 */
@FunctionalInterface
public interface Fn4<A, B, C, D, E> extends Fn3<A, B, C, Fn1<D, E>> {

    E checkedApply(A a, B b, C c, D d) throws Throwable;

    /**
     * Invoke this function with the given arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @return the result of the function application
     */
    default E apply(A a, B b, C c, D d) {
        try {
            return checkedApply(a, b, c, d);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Fn1<D, E> checkedApply(A a, B b, C c) throws Throwable {
        return d -> checkedApply(a, b, c, d);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Fn5<Z, A, B, C, D, E> widen() {
        return fn5(constantly(this));
    }

    /**
     * Partially apply this function by taking its first argument.
     *
     * @param a the first argument
     * @return an {@link Fn3}&lt;B, C, D, E&gt;
     */
    @Override
    default Fn3<B, C, D, E> apply(A a) {
        return (b, c, d) -> apply(a, b, c, d);
    }

    /**
     * Partially apply this function by taking its first two arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @return an {@link Fn2}&lt;C, D, E&gt;
     */
    @Override
    default Fn2<C, D, E> apply(A a, B b) {
        return (c, d) -> apply(a, b, c, d);
    }

    /**
     * Partially apply this function by taking its first three arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @return an {@link Fn1}&lt;D, E&gt;
     */
    @Override
    default Fn1<D, E> apply(A a, B b, C c) {
        return (d) -> apply(a, b, c, d);
    }

    /**
     * Flip the order of the first two arguments.
     *
     * @return an {@link Fn4}&lt;B, A, C, D, E&gt;
     */
    @Override
    default Fn4<B, A, C, D, E> flip() {
        return (b, a, c, d) -> apply(a, b, c, d);
    }

    /**
     * Returns an {@link Fn3} that takes the first two arguments as a <code>{@link Product2}&lt;A, B&gt;</code> and the
     * third and fourth arguments.
     *
     * @return an {@link Fn3} taking a {@link Product2} and the third and fourth arguments
     */
    @Override
    default Fn3<? super Product2<? extends A, ? extends B>, C, D, E> uncurry() {
        return (ab, c, d) -> apply(ab._1(), ab._2(), c, d);
    }

    @Override
    default <F> Fn4<A, B, C, D, E> discardR(Applicative<F, Fn1<A, ?>> appB) {
        return fn4(Fn3.super.discardR(appB));
    }

    @Override
    default <Z> Fn4<Z, B, C, D, E> diMapL(Function<? super Z, ? extends A> fn) {
        return fn4(Fn3.super.diMapL(fn));
    }

    @Override
    default <Z> Fn4<Z, B, C, D, E> contraMap(Function<? super Z, ? extends A> fn) {
        return fn4(Fn3.super.contraMap(fn));
    }

    @Override
    default <Y, Z> Fn5<Y, Z, B, C, D, E> compose(BiFunction<? super Y, ? super Z, ? extends A> before) {
        return fn5(Fn3.super.compose(before));
    }

    @Override
    default <Y, Z> Fn5<Y, Z, B, C, D, E> compose(Fn2<? super Y, ? super Z, ? extends A> before) {
        return fn5(Fn3.super.compose(before));
    }

    /**
     * Static factory method for wrapping a curried {@link Fn1} in an {@link Fn4}.
     *
     * @param curriedFn1 the curried fn1 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the output type
     * @return the {@link Fn4}
     */
    static <A, B, C, D, E> Fn4<A, B, C, D, E> fn4(Fn1<A, Fn3<B, C, D, E>> curriedFn1) {
        return (a, b, c, d) -> curriedFn1.apply(a).apply(b, c, d);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn2} in an {@link Fn4}.
     *
     * @param curriedFn2 the curried fn2 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the output type
     * @return the {@link Fn4}
     */
    static <A, B, C, D, E> Fn4<A, B, C, D, E> fn4(Fn2<A, B, Fn2<C, D, E>> curriedFn2) {
        return (a, b, c, d) -> curriedFn2.apply(a, b).apply(c, d);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn3} in an {@link Fn4}.
     *
     * @param curriedFn3 the curried fn3 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the fourth input argument type
     * @param <E>        the output type
     * @return the {@link Fn4}
     */
    static <A, B, C, D, E> Fn4<A, B, C, D, E> fn4(Fn3<A, B, C, Fn1<D, E>> curriedFn3) {
        return (a, b, c, d) -> curriedFn3.apply(a, b, c).apply(d);
    }

    /**
     * Static factory method for coercing a lambda to an {@link Fn4}.
     *
     * @param fn  the lambda to coerce
     * @param <A> the first input argument type
     * @param <B> the second input argument type
     * @param <C> the third input argument type
     * @param <D> the fourth input argument type
     * @param <E> the output type
     * @return the {@link Fn4}
     */
    static <A, B, C, D, E> Fn4<A, B, C, D, E> fn4(Fn4<A, B, C, D, E> fn) {
        return fn;
    }
}
