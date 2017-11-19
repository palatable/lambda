package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.specialized.checked.CheckedFn1;
import com.jnape.palatable.lambda.functions.specialized.checked.CheckedSupplier;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static java.util.Arrays.asList;

/**
 * The binary tagged union. General semantics tend to connote "success" values via the right value and "failure" values
 * via the left values. <code>Either</code>s are both <code>Functor</code>s over their right value and
 * <code>Bifunctor</code>s over both values.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 */
public abstract class Either<L, R> implements CoProduct2<L, R, Either<L, R>>, Monad<R, Either<L, ?>>, Traversable<R, Either<L, ?>>, Bifunctor<L, R, Either> {

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
    public final R recover(Function<? super L, ? extends R> recoveryFn) {
        return match(recoveryFn, id());
    }

    /**
     * Inverse of recover. If this is a right value, apply the wrapped value to <code>forfeitFn</code> and return it;
     * otherwise, return the wrapped left value.
     *
     * @param forfeitFn a function from R to L
     * @return either the wrapped value (if left) or the result of the right value applied to forfeitFn
     */
    public final L forfeit(Function<? super R, ? extends L> forfeitFn) {
        return match(id(), forfeitFn);
    }

    /**
     * Return the wrapped value if this is a right; otherwise, map the wrapped left value to a <code>T</code> and throw
     * it.
     *
     * @param throwableFn a function from L to T
     * @param <T>         the left parameter type (the throwable type)
     * @return the wrapped value if this is a right
     * @throws T the result of applying the wrapped left value to throwableFn, if this is a left
     */
    public final <T extends Throwable> R orThrow(Function<? super L, ? extends T> throwableFn) throws T {
        return match((CheckedFn1<T, L, R>) l -> {
            throw throwableFn.apply(l);
        }, id());
    }

