package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.SingletonHList;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.function.Function;

/**
 * Given a <code>{@link Function}&lt;A, B&gt;</code> and a <code>{@link SingletonHList}&lt;A&gt;</code>,
 * pop the head and apply it to the function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the result type
 */
public final class Into1<A, B> implements Fn2<Function<? super A, ? extends B>, SingletonHList<A>, B> {

    private static final Into1<?, ?> INSTANCE = new Into1<>();

    @Override
    public B checkedApply(Function<? super A, ? extends B> fn, SingletonHList<A> singletonHList) {
        return fn.apply(singletonHList.head());
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Into1<A, B> into1() {
        return (Into1<A, B>) INSTANCE;
    }

    public static <A, B> Fn1<SingletonHList<A>, B> into1(Function<? super A, ? extends B> fn) {
        return Into1.<A, B>into1().apply(fn);
    }

    public static <A, B> B into1(Function<? super A, ? extends B> fn, SingletonHList<A> singletonHList) {
        return Into1.<A, B>into1(fn).apply(singletonHList);
    }
}
