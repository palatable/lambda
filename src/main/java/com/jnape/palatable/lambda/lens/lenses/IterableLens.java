package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.builtin.fn1.Head;
import com.jnape.palatable.lambda.functions.builtin.fn1.Tail;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.functions.Fn2.fn2;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link Iterable}s.
 */
public final class IterableLens {

    private IterableLens() {
    }

    /**
     * A lens focusing on the head of a given {@link Iterable}.
     * <p>
     * Note that this lens is effectively lawful, though difficult to prove since there is no
     * useful equality implementation for {@link Iterable}.
     *
     * @param <A> the Iterable element type
     * @return a lens focusing on the head element of an {@link Iterable}
     */
    public static <A> Lens.Simple<Iterable<A>, Maybe<A>> head() {
        return simpleLens(Head::head, (s, maybeB) -> {
            Iterable<A> tail = Tail.tail(s);
            return maybeB.fmap(b -> cons(b, tail)).orElse(tail);
        });
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
