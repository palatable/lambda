package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Tail.tail;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static java.util.Collections.emptyList;

public final class PrependAll<A> implements Fn2<A, Iterable<A>, Iterable<A>> {

    private static final PrependAll INSTANCE = new PrependAll();

    private PrependAll() {
    }

    @Override
    public Iterable<A> apply(A a, Iterable<A> as) {
        return () -> head(as).map(head -> cons(a, cons(head, prependAll(a, tail(as))))).orElse(emptyList()).iterator();
    }

    @SuppressWarnings("unchecked")
    public static <A> PrependAll<A> prependAll() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> prependAll(A a) {
        return PrependAll.<A>prependAll().apply(a);
    }

    public static <A> Iterable<A> prependAll(A a, Iterable<A> as) {
        return prependAll(a).apply(as);
    }
}
