package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link Maybe}.
 */
public final class MaybeLens {

    private MaybeLens() {
    }

    /**
     * Given a lens and a default <code>S</code>, lift <code>S</code> into {@link Maybe}.
     *
     * @param lens     the lens
     * @param defaultS the S to use if {@link Maybe#nothing()} is given
     * @param <S>      the type of the "larger" value for reading
     * @param <T>      the type of the "larger" value for putting
     * @param <A>      the type of the "smaller" value that is read
     * @param <B>      the type of the "smaller" update value
     * @return the lens with S lifted
     */
    public static <S, T, A, B> Lens<Maybe<S>, T, A, B> liftS(Lens<S, T, A, B> lens, S defaultS) {
        return lens.mapS(m -> m.orElse(defaultS));
    }

    /**
     * Given a lens, lift <code>T</code> into {@link Maybe}.
     *
     * @param lens the lens
     * @param <S>  the type of the "larger" value for reading
     * @param <T>  the type of the "larger" value for putting
     * @param <A>  the type of the "smaller" value that is read
     * @param <B>  the type of the "smaller" update value
     * @return the lens with T lifted
     */
    public static <S, T, A, B> Lens<S, Maybe<T>, A, B> liftT(Lens<S, T, A, B> lens) {
        return lens.mapT(Maybe::just);
    }

    /**
     * Given a lens, lift <code>A</code> into {@link Maybe}.
     *
     * @param lens the lens
     * @param <S>  the type of the "larger" value for reading
     * @param <T>  the type of the "larger" value for putting
     * @param <A>  the type of the "smaller" value that is read
     * @param <B>  the type of the "smaller" update value
     * @return the lens with A lifted
     */
    public static <S, T, A, B> Lens<S, T, Maybe<A>, B> liftA(Lens<S, T, A, B> lens) {
        return lens.mapA(Maybe::just);
    }

    /**
     * Given a lens and a default <code>B</code>, lift <code>B</code> into {@link Maybe}.
     *
     * @param lens     the lens
     * @param defaultB the B to use if {@link Maybe#nothing()} is given
     * @param <S>      the type of the "larger" value for reading
     * @param <T>      the type of the "larger" value for putting
     * @param <A>      the type of the "smaller" value that is read
     * @param <B>      the type of the "smaller" update value
     * @return the lens with B lifted
     */
    public static <S, T, A, B> Lens<S, T, A, Maybe<B>> liftB(Lens<S, T, A, B> lens, B defaultB) {
        return lens.mapB(m -> m.orElse(defaultB));
    }

    /**
     * Given a lens with <code>S</code> lifted into {@link Maybe}, flatten <code>S</code> back down.
     *
     * @param lens the lens
     * @param <S>  the type of the "larger" value for reading
     * @param <T>  the type of the "larger" value for putting
     * @param <A>  the type of the "smaller" value that is read
     * @param <B>  the type of the "smaller" update value
     * @return the lens with S flattened
     */
    public static <S, T, A, B> Lens<S, T, A, B> unLiftS(Lens<Maybe<S>, T, A, B> lens) {
        return lens.mapS(Maybe::just);
    }

    /**
     * Given a lens with <code>T</code> lifted into {@link Maybe} and a default <code>T</code>, flatten <code>T</code>
     * back down.
     *
     * @param lens     the lens
     * @param defaultT the T to use if lens produces {@link Maybe#nothing()}
     * @param <S>      the type of the "larger" value for reading
     * @param <T>      the type of the "larger" value for putting
     * @param <A>      the type of the "smaller" value that is read
     * @param <B>      the type of the "smaller" update value
     * @return the lens with T flattened
     */
    public static <S, T, A, B> Lens<S, T, A, B> unLiftT(Lens<S, Maybe<T>, A, B> lens, T defaultT) {
        return lens.mapT(m -> m.orElse(defaultT));
    }

    /**
     * Given a lens with <code>A</code> lifted into {@link Maybe} and a default <code>A</code>, flatten <code>A</code>
     * back
     * down.
     *
     * @param lens     the lens
     * @param defaultA the A to use if lens produces {@link Maybe#nothing()}
     * @param <S>      the type of the "larger" value for reading
     * @param <T>      the type of the "larger" value for putting
     * @param <A>      the type of the "smaller" value that is read
     * @param <B>      the type of the "smaller" update value
     * @return the lens with A flattened
     */
    public static <S, T, A, B> Lens<S, T, A, B> unLiftA(Lens<S, T, Maybe<A>, B> lens, A defaultA) {
        return lens.mapA(m -> m.orElse(defaultA));
    }

    /**
     * Given a lens with <code>B</code> lifted into {@link Maybe}, flatten <code>B</code> back down.
     *
     * @param lens the lens
     * @param <S>  the type of the "larger" value for reading
     * @param <T>  the type of the "larger" value for putting
     * @param <A>  the type of the "smaller" value that is read
     * @param <B>  the type of the "smaller" update value
     * @return the lens with B flattened
     */
    public static <S, T, A, B> Lens<S, T, A, B> unLiftB(Lens<S, T, A, Maybe<B>> lens) {
        return lens.mapB(Maybe::just);
    }

    /**
     * Convenience static factory method for creating a lens that focuses on a value as a {@link Maybe}.
     *
     * @param <V> the value type
     * @return a lens that focuses on the value as a {@link Maybe}
     */
    public static <V> Lens.Simple<V, Maybe<V>> asMaybe() {
        return simpleLens(Maybe::maybe, (v, maybeV) -> maybeV.orElse(v));
    }
}
