package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Effect;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * Given an {@link Effect}, "peek" at the value contained inside a {@link Functor} via {@link Functor#fmap(Fn1)},
 * applying the {@link Effect} to the contained value, if there is one.
 *
 * @param <A>  the functor parameter type
 * @param <FA> the functor type
 */
public final class Peek<A, FA extends Functor<A, ?>> implements Fn2<Effect<? super A>, FA, FA> {
    private static final Peek<?, ?> INSTANCE = new Peek<>();

    private Peek() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public FA checkedApply(Effect<? super A> consumer, FA fa) {
        return (FA) fa.fmap(a -> {
            consumer.apply(a).unsafePerformIO();
            return a;
        });
    }

    @SuppressWarnings("unchecked")
    public static <A, FA extends Functor<A, ?>> Peek<A, FA> peek() {
        return (Peek<A, FA>) INSTANCE;
    }

    public static <A, FA extends Functor<A, ?>> Fn1<FA, FA> peek(Effect<? super A> consumer) {
        return Peek.<A, FA>peek().apply(consumer);
    }

    public static <A, FA extends Functor<A, ?>> FA peek(Effect<? super A> consumer, FA fa) {
        return Peek.<A, FA>peek(consumer).apply(fa);
    }
}