    /**
     * If this is a right value, apply pred to it. If the result is true, return the same value; otherwise, return the
     * result of leftSupplier wrapped as a left value.
     * <p>
     * If this is a left value, return it.
     *
     * @param pred         the predicate to apply to a right value
     * @param leftSupplier the supplier of a left value if pred fails
     * @return this if a left value or a right value that pred matches; otherwise, the result of leftSupplier wrapped in
     * a left
     */
    public final Either<L, R> filter(Function<? super R, Boolean> pred, Supplier<L> leftSupplier) {
        return flatMap(r -> pred.apply(r) ? right(r) : left(leftSupplier.get()));
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
    public <R2> Either<L, R2> flatMap(Function<? super R, ? extends Monad<R2, Either<L, ?>>> rightFn) {
        return flatMap(Either::left, rightFn.andThen(Applicative::coerce));
    }

    /**
     * If a right value, apply <code>rightFn</code> to the unwrapped right value and return the resulting
     * <code>Either</code>; otherwise, apply the unwrapped left value to leftFn and return the resulting
     * <code>Either</code>.
     *
     * @param leftFn  the function to apply if a left value
     * @param rightFn the function to apply if a right value
     * @param <L2>    the new left parameter type
     * @param <R2>    the new right parameter type
     * @return the result of either rightFn or leftFn, depending on whether this is a right or a left
     */
    public final <L2, R2> Either<L2, R2> flatMap(Function<? super L, ? extends Either<L2, R2>> leftFn,
                                                 Function<? super R, ? extends Either<L2, R2>> rightFn) {
        return match(leftFn, rightFn);
    }

    @Override
    public final Either<R, L> invert() {
        return flatMap(Either::right, Either::left);
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
    public final Either<L, R> merge(BiFunction<? super L, ? super L, ? extends L> leftFn,
                                    BiFunction<? super R, ? super R, ? extends R> rightFn,
                                    Either<L, R>... others) {
        return foldLeft((x, y) -> x.match(l1 -> y.match(l2 -> left(leftFn.apply(l1, l2)), r -> left(l1)),
                                          r1 -> y.match(Either::left, r2 -> right(rightFn.apply(r1, r2)))),
                        this,
                        asList(others));
    }

    /**
     * Perform side-effects against a wrapped right value, returning back the <code>Either</code> unaltered.
     *
     * @param rightConsumer the effecting consumer
     * @return the Either, unaltered
     */
    public Either<L, R> peek(Consumer<R> rightConsumer) {
        return peek(l -> {
        }, rightConsumer);
    }

    /**
     * Perform side-effects against a wrapped right or left value, returning back the <code>Either</code> unaltered.
     *
     * @param leftConsumer  the effecting consumer for left values
     * @param rightConsumer the effecting consumer for right values
     * @return the Either, unaltered
     */
    public Either<L, R> peek(Consumer<L> leftConsumer, Consumer<R> rightConsumer) {
        return flatMap(l -> {
            leftConsumer.accept(l);
            return this;
        }, r -> {
            rightConsumer.accept(r);
            return this;
        });
    }

    /**
     * Given two mapping functions (one from an <code>L</code> to a <code>V</code>, one from an <code>R</code> to a
     * <code>V</code>), unwrap the value stored in this <code>Either</code>, apply the appropriate mapping function,
     * and return the result.
     *
     * @param leftFn  the left value mapping function
     * @param rightFn the right value mapping function
     * @param <V>     the result type
     * @return the result of applying the appropriate mapping function to the wrapped value
     */
    @Override
    public abstract <V> V match(Function<? super L, ? extends V> leftFn, Function<? super R, ? extends V> rightFn);

    @Override
    public final <R2> Either<L, R2> fmap(Function<? super R, ? extends R2> fn) {
        return Monad.super.<R2>fmap(fn).coerce();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <L2> Either<L2, R> biMapL(Function<? super L, ? extends L2> fn) {
        return (Either<L2, R>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R2> Either<L, R2> biMapR(Function<? super R, ? extends R2> fn) {
        return (Either<L, R2>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public final <L2, R2> Either<L2, R2> biMap(Function<? super L, ? extends L2> leftFn,
                                               Function<? super R, ? extends R2> rightFn) {
        return match(l -> left(leftFn.apply(l)), r -> right(rightFn.apply(r)));
    }

    @Override
    public <R2> Either<L, R2> pure(R2 r2) {
        return right(r2);
    }

    @Override
    public <R2> Either<L, R2> zip(Applicative<Function<? super R, ? extends R2>, Either<L, ?>> appFn) {
        return appFn.<Either<L, Function<? super R, ? extends R2>>>coerce().flatMap(this::biMapR);
    }

    @Override
    public <R2> Either<L, R2> discardL(Applicative<R2, Either<L, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <R2> Either<L, R> discardR(Applicative<R2, Either<L, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <R2, App extends Applicative> Applicative<Either<L, R2>, App> traverse(
            Function<? super R, ? extends Applicative<R2, App>> fn,
            Function<? super Traversable<R2, Either<L, ?>>, ? extends Applicative<? extends Traversable<R2, Either<L, ?>>, App>> pure) {
        return match(l -> pure.apply(left(l)).fmap(x -> (Either<L, R2>) x),
                     r -> fn.apply(r).fmap(Either::right));
    }

    /**
     * In the left case, returns a {@link Maybe#nothing()}; otherwise, returns {@link Maybe#maybe} around the right
     * value.
     *
     * @return Maybe the right value
     */
    public Maybe<R> toMaybe() {
        return projectB();
    }

    /**
     * Convert a {@link Maybe}&lt;R&gt; into an <code>Either&lt;L, R&gt;</code>, supplying the left value from
     * <code>leftFn</code> in the case of {@link Maybe#nothing()}.
     *
     * @param maybe  the maybe
     * @param leftFn the supplier to use for left values
     * @param <L>    the left parameter type
     * @param <R>    the right parameter type
     * @return a right value of the contained maybe value, or a left value of leftFn's result
     */
    public static <L, R> Either<L, R> fromMaybe(Maybe<R> maybe, Supplier<L> leftFn) {
        return maybe.<Either<L, R>>fmap(Either::right)
                .orElseGet(() -> left(leftFn.get()));
    }

    /**
     * Attempt to execute the {@link CheckedSupplier}, returning its result in a right value. If the supplier throws an
     * exception, apply leftFn to it, wrap it in a left value and return it.
     *
     * @param supplier the supplier of the right value
     * @param leftFn   a function mapping E to L
     * @param <E>      the most contravariant exception that the supplier might throw
     * @param <L>      the left parameter type
     * @param <R>      the right parameter type
     * @return the supplier result as a right value, or leftFn's mapping result as a left value
     */
    @SuppressWarnings("unchecked")
    public static <E extends Exception, L, R> Either<L, R> trying(CheckedSupplier<E, ? extends R> supplier,
                                                                  Function<? super E, ? extends L> leftFn) {
        try {
            return right(supplier.get());
        } catch (Exception e) {
            return left(leftFn.apply((E) e));
        }
    }

    /**
     * Attempt to execute the {@link CheckedSupplier}, returning its result in a right value. If the supplier throws an
     * exception, wrap it in a left value and return it.
     *
     * @param supplier the supplier of the right value
     * @param <E>      the left parameter type (the most contravariant exception that supplier might throw)
     * @param <R>      the right parameter type
     * @return the supplier result as a right value, or a left value of the thrown exception
     */
    public static <E extends Exception, R> Either<E, R> trying(CheckedSupplier<E, R> supplier) {
        return trying(supplier, id());
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
        public <V> V match(Function<? super L, ? extends V> leftFn, Function<? super R, ? extends V> rightFn) {
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
        public <V> V match(Function<? super L, ? extends V> leftFn, Function<? super R, ? extends V> rightFn) {
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
