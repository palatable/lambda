package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn2.Peek;
import com.jnape.palatable.lambda.functions.builtin.fn2.Peek2;
import com.jnape.palatable.lambda.functions.specialized.SideEffect;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static java.util.Arrays.asList;

/**
 * The binary tagged union, implemented as a specialized {@link CoProduct2}. General semantics tend to connote "success"
 * values via the right value and "failure" values via the left values. {@link Either}s are both {@link Monad}s and
 * {@link Traversable}s over their right value and are {@link Bifunctor}s over both values.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 */
public abstract class Either<L, R> implements
        CoProduct2<L, R, Either<L, R>>,
        Monad<R, Either<L, ?>>,
        Traversable<R, Either<L, ?>>,
        Bifunctor<L, R, Either<?, ?>> {

    private Either() {
    }

    /**
     * Return the value wrapped by this <code>Either</code> if it's a right value; otherwise, return defaultValue.
     *
     * @param defaultValue the value to return if this is a left
     * @return the value wrapped by this Either if right; otherwise, defaultValue
     */
    public final R or(R defaultValue) {
        return recover(l -> defaultValue);
    }

    /**
     * "Recover" from a left value by applying a recoveryFn to the wrapped value and returning it in the case of a left
     * value; otherwise, return the wrapped right value.
     *
     * @param recoveryFn a function from L to R
     * @return either the wrapped value (if right) or the result of the left value applied to recoveryFn
     */
    public final R recover(Fn1<? super L, ? extends R> recoveryFn) {
        return match(recoveryFn, id());
    }

    /**
     * Inverse of recover. If this is a right value, apply the wrapped value to <code>forfeitFn</code> and return it;
     * otherwise, return the wrapped left value.
     *
     * @param forfeitFn a function from R to L
     * @return either the wrapped value (if left) or the result of the right value applied to forfeitFn
     */
    public final L forfeit(Fn1<? super R, ? extends L> forfeitFn) {
        return match(id(), forfeitFn);
    }

    /**
     * Return the wrapped value if this is a right; otherwise, map the wrapped left value to a <code>T</code> and throw
     * it.
     *
     * @param <T>         the left parameter type (the throwable type)
     * @param throwableFn a function from L to T
     * @return the wrapped value if this is a right
     * @throws T the result of applying the wrapped left value to throwableFn, if this is a left
     */
    public final <T extends Throwable> R orThrow(Fn1<? super L, ? extends T> throwableFn) throws T {
        return match(l -> {
            throw throwableFn.apply(l);
        }, id());
    }

    /**
     * If this is a right value, apply <code>pred</code> to it. If the result is <code>true</code>, return the same
     * value; otherwise, return the result of <code>leftSupplier</code> wrapped as a left value.
     * <p>
     * If this is a left value, return it.
     *
     * @param pred    the predicate to apply to a right value
     * @param leftFn0 the supplier of a left value if pred fails
     * @return this if a left value or a right value that pred matches; otherwise, the result of leftSupplier wrapped in
     * a left
     */
    public final Either<L, R> filter(Fn1<? super R, ? extends Boolean> pred, Fn0<L> leftFn0) {
        return filter(pred, __ -> leftFn0.apply());
    }

    /**
     * If this is a right value, apply <code>pred</code> to it. If the result is <code>true</code>, return the same
     * value; otherwise, return the results of applying the right value to <code>leftFn</code> wrapped as a left value.
     *
     * @param pred   the predicate to apply to a right value
     * @param leftFn the function from the right value to a left value if pred fails
     * @return this is a left value or a right value that pred matches; otherwise, the result of leftFn applied to the
     * right value, wrapped in a left
     */
    public final Either<L, R> filter(Fn1<? super R, ? extends Boolean> pred,
                                     Fn1<? super R, ? extends L> leftFn) {
        return flatMap(r -> pred.apply(r) ? right(r) : left(leftFn.apply(r)));
    }

    /**
     * If a right value, unwrap it and apply it to <code>rightFn</code>, returning the resulting
     * <code>Either&lt;L ,R&gt;</code>. Otherwise, return the left value.
     * <p>
     * Note that because this monadic form of <code>flatMap</code> only supports mapping over a theoretical right value,
     * the resulting <code>Either</code> must be invariant on the same left value to flatten properly.
     *
     * @param rightFn the function to apply to a right value
     * @param <R2>    the new right parameter type
     * @return the Either resulting from applying rightFn to this right value, or this left value if left
     */
    @Override
    @SuppressWarnings("RedundantTypeArguments")
    public <R2> Either<L, R2> flatMap(Fn1<? super R, ? extends Monad<R2, Either<L, ?>>> rightFn) {
        return match(Either::left, rightFn.fmap(Monad<R2, Either<L, ?>>::coerce));
    }

    @Override
    public final Either<R, L> invert() {
        return match(Either::right, Either::left);
    }

    /**
     * Given two binary operators over L and R, merge multiple <code>Either&lt;L, R&gt;</code>s into a single
     * <code>Either&lt;L, R&gt;</code>. Note that <code>merge</code> biases towards left values; that is, if any left
     * value exists, the result will be a left value, such that only unanimous right values result in an ultimate right
     * value.
     *
     * @param leftFn  the binary operator for L
     * @param rightFn the binary operator for R
     * @param others  the other Eithers to merge into this one
     * @return the merged Either
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final Either<L, R> merge(Fn2<? super L, ? super L, ? extends L> leftFn,
                                    Fn2<? super R, ? super R, ? extends R> rightFn,
                                    Either<L, R>... others) {
        return foldLeft((x, y) -> x.match(l1 -> y.match(l2 -> left(leftFn.apply(l1, l2)), r -> left(l1)),
                                          r1 -> y.match(Either::left, r2 -> right(rightFn.apply(r1, r2)))),
                        this,
                        asList(others));
    }

    /**
     * Perform side-effects against a wrapped right value, returning back the <code>Either</code> unaltered.
     *
     * @param effect the effecting consumer
     * @return the Either, unaltered
     */
    public Either<L, R> peek(Fn1<? super R, ? extends IO<?>> effect) {
        return Peek.peek(effect, this);
    }

    /**
     * Perform side-effects against a wrapped right or left value, returning back the <code>Either</code> unaltered.
     *
     * @param leftEffect  the effecting consumer for left values
     * @param rightEffect the effecting consumer for right values
     * @return the Either, unaltered
     */
    public Either<L, R> peek(Fn1<? super L, ? extends IO<?>> leftEffect, Fn1<? super R, ? extends IO<?>> rightEffect) {
        return Peek2.peek2(leftEffect, rightEffect, this);
    }

    /**
     * Given two mapping functions (one from an <code>L</code> to a <code>V</code>, one from an <code>R</code> to a
     * <code>V</code>), unwrap the value stored in this <code>Either</code>, apply the appropriate mapping function,
     * and return the result.
     *
     * @param <V>     the result type
     * @param leftFn  the left value mapping function
     * @param rightFn the right value mapping function
     * @return the result of applying the appropriate mapping function to the wrapped value
     */
    @Override
    public abstract <V> V match(Fn1<? super L, ? extends V> leftFn, Fn1<? super R, ? extends V> rightFn);

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Choice3<L, R, C> diverge() {
        return match(Choice3::a, Choice3::b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R2> Either<L, R2> fmap(Fn1<? super R, ? extends R2> fn) {
        return Monad.super.<R2>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <L2> Either<L2, R> biMapL(Fn1<? super L, ? extends L2> fn) {
        return (Either<L2, R>) Bifunctor.super.<L2>biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R2> Either<L, R2> biMapR(Fn1<? super R, ? extends R2> fn) {
        return (Either<L, R2>) Bifunctor.super.<R2>biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <L2, R2> Either<L2, R2> biMap(Fn1<? super L, ? extends L2> leftFn,
                                               Fn1<? super R, ? extends R2> rightFn) {
        return match(l -> left(leftFn.apply(l)), r -> right(rightFn.apply(r)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R2> Either<L, R2> pure(R2 r2) {
        return right(r2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R2> Either<L, R2> zip(Applicative<Fn1<? super R, ? extends R2>, Either<L, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> Lazy<Either<L, R2>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super R, ? extends R2>, Either<L, ?>>> lazyAppFn) {
        return match(l -> lazy(left(l)),
                     r -> lazyAppFn.fmap(eitherLF -> eitherLF.<R2>fmap(f -> f.apply(r)).coerce()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R2> Either<L, R2> discardL(Applicative<R2, Either<L, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R2> Either<L, R> discardR(Applicative<R2, Either<L, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public final <R2, App extends Applicative<?, App>, TravB extends Traversable<R2, Either<L, ?>>,
            AppB extends Applicative<R2, App>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super R, ? extends AppB> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return (AppTrav) match(l -> pure.apply((TravB) left(l)), r -> fn.apply(r).fmap(Either::right));
    }

    /**
     * In the left case, returns a {@link Maybe#nothing()}; otherwise, returns {@link Maybe#maybe} around the right
     * value.
     *
     * @return Maybe the right value
     */
    public final Maybe<R> toMaybe() {
        return projectB();
    }

    /**
     * Convert a {@link Maybe}&lt;R&gt; into an <code>Either&lt;L, R&gt;</code>, supplying the left value from
     * <code>leftFn</code> in the case of {@link Maybe#nothing()}.
     *
     * @param <L>     the left parameter type
     * @param <R>     the right parameter type
     * @param maybe   the maybe
     * @param leftFn0 the supplier to use for left values
     * @return a right value of the contained maybe value, or a left value of leftFn's result
     */
    public static <L, R> Either<L, R> fromMaybe(Maybe<R> maybe, Fn0<L> leftFn0) {
        return maybe.<Either<L, R>>fmap(Either::right)
                .orElseGet(() -> left(leftFn0.apply()));
    }

    /**
     * Attempt to execute the {@link Fn0}, returning its result in a right value. If the supplier throws an
     * exception, apply leftFn to it, wrap it in a left value and return it.
     *
     * @param <L>    the left parameter type
     * @param <R>    the right parameter type
     * @param fn0    the supplier of the right value
     * @param leftFn a function mapping E to L
     * @return the supplier result as a right value, or leftFn's mapping result as a left value
     */
    public static <L, R> Either<L, R> trying(Fn0<? extends R> fn0, Fn1<? super Throwable, ? extends L> leftFn) {
        return Try.<R>trying(fn0).toEither(leftFn);
    }

    /**
     * Attempt to execute the {@link Fn0}, returning its result in a right value. If the supplier throws an
     * exception, wrap it in a left value and return it.
     *
     * @param fn0 the supplier of the right value
     * @param <R> the right parameter type
     * @return the supplier result as a right value, or a left value of the thrown exception
     */
    public static <R> Either<Throwable, R> trying(Fn0<? extends R> fn0) {
        return trying(fn0, id());
    }

    /**
     * Attempt to execute the {@link SideEffect}, returning {@link Unit} in a right value. If the runnable throws
     * an exception, apply <code>leftFn</code> to it, wrap it in a left value, and return it.
     *
     * @param <L>        the left parameter type
     * @param sideEffect the runnable
     * @param leftFn     a function mapping E to L
     * @return {@link Unit} as a right value, or leftFn's mapping result as a left value
     */
    public static <L> Either<L, Unit> trying(SideEffect sideEffect, Fn1<? super Throwable, ? extends L> leftFn) {
        return Try.trying(sideEffect).toEither(leftFn);
    }

    /**
     * Attempt to execute the {@link SideEffect}, returning {@link Unit} in a right value. If the runnable throws
     * exception, wrap it in a left value and return it.
     *
     * @param sideEffect the runnable
     * @return {@link Unit} as a right value, or a left value of the thrown exception
     */
    public static Either<Throwable, Unit> trying(SideEffect sideEffect) {
        return trying(sideEffect, id());
    }

    /**
     * Static factory method for creating a left value.
     *
     * @param l   the wrapped value
     * @param <L> the left parameter type
     * @param <R> the right parameter type
     * @return a left value of l
     */
    public static <L, R> Either<L, R> left(L l) {
        return new Left<>(l);
    }

    /**
     * Static factory method for creating a right value.
     *
     * @param r   the wrapped value
     * @param <L> the left parameter type
     * @param <R> the right parameter type
     * @return a right value of r
     */
    public static <L, R> Either<L, R> right(R r) {
        return new Right<>(r);
    }

    private static final class Left<L, R> extends Either<L, R> {
        private final L l;

        private Left(L l) {
            this.l = l;
        }

        @Override
        public <V> V match(Fn1<? super L, ? extends V> leftFn, Fn1<? super R, ? extends V> rightFn) {
            return leftFn.apply(l);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Left && Objects.equals(l, ((Left) other).l);
        }

        @Override
        public int hashCode() {
            return Objects.hash(l);
        }

        @Override
        public String toString() {
            return "Left{" +
                    "l=" + l +
                    '}';
        }
    }

    private static final class Right<L, R> extends Either<L, R> {
        private final R r;

        private Right(R r) {
            this.r = r;
        }

        @Override
        public <V> V match(Fn1<? super L, ? extends V> leftFn, Fn1<? super R, ? extends V> rightFn) {
            return rightFn.apply(r);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Right && Objects.equals(r, ((Right) other).r);
        }

        @Override
        public int hashCode() {
            return Objects.hash(r);
        }

        @Override
        public String toString() {
            return "Right{" +
                    "r=" + r +
                    '}';
        }
    }
}
