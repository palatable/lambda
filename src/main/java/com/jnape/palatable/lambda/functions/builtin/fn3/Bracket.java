package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.Monad;

/**
 * Given an {@link IO} that yields some type <code>A</code>, a cleanup operation to run if a value of that type could be
 * provisioned, and a kleisli arrow from that type to a new {@link IO} of type <code>B</code>, produce an
 * <code>{@link IO}&lt;B&gt;</code> that, when run, will provision the <code>A</code>,
 * {@link Monad#flatMap(Fn1) flatMap} it to <code>B</code>, and clean up the original value if it was produced in the
 * first place.
 *
 * @param <A> the initial value to map and clean up
 * @param <B> the resulting type
 */
public final class Bracket<A, B> implements
        Fn3<IO<A>, Fn1<? super A, ? extends IO<?>>, Fn1<? super A, ? extends IO<B>>, IO<B>> {

    private static final Bracket<?, ?> INSTANCE = new Bracket<>();

    private Bracket() {
    }

    @Override
    public IO<B> checkedApply(IO<A> io, Fn1<? super A, ? extends IO<?>> cleanupIO,
                              Fn1<? super A, ? extends IO<B>> bodyIO) throws Throwable {
        return io.flatMap(a -> bodyIO.apply(a).ensuring(cleanupIO.apply(a)));
    }

    @SuppressWarnings("unchecked")
    public static <A, B> Bracket<A, B> bracket() {
        return (Bracket<A, B>) INSTANCE;
    }

    public static <A, B> Fn2<Fn1<? super A, ? extends IO<?>>, Fn1<? super A, ? extends IO<B>>, IO<B>> bracket(
            IO<A> io) {
        return Bracket.<A, B>bracket().apply(io);
    }

    public static <A, B> Fn1<Fn1<? super A, ? extends IO<B>>, IO<B>> bracket(
            IO<A> io, Fn1<? super A, ? extends IO<?>> cleanupIO) {
        return Bracket.<A, B>bracket(io).apply(cleanupIO);
    }

    public static <A, B> IO<B> bracket(IO<A> io, Fn1<? super A, ? extends IO<?>> cleanupIO,
                                       Fn1<? super A, ? extends IO<B>> bodyIO) {
        return Bracket.<A, B>bracket(io, cleanupIO).apply(bodyIO);
    }
}
