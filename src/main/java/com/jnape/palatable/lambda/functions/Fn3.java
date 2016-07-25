package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

/**
 * A function taking three arguments. Defined in terms of <code>Fn2</code>, so similarly auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The third argument type
 * @param <D> The return type
 * @see Fn2
 * @see com.jnape.palatable.lambda.functions.builtin.fn2.Partial3
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
     * @return an Fn2 that takes the second and third argument and returns the result
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
     * @return an Fn1 that takes the third argument and returns the result
     */
    @Override
    default Fn1<C, D> apply(A a, B b) {
        return (c) -> apply(a, b, c);
    }

    /**
     * Flip the order of the first two arguments.
     *
     * @return an Fn3 that takes the first and second arguments in reversed order
     */
    @Override
    default Fn3<B, A, C, D> flip() {
        return (b, a, c) -> apply(a, b, c);
    }

    /**
     * Returns an <code>Fn2</code> that takes the first two arguments as a <code>Tuple2&lt;A, B&gt;</code> and the third
     * argument.
     *
     * @return an Fn2 taking a Tuple2 and the third argument
     */
    @Override
    default Fn2<Tuple2<A, B>, C, D> uncurry() {
        return (ab, c) -> apply(ab._1(), ab._2(), c);
    }
}
