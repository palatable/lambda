package com.jnape.palatable.lambda.functions.recursion;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn2.Unfoldr;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult.Recurse;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult.Terminate;

/**
 * Given an <code>{@link Fn1}&lt;A, {@link CoProduct2}&lt;A, B, ?&gt;&gt;</code> (analogous to "recurse" and "return"
 * tail position instructions, respectively), produce a <code>{@link Fn1}&lt;A, B&gt;</code> that unrolls the original
 * function by iteratively passing each result that matches the input (<code>A</code>) back to the original function,
 * and then terminating on and returning the first output (<code>B</code>).
 * <p>
 * This is isomorphic to - though presumably faster than - taking the last element of an {@link Unfoldr} call.
 *
 * @param <A> the trampolined function's input type
 * @param <B> the trampolined function's output type
 */
public final class Trampoline<A, B> implements Fn2<Fn1<? super A, ? extends RecursiveResult<A, B>>, A, B> {

    private static final Trampoline<?, ?> INSTANCE = new Trampoline<>();

    @Override
    public B checkedApply(Fn1<? super A, ? extends RecursiveResult<A, B>> fn, A a) {
        RecursiveResult<A, B> next = fn.apply(a);
        while (next instanceof Recurse)
            next = fn.apply(((Recurse<A, B>) next).a);
        return ((Terminate<A, B>) next).b;
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Trampoline<A, B> trampoline() {
        return (Trampoline<A, B>) INSTANCE;
    }

    public static <A, B> Fn1<A, B> trampoline(Fn1<? super A, ? extends RecursiveResult<A, B>> fn) {
        return Trampoline.<A, B>trampoline().apply(fn);
    }

    public static <A, B> B trampoline(Fn1<? super A, ? extends RecursiveResult<A, B>> fn, A a) {
        return trampoline(fn).apply(a);
    }
}
