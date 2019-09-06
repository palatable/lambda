package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Effect;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.BoundedBifunctor;
import com.jnape.palatable.lambda.io.IO;

/**
 * Given two {@link Effect}s, "peek" at the values contained inside a {@link Bifunctor} via
 * {@link BoundedBifunctor#biMap(Fn1, Fn1)}, applying the {@link Effect}s to the contained values, if there are any.
 *
 * @param <A>   the bifunctor's first parameter type
 * @param <B>   the bifunctor's second parameter type
 * @param <FAB> the bifunctor type
 * @deprecated in favor of producing an {@link IO} from the given {@link BoundedBifunctor} and explicitly running it
 */
@Deprecated
public final class Peek2<A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> implements
        Fn3<Fn1<? super A, ? extends IO<?>>, Fn1<? super B, ? extends IO<?>>, FAB, FAB> {
    private static final Peek2<?, ?, ?> INSTANCE = new Peek2<>();

    private Peek2() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public FAB checkedApply(Fn1<? super A, ? extends IO<?>> effectA, Fn1<? super B, ? extends IO<?>> effectB, FAB fab) {
        return (FAB) fab.biMap(a -> {
            effectA.apply(a).unsafePerformIO();
            return a;
        }, b -> {
            effectB.apply(b).unsafePerformIO();
            return b;
        });
    }

    @SuppressWarnings("unchecked")
    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> Peek2<A, B, FAB> peek2() {
        return (Peek2<A, B, FAB>) INSTANCE;
    }

    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>>
    Fn2<Fn1<? super B, ? extends IO<?>>, FAB, FAB> peek2(Fn1<? super A, ? extends IO<?>> effectA) {
        return Peek2.<A, B, FAB>peek2().apply(effectA);
    }

    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> Fn1<FAB, FAB> peek2(
            Fn1<? super A, ? extends IO<?>> effectA,
            Fn1<? super B, ? extends IO<?>> effectB) {
        return Peek2.<A, B, FAB>peek2(effectA).apply(effectB);
    }

    public static <A, B, FAB extends BoundedBifunctor<A, B, ? super A, ? super B, ?>> FAB peek2(
            Fn1<? super A, ? extends IO<?>> effectA,
            Fn1<? super B, ? extends IO<?>> effectB,
            FAB fab) {
        return Peek2.<A, B, FAB>peek2(effectA, effectB).apply(fab);
    }
}
