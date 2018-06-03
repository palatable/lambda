package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Applicative;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.Fn4.fn4;

/**
 * A function taking three arguments. Defined in terms of {@link Fn2}, so similarly auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The third argument type
 * @param <D> The return type
 * @see Fn2
 */
@FunctionalInterface
public interface Fn3<A, B, C, D> extends Fn2<A, B, Fn1<C, D>> {

    /**
     * Invoke this function with the given arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @return the result of the function application
     */
    D apply(A a, B b, C c);

    /**
     * Partially apply this function by taking its first argument.
     *
     * @param a the first argument
     * @return an {@link Fn2}&lt;B, C, D&gt;
     */
    @Override
    default Fn2<B, C, D> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }

    /**
     * Partially apply this function by taking its first two arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @return an {@link Fn1}&lt;C, D&gt;
     */
    @Override
    default Fn1<C, D> apply(A a, B b) {
        return (c) -> apply(a, b, c);
    }

    /**
     * Flip the order of the first two arguments.
     *
     * @return an {@link Fn3}&lt;B, A, C, D&gt;
     */
    @Override
    default Fn3<B, A, C, D> flip() {
        return (b, a, c) -> apply(a, b, c);
    }

    /**
     * Returns an {@link Fn2} that takes the first two arguments as a <code>{@link Tuple2}&lt;A, B&gt;</code> and the
     * third argument.
     *
     * @return an {@link Fn2} taking a {@link Tuple2} and the third argument
     */
    @Override
    default Fn2<Tuple2<A, B>, C, D> uncurry() {
        return (ab, c) -> apply(ab._1(), ab._2(), c);
    }

    @Override
    default <E> Fn3<A, B, C, D> discardR(Applicative<E, Fn1<A, ?>> appB) {
        return fn3(Fn2.super.discardR(appB));
    }

    @Override
    default <Z> Fn3<Z, B, C, D> diMapL(Function<? super Z, ? extends A> fn) {
        return fn3(Fn2.super.diMapL(fn));
    }

    @Override
    default <Z> Fn3<Z, B, C, D> contraMap(Function<? super Z, ? extends A> fn) {
        return fn3(Fn2.super.contraMap(fn));
    }

    @Override
    default <Y, Z> Fn4<Y, Z, B, C, D> compose(BiFunction<? super Y, ? super Z, ? extends A> before) {
        return fn4(Fn2.super.compose(before));
    }

    @Override
    default <Y, Z> Fn4<Y, Z, B, C, D> compose(Fn2<? super Y, ? super Z, ? extends A> before) {
        return fn4(Fn2.super.compose(before));
    }

    /**
     * Static factory method for wrapping a curried {@link Fn1} in an {@link Fn3}.
     *
     * @param curriedFn1 the curried fn1 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the output type
     * @return the {@link Fn3}
     */
    static <A, B, C, D> Fn3<A, B, C, D> fn3(Fn1<A, Fn2<B, C, D>> curriedFn1) {
        return (a, b, c) -> curriedFn1.apply(a).apply(b, c);
    }

    /**
     * Static factory method for wrapping a curried {@link Fn2} in an {@link Fn3}.
     *
     * @param curriedFn2 the curried fn2 to adapt
     * @param <A>        the first input argument type
     * @param <B>        the second input argument type
     * @param <C>        the third input argument type
     * @param <D>        the output type
     * @return the {@link Fn3}
     */
    static <A, B, C, D> Fn3<A, B, C, D> fn3(Fn2<A, B, Fn1<C, D>> curriedFn2) {
        return (a, b, c) -> curriedFn2.apply(a, b).apply(c);
    }

    /**
     * Static factory method for coercing a lambda to an {@link Fn3};
     *
     * @param fn  the lambda to coerce
     * @param <A> the first input argument type
     * @param <B> the second input argument type
     * @param <C> the third input argument type
     * @param <D> the output type
     * @return the {@link Fn3}
     */
    static <A, B, C, D> Fn3<A, B, C, D> fn3(Fn3<A, B, C, D> fn) {
        return fn;
    }
}
