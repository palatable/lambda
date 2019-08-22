package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.functor.builtin.Exchange;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.optics.functions.Over;
import com.jnape.palatable.lambda.optics.functions.Set;
import com.jnape.palatable.lambda.optics.functions.View;

import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.optics.Iso.Simple.adapt;
import static com.jnape.palatable.lambda.optics.Lens.lens;
import static com.jnape.palatable.lambda.optics.Optic.optic;
import static com.jnape.palatable.lambda.optics.functions.View.view;

/**
 * An {@link Iso} (short for "isomorphism") is an invertible {@link Lens}: an {@link Optic} encoding of a
 * bi-directional focusing of two types, and like <code>{@link Lens}es</code>, can be <code>{@link View}ed</code>,
 * {@link Set}, and <code>{@link Over updated}</code>.
 * <p>
 * As an example, consider the isomorphism between valid {@link String}s and {@link Integer}s:
 * <pre>
 * {@code
 * Iso<String, String, Integer, Integer> stringIntIso = Iso.iso(Integer::parseInt, Object::toString);
 * Integer asInt = view(stringIntIso, "123"); // 123
 * String asString = view(stringIntIso.mirror(), 123); // "123"
 * }
 * </pre>
 * In the previous example, <code>stringIntIso</code> can be viewed as an
 * <code>{@link Optic}&lt;String, String, Integer, Integer&gt;</code>, and can be <code>{@link Iso#mirror}ed</code> and
 * viewed as a <code>{@link Optic}&lt;Integer, Integer, String, String&gt;</code>.
 * <p>
 * As with {@link Lens}, variance is supported between <code>S/T</code> and <code>A/B</code>, and where these pairs do
 * not vary, a {@link Simple} iso can be used (for instance, in the previous example, <code>stringIntIso</code> could
 * have had the simplified <code>Iso.Simple&lt;String, Integer&gt;</code> type).
 * <p>
 * For more information, read about
 * <a href="https://hackage.haskell.org/package/lens-4.16.1/docs/Control-Lens-Iso.html">isos</a>.
 *
 * @param <S> the larger type for focusing
 * @param <T> the larger type for mirrored focusing
 * @param <A> the smaller type for focusing
 * @param <B> the smaller type for mirrored focusing
 * @see Optic
 * @see Lens
 */
