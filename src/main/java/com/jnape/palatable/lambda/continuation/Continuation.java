package com.jnape.palatable.lambda.continuation;

import com.jnape.palatable.lambda.adt.tuples.Tuple2;
import com.jnape.palatable.lambda.applicative.Functor;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import java.util.Iterator;
import java.util.Optional;

import static com.jnape.palatable.lambda.adt.tuples.Tuple2.tuple;
import static com.jnape.palatable.lambda.continuation.Memo.memoize;

@FunctionalInterface
public interface Continuation<A> extends Functor<A>, Iterable<A> {

    Optional<Tuple2<A, Continuation<A>>> next();

    @Override
    default Iterator<A> iterator() {
        return ContinuationIterator.wrap(this);
    }

    default Continuation<A> then(Continuation<A> more) {
        return () -> next()
                .map(t -> Optional.of(tuple(t._1, t._2.then(more))))
                .orElseGet(more::next);
    }

    @Override
    default <B> Continuation<B> fmap(MonadicFunction<? super A, ? extends B> fn) {
        return (() -> next().map(t -> t.biMap(fn, c -> c.fmap(fn))));
    }

    default Continuation<A> memoized() {
        return memoize((() -> next().map(t -> tuple(t._1, t._2.memoized()))))::get;
    }
}
