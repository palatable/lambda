package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.BoundedBifunctor;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Given two {@link Consumer}s, "peek" at the values contained inside a {@link Bifunctor} via
 * {@link Bifunctor#biMap(Function, Function)}, applying the {@link Consumer}s to the contained values,
 * if there are any.
 *
 * @param <A>   the bifunctor's first parameter type
 * @param <B>   the bifunctor's second parameter type
 * @param <FAB> the bifunctor type
 */
public final class Peek2<A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> implements Fn3<Consumer<? super A>, Consumer<? super B>, FAB, FAB> {
    private static final Peek2 INSTANCE = new Peek2<>();

    private Peek2() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public FAB apply(Consumer<? super A> aConsumer, Consumer<? super B> bConsumer, FAB fab) {
        return (FAB) fab.biMap(a -> {
            aConsumer.accept(a);
            return a;
        }, b -> {
            bConsumer.accept(b);
            return b;
        });
    }

    @SuppressWarnings("unchecked")
    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> Peek2<A, B, FAB> peek2() {
        return INSTANCE;
    }

    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> Fn2<Consumer<? super B>, FAB, FAB> peek2(
            Consumer<? super A> aConsumer) {
        return Peek2.<A, B, FAB>peek2().apply(aConsumer);
    }

    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> Fn1<FAB, FAB> peek2(
            Consumer<? super A> aConsumer,
            Consumer<? super B> bConsumer) {
        return Peek2.<A, B, FAB>peek2(aConsumer).apply(bConsumer);
    }

    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> FAB peek2(
            Consumer<? super A> aConsumer,
            Consumer<? super B> bConsumer,
            FAB fab) {
        return Peek2.<A, B, FAB>peek2(aConsumer, bConsumer).apply(fab);
    }
}
