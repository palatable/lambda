package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.internal.ImmutableQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Not.not;
import static com.jnape.palatable.lambda.io.IO.io;

public final class TrampoliningIterator<A, B> implements Iterator<B> {
    private final Fn1<? super A, ? extends Iterable<RecursiveResult<A, B>>> fn;
    private final A                                                         a;

    private ImmutableQueue<Iterator<RecursiveResult<A, B>>> remaining;
    private B                                               b;

    public TrampoliningIterator(Fn1<? super A, ? extends Iterable<RecursiveResult<A, B>>> fn, A a) {
        this.fn = fn;
        this.a = a;
    }

    @Override
    public boolean hasNext() {
        queueNextIfPossible();
        return b != null;
    }

    @Override
    public B next() {
        if (!hasNext())
            throw new NoSuchElementException();
        B next = b;
        b = null;
        return next;
    }

    private void queueNextIfPossible() {
        if (remaining == null)
            pruneAfter(() -> remaining = ImmutableQueue.<Iterator<RecursiveResult<A, B>>>empty()
                    .pushFront(fn.apply(a).iterator()));

        while (b == null && remaining.head().match(constantly(false), constantly(true))) {
            tickNext();
        }
    }

    private void tickNext() {
        pruneAfter(() -> remaining.head().orElseThrow(NoSuchElementException::new).next())
                .match(a -> io(() -> {
                    pruneAfter(() -> remaining = remaining.pushFront(fn.apply(a).iterator()));
                }), b -> io(() -> {
                    this.b = b;
                })).unsafePerformIO();
    }

    private <R> R pruneAfter(Fn0<? extends R> fn) {
        R r = fn.apply();
        while (remaining.head().match(constantly(false), not(Iterator::hasNext))) {
            remaining = remaining.tail();
        }
        return r;
    }
}
