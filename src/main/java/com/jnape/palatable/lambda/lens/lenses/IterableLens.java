package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.functions.builtin.fn1.Head;
import com.jnape.palatable.lambda.functions.builtin.fn1.Tail;
import com.jnape.palatable.lambda.functions.builtin.fn2.Cons;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.Optional;

import static com.jnape.palatable.lambda.functions.Fn2.fn2;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.lens.Lens.lens;
import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public final class IterableLens {

    private IterableLens() {
    }

    public static <A> Lens<Iterable<A>, Iterable<A>, Optional<A>, A> head() {
        return lens(Head::head, Cons.<A>cons().flip().compose(Tail.tail()).toBiFunction());
    }

    public static <A> Lens.Simple<Iterable<A>, Iterable<A>> tail() {
        return simpleLens(Tail::tail, fn2(Head.<A>head().andThen(o -> o.map(cons()).orElse(id()))).toBiFunction());
    }
}
