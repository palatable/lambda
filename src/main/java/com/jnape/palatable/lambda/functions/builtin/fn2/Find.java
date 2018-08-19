package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Not.not;
import static com.jnape.palatable.lambda.functions.builtin.fn2.DropWhile.dropWhile;

/**
 * Iterate the elements in an <code>Iterable</code>, applying a predicate to each one, returning the first element that
 * matches the predicate, wrapped in a {@link Maybe}. If no elements match the predicate, the result is
 * {@link Maybe#nothing()}. This function short-circuits, and so is safe to use on potentially infinite {@link Iterable}
 * instances that guarantee to have an eventually matching element.
 *
 * @param <A> the Iterable element type
 */
public final class Find<A> implements Fn2<Function<? super A, ? extends Boolean>, Iterable<A>, Maybe<A>> {

    private static final Find INSTANCE = new Find();

    private Find() {
    }

    @Override
    public Maybe<A> apply(Function<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        return head(dropWhile(not(predicate), as));
    }

    @SuppressWarnings("unchecked")
    public static <A> Find<A> find() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Maybe<A>> find(Function<? super A, ? extends Boolean> predicate) {
        return Find.<A>find().apply(predicate);
    }

    public static <A> Maybe<A> find(Function<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        return Find.<A>find(predicate).apply(as);
    }
}
