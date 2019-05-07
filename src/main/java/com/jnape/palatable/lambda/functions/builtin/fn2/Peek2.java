package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Effect;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.BoundedBifunctor;

/**
 * Given two {@link Effect}s, "peek" at the values contained inside a {@link Bifunctor} via
 * {@link BoundedBifunctor#biMap(Fn1, Fn1)}, applying the {@link Effect}s to the contained values, if there are any.
 *
 * @param <A>   the bifunctor's first parameter type
 * @param <B>   the bifunctor's second parameter type
 * @param <FAB> the bifunctor type
 */
public final class Peek2<A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> implements
        Fn3<Effect<? super A>, Effect<? super B>, FAB, FAB> {
    private static final Peek2<?, ?, ?> INSTANCE = new Peek2<>();

    private Peek2() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public FAB checkedApply(Effect<? super A> aConsumer, Effect<? super B> bConsumer, FAB fab) {
        return (FAB) fab.biMap(a -> {
            aConsumer.apply(a).unsafePerformIO();
            return a;
        }, b -> {
            bConsumer.apply(b).unsafePerformIO();
            return b;
        });
    }

    @SuppressWarnings("unchecked")
    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> Peek2<A, B, FAB> peek2() {
        return (Peek2<A, B, FAB>) INSTANCE;
    }

    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> Fn2<Effect<? super B>, FAB, FAB>
    peek2(Effect<? super A> aConsumer) {
        return Peek2.<A, B, FAB>peek2().apply(aConsumer);
    }

    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> Fn1<FAB, FAB> peek2(
            Effect<? super A> aConsumer,
            Effect<? super B> bConsumer) {
        return Peek2.<A, B, FAB>peek2(aConsumer).apply(bConsumer);
    }

    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> FAB peek2(
            Effect<? super A> aConsumer,
            Effect<? super B> bConsumer,
            FAB fab) {
        return Peek2.<A, B, FAB>peek2(aConsumer, bConsumer).apply(fab);
    }
}
