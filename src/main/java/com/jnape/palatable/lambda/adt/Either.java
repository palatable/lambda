package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.applicative.BiFunctor;
import com.jnape.palatable.lambda.applicative.Functor;
import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.specialized.unchecked.CheckedSupplier;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Identity.id;

public abstract class Either<L, R> implements Functor<R>, BiFunctor<L, R> {

    public final R or(R defaultValue) {
        return recover(l -> defaultValue);
    }

    public final R recover(MonadicFunction<? super L, ? extends R> recoveryFn) {
        return match(recoveryFn, id());
    }

    public final L forfeit(MonadicFunction<? super R, ? extends L> forfeitFn) {
        return match(id(), forfeitFn);
    }

    public final <E extends RuntimeException> R orThrow(
            MonadicFunction<? super L, ? extends E> throwableFn) throws E {
        return match(l -> {
            throw throwableFn.apply(l);
        }, id());
    }

    public final Either<L, R> filter(MonadicFunction<? super R, Boolean> pred,
                                     Supplier<L> leftSupplier) {
        return flatMap(r -> pred.apply(r) ? right(r) : left(leftSupplier.get()));
    }

    public final <R2> Either<L, R2> flatMap(MonadicFunction<? super R, ? extends Either<L, R2>> rightFn) {
        return flatMap(Either::left, rightFn);
    }

    public final <L2, R2> Either<L2, R2> flatMap(MonadicFunction<? super L, ? extends Either<L2, R2>> leftFn,
                                                 MonadicFunction<? super R, ? extends Either<L2, R2>> rightFn) {
        return match(leftFn, rightFn);
    }

    public final Either<L, R> merge(DyadicFunction<? super L, ? super L, ? extends L> leftFn,
                                    DyadicFunction<? super R, ? super R, ? extends R> rightFn,
                                    Either<L, R> other) {
        return this.match(
                l1 -> other.match(l2 -> left(leftFn.apply(l1, l2)), r -> left(l1)),
                r1 -> other.match(Either::left, r2 -> right(rightFn.apply(r1, r2))));
    }

    public abstract <V> V match(MonadicFunction<? super L, ? extends V> leftFn,
                                MonadicFunction<? super R, ? extends V> rightFn);

    @Override
    public final <R2> Either<L, R2> fmap(MonadicFunction<? super R, ? extends R2> fn) {
        return biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <L2> Either<L2, R> biMapL(MonadicFunction<? super L, ? extends L2> fn) {
        return (Either<L2, R>) BiFunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R2> Either<L, R2> biMapR(MonadicFunction<? super R, ? extends R2> fn) {
        return (Either<L, R2>) BiFunctor.super.biMapR(fn);
    }

    @Override
    public final <L2, R2> Either<L2, R2> biMap(MonadicFunction<? super L, ? extends L2> leftFn,
                                               MonadicFunction<? super R, ? extends R2> rightFn) {
        return match(l -> left(leftFn.apply(l)), r -> right(rightFn.apply(r)));
    }

    public static <L, R> Either<L, R> fromOptional(Optional<R> optional, Supplier<L> leftFn) {
        return optional.<Either<L, R>>map(Either::right)
                .orElse(left(leftFn.get()));
    }

    @SuppressWarnings("unchecked")
    public static <E extends Exception, L, R> Either<L, R> trying(CheckedSupplier<E, ? extends R> supplier,
                                                                  MonadicFunction<? super E, ? extends L> leftFn) {
        try {
            return right(supplier.get());
        } catch (Exception e) {
            return left(leftFn.apply((E) e));
        }
    }

    public static <E extends Exception, R> Either<E, R> trying(CheckedSupplier<E, R> supplier) {
        return trying(supplier, id());
    }

    public static <L, R> Either<L, R> left(L l) {
        return new Left<>(l);
    }

    public static <L, R> Either<L, R> right(R r) {
        return new Right<>(r);
    }

    private static final class Left<L, R> extends Either<L, R> {
        private final L l;

        private Left(L l) {
            this.l = l;
        }

        @Override
        public <V> V match(MonadicFunction<? super L, ? extends V> leftFn,
                           MonadicFunction<? super R, ? extends V> rightFn) {
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
        public <V> V match(MonadicFunction<? super L, ? extends V> leftFn,
                           MonadicFunction<? super R, ? extends V> rightFn) {
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
