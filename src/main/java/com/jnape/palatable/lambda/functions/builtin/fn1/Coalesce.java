package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Snoc;
import com.jnape.palatable.lambda.monoid.builtin.Merge;

import java.util.Collections;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

/**
 * Fold an <code>{@link Iterable}&lt;{@link Either}&lt;L, R&gt;&gt;</code> into an <code>{@link Either}&lt;{@link
 * Iterable}&lt;L&gt;, {@link Iterable}&lt;R&gt;&gt;</code>, preserving all results of the side that's returned. That
 * is, if the result is a <code>left</code>, it will contain all left values; if it is a <code>right</code>, it will
 * contain all right values.
 * <p>
 * It may be useful to think of this as a more efficient version of <code>{@link Merge}&lt;{@link Iterable}&lt;L&gt;,
 * {@link Iterable}&lt;R&gt;&gt;</code>.
 *
 * @param <L> the left parameter type
 * @param <R> the right parameter type
 */
public final class Coalesce<L, R> implements Fn1<Iterable<Either<L, R>>, Either<Iterable<L>, Iterable<R>>> {

    private static final Coalesce INSTANCE = new Coalesce();

    private Coalesce() {
    }

    @Override
    public Either<Iterable<L>, Iterable<R>> apply(Iterable<Either<L, R>> eithers) {
        return foldLeft((acc, e) -> acc
                                .biMapL(ls -> e.match(Snoc.<L>snoc().flip().apply(ls), constantly(ls)))
                                .flatMap(rs -> e.biMap(Collections::singletonList,
                                                       Snoc.<R>snoc().flip().apply(rs))),
                        right(Collections::emptyIterator),
                        eithers);
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Coalesce<A, B> coalesce() {
        return INSTANCE;
    }

    public static <A, B> Either<Iterable<A>, Iterable<B>> coalesce(Iterable<Either<A, B>> eithers) {
        return Coalesce.<A, B>coalesce().apply(eithers);
    }
}
