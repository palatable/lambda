package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

/**
 * Given some number of times <code>n</code> to invoke a function <code>A -&gt; A</code>, and given an input
 * <code>A</code>, iteratively apply the function to the input, and then to the result of the invocation, a total of
 * <code>n</code> times, returning the result.
 * <p>
 * Example:
 *
 * <code>times(3, x -&gt; x + 1, 0); // 3</code>
 *
 * @param <A> the input and output type
 */
public final class Times<A> implements Fn3<Integer, Fn1<? super A, ? extends A>, A, A> {

    private static final Times<?> INSTANCE = new Times<>();

    private Times() {
    }

    @Override
    public A checkedApply(Integer n, Fn1<? super A, ? extends A> fn, A a) {
        if (n < 0)
            throw new IllegalStateException("n must not be less than 0");

        return foldLeft((acc, f) -> f.apply(acc), a, replicate(n, fn));
    }

    @SuppressWarnings("unchecked")
    public static <A> Times<A> times() {
        return (Times<A>) INSTANCE;
    }

    public static <A> Fn2<Fn1<? super A, ? extends A>, A, A> times(Integer n) {
        return Times.<A>times().apply(n);
    }

    public static <A> Fn1<A, A> times(Integer n, Fn1<? super A, ? extends A> fn) {
        return Times.<A>times(n).apply(fn);
    }

    public static <A> A times(Integer n, Fn1<? super A, ? extends A> fn, A a) {
        return Times.<A>times(n, fn).apply(a);
    }
}
