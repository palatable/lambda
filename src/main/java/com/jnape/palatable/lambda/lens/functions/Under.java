package com.jnape.palatable.lambda.lens.functions;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.lens.Iso;
import com.jnape.palatable.lambda.lens.LensLike;

import java.util.function.Function;

/**
 * The inverse of {@link Over}: given an {@link Iso}, a function from <code>T</code> to <code>S</code>, and a "smaller"
 * value <code>B</code>, return a "smaller" value <code>A</code> by traversing around the type ring (<code>B -&gt; T
 * -&gt; S -&gt; A</code>).
 * <p>
 * Note this is only possible for {@link Iso}s and not general {@link LensLike}s because of the mandatory need for the
 * correspondence <code>B -&gt; T</code>.
 *
 * @param <S> the larger type for focusing
 * @param <T> the larger type for mirrored focusing
 * @param <A> the smaller type for focusing
 * @param <B> the smaller type for mirrored focusing
 */
public final class Under<S, T, A, B> implements Fn3<Iso<S, T, A, B>, Function<? super T, ? extends S>, B, A> {

    private static final Under<?, ?, ?, ?> INSTANCE = new Under<>();

    private Under() {
    }

    @Override
    public A apply(Iso<S, T, A, B> iso, Function<? super T, ? extends S> fn, B b) {
        return iso.unIso().into((sa, bt) -> bt.fmap(fn).fmap(sa)).apply(b);
    }

    @SuppressWarnings("unchecked")
    public static <S, T, A, B> Under<S, T, A, B> under() {
        return (Under<S, T, A, B>) INSTANCE;
    }

    public static <S, T, A, B> Fn2<Function<? super T, ? extends S>, B, A> under(Iso<S, T, A, B> iso) {
        return Under.<S, T, A, B>under().apply(iso);
    }

    public static <S, T, A, B> Fn1<B, A> under(Iso<S, T, A, B> iso, Function<? super T, ? extends S> fn) {
        return under(iso).apply(fn);
    }

    public static <S, T, A, B> A under(Iso<S, T, A, B> iso, Function<? super T, ? extends S> fn, B b) {
        return under(iso, fn).apply(b);
    }
}
