package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn3.Bracket;
import com.jnape.palatable.lambda.io.IO;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Bracket.bracket;
import static com.jnape.palatable.lambda.io.IO.io;

/**
 * Given an {@link IO} yielding some {@link AutoCloseable} type <code>A</code> and a kleisli arrow from that type to a
 * new {@link IO} of type <code>B</code>, attempt to provision the <code>A</code>, applying the body operation if
 * provisioning was successful and ensuring that {@link AutoCloseable#close} is called regardless of whether the body
 * succeeds or fails.
 * <p>
 * This is the canonical {@link Bracket bracketing} operation for {@link AutoCloseable AutoCloseables}.
 *
 * @param <A> the initial {@link AutoCloseable} value type to map and clean up
 * @param <B> the resulting type
 * @see Bracket
 */
public final class AutoBracket<A extends AutoCloseable, B> implements
        Fn2<IO<A>, Fn1<? super A, ? extends IO<B>>, IO<B>> {

    private static final AutoBracket<?, ?> INSTANCE = new AutoBracket<>();

    private AutoBracket() {
    }

    @Override
    public IO<B> checkedApply(IO<A> io, Fn1<? super A, ? extends IO<B>> bodyIO) {
        return bracket(io, a -> io(a::close), bodyIO);
    }

    @SuppressWarnings("unchecked")
    public static <A extends AutoCloseable, B> AutoBracket<A, B> autoBracket() {
        return (AutoBracket<A, B>) INSTANCE;
    }

    public static <A extends AutoCloseable, B> Fn1<Fn1<? super A, ? extends IO<B>>, IO<B>> autoBracket(IO<A> io) {
        return AutoBracket.<A, B>autoBracket().apply(io);
    }

    public static <A extends AutoCloseable, B> IO<B> autoBracket(IO<A> io, Fn1<? super A, ? extends IO<B>> bodyIO) {
        return AutoBracket.<A, B>autoBracket(io).apply(bodyIO);
    }
}
