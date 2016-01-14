package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.applicative.BiFunctor;
import com.jnape.palatable.lambda.applicative.Functor;
import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public interface Either<L, R> extends Functor<R>, BiFunctor<L, R> {

    static <L, R> Either<L, R> either(Optional<R> optional, Supplier<L> leftFn) {
        return optional.<Either<L, R>>map(Either::right)
                .orElse(left(leftFn.get()));
    }

    default R or(R defaultValue) {
        return recover(l -> defaultValue);
    }

    R recover(MonadicFunction<? super L, ? extends R> recoveryFn);

    <E extends RuntimeException> R orThrow(MonadicFunction<? super L, ? extends E> throwableFn) throws E;

    Either<L, R> filter(MonadicFunction<? super R, Boolean> pred,
                        Supplier<L> leftSupplier);

    <L2, R2> Either<L2, R2> flatMap(MonadicFunction<? super L, ? extends Either<L2, R2>> leftFn,
                                    MonadicFunction<? super R, ? extends Either<L2, R2>> rightFn);

    Either<L, R> merge(DyadicFunction<? super L, ? super L, ? extends L> leftFn,
                       DyadicFunction<? super R, ? super R, ? extends R> rightFn,
                       Either<L, R> other);

    <V> V match(MonadicFunction<? super L, ? extends V> leftFn,
                MonadicFunction<? super R, ? extends V> rightFn);

    @Override
    default <R2> Either<L, R2> fmap(MonadicFunction<? super R, ? extends R2> fn) {
        return biMapR(fn);
    }

    @Override
    default <L2> Either<L2, R> biMapL(MonadicFunction<? super L, ? extends L2> fn) {
        return (Either<L2, R>) BiFunctor.super.biMapL(fn);
    }

    @Override
    default <R2> Either<L, R2> biMapR(MonadicFunction<? super R, ? extends R2> fn) {
        return (Either<L, R2>) BiFunctor.super.biMapR(fn);
    }

    @Override
    <L2, R2> Either<L2, R2> biMap(MonadicFunction<? super L, ? extends L2> leftFn,
                                  MonadicFunction<? super R, ? extends R2> rightFn);

    static <L, R> Either<L, R> left(L l) {
        return new Left<>(l);
    }

    static <L, R> Either<L, R> right(R r) {
        return new Right<>(r);
    }

    final class Left<L, R> implements Either<L, R> {
        private final L l;

        private Left(L l) {
            this.l = l;
        }

        @Override
        public R recover(MonadicFunction<? super L, ? extends R> recoveryFn) {
            return recoveryFn.apply(l);
        }

        @Override
        public <E extends RuntimeException> R orThrow(MonadicFunction<? super L, ? extends E> throwableFn) throws E {
            throw throwableFn.apply(l);
        }

        @Override
        public Either<L, R> filter(MonadicFunction<? super R, Boolean> pred, Supplier<L> leftSupplier) {
            return this;
        }

        @Override
        public <L2, R2> Either<L2, R2> flatMap(MonadicFunction<? super L, ? extends Either<L2, R2>> leftFn,
                                               MonadicFunction<? super R, ? extends Either<L2, R2>> rightFn) {
            return leftFn.apply(l);
        }

        @Override
        public Either<L, R> merge(DyadicFunction<? super L, ? super L, ? extends L> leftFn,
                                  DyadicFunction<? super R, ? super R, ? extends R> rightFn, Either<L, R> other) {
            return other instanceof Left
                    ? left(leftFn.apply(l, ((Left<L, R>) other).l))
                    : this;
        }

        @Override
        public <V> V match(MonadicFunction<? super L, ? extends V> leftFn,
                           MonadicFunction<? super R, ? extends V> rightFn) {
            return leftFn.apply(l);
        }

        @Override
        public <L2, R2> Either<L2, R2> biMap(MonadicFunction<? super L, ? extends L2> leftFn,
                                             MonadicFunction<? super R, ? extends R2> rightFn) {
            return left(leftFn.apply(l));
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

    final class Right<L, R> implements Either<L, R> {
        private final R r;

        private Right(R r) {
            this.r = r;
        }

        @Override
        public R recover(MonadicFunction<? super L, ? extends R> recoveryFn) {
            return r;
        }

        @Override
        public <E extends RuntimeException> R orThrow(MonadicFunction<? super L, ? extends E> throwableFn) throws E {
            return r;
        }

        @Override
        public Either<L, R> filter(MonadicFunction<? super R, Boolean> pred, Supplier<L> leftSupplier) {
            return pred.apply(r) ? this : left(leftSupplier.get());
        }

        @Override
        public <L2, R2> Either<L2, R2> flatMap(MonadicFunction<? super L, ? extends Either<L2, R2>> leftFn,
                                               MonadicFunction<? super R, ? extends Either<L2, R2>> rightFn) {
            return rightFn.apply(r);
        }

        @Override
        public Either<L, R> merge(DyadicFunction<? super L, ? super L, ? extends L> leftFn,
                                  DyadicFunction<? super R, ? super R, ? extends R> rightFn, Either<L, R> other) {
            return other instanceof Right
                    ? right(rightFn.apply(r, ((Right<L, R>) other).r))
                    : other;
        }

        @Override
        public <V> V match(MonadicFunction<? super L, ? extends V> leftFn,
                           MonadicFunction<? super R, ? extends V> rightFn) {
            return rightFn.apply(r);
        }

        @Override
        public <L2, R2> Either<L2, R2> biMap(MonadicFunction<? super L, ? extends L2> leftFn,
                                             MonadicFunction<? super R, ? extends R2> rightFn) {
            return right(rightFn.apply(r));
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
