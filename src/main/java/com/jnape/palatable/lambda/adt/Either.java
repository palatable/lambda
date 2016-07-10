package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.specialized.checked.CheckedSupplier;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * The binary tagged union. General semantics tend to connote "success" values via the right value and "failure" values
 * via the left values. <code>Either</code>s are both <code>Functor</code>s over their right value and
 * <code>Bifunctor</code>s over both values.
 *
 * @param <L> The left parameter type
 * @param <R> The right parameter type
 */
public abstract class Either<L, R> implements Functor<R>, Bifunctor<L, R> {

    private Either() {
    }

    public final R or(R defaultValue) {
        return recover(l -> defaultValue);
    }

    public final R recover(Fn1<? super L, ? extends R> recoveryFn) {
        return match(recoveryFn, id());
    }

    public final L forfeit(Fn1<? super R, ? extends L> forfeitFn) {
        return match(id(), forfeitFn);
    }

    public final <E extends RuntimeException> R orThrow(
            Fn1<? super L, ? extends E> throwableFn) throws E {
        return match(l -> {
            throw throwableFn.apply(l);
        }, id());
    }

    public final Either<L, R> filter(Fn1<? super R, Boolean> pred,
                                     Supplier<L> leftSupplier) {
        return flatMap(r -> pred.apply(r) ? right(r) : left(leftSupplier.get()));
    }

    public final <R2> Either<L, R2> flatMap(Fn1<? super R, ? extends Either<L, R2>> rightFn) {
        return flatMap(Either::left, rightFn);
    }

    public final <L2, R2> Either<L2, R2> flatMap(Fn1<? super L, ? extends Either<L2, R2>> leftFn,
                                                 Fn1<? super R, ? extends Either<L2, R2>> rightFn) {
        return match(leftFn, rightFn);
    }

    public final Either<L, R> merge(Fn2<? super L, ? super L, ? extends L> leftFn,
                                    Fn2<? super R, ? super R, ? extends R> rightFn,
                                    Either<L, R> other) {
        return this.match(
                l1 -> other.match(l2 -> left(leftFn.apply(l1, l2)), r -> left(l1)),
                r1 -> other.match(Either::left, r2 -> right(rightFn.apply(r1, r2))));
    }

    public Either<L, R> peek(Consumer<R> rightConsumer) {
        return peek(l -> {
        }, rightConsumer);
    }

    public Either<L, R> peek(Consumer<L> leftConsumer, Consumer<R> rightConsumer) {
        return flatMap(l -> {
            leftConsumer.accept(l);
            return this;
        }, r -> {
            rightConsumer.accept(r);
            return this;
        });
    }

    public abstract <V> V match(Fn1<? super L, ? extends V> leftFn,
                                Fn1<? super R, ? extends V> rightFn);

    @Override
    public final <R2> Either<L, R2> fmap(Fn1<? super R, ? extends R2> fn) {
        return biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <L2> Either<L2, R> biMapL(Fn1<? super L, ? extends L2> fn) {
        return (Either<L2, R>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R2> Either<L, R2> biMapR(Fn1<? super R, ? extends R2> fn) {
        return (Either<L, R2>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public final <L2, R2> Either<L2, R2> biMap(Fn1<? super L, ? extends L2> leftFn,
                                               Fn1<? super R, ? extends R2> rightFn) {
        return match(l -> left(leftFn.apply(l)), r -> right(rightFn.apply(r)));
    }

    public static <L, R> Either<L, R> fromOptional(Optional<R> optional, Supplier<L> leftFn) {
        return optional.<Either<L, R>>map(Either::right)
                .orElse(left(leftFn.get()));
    }

    @SuppressWarnings("unchecked")
    public static <E extends Exception, L, R> Either<L, R> trying(CheckedSupplier<E, ? extends R> supplier,
                                                                  Fn1<? super E, ? extends L> leftFn) {
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
        public <V> V match(Fn1<? super L, ? extends V> leftFn,
                           Fn1<? super R, ? extends V> rightFn) {
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
        public <V> V match(Fn1<? super L, ? extends V> leftFn,
                           Fn1<? super R, ? extends V> rightFn) {
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
