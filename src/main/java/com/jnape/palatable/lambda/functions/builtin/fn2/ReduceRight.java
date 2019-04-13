package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight;

import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Reverse.reverse;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ReduceLeft.reduceLeft;

/**
 * Given an <code>{@link Iterable}&lt;A&gt;</code> and a <code>{@link BiFunction}&lt;A, A, A&gt;</code>, iteratively
 * accumulate over the {@link Iterable}, returning <code>{@link Maybe}&lt;A&gt;</code>. If the {@link Iterable} is
 * empty, the result is {@link Maybe#nothing()}; otherwise, the result is wrapped in {@link Maybe#just}. For this
 * reason, <code>null</code> accumulation results are considered erroneous and will throw.
 * <p>
 * This function is isomorphic to a right fold over the {@link Iterable} where the tail element is the starting
 * accumulation value and the result is lifted into {@link Maybe}.
 *
 * @param <A> The input Iterable element type, as well as the accumulation type
 * @see ReduceLeft
 * @see FoldRight
 */
public final class ReduceRight<A> implements Fn2<BiFunction<? super A, ? super A, ? extends A>, Iterable<A>, Maybe<A>> {

    private static final ReduceRight<?> INSTANCE = new ReduceRight<>();

    private ReduceRight() {
    }

    @Override
    public final Maybe<A> apply(BiFunction<? super A, ? super A, ? extends A> fn, Iterable<A> as) {
        return reduceLeft((b, a) -> fn.apply(a, b), reverse(as));
    }

    @SuppressWarnings("unchecked")
    public static <A> ReduceRight<A> reduceRight() {
        return (ReduceRight<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Maybe<A>> reduceRight(BiFunction<? super A, ? super A, ? extends A> fn) {
        return ReduceRight.<A>reduceRight().apply(fn);
    }

    public static <A> Maybe<A> reduceRight(BiFunction<? super A, ? super A, ? extends A> fn, Iterable<A> as) {
        return ReduceRight.<A>reduceRight(fn).apply(as);
    }
}
