package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.semigroup.Semigroup;

/**
 * Run {@link IO} operations, aggregating their results in terms of the provided {@link Semigroup}.
 *
 * @param <A> the {@link IO} result
 * @see com.jnape.palatable.lambda.monoid.builtin.RunAll
 */
public final class RunAll<A> implements SemigroupFactory<Semigroup<A>, IO<A>> {

    private static final RunAll<?> INSTANCE = new RunAll<>();

    private RunAll() {
    }

    @Override
    public Semigroup<IO<A>> checkedApply(Semigroup<A> semigroup) {
        return (ioX, ioY) -> ioY.zip(ioX.fmap(semigroup));
    }

    @SuppressWarnings("unchecked")
    public static <A> RunAll<A> runAll() {
        return (RunAll<A>) INSTANCE;
    }

    public static <A> Semigroup<IO<A>> runAll(Semigroup<A> semigroup) {
        return RunAll.<A>runAll().apply(semigroup);
    }

    public static <A> Fn1<IO<A>, IO<A>> runAll(Semigroup<A> semigroup, IO<A> ioX) {
        return runAll(semigroup).apply(ioX);
    }

    public static <A> IO<A> runAll(Semigroup<A> semigroup, IO<A> ioX, IO<A> ioY) {
        return runAll(semigroup, ioX).apply(ioY);
    }
}
