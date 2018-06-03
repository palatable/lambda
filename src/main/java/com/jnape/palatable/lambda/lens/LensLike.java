package com.jnape.palatable.lambda.lens;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;

/**
 * The generic supertype of all types that can be treated as lenses but should preserve type-specific return types in
 * overrides. This type only exists to appease Java's unfortunate parametric type hierarchy constraints. If you're here,
 * you're probably looking for {@link Lens} or {@link Iso}.
 *
 * @param <S>  the type of the "larger" value for reading
 * @param <T>  the type of the "larger" value for putting
 * @param <A>  the type of the "smaller" value that is read
 * @param <B>  the type of the "smaller" update value
 * @param <LL> the concrete lens subtype
 * @see Lens
 * @see Iso
 */
public interface LensLike<S, T, A, B, LL extends LensLike> extends Monad<T, LensLike<S, ?, A, B, LL>>, Profunctor<S, T, LensLike<?, ?, A, B, LL>> {

    <F extends Functor, FT extends Functor<T, F>, FB extends Functor<B, F>> FT apply(
            Function<? super A, ? extends FB> fn, S s);

    /**
     * Right-to-left composition of lenses. Requires compatibility between A and B.
     *
     * @param g   the other lens
     * @param <Q> the new "larger" value for reading (previously S)
     * @param <R> the new "larger" value for putting (previously T)
     * @return the composed lens
     */
    <Q, R> LensLike<Q, R, A, B, ?> compose(LensLike<Q, R, S, T, ?> g);

    /**
     * Left-to-right composition of lenses. Requires compatibility between S and T.
     *
     * @param f   the other lens
     * @param <C> the new "smaller" value to read (previously A)
     * @param <D> the new "smaller" update value (previously B)
     * @return the composed lens
     */
    <C, D> LensLike<S, T, C, D, ?> andThen(LensLike<A, B, C, D, ?> f);

    /**
     * Contravariantly map <code>S</code> to <code>R</code>, yielding a new lens.
     *
     * @param fn  the mapping function
     * @param <R> the type of the new "larger" value for reading
     * @return the new lens
     */
    <R> LensLike<R, T, A, B, LL> mapS(Function<? super R, ? extends S> fn);

    /**
     * Covariantly map <code>T</code> to <code>U</code>, yielding a new lens.
     *
     * @param fn  the mapping function
     * @param <U> the type of the new "larger" value for putting
     * @return the new lens
     */
    <U> LensLike<S, U, A, B, LL> mapT(Function<? super T, ? extends U> fn);

    /**
     * Covariantly map <code>A</code> to <code>C</code>, yielding a new lens.
     *
     * @param fn  the mapping function
     * @param <C> the type of the new "smaller" value that is read
     * @return the new lens
     */
    <C> LensLike<S, T, C, B, LL> mapA(Function<? super A, ? extends C> fn);

    /**
     * Contravariantly map <code>B</code> to <code>Z</code>, yielding a new lens.
     *
     * @param fn  the mapping function
     * @param <Z> the type of the new "smaller" update value
     * @return the new lens
     */
    <Z> LensLike<S, T, A, Z, LL> mapB(Function<? super Z, ? extends B> fn);

    @Override
    <U> LensLike<S, U, A, B, LL> flatMap(Function<? super T, ? extends Monad<U, LensLike<S, ?, A, B, LL>>> f);

    @Override
    <U> LensLike<S, U, A, B, LL> pure(U u);

    @Override
    default <U> LensLike<S, U, A, B, LL> fmap(Function<? super T, ? extends U> fn) {
        return Monad.super.<U>fmap(fn).coerce();
    }

    @Override
    default <U> LensLike<S, U, A, B, LL> zip(
            Applicative<Function<? super T, ? extends U>, LensLike<S, ?, A, B, LL>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    default <U> LensLike<S, U, A, B, LL> discardL(Applicative<U, LensLike<S, ?, A, B, LL>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    default <U> LensLike<S, T, A, B, LL> discardR(Applicative<U, LensLike<S, ?, A, B, LL>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    default <R> LensLike<R, T, A, B, LL> diMapL(Function<? super R, ? extends S> fn) {
        return (LensLike<R, T, A, B, LL>) Profunctor.super.<R>diMapL(fn);
    }

    @Override
    default <U> LensLike<S, U, A, B, LL> diMapR(Function<? super T, ? extends U> fn) {
        return (LensLike<S, U, A, B, LL>) Profunctor.super.<U>diMapR(fn);
    }

    @Override
    default <R, U> LensLike<R, U, A, B, LL> diMap(Function<? super R, ? extends S> lFn,
                                                  Function<? super T, ? extends U> rFn) {
        return this.<R>mapS(lFn).mapT(rFn);
    }

    @Override
    default <R> LensLike<R, T, A, B, LL> contraMap(Function<? super R, ? extends S> fn) {
        return (LensLike<R, T, A, B, LL>) Profunctor.super.<R>contraMap(fn);
    }

    /**
     * A simpler type signature for lenses where <code>S/T</code> and <code>A/B</code> are equivalent.
     *
     * @param <S>  the "larger" type
     * @param <A>  the "smaller" type
     * @param <LL> the concrete lens subtype
     */
    interface Simple<S, A, LL extends LensLike> extends LensLike<S, S, A, A, LL> {

        /**
         * Compose two simple lenses from right to left.
         *
         * @param g   the other simple lens
         * @param <R> the other simple lens' larger type
         * @return the composed simple lens
         */
        <R> LensLike.Simple<R, A, ?> compose(LensLike.Simple<R, S, ?> g);

        /**
         * Compose two simple lenses from left to right.
         *
         * @param f   the other simple lens
         * @param <B> the other simple lens' smaller type
         * @return the composed simple lens
         */
        <B> LensLike.Simple<S, B, ?> andThen(LensLike.Simple<A, B, ?> f);
    }
}
