package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import java.util.Optional;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.ReduceLeft.reduceLeft;
import static com.jnape.palatable.lambda.functions.builtin.monadic.Reverse.reverse;

/**
 * Given an <code>Iterable</code> of <code>A</code>s and a <code>{@link DyadicFunction}&lt;A, A, A&gt;</code>,
 * iteratively accumulate over the <code>Iterable</code>, returning an <code>Optional&lt;A&gt;
 * </code> (if the
 * <code>Iterable</code> is empty, the result is <code>Optional.empty()</code>; otherwise, the result is wrapped in
 * <code>Optional.of()</code>. For this reason, <code>null</code> accumulation results are considered erroneous and will
 * throw.
 * <p>
 * This function is isomorphic to a right fold over the <code>Iterable</code> where the tail element is the starting
 * accumulation value and the result is lifted into an <code>Optional</code>.
 *
 * @param <A> The input Iterable element type, as well as the accumulation type
 * @see ReduceLeft
 * @see com.jnape.palatable.lambda.functions.builtin.triadic.FoldRight
 */
public final class ReduceRight<A> implements DyadicFunction<DyadicFunction<? super A, ? super A, ? extends A>, Iterable<A>, Optional<A>> {

    private ReduceRight() {
    }

    @Override
    public final Optional<A> apply(DyadicFunction<? super A, ? super A, ? extends A> function, Iterable<A> as) {
        return reduceLeft(function.flip(), reverse(as));
    }

    public static <A> ReduceRight<A> reduceRight() {
        return new ReduceRight<>();
    }

    public static <A> MonadicFunction<Iterable<A>, Optional<A>> reduceRight(
            DyadicFunction<? super A, ? super A, ? extends A> function) {
        return ReduceRight.<A>reduceRight().apply(function);
    }

    public static <A> Optional<A> reduceRight(DyadicFunction<? super A, ? super A, ? extends A> function,
                                              Iterable<A> as) {
        return ReduceRight.<A>reduceRight(function).apply(as);
    }
}
