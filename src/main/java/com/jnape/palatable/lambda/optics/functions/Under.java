package com.jnape.palatable.lambda.optics.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functor.builtin.Exchange;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.optics.Iso;
import com.jnape.palatable.lambda.optics.Optic;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * The inverse of {@link Over}: given an {@link Iso}, a function from <code>T</code> to <code>S</code>, and a "smaller"
 * value <code>B</code>, return a "smaller" value <code>A</code> by traversing around the type ring (<code>B -&gt; T
 * -&gt; S -&gt; A</code>).
 *
 * @param <S> the larger type for focusing
 * @param <T> the larger type for mirrored focusing
 * @param <A> the smaller type for focusing
 * @param <B> the smaller type for mirrored focusing
 */
public final class Under<S, T, A, B> implements
        Fn3<Optic<? super Exchange<A, B, ?, ?>, ? super Identity<?>, S, T, A, B>,
                Fn1<? super T, ? extends S>, B, A> {

    private static final Under<?, ?, ?, ?> INSTANCE = new Under<>();

    private Under() {
    }

    @Override
    public A checkedApply(Optic<? super Exchange<A, B, ?, ?>, ? super Identity<?>, S, T, A, B> optic,
                          Fn1<? super T, ? extends S> fn,
                          B b) {
        Exchange<A, B, S, Identity<T>> exchange = optic.apply(new Exchange<>(id(), Identity::new));
        return exchange.sa().apply(fn.apply(exchange.bt().apply(b).runIdentity()));
    }

    @SuppressWarnings("unchecked")
    public static <S, T, A, B> Under<S, T, A, B> under() {
        return (Under<S, T, A, B>) INSTANCE;
    }

    public static <S, T, A, B> Fn2<Fn1<? super T, ? extends S>, B, A> under(
            Optic<? super Exchange<A, B, ?, ?>, ? super Identity<?>, S, T, A, B> optic) {
        return Under.<S, T, A, B>under().apply(optic);
    }

    public static <S, T, A, B> Fn1<B, A> under(
            Optic<? super Exchange<A, B, ?, ?>, ? super Identity<?>, S, T, A, B> optic,
            Fn1<? super T, ? extends S> fn) {
        return under(optic).apply(fn);
    }

    public static <S, T, A, B> A under(Optic<? super Exchange<A, B, ?, ?>, ? super Identity<?>, S, T, A, B> optic,
                                       Fn1<? super T, ? extends S> fn, B b) {
        return under(optic, fn).apply(b);
    }
}
