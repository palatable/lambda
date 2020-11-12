package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Flatten.flatten;
import static com.jnape.palatable.lambda.functions.builtin.fn2.$.$;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;

/**
 * Repeat each element in an <code>Iterable</code> <code>n</code> times.
 *
 * @param <A> The Iterable element type
 */
public class Echo<A> implements Fn2<Integer, Iterable<A>, Iterable<A>> {

    private static final Echo<?> INSTANCE = new Echo<>();

    private Echo() {
    }

    @Override
    public Iterable<A> checkedApply(Integer n, Iterable<A> as) throws Throwable {
        return flatten(map(replicate(n), as));
    }

    @SuppressWarnings("unchecked")
    public static <A> Echo<A> echo() {
        return (Echo<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> echo(Integer n) {
        return $(echo(), n);
    }

    public static <A> Iterable<A> echo(Integer n, Iterable<A> as) {
        return $(echo(n), as);
    }
}
