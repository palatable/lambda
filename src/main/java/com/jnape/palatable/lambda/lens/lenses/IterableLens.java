package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.builtin.fn1.Head;
import com.jnape.palatable.lambda.functions.builtin.fn1.Tail;
import com.jnape.palatable.lambda.functions.builtin.fn2.Cons;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.functions.Fn2.fn2;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.lens.Lens.lens;
import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link Iterable}s.
 */
public final class IterableLens {

    private IterableLens() {
    }

    /**
     * A lens focusing on the head of a given {@link Iterable}.
     *
     * @param <A> the Iterable element type
     * @return a lens focusing on the head element of an {@link Iterable}
     */
    public static <A> Lens<Iterable<A>, Iterable<A>, Maybe<A>, A> head() {
        return lens(Head::head, Cons.<A>cons().flip().compose(Tail.tail()).toBiFunction());
    }

    /**
     * A lens focusing on the tail of an {@link Iterable}.
     *
     * @param <A> the Iterable element type
     * @return a lens focusing on the tail of an {@link Iterable}
     */
    public static <A> Lens.Simple<Iterable<A>, Iterable<A>> tail() {
        return simpleLens(Tail::tail, fn2(Head.<A>head().andThen(o -> o.fmap(cons()).orElse(id()))).toBiFunction());
    }
}
