package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Cocartesian;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.functor.builtin.Market;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.optics.functions.Matching;
import com.jnape.palatable.lambda.optics.functions.Pre;
import com.jnape.palatable.lambda.optics.functions.Re;
import com.jnape.palatable.lambda.optics.functions.View;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Downcast.downcast;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;
import static com.jnape.palatable.lambda.optics.Prism.Simple.adapt;
import static com.jnape.palatable.lambda.optics.functions.Matching.matching;
import static com.jnape.palatable.lambda.optics.functions.Re.re;

/**
 * Prisms are {@link Iso Isos} that can fail in one direction. Example:
 * <pre>
 * {@code
 * Prism<String, String, Integer, Integer> parseInt =
 *     prism(str -> Either.trying(() -> Integer.parseInt(str),
 *                                constantly(str)),
 *           Object::toString);
 *
 * String         str   = view(re(parseInt), 123); // "123"
 * Maybe<Integer> works = view(pre(parseInt), "123"); // Just 123
 * Maybe<Integer> fails = view(pre(parseInt), "foo"); // Nothing
 * }
 * </pre>
 * <p>
 * Note that because a {@link Prism} might fail in one direction, it cannot be immediately used for
 * {@link View viewing}; however, the combinators {@link Re re}, {@link Pre pre}, and {@link Matching matching} can all
 * be used to provide the additional context to a {@link Prism} so it can be used for viewing.
 *
 * @param <S> the input that might fail to map to its output
 * @param <T> the guaranteed output
 * @param <A> the output that might fail to be produced
 * @param <B> the input that guarantees its output
 */
@FunctionalInterface
public interface Prism<S, T, A, B> extends ProtoOptic<Cocartesian<?, ?, ?>, S, T, A, B>, Monad<T, Prism<S, ?, A, B>> {

