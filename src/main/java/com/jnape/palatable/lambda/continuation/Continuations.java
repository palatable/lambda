package com.jnape.palatable.lambda.continuation;

import java.util.Iterator;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public final class Continuations {

    private Continuations() {
    }

    @SafeVarargs
    public static <A> Continuation<A> continuing(A... as) {
        return continuing(asList(as));
    }

    public static <A> Continuation<A> continuing(Stream<A> as) {
        return continuing(as.iterator());
    }

    public static <A> Continuation<A> continuing(Iterable<A> as) {
        return continuing(as.iterator());
    }

    public static <A> Continuation<A> continuing(Iterator<A> as) {
        return new IteratorWrappingContinuation<>(as);
    }
}
