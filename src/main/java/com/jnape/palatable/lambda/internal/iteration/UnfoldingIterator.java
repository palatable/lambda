package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.NoSuchElementException;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

public final class UnfoldingIterator<A, B> extends ImmutableIterator<A> {
    private final Fn1<? super B, Maybe<Tuple2<A, B>>> function;
    private       B                                   seed;
    private       Maybe<Tuple2<A, B>>                 maybeAcc;

    public UnfoldingIterator(Fn1<? super B, Maybe<Tuple2<A, B>>> function, B seed) {
        this.function = function;
        this.seed = seed;
    }

    @Override
    public boolean hasNext() {
        if (maybeAcc == null)
            maybeAcc = function.apply(seed);

        return maybeAcc.fmap(constantly(true)).orElse(false);
    }

    @Override
    public A next() {
        if (!hasNext())
            throw new NoSuchElementException();

        Tuple2<A, B> acc  = maybeAcc.orElseThrow(NoSuchElementException::new);
        A            next = acc._1();
        seed = acc._2();
        maybeAcc = null;
        return next;
    }
}
