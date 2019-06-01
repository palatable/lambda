package com.jnape.palatable.lambda.functions.builtin.fn4;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.internal.iteration.SplicingIterator;

/**
 * Return an {@link Iterable} that is the result of splicing all of the elements of an {@link Iterable} into
 * another {@link Iterable} at a given position, optionally replacing a number of elements in the original {@link Iterable}.
 * <p>
 * If <code>startIndex</code> exceeds the size of <code>original</code>, <code>replacement</code> is concatenated to the end.
 * <p>
 * Note that <code>splice</code> is equivalent to, but more efficient than
 * <code>take(startIndex, original) ++ replacement ++ drop(startIndex + replaceCount, original)</code>.
 *
 * @param <A> The Iterable element type
 */
public final class Splice<A> implements Fn4<Integer, Integer, Iterable<A>, Iterable<A>, Iterable<A>> {

    private static final Splice<?> INSTANCE = new Splice<>();

    private Splice() {
    }

    @Override
    public Iterable<A> checkedApply(Integer startIndex, Integer replaceCount, Iterable<A> replacement, Iterable<A> original) {
        return () -> new SplicingIterator<>(startIndex, replaceCount, replacement.iterator(), original.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <A> Splice<A> splice() {
        return (Splice<A>) INSTANCE;
    }

    public static <A> Fn3<Integer, Iterable<A>, Iterable<A>, Iterable<A>> splice(Integer startIndex) {
        return Splice.<A>splice().apply(startIndex);
    }

    public static <A> Fn2<Iterable<A>, Iterable<A>, Iterable<A>> splice(Integer startIndex, Integer replaceCount) {
        return Splice.<A>splice(startIndex).apply(replaceCount);
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> splice(Integer startIndex, Integer replaceCount, Iterable<A> replacement) {
        return Splice.<A>splice(startIndex, replaceCount).apply(replacement);
    }

    public static <A> Iterable<A> splice(Integer startIndex, Integer replaceCount, Iterable<A> replacement, Iterable<A> original) {
        return Splice.splice(startIndex, replaceCount, replacement).apply(original);
    }
}
