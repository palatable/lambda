package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monoid.Monoid.monoid;

/**
 * Run {@link IO} operations, aggregating their results in terms of the provided {@link Monoid}.
 *
 * @param <A> the {@link IO} result
 * @see com.jnape.palatable.lambda.semigroup.builtin.RunAll
 */
public final class RunAll<A> implements MonoidFactory<Monoid<A>, IO<A>> {

    private static final RunAll<?> INSTANCE = new RunAll<>();

    private RunAll() {
    }

    @Override
    public Monoid<IO<A>> apply(Monoid<A> monoid) {
        Semigroup<IO<A>> semigroup = com.jnape.palatable.lambda.semigroup.builtin.RunAll.runAll(monoid);
        return monoid(semigroup, io(monoid.identity()));
    }

    @SuppressWarnings("unchecked")
    public static <A> RunAll<A> runAll() {
        return (RunAll<A>) INSTANCE;
    }

    public static <A> Monoid<IO<A>> runAll(Monoid<A> monoid) {
        return RunAll.<A>runAll().apply(monoid);
    }

    public static <A> Fn1<IO<A>, IO<A>> runAll(Monoid<A> monoid, IO<A> x) {
        return runAll(monoid).apply(x);
    }

    public static <A> IO<A> runAll(Monoid<A> monoid, IO<A> x, IO<A> y) {
        return runAll(monoid, x).apply(y);
    }
}
