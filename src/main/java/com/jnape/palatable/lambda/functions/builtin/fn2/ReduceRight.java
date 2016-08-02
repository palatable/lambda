package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Optional;
import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Reverse.reverse;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ReduceLeft.reduceLeft;

/**
 * Given an <code>Iterable</code> of <code>A</code>s and a <code>{@link BiFunction}&lt;A, A, A&gt;</code>, iteratively
 * accumulate over the <code>Iterable</code>, returning an <code>Optional&lt;A&gt; </code> (if the <code>Iterable</code>
 * is empty, the result is <code>Optional.empty()</code>; otherwise, the result is wrapped in
 * <code>Optional.of()</code>. For this reason, <code>null</code> accumulation results are considered erroneous and will
 * throw.
 * <p>
 * This function is isomorphic to a right fold over the <code>Iterable</code> where the tail element is the starting
 * accumulation value and the result is lifted into an <code>Optional</code>.
 *
 * @param <A> The input Iterable element type, as well as the accumulation type
 * @see ReduceLeft
 * @see com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight
 */
public final class ReduceRight<A> implements Fn2<BiFunction<? super A, ? super A, ? extends A>, Iterable<A>, Optional<A>> {

    private ReduceRight() {
    }

    @Override
    public final Optional<A> apply(BiFunction<? super A, ? super A, ? extends A> fn, Iterable<A> as) {
        return reduceLeft((b, a) -> fn.apply(a, b), reverse(as));
    }

    public static <A> ReduceRight<A> reduceRight() {
        return new ReduceRight<>();
    }

    public static <A> Fn1<Iterable<A>, Optional<A>> reduceRight(BiFunction<? super A, ? super A, ? extends A> fn) {
        return ReduceRight.<A>reduceRight().apply(fn);
    }

    public static <A> Optional<A> reduceRight(BiFunction<? super A, ? super A, ? extends A> fn, Iterable<A> as) {
        return ReduceRight.<A>reduceRight(fn).apply(as);
    }
}
