package com.jnape.palatable.lambda.lens;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.functor.builtin.Exchange;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.lens.functions.Over;
import com.jnape.palatable.lambda.lens.functions.Set;
import com.jnape.palatable.lambda.lens.functions.View;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.lens.Iso.Simple.adapt;
import static com.jnape.palatable.lambda.lens.functions.View.view;

/**
 * An {@link Iso} (short for "isomorphism") is an invertible {@link Lens}: a {@link LensLike} encoding of a
 * bi-directional focusing of two types, and like <code>{@link Lens}es</code>, can be <code>{@link View}ed</code>,
 * {@link Set}, and <code>{@link Over}ed</code>.
 * <p>
 * As an example, consider the isomorphism between valid {@link String}s and {@link Integer}s:
 * <pre>
 * {@code
 * Iso<String, String, Integer, Integer> stringIntIso = Iso.iso(Integer::parseInt, Object::toString);
 * Integer asInt = view(stringIntIso, "123"); // 123
 * String asString = view(stringIntIso.mirror(), 123); // "123"
 * }
 * </pre>
 * In the previous example, <code>stringIntIso</code> can be viewed as a <code>{@link Lens}&lt;String, String, Integer,
 * Integer&gt;</code>, and can be <code>{@link Iso#mirror}ed</code> and viewed as a <code>{@link Lens}&lt;Integer,
 * Integer, String, String&gt;</code>.
 * <p>
 * As with {@link Lens}, variance is supported between <code>S/T</code> and <code>A/B</code>, and where these pairs do
 * not vary, a {@link Simple} iso can be used (for instance, in the previous example, <code>stringIntIso</code> could
 * have had the simplified <code>Iso.Simple&lt;String, Integer&gt;</code> type).
 * <p>
 * For more information, read about <a href="https://hackage.haskell.org/package/lens-4.16.1/docs/Control-Lens-Iso.html">isos</a>.
 *
 * @param <S> the larger type for focusing
 * @param <T> the larger type for mirrored focusing
 * @param <A> the smaller type for focusing
 * @param <B> the smaller type for mirrored focusing
 */
@FunctionalInterface
public interface Iso<S, T, A, B> extends LensLike<S, T, A, B, Iso> {

    <P extends Profunctor, F extends Functor, FB extends Functor<B, F>, FT extends Functor<T, F>,
            PAFB extends Profunctor<A, FB, P>,
            PSFT extends Profunctor<S, FT, P>> PSFT apply(PAFB pafb);

    @Override
    default <F extends Functor, FT extends Functor<T, F>, FB extends Functor<B, F>> FT apply(
            Function<? super A, ? extends FB> fn, S s) {
        return this.<Fn1, F, FB, FT, Fn1<A, FB>, Fn1<S, FT>>apply(fn1(fn)).apply(s);
    }

