package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.Optional;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link Optional}s.
 */
public final class OptionalLens {

    private OptionalLens() {
    }

    /**
     * Convenience static factory method for creating a lens that focuses on a value as an {@link Optional}.
     *
     * @param <V> the value type
     * @return a lens that focuses on the value as an Optional
     */
    public static <V> Lens.Simple<V, Optional<V>> asOptional() {
        return simpleLens(Optional::ofNullable, (v, optV) -> optV.orElse(v));
    }

    /**
     * Given a lens and a default <code>S</code>, lift <code>S</code> into Optional.
     *
     * @param lens     the lens
     * @param defaultS the S to use if an empty Optional value is given
     * @param <S>      the type of the "larger" value for reading
     * @param <T>      the type of the "larger" value for putting
     * @param <A>      the type of the "smaller" value that is read
     * @param <B>      the type of the "smaller" update value
     * @return the lens with S lifted
     */
    public static <S, T, A, B> Lens<Optional<S>, T, A, B> liftS(Lens<S, T, A, B> lens, S defaultS) {
        return lens.mapS(optS -> optS.orElse(defaultS));
    }

    /**
     * Given a lens, lift <code>T</code> into Optional.
     *
     * @param lens the lens
     * @param <S>  the type of the "larger" value for reading
     * @param <T>  the type of the "larger" value for putting
     * @param <A>  the type of the "smaller" value that is read
     * @param <B>  the type of the "smaller" update value
     * @return the lens with T lifted
     */
    public static <S, T, A, B> Lens<S, Optional<T>, A, B> liftT(Lens<S, T, A, B> lens) {
        return lens.mapT(Optional::ofNullable);
    }

    /**
     * Given a lens, lift <code>A</code> into Optional.
     *
     * @param lens the lens
     * @param <S>  the type of the "larger" value for reading
     * @param <T>  the type of the "larger" value for putting
     * @param <A>  the type of the "smaller" value that is read
     * @param <B>  the type of the "smaller" update value
     * @return the lens with A lifted
     */
    public static <S, T, A, B> Lens<S, T, Optional<A>, B> liftA(Lens<S, T, A, B> lens) {
        return lens.mapA(Optional::ofNullable);
    }

    /**
     * Given a lens and a default <code>B</code>, lift <code>B</code> into Optional.
     *
     * @param lens     the lens
     * @param defaultB the B to use if an empty Optional value is given
     * @param <S>      the type of the "larger" value for reading
     * @param <T>      the type of the "larger" value for putting
     * @param <A>      the type of the "smaller" value that is read
     * @param <B>      the type of the "smaller" update value
     * @return the lens with B lifted
     */
    public static <S, T, A, B> Lens<S, T, A, Optional<B>> liftB(Lens<S, T, A, B> lens, B defaultB) {
        return lens.mapB(optB -> optB.orElse(defaultB));
    }

    /**
     * Given a lens with <code>S</code> lifted into Optional, flatten <code>S</code> back down.
     *
     * @param lens the lens
     * @param <S>  the type of the "larger" value for reading
     * @param <T>  the type of the "larger" value for putting
     * @param <A>  the type of the "smaller" value that is read
     * @param <B>  the type of the "smaller" update value
     * @return the lens with S flattened
     */
    public static <S, T, A, B> Lens<S, T, A, B> unLiftS(Lens<Optional<S>, T, A, B> lens) {
        return lens.mapS(Optional::ofNullable);
    }

    /**
     * Given a lens with <code>T</code> lifted into Optional and a default <code>T</code>, flatten <code>T</code> back
     * down.
     *
     * @param lens     the lens
     * @param defaultT the T to use if lens produces an empty Optional
     * @param <S>      the type of the "larger" value for reading
     * @param <T>      the type of the "larger" value for putting
     * @param <A>      the type of the "smaller" value that is read
     * @param <B>      the type of the "smaller" update value
     * @return the lens with T flattened
     */
    public static <S, T, A, B> Lens<S, T, A, B> unLiftT(Lens<S, Optional<T>, A, B> lens, T defaultT) {
        return lens.mapT(optT -> optT.orElse(defaultT));
    }

    /**
     * Given a lens with <code>A</code> lifted into Optional and a default <code>A</code>, flatten <code>A</code> back
     * down.
     *
     * @param lens     the lens
     * @param defaultA the A to use if lens produces an empty Optional
     * @param <S>      the type of the "larger" value for reading
     * @param <T>      the type of the "larger" value for putting
     * @param <A>      the type of the "smaller" value that is read
     * @param <B>      the type of the "smaller" update value
     * @return the lens with A flattened
     */
    public static <S, T, A, B> Lens<S, T, A, B> unLiftA(Lens<S, T, Optional<A>, B> lens, A defaultA) {
        return lens.mapA(optA -> optA.orElse(defaultA));
    }

    /**
     * Given a lens with <code>B</code> lifted, flatten <code>B</code> back down.
     *
     * @param lens the lens
     * @param <S>  the type of the "larger" value for reading
     * @param <T>  the type of the "larger" value for putting
     * @param <A>  the type of the "smaller" value that is read
     * @param <B>  the type of the "smaller" update value
     * @return the lens with B flattened
     */
    public static <S, T, A, B> Lens<S, T, A, B> unLiftB(Lens<S, T, A, Optional<B>> lens) {
        return lens.mapB(Optional::ofNullable);
    }
}
