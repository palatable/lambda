package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

/**
 * A function taking four arguments. Defined in terms of <code>Fn3</code>, so similarly auto-curried.
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

    /**
     * Invoke this function with the given arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @param d the fourth argument
     * @return the result of the function application
     */
    E apply(A a, B b, C c, D d);

    /**
     * Partially apply this function by taking its first argument.
     *
     * @param a the first argument
     * @return an Fn3 that takes the second, third, and fourth argument and returns the result
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
     * @return an Fn2 that takes the third and fourth arguments and returns the result
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
     * @return an Fn1 that takes the fourth argument and returns the result
     */
    @Override
    default Fn1<D, E> apply(A a, B b, C c) {
        return (d) -> apply(a, b, c, d);
    }

    /**
     * Flip the order of the first two arguments.
     *
     * @return an Fn3 that takes the first and second arguments in reversed order
     */
    @Override
    default Fn4<B, A, C, D, E> flip() {
        return (b, a, c, d) -> apply(a, b, c, d);
    }

    /**
     * Returns an <code>Fn3</code> that takes the first two arguments as a <code>Tuple2&lt;A, B&gt;</code> and the third
     * and fourth arguments.
     *
     * @return an Fn3 taking a Tuple2 and the third and fourth arguments
     */
    @Override
    default Fn3<Tuple2<A, B>, C, D, E> uncurry() {
        return (ab, c, d) -> apply(ab._1(), ab._2(), c, d);
    }
}
