package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Effect;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.io.IO;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * Given an <code>{@link Effect}&lt;A&gt;</code> and some <code>A</code>, produce an {@link IO} that, when run, performs
 * the effect on <code>A</code> and returns it.
 *
 * @param <A> the input and output
 */
public final class Alter<A> implements Fn2<Effect<? super A>, A, IO<A>> {

    private static final Alter INSTANCE = new Alter();

    private Alter() {
    }

    @Override
    public IO<A> apply(Effect<? super A> effect, A a) {
        return effect.fmap(io -> io.fmap(constantly(a))).apply(a);
    }

    @SuppressWarnings("unchecked")
    public static <A> Alter<A> alter() {
        return INSTANCE;
    }

    public static <A> Fn1<A, IO<A>> alter(Effect<? super A> effect) {
        return Alter.<A>alter().apply(effect);
    }

    public static <A> IO<A> alter(Effect<? super A> effect, A a) {
        return Alter.<A>alter(effect).apply(a);
    }
}
