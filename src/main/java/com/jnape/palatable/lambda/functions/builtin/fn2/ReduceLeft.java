package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;

import java.util.Iterator;
import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

/**
 * Given an <code>{@link Iterable}&lt;A&gt;</code> and a <code>{@link BiFunction}&lt;A, A, A&gt;</code>, iteratively
 * accumulate over the {@link Iterable}, returning <code>{@link Maybe}&lt;A&gt;</code>. If the {@link Iterable} is
 * empty, the result is {@link Maybe#nothing()}; otherwise, the result is wrapped in {@link Maybe#just}. For this
 * reason, <code>null</code> accumulation results are considered erroneous and will throw.
 * <p>
 * This function is isomorphic to a left fold over the {@link Iterable} where the head element is the starting
 * accumulation value and the result is lifted into {@link Maybe}.
 *
 * @param <A> The input Iterable element type, as well as the accumulation type
 * @see ReduceRight
 * @see FoldLeft
 */
public final class ReduceLeft<A> implements Fn2<BiFunction<? super A, ? super A, ? extends A>, Iterable<A>, Maybe<A>> {

    private static final ReduceLeft<?> INSTANCE = new ReduceLeft<>();

    private ReduceLeft() {
    }

    @Override
    public Maybe<A> checkedApply(BiFunction<? super A, ? super A, ? extends A> fn, Iterable<A> as) {
        Iterator<A> iterator = as.iterator();
        return !iterator.hasNext() ? nothing() : just(foldLeft(fn, iterator.next(), () -> iterator));
    }

    @SuppressWarnings("unchecked")
    public static <A> ReduceLeft<A> reduceLeft() {
        return (ReduceLeft<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Maybe<A>> reduceLeft(BiFunction<? super A, ? super A, ? extends A> fn) {
        return ReduceLeft.<A>reduceLeft().apply(fn);
    }

    public static <A> Maybe<A> reduceLeft(BiFunction<? super A, ? super A, ? extends A> fn, Iterable<A> as) {
        return ReduceLeft.<A>reduceLeft(fn).apply(as);
    }
}
