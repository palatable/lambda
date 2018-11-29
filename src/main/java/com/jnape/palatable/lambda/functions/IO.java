package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

/**
 * A {@link Monad} representing some effectful computation to be performed.
 *
 * @param <A> the result type
 */
public interface IO<A> extends Monad<A, IO<?>> {

    /**
     * Run the effect represented by this {@link IO} instance
     *
     * @return the result of the effect
     */
    A unsafePerformIO();

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> flatMap(Function<? super A, ? extends Monad<B, IO<?>>> f) {
        return () -> f.apply(unsafePerformIO()).<IO<B>>coerce().unsafePerformIO();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> pure(B b) {
        return () -> b;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> fmap(Function<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> zip(Applicative<Function<? super A, ? extends B>, IO<?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> discardL(Applicative<B, IO<?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<A> discardR(Applicative<B, IO<?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * Static factory method for coercing a lambda to an {@link IO}.
     *
     * @param io  the lambda to coerce
     * @param <A> the result type
     * @return the {@link IO}
     */
    static <A> IO<A> io(IO<A> io) {
        return io;
    }

    /**
     * Static factory method for creating an {@link IO} that just returns <code>a</code> when performed.
     *
     * @param a   the result
     * @param <A> the result type
     * @return the {@link IO}
     */
    static <A> IO<A> io(A a) {
        return io(() -> a);
    }

    /**
     * Static factory method for creating an {@link IO} that runs <code>runnable</code> and returns {@link Unit}.
     *
     * @param runnable the {@link Runnable}
     * @return the {@link IO}
     */
    static IO<Unit> io(Runnable runnable) {
        return io(() -> {
            runnable.run();
            return UNIT;
        });
    }

    /**
     * Static factory method for creating an {@link IO} from an <code>{@link Fn1}&lt;{@link Unit}, A&gt;</code>.
     *
     * @param fn1 the {@link Fn1}
     * @param <A> the result type
     * @return the {@link IO}
     */
    static <A> IO<A> io(Fn1<Unit, A> fn1) {
        return io(() -> fn1.apply(UNIT));
    }
}
