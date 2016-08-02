package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import java.util.function.BiFunction;

/**
 * A function taking two arguments. Note that defining <code>Fn2</code> in terms of <code>Fn1</code> provides a
 * reasonable approximation of currying in the form of multiple <code>apply</code> overloads that take different numbers
 * of arguments.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The return type
 * @see Fn1
 * @see com.jnape.palatable.lambda.functions.builtin.fn2.Partial2
 */
@FunctionalInterface
public interface Fn2<A, B, C> extends Fn1<A, Fn1<B, C>> {

    /**
     * Invoke this function with the given arguments.
     *
     * @param a the first argument
     * @param b the second argument
     * @return the result of the function application
     */
    C apply(A a, B b);

    /**
     * Partially apply this function by passing its first argument.
     *
     * @param a the first argument
     * @return an Fn1 that takes the second argument and returns the result
     */
    @Override
    default Fn1<B, C> apply(A a) {
        return (b) -> apply(a, b);
    }

    /**
     * Flip the order of the arguments.
     *
     * @return an Fn2 that takes the first and second arguments in reversed order
     */
    default Fn2<B, A, C> flip() {
        return (b, a) -> apply(a, b);
    }

    /**
     * Returns an <code>Fn1</code> that takes the arguments as a <code>Tuple2&lt;A, B&gt;</code>.
     *
     * @return an Fn1 taking a Tuple2
     */
    default Fn1<Tuple2<A, B>, C> uncurry() {
        return (ab) -> apply(ab._1(), ab._2());
    }

    /**
     * View this <code>Fn2</code> as a <code>j.u.f.BiFunction</code>.
     *
     * @return the same logic as a <code>BiFunction</code>
     * @see BiFunction
     */
    default BiFunction<A, B, C> toBiFunction() {
        return this::apply;
    }
}
