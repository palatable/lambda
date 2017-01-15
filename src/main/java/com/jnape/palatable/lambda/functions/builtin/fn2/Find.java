package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn2.DropWhile.dropWhile;

/**
 * Iterate the elements in an <code>Iterable</code>, applying a predicate to each one, returning the first element that
 * matches the predicate, wrapped in an <code>Optional</code>. If no elements match the predicate, the result is
 * <code>Optional.empty()</code>. This function short-circuits, and so is safe to use on potentially infinite
 * <code>Iterable</code>s that guarantee to have an eventually matching element.
 *
 * @param <A> the Iterable element type
 */
public final class Find<A> implements Fn2<Function<? super A, Boolean>, Iterable<A>, Optional<A>> {

    private static final Find INSTANCE = new Find();

    private Find() {
    }

    @Override
    public Optional<A> apply(Function<? super A, Boolean> predicate, Iterable<A> as) {
        return head(dropWhile(((Predicate<A>) predicate::apply).negate(), as));
    }

    @SuppressWarnings("unchecked")
    public static <A> Find<A> find() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Optional<A>> find(Function<? super A, Boolean> predicate) {
        return Find.<A>find().apply(predicate);
    }

    public static <A> Optional<A> find(Function<? super A, Boolean> predicate, Iterable<A> as) {
        return Find.<A>find(predicate).apply(as);
    }
}