@FunctionalInterface
public interface Iso<S, T, A, B> extends
        Optic<Profunctor<?, ?, ?>, Functor<?, ?>, S, T, A, B>,
        Monad<T, Iso<S, ?, A, B>>,
        Profunctor<S, T, Iso<?, ?, A, B>> {

    /**
     * Convert this {@link Iso} into a {@link Lens}.
     *
     * @return the equivalent lens
     */
    default Lens<S, T, A, B> toLens() {
        return lens(this);
    }

    /**
     * Flip this {@link Iso} around.
     *
     * @return the mirrored {@link Iso}
     */
    default Iso<B, A, T, S> mirror() {
        return unIso().into((sa, bt) -> iso(bt, sa));
    }

    /**
     * Destructure this {@link Iso} into the two functions <code>S -&lt; A</code> and <code>B -&lt; T</code> that
     * constitute the isomorphism.
     *
     * @return the destructured iso
     */
    default Tuple2<Fn1<? super S, ? extends A>, Fn1<? super B, ? extends T>> unIso() {
        return Tuple2.fill(this.<Exchange<A, B, ?, ?>, Identity<?>,
                Identity<B>,
                Identity<T>,
                Exchange<A, B, A, Identity<B>>,
                Exchange<A, B, S, Identity<T>>>apply(new Exchange<>(id(), Identity::new)).diMapR(Identity::runIdentity))
                .biMap(e -> fn1(e.sa()), e -> fn1(e.bt()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Iso<S, U, A, B> fmap(Fn1<? super T, ? extends U> fn) {
        return Monad.super.<U>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Iso<S, U, A, B> pure(U u) {
        return iso(view(this), constantly(u));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Iso<S, U, A, B> zip(Applicative<Fn1<? super T, ? extends U>, Iso<S, ?, A, B>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Iso<S, U, A, B> discardL(Applicative<U, Iso<S, ?, A, B>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Iso<S, T, A, B> discardR(Applicative<U, Iso<S, ?, A, B>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("RedundantTypeArguments")
    default <U> Iso<S, U, A, B> flatMap(Fn1<? super T, ? extends Monad<U, Iso<S, ?, A, B>>> fn) {
        return unIso().<Fn2<B, B, U>>fmap(bt -> Fn2.<B, B, U>curried(
                fn1(bt.fmap(fn.<Iso<S, U, A, B>>fmap(Monad<U, Iso<S, ?, A, B>>::coerce))
                            .fmap(Iso::unIso)
                            .fmap(Tuple2::_2)
                            .fmap(Fn1::fn1))))
                .fmap(Fn2::uncurry)
                .fmap(bbu -> bbu.<B>diMapL(Tuple2::fill))
                .into(Iso::iso);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R> Iso<R, T, A, B> diMapL(Fn1<? super R, ? extends S> fn) {
        return (Iso<R, T, A, B>) Profunctor.super.<R>diMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Iso<S, U, A, B> diMapR(Fn1<? super T, ? extends U> fn) {
        return (Iso<S, U, A, B>) Profunctor.super.<U>diMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R, U> Iso<R, U, A, B> diMap(Fn1<? super R, ? extends S> lFn,
                                         Fn1<? super T, ? extends U> rFn) {
        return this.<R>mapS(lFn).mapT(rFn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R> Iso<R, T, A, B> contraMap(Fn1<? super R, ? extends S> fn) {
        return (Iso<R, T, A, B>) Profunctor.super.<R>contraMap(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R> Iso<R, T, A, B> mapS(Fn1<? super R, ? extends S> fn) {
        return iso(Optic.super.mapS(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Iso<S, U, A, B> mapT(Fn1<? super T, ? extends U> fn) {
        return iso(Optic.super.mapT(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Iso<S, T, C, B> mapA(Fn1<? super A, ? extends C> fn) {
        return iso(Optic.super.mapA(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Iso<S, T, A, Z> mapB(Fn1<? super Z, ? extends B> fn) {
        return iso(Optic.super.mapB(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z, C> Iso<S, T, Z, C> andThen(Optic<? super Profunctor<?, ?, ?>, ? super Functor<?, ?>, A, B, Z, C> f) {
        return iso(Optic.super.andThen(f));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R, U> Iso<R, U, A, B> compose(Optic<? super Profunctor<?, ?, ?>, ? super Functor<?, ?>, R, U, S, T> g) {
        return iso(Optic.super.compose(g));
    }

    /**
     * Static factory method for creating an iso from a function and it's inverse.
     *
     * @param f   the function
     * @param g   f's inverse
     * @param <S> the larger type for focusing
     * @param <T> the larger type for mirrored focusing
     * @param <A> the smaller type for focusing
     * @param <B> the smaller type for mirrored focusing
     * @return the iso
     */
    static <S, T, A, B> Iso<S, T, A, B> iso(Fn1<? super S, ? extends A> f, Fn1<? super B, ? extends T> g) {
        return iso(optic(pafb -> pafb.diMap(f, fb -> fb.fmap(g))));
    }

    /**
     * Promote an optic with compatible bounds to an {@link Iso}.
     *
     * @param optic the {@link Optic}
     * @param <S>   the larger type for focusing
     * @param <T>   the larger type for mirrored focusing
     * @param <A>   the smaller type for focusing
     * @param <B>   the smaller type for mirrored focusing
     * @return the {@link Iso}
     */
    static <S, T, A, B> Iso<S, T, A, B> iso(
            Optic<? super Profunctor<?, ?, ?>, ? super Functor<?, ?>, S, T, A, B> optic) {
        return new Iso<S, T, A, B>() {
            @Override
            public <CoP extends Profunctor<?, ?, ? extends Profunctor<?, ?, ?>>,
                    CoF extends Functor<?, ? extends Functor<?, ?>>, FB extends Functor<B, ? extends CoF>,
                    FT extends Functor<T, ? extends CoF>, PAFB extends Profunctor<A, FB, ? extends CoP>,
                    PSFT extends Profunctor<S, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
                return optic.apply(pafb);
            }
        };
    }

    /**
     * Static factory method for creating a simple {@link Iso} from a function and its inverse.
     *
     * @param f   a function
     * @param g   f's inverse
     * @param <S> one side of the isomorphism
     * @param <A> the other side of the isomorphism
     * @return the simple iso
     */
    static <S, A> Iso.Simple<S, A> simpleIso(Fn1<? super S, ? extends A> f, Fn1<? super A, ? extends S> g) {
        return adapt(iso(f, g));
    }

    /**
     * The canonical {@link Pure} instance for {@link Iso}.
     *
     * @param sa  one side of the isomorphism
     * @param <S> the larger type for focusing
     * @param <A> the smaller type for focusing
     * @param <B> the smaller type for mirrored focusing
     * @return the {@link Pure} instance
     */
    static <S, A, B> Pure<Iso<S, ?, A, B>> pureIso(Fn1<? super S, ? extends A> sa) {
        return new Pure<Iso<S, ?, A, B>>() {
            @Override
            public <T> Iso<S, T, A, B> checkedApply(T t) {
                return iso(sa, constantly(t));
            }
        };
    }

    /**
     * A convenience type with a simplified type signature for common isos with both unified "larger" values and
     * unified "smaller" values.
     *
     * @param <S> the type of both "larger" values
     * @param <A> the type of both "smaller" values
     */
    @FunctionalInterface
    interface Simple<S, A> extends Iso<S, S, A, A>, Optic.Simple<Profunctor<?, ?, ?>, Functor<?, ?>, S, A> {

        /**
         * Compose two simple isos from right to left.
         *
         * @param g   the other simple iso
         * @param <R> the other simple iso' larger type
         * @return the composed simple iso
         */
        @SuppressWarnings("overloads")
        default <R> Iso.Simple<R, A> compose(Iso.Simple<R, S> g) {
            return Iso.Simple.adapt(Iso.super.compose(g));
        }

        /**
         * Compose two simple isos from left to right.
         *
         * @param f   the other simple iso
         * @param <B> the other simple iso' smaller type
         * @return the composed simple iso
         */
        @SuppressWarnings("overloads")
        default <B> Iso.Simple<S, B> andThen(Iso.Simple<A, B> f) {
            return Iso.Simple.adapt(f.compose(this));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        default Iso.Simple<A, S> mirror() {
            return Iso.Simple.adapt(Iso.super.mirror());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        default Lens.Simple<S, A> toLens() {
            return Lens.Simple.adapt(Iso.super.toLens());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        default <U> Iso.Simple<S, A> discardR(Applicative<U, Iso<S, ?, A, A>> appB) {
            return Iso.Simple.adapt(Iso.super.discardR(appB));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        default <B> Iso.Simple<S, B> andThen(Optic.Simple<? super Profunctor<?, ?, ?>, ? super Functor<?, ?>, A, B> f) {
            return Iso.Simple.adapt(Iso.super.andThen(f));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        default <R> Iso.Simple<R, A> compose(Optic.Simple<? super Profunctor<?, ?, ?>, ? super Functor<?, ?>, R, S> g) {
            return Iso.Simple.adapt(Iso.super.compose(g));
        }

        /**
         * Adapt an {@link Optic} with the right variance to an {@link Iso.Simple}.
         *
         * @param optic the optic
         * @param <S>   S/T
         * @param <A>   A/B
         * @return the simple iso
         */
        static <S, A> Iso.Simple<S, A> adapt(
                Optic<? super Profunctor<?, ?, ?>, ? super Functor<?, ?>, S, S, A, A> optic) {
            return new Iso.Simple<S, A>() {
                @Override
                public <CoP extends Profunctor<?, ?, ? extends Profunctor<?, ?, ?>>,
                        CoF extends Functor<?, ? extends Functor<?, ?>>, FB extends Functor<A, ? extends CoF>,
                        FT extends Functor<S, ? extends CoF>, PAFB extends Profunctor<A, FB, ? extends CoP>,
                        PSFT extends Profunctor<S, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
                    return optic.apply(pafb);
                }
            };
        }
    }
}