    /**
     * Recover the two mappings encapsulated by this {@link Prism} by sending it through a {@link Market}.
     *
     * @return a {@link Tuple2 tuple} of the two mappings encapsulated by this {@link Prism}
     */
    default Tuple2<Fn1<? super B, ? extends T>, Fn1<? super S, ? extends Either<T, A>>> unPrism() {
        return Tuple2.fill(this.<Market<A, B, ?, ?>, Identity<?>, Identity<B>, Identity<T>,
                Market<A, B, A, Identity<B>>, Market<A, B, S, Identity<T>>>apply(
                new Market<>(Identity::new, Either::right)).fmap(Identity::runIdentity))
                .biMap(Market::bt, Market::sta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <CoP extends Profunctor<?, ?, ? extends Cocartesian<?, ?, ?>>,
            CoF extends Functor<?, ? extends Identity<?>>,
            FB extends Functor<B, ? extends CoF>, FT extends Functor<T, ? extends CoF>,
            PAFB extends Profunctor<A, FB, ? extends CoP>,
            PSFT extends Profunctor<S, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
        @SuppressWarnings("RedundantTypeArguments")
        Optic<Cocartesian<?, ?, ?>, Identity<?>, S, T, A, B> optic = this.<Identity<?>>toOptic(Identity::new);
        return optic.apply(pafb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Prism<S, U, A, B> pure(U u) {
        return prism(constantly(left(u)), constantly(u));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Prism<S, U, A, B> flatMap(Fn1<? super T, ? extends Monad<U, Prism<S, ?, A, B>>> f) {
        return unPrism().into((bt, seta) -> prism(
                s -> seta.apply(s).match(t -> matching(f.apply(t).<Prism<S, U, A, B>>coerce(), s), Either::right),
                b -> View.<B, B, U, U>view(re(f.apply(bt.apply(b)).coerce())).apply(b)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Prism<S, U, A, B> fmap(Fn1<? super T, ? extends U> fn) {
        return Monad.super.<U>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Prism<S, U, A, B> zip(Applicative<Fn1<? super T, ? extends U>, Prism<S, ?, A, B>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Lazy<Prism<S, U, A, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super T, ? extends U>, Prism<S, ?, A, B>>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Monad<U, Prism<S, ?, A, B>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Prism<S, U, A, B> discardL(Applicative<U, Prism<S, ?, A, B>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Prism<S, T, A, B> discardR(Applicative<U, Prism<S, ?, A, B>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * Static factory method for creating a {@link Prism} given a mapping from
     * <code>S -&gt; {@link Either}&lt;T, A&gt;</code> and a mapping from <code>B -&gt; T</code>.
     *
     * @param sta the mapping from S -&gt; {@link Either}&lt;T, A&gt;
     * @param bt  the mapping from B -&gt; T
     * @param <S> the input that might fail to map to its output
     * @param <T> the guaranteed output
     * @param <A> the output that might fail to be produced
     * @param <B> the input that guarantees its output
     * @return the {@link Prism}
     */
    static <S, T, A, B> Prism<S, T, A, B> prism(Fn1<? super S, ? extends CoProduct2<T, A, ?>> sta,
                                                Fn1<? super B, ? extends T> bt) {
        return new Prism<S, T, A, B>() {
            @Override
            public <F extends Applicative<?, F>> Optic<Cocartesian<?, ?, ?>, F, S, T, A, B> toOptic(Pure<F> pure) {
                return Optic.<Cocartesian<?, ?, ?>,
                        F,
                        S, T, A, B,
                        Functor<B, ? extends F>,
                        Functor<T, ? extends F>,
                        Cocartesian<A, Functor<B, ? extends F>, ?>,
                        Cocartesian<S, Functor<T, ? extends F>, ?>>optic(pafb -> pafb.<T>cocartesian()
                        .diMap(s -> sta.apply(s).match(Choice2::a, Choice2::b),
                               tOrFb -> tOrFb.<Functor<T, ? extends F>>match(pure::apply, fb -> fb.fmap(bt))));
            }
        };
    }

    /**
     * Promote a {@link ProtoOptic} with compatible bounds to an {@link Prism}.
     *
     * @param protoOptic the {@link ProtoOptic}
     * @param <S>        the input that might fail to map to its output
     * @param <T>        the guaranteed output
     * @param <A>        the output that might fail to be produced
     * @param <B>        the input that guarantees its output
     * @return the {@link Prism}
     */
    static <S, T, A, B> Prism<S, T, A, B> prism(ProtoOptic<? super Cocartesian<?, ?, ?>, S, T, A, B> protoOptic) {
        return new Prism<S, T, A, B>() {
            @Override
            public <F extends Applicative<?, F>> Optic<Cocartesian<?, ?, ?>, F, S, T, A, B> toOptic(Pure<F> pure) {
                Optic<? super Cocartesian<?, ?, ?>, F, S, T, A, B> optic = protoOptic.toOptic(pure);
                return Optic.reframe(optic);
            }
        };
    }

    /**
     * Promote an {@link Optic} with compatible bounds to an {@link Prism}. Note that because the {@link Optic} must
     * guarantee an unbounded {@link Functor} constraint in order to satisfy any future covariant constraint, the
     * resulting {@link Prism prism's} <code>toOptic</code> method will never need to consult its given
     * {@link Pure lifting} function.
     *
     * @param optic the {@link Optic}
     * @param <S>   the input that might fail to map to its output
     * @param <T>   the guaranteed output
     * @param <A>   the output that might fail to be produced
     * @param <B>   the input that guarantees its output
     * @return the {@link Prism}
     */
    static <S, T, A, B> Prism<S, T, A, B> prism(
            Optic<? super Cocartesian<?, ?, ?>, ? super Functor<?, ?>, S, T, A, B> optic) {
        return new Prism<S, T, A, B>() {
            @Override
            public <F extends Applicative<?, F>> Optic<Cocartesian<?, ?, ?>, F, S, T, A, B> toOptic(Pure<F> pure) {
                return Optic.reframe(optic);
            }
        };
    }

    /**
     * Static factory method for creating a simple {@link Prism} from a function and its potentially failing inverse.
     *
     * @param sMaybeA a partial mapping from <code>S -&gt; A</code>
     * @param as      a total mapping from <code>A -&gt; S</code>
     * @param <S>     the input that might fail to map to its output and the guaranteed output from the other direction
     * @param <A>     the output that might fail to be produced and the input that guarantees its output in the other
     *                direction
     * @return the {@link Simple simple prism}
     */
    static <S, A> Prism.Simple<S, A> simplePrism(Fn1<? super S, ? extends Maybe<A>> sMaybeA,
                                                 Fn1<? super A, ? extends S> as) {
        return Prism.<S, S, A, A>prism(s -> sMaybeA.apply(s).toEither(() -> s), as)::toOptic;
    }


    static <S, A> Prism.Simple<S, A> fromPartial(Fn1<? super S, ? extends A> partialSa,
                                                 Fn1<? super A, ? extends S> as) {
        return adapt(prism(partialSa.<S, A>diMap(downcast(), upcast()).choose(), as));
    }

    /**
     * A convenience type with a simplified type signature for common {@link Prism prism} with unified <code>S/T</code>
     * and <code>A/B</code> types.
     *
     * @param <S> the input that might fail to map to its output and the guaranteed output from the other direction
     * @param <A> the output that might fail to be produced and the input that guarantees its output in the other
     *            direction
     */
    interface Simple<S, A> extends Prism<S, S, A, A> {

        /**
         * Adapt a {@link Prism} with compatible bounds to a {@link Prism.Simple simple Prism}.
         *
         * @param prism the {@link Prism}
         * @param <S>   the input that might fail to map to its output and the guaranteed output from the other
         *              direction
         * @param <A>   the output that might fail to be produced and the input that guarantees its output in the other
         *              direction
         * @return the {@link Prism.Simple simple Prism}
         */
        static <S, A> Prism.Simple<S, A> adapt(Prism<S, S, A, A> prism) {
            return prism::toOptic;
        }

        /**
         * Adapt a {@link ProtoOptic} with compatible bounds to a {@link Prism.Simple simple Prism}.
         *
         * @param protoOptic the {@link ProtoOptic}
         * @param <S>        the input that might fail to map to its output and the guaranteed output from the other
         *                   direction
         * @param <A>        the output that might fail to be produced and the input that guarantees its output in the
         *                   other direction
         * @return the {@link Prism.Simple simple Prism}
         */
        static <S, A> Prism.Simple<S, A> adapt(ProtoOptic<? super Cocartesian<?, ?, ?>, S, S, A, A> protoOptic) {
            return adapt(prism(protoOptic));
        }

        /**
         * Adapt an {@link Optic} with compatible bounds to a {@link Prism.Simple simple Prism}.
         *
         * @param optic the {@link Optic}
         * @param <S>   the input that might fail to map to its output and the guaranteed output from the other
         *              direction
         * @param <A>   the output that might fail to be produced and the input that guarantees its output in the
         *              other direction
         * @return the {@link Prism.Simple simple Prism}
         */
        static <S, A> Prism.Simple<S, A> adapt(
                Optic<? super Cocartesian<?, ?, ?>, ? super Functor<?, ?>, S, S, A, A> optic) {
            return adapt(prism(optic));
        }
    }
}
