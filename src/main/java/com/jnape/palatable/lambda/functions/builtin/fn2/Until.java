package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.io.IO;

import static com.jnape.palatable.lambda.io.IO.io;

/**
 * Given a {@link Fn1 predicate function} for a value of some type <code>A</code> and an {@link IO} that yields a value
 * of type <code>A</code>, produce an {@link IO} that repeatedly executes the original {@link IO} until the predicate
 * returns true when applied to the yielded value.
 *
 * @param <A> the {@link IO} value type
 */
public final class Until<A> implements Fn2<Fn1<? super A, ? extends Boolean>, IO<A>, IO<A>> {

    private static final Until<?> INSTANCE = new Until<>();

    private Until() {
    }

    @Override
    public IO<A> checkedApply(Fn1<? super A, ? extends Boolean> pred, IO<A> io) {
        return io.flatMap(a -> pred.apply(a) ? io(a) : until(pred, io));
    }

    @SuppressWarnings("unchecked")
    public static <A> Until<A> until() {
        return (Until<A>) INSTANCE;
    }

    public static <A> Fn1<IO<A>, IO<A>> until(Fn1<? super A, ? extends Boolean> pred) {
        return Until.<A>until().apply(pred);
    }

    public static <A> IO<A> until(Fn1<? super A, ? extends Boolean> pred, IO<A> io) {
        return Until.<A>until(pred).apply(io);
    }
}
