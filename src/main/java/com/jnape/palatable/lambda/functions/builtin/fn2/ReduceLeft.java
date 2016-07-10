package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Iterator;
import java.util.Optional;

import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

/**
 * Given an <code>Iterable</code> of <code>A</code>s and a <code>{@link Fn2}&lt;A, A, A&gt;</code>, iteratively
 * accumulate over the <code>Iterable</code>, returning an <code>Optional&lt;A&gt;</code> (if the <code>Iterable</code>
 * is empty, the result is <code>Optional.empty()</code>; otherwise, the result is wrapped in
 * <code>Optional.of()</code>. For this reason, <code>null</code> accumulation results are considered erroneous and will
 * throw.
 * <p>
 * This function is isomorphic to a left fold over the <code>Iterable</code> where the head element is the starting
 * accumulation value and the result is lifted into an <code>Optional</code>.
 *
 * @param <A> The input Iterable element type, as well as the accumulation type
 * @see ReduceRight
 * @see com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft
 */
public final class ReduceLeft<A> implements Fn2<Fn2<? super A, ? super A, ? extends A>, Iterable<A>, Optional<A>> {

    private ReduceLeft() {
    }

    @Override
    public Optional<A> apply(Fn2<? super A, ? super A, ? extends A> fn, Iterable<A> as) {
        Iterator<A> iterator = as.iterator();
        if (!iterator.hasNext())
            return Optional.empty();

        return Optional.of(foldLeft(fn, iterator.next(), () -> iterator));
    }

    public static <A> ReduceLeft<A> reduceLeft() {
        return new ReduceLeft<>();
    }

    public static <A> Fn1<Iterable<A>, Optional<A>> reduceLeft(Fn2<? super A, ? super A, ? extends A> fn) {
        return ReduceLeft.<A>reduceLeft().apply(fn);
    }

    public static <A> Optional<A> reduceLeft(Fn2<? super A, ? super A, ? extends A> fn, Iterable<A> as) {
        return ReduceLeft.<A>reduceLeft(fn).apply(as);
    }
}