    /**
     * Convert this {@link Iso} into a {@link Lens}.
     *
     * @return the equivalent lens
     */
    default Lens<S, T, A, B> toLens() {
        return new Lens<S, T, A, B>() {
            @Override
            public <F extends Functor, FT extends Functor<T, F>, FB extends Functor<B, F>> FT apply(
                    Function<? super A, ? extends FB> fn, S s) {
                return Iso.this.apply(fn1(fn), s);
            }
        };
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
    default Tuple2<? extends Function<? super S, ? extends A>, ? extends Function<? super B, ? extends T>> unIso() {
        return Tuple2.fill(this.<Exchange<A, B, ?, ?>, Identity, Identity<B>, Identity<T>,
                Exchange<A, B, A, Identity<B>>,
                Exchange<A, B, S, Identity<T>>>apply(new Exchange<>(id(), Identity::new)).diMapR(Identity::runIdentity))
                .biMap(Exchange::sa, Exchange::bt);
    }

    @Override
    default <U> Iso<S, U, A, B> fmap(Function<? super T, ? extends U> fn) {
        return LensLike.super.<U>fmap(fn).coerce();
    }

    @Override
    default <U> Iso<S, U, A, B> pure(U u) {
        return iso(view(this), constantly(u));
    }

    @Override
    default <U> Iso<S, U, A, B> zip(Applicative<Function<? super T, ? extends U>, LensLike<S, ?, A, B, Iso>> appFn) {
        return LensLike.super.zip(appFn).coerce();
    }

    @Override
    default <U> Iso<S, U, A, B> discardL(Applicative<U, LensLike<S, ?, A, B, Iso>> appB) {
        return LensLike.super.discardL(appB).coerce();
    }

    @Override
    default <U> Iso<S, T, A, B> discardR(Applicative<U, LensLike<S, ?, A, B, Iso>> appB) {
        return LensLike.super.discardR(appB).coerce();
    }

    @Override
    default <U> Iso<S, U, A, B> flatMap(Function<? super T, ? extends Monad<U, LensLike<S, ?, A, B, Iso>>> fn) {
        return unIso().fmap(bt -> Fn2.<B, B, U>fn2(fn1(bt.andThen(fn.<Iso<S, U, A, B>>andThen(Applicative::coerce))
                                                               .andThen(Iso::unIso)
                                                               .andThen(Tuple2::_2)
                                                               .andThen(Fn1::fn1))))
                .fmap(Fn2::uncurry)
                .fmap(bbu -> bbu.<B>diMapL(Tuple2::fill))
                .into(Iso::iso);
    }

    @Override
    default <R> Iso<R, T, A, B> diMapL(Function<? super R, ? extends S> fn) {
        return LensLike.super.<R>diMapL(fn).coerce();
    }

    @Override
    default <U> Iso<S, U, A, B> diMapR(Function<? super T, ? extends U> fn) {
        return LensLike.super.<U>diMapR(fn).coerce();
    }

    @Override
    default <R, U> Iso<R, U, A, B> diMap(Function<? super R, ? extends S> lFn,
                                         Function<? super T, ? extends U> rFn) {
        return LensLike.super.<R, U>diMap(lFn, rFn).coerce();
    }

    @Override
    default <R> Iso<R, T, A, B> contraMap(Function<? super R, ? extends S> fn) {
        return LensLike.super.<R>contraMap(fn).coerce();
    }

    @Override
    default <R> Iso<R, T, A, B> mapS(Function<? super R, ? extends S> fn) {
        return unIso().biMapL(f -> f.compose(fn)).into(Iso::iso);
    }

    @Override
    default <U> Iso<S, U, A, B> mapT(Function<? super T, ? extends U> fn) {
        return unIso().biMapR(f -> f.andThen(fn)).into(Iso::iso);
    }

    @Override
    default <C> Iso<S, T, C, B> mapA(Function<? super A, ? extends C> fn) {
        return unIso().biMapL(f -> f.andThen(fn)).into(Iso::iso);
    }

    @Override
    default <Z> Iso<S, T, A, Z> mapB(Function<? super Z, ? extends B> fn) {
        return unIso().biMapR(f -> f.compose(fn)).into(Iso::iso);
    }

    /**
     * Left-to-right composition of {@link Iso}.
     *
     * @param f   the iso to apply after this one
     * @param <C> the smaller type the first larger type can be viewed as
     * @param <D> the smaller type that can be viewed as the second larger type
     * @return the composed {@link Iso}
     */
    default <C, D> Iso<S, T, C, D> andThen(Iso<A, B, C, D> f) {
        return unIso().into((sa, bt) -> f.unIso().into((ac, db) -> iso(sa.andThen(ac), db.andThen(bt))));
    }

    /**
     * Right-to-left composition of {@link Iso}.
     *
     * @param g   the iso to apply before this one
     * @param <Q> the larger type that can be viewed as the first smaller type
     * @param <R> the larger type the second smaller type can be viewed as
     * @return the composed {@link Iso}
     */
    default <Q, R> Iso<Q, R, A, B> compose(Iso<Q, R, S, T> g) {
        return g.andThen(this);
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
    static <S, T, A, B> Iso<S, T, A, B> iso(Function<? super S, ? extends A> f,
                                            Function<? super B, ? extends T> g) {
        return new Iso<S, T, A, B>() {
            @Override
            @SuppressWarnings("unchecked")
            public <P extends Profunctor, F extends Functor, FB extends Functor<B, F>, FT extends Functor<T, F>, PAFB extends Profunctor<A, FB, P>, PSFT extends Profunctor<S, FT, P>> PSFT apply(
                    PAFB pafb) {
                return (PSFT) pafb.<S, FT>diMap(f, fb -> (FT) fb.<T>fmap(g));
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
    static <S, A> Iso.Simple<S, A> simpleIso(Function<? super S, ? extends A> f, Function<? super A, ? extends S> g) {
        return adapt(iso(f, g));
    }

    /**
     * A convenience type with a simplified type signature for common isos with both unified "larger" values and
     * unified "smaller" values.
     *
     * @param <S> the type of both "larger" values
     * @param <A> the type of both "smaller" values
     */
    interface Simple<S, A> extends Iso<S, S, A, A>, LensLike.Simple<S, A, Iso> {

        @Override
        default Iso.Simple<A, S> mirror() {
            return Iso.Simple.adapt(Iso.super.mirror());
        }

        @Override
        default Lens.Simple<S, A> toLens() {
            return Lens.Simple.adapt(Iso.super.toLens());
        }

        /**
         * Compose two simple isos from right to left.
         *
         * @param g   the other simple iso
         * @param <R> the other simple iso' larger type
         * @return the composed simple iso
         */
        default <R> Iso.Simple<R, A> compose(Iso.Simple<R, S> g) {
            return g.andThen(this);
        }

        /**
         * Compose two simple isos from left to right.
         *
         * @param f   the other simple iso
         * @param <B> the other simple iso' smaller type
         * @return the composed simple iso
         */
        default <B> Iso.Simple<S, B> andThen(Iso.Simple<A, B> f) {
            return Iso.Simple.adapt(f.compose(this));
        }

        /**
         * Adapt an {@link Iso} with the right variance to an {@link Iso.Simple}.
         *
         * @param iso the iso
         * @param <S> S/T
         * @param <A> A/B
         * @return the simple iso
         */
        @SuppressWarnings("unchecked")
        static <S, A> Iso.Simple<S, A> adapt(Iso<S, S, A, A> iso) {
            return iso::apply;
        }
    }
}