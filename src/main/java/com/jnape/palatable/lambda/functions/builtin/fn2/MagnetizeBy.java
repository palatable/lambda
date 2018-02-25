package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Collections;
import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Size.size;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Uncons.uncons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Drop.drop;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Unfoldr.unfoldr;

/**
 * Given a binary predicate and an <code>{@link Iterable}&lt;A&gt;</code>, return an <code>{@link Iterable}&lt;{@link
 * Iterable}&lt;A&gt;&gt;</code> of the contiguous groups of elements that match the predicate pairwise.
 * <p>
 * Example: <code>magnetizeBy((x, y) -> x <= y, asList(1, 2, 3, 2, 2, 3, 2, 1)); // [[1, 2, 3], [2, 2, 3], [2],
 * [1]]</code>
 *
 * @param <A>
 */
public final class MagnetizeBy<A> implements Fn2<BiFunction<? super A, ? super A, Boolean>, Iterable<A>, Iterable<Iterable<A>>> {

    private static final MagnetizeBy INSTANCE = new MagnetizeBy();

    private MagnetizeBy() {
    }

    @Override
    public Iterable<Iterable<A>> apply(BiFunction<? super A, ? super A, Boolean> predicate, Iterable<A> as) {
        return () -> uncons(as).fmap(into((A head, Iterable<A> tail) -> {
            Iterable<A> group = cons(head, unfoldr(into((pivot, ys) -> uncons(ys)
                    .flatMap(into((y, recurse) -> predicate.apply(pivot, y)
                                                  ? just(tuple(y, tuple(y, recurse)))
                                                  : nothing()))), tuple(head, tail)));
            return cons(group, () -> apply(predicate, drop(size(group).intValue(), as)).iterator());
        })).orElseGet(() -> Collections::emptyIterator).iterator();
    }

    @SuppressWarnings("unchecked")
    public static <A> MagnetizeBy<A> magnetizeBy() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<Iterable<A>>> magnetizeBy(
            BiFunction<? super A, ? super A, Boolean> predicate) {
        return MagnetizeBy.<A>magnetizeBy().apply(predicate);
    }

    public static <A> Iterable<Iterable<A>> magnetizeBy(
            BiFunction<? super A, ? super A, Boolean> predicate,
            Iterable<A> as) {
        return MagnetizeBy.<A>magnetizeBy(predicate).apply(as);
    }
}
