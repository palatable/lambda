package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.functions.builtin.fn2.LazyRec.lazyRec;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * Given an <code>Iterable</code> of <code>A</code>s, a starting {@link Lazy lazy} value <code>B</code>, and a
 * <code>{@link BiFunction}&lt;A, {@link Lazy}&lt;B&gt;, {@link Lazy}&lt;B&gt;&gt;</code>, iteratively accumulate over
 * the <code>Iterable</code>, ultimately returning a final <code>{@link Lazy}&lt;B&gt;</code> value. If the
 * <code>Iterable</code> is empty, just return the starting <code>{@link Lazy}&lt;B&gt;</code> value.
 * This function is computationally the iterative inverse of {@link FoldLeft}, but uses {@link Lazy} to allow support
 * stack-safe execution.
 * <p>
 * Example:
 * <pre>
 * {@code
 * Lazy<Iterable<Integer>> lazyCopy = foldRight(
 *     (head, lazyTail) -&gt; lazy(cons(head, () -&gt; lazyTail.value().iterator())),
 *     lazy(emptyList()),
 *     iterate(x -&gt; x + 1, 0));
 * Iterable<Integer> copy = () -&gt; lazyCopy.value().iterator();
 * take(3, copy).forEach(System.out::println); // prints "1, 2, 3"
 * take(3, copy).forEach(System.out::println); // prints "1, 2, 3"
 * }
 * </pre>
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Catamorphism"
 * target="_top">Catamorphisms</a>.
 *
 * @param <A> The Iterable element type
 * @param <B> The accumulation type
 * @see FoldLeft
 */
public final class FoldRight<A, B> implements
        Fn3<BiFunction<? super A, ? super Lazy<B>, ? extends Lazy<B>>, Lazy<B>, Iterable<A>, Lazy<B>> {

    private static final FoldRight<?, ?> INSTANCE = new FoldRight<>();

    private FoldRight() {
    }

    @Override
    public Lazy<B> checkedApply(BiFunction<? super A, ? super Lazy<B>, ? extends Lazy<B>> fn, Lazy<B> acc,
                                Iterable<A> as) {
        return lazyRec((f, lazyIt) -> lazyIt.flatMap(it -> it.hasNext()
                                                           ? fn.apply(it.next(), f.apply(lazy(it)))
                                                           : acc),
                       lazy(as::iterator));
    }

    @SuppressWarnings("unchecked")
    public static <A, B> FoldRight<A, B> foldRight() {
        return (FoldRight<A, B>) INSTANCE;
    }

    public static <A, B> Fn2<Lazy<B>, Iterable<A>, Lazy<B>> foldRight(
            BiFunction<? super A, ? super Lazy<B>, ? extends Lazy<B>> fn) {
        return FoldRight.<A, B>foldRight().apply(fn);
    }

    public static <A, B> Fn1<Iterable<A>, Lazy<B>> foldRight(
            BiFunction<? super A, ? super Lazy<B>, ? extends Lazy<B>> fn,
            Lazy<B> acc) {
        return FoldRight.<A, B>foldRight(fn).apply(acc);
    }

    public static <A, B> Lazy<B> foldRight(BiFunction<? super A, ? super Lazy<B>, ? extends Lazy<B>> fn, Lazy<B> acc,
                                           Iterable<A> as) {
        return FoldRight.<A, B>foldRight(fn, acc).apply(as);
    }
}
