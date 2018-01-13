package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Given a {@link Consumer}, "peek" at the value contained inside a {@link Functor} via
 * {@link Functor#fmap(Function)}, applying the {@link Consumer} to the contained value, if there is one.
 *
 * @param <A>  the functor parameter type
 * @param <FA> the functor type
 */
public final class Peek<A, FA extends Functor<A, ?>> implements Fn2<Consumer<? super A>, FA, FA> {
    private static final Peek INSTANCE = new Peek<>();

    private Peek() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public FA apply(Consumer<? super A> consumer, FA fa) {
        return (FA) fa.fmap(a -> {
            consumer.accept(a);
            return a;
        });
    }

    @SuppressWarnings("unchecked")
    public static <A, FA extends Functor<A, ?>> Peek<A, FA> peek() {
        return INSTANCE;
    }

    public static <A, FA extends Functor<A, ?>> Fn1<FA, FA> peek(Consumer<? super A> consumer) {
        return Peek.<A, FA>peek().apply(consumer);
    }

    public static <A, FA extends Functor<A, ?>> FA peek(Consumer<? super A> consumer, FA fa) {
        return Peek.<A, FA>peek(consumer).apply(fa);
    }
}
