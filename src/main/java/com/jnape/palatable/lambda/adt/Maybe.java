package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.functions.specialized.checked.CheckedSupplier;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * The optional type, representing a potentially absent value. This is lambda's analog of {@link Optional}, supporting
 * all the usual suspects like {@link Functor}, {@link Applicative}, {@link Traversable}, etc.
 *
 * @param <A> the optional parameter type
 * @see Optional
 */
public abstract class Maybe<A> implements Monad<A, Maybe>, Traversable<A, Maybe> {
    private Maybe() {
    }

    /**
     * If the value is present, return it; otherwise, return the value supplied by <code>otherSupplier</code>.
     *
     * @param otherSupplier the supplier for the other value
     * @return this value, or the supplied other value
     */
    public abstract A orElseGet(Supplier<A> otherSupplier);

    /**
     * If the value is present, return it; otherwise, return <code>other</code>.
     *
     * @param other the other value
     * @return this value, or the other value
     */
    public final A orElse(A other) {
        return orElseGet(() -> other);
    }

    /**
     * If the value is present, return it; otherwise, throw the {@link Throwable} supplied by
     * <code>throwableSupplier</code>.
     *
     * @param throwableSupplier the supplier of the potentially thrown {@link Throwable}
     * @param <E>               the Throwable type
     * @return the value, if present
     * @throws E the throwable, if the value is absent
     */
    public final <E extends Throwable> A orElseThrow(Supplier<E> throwableSupplier) throws E {
        return orElseGet((CheckedSupplier<E, A>) () -> {
            throw throwableSupplier.get();
        });
    }

    /**
     * If this value is present and satisfies <code>predicate</code>, return <code>just</code> the value; otherwise,
     * return <code>nothing</code>.
     *
     * @param predicate the predicate to apply to the possibly absent value
     * @return maybe the present value that satisfied the predicate
     */
    public final Maybe<A> filter(Function<? super A, ? extends Boolean> predicate) {
        return flatMap(a -> predicate.apply(a) ? just(a) : nothing());
    }

    /**
     * If this value is absent, return the value supplied by <code>lSupplier</code> wrapped in <code>Either.left</code>.
     * Otherwise, wrap the value in <code>Either.right</code> and return it.
     *
     * @param lSupplier the supplier for the left value
     * @param <L>       the left parameter type
     * @return this value wrapped in an Either.right, or an Either.left around the result of lSupplier
     */
    public final <L> Either<L, A> toEither(Supplier<L> lSupplier) {
        return fmap(Either::<L, A>right).orElseGet(() -> left(lSupplier.get()));
    }

    /**
     * Convert to {@link Optional}.
     *
     * @return the Optional
     */
    public final Optional<A> toOptional() {
        return fmap(Optional::of).orElseGet(Optional::empty);
    }

    /**
     * Lift the value into the {@link Maybe} monad
     *
     * @param b   the value
     * @param <B> the value type
     * @return Just b
     */
    @Override
    public final <B> Maybe<B> pure(B b) {
        return just(b);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the value is present, return {@link Maybe#just} <code>fn</code> applied to the value; otherwise, return
     * {@link Maybe#nothing}.
     */
    @Override
    public final <B> Maybe<B> fmap(Function<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    @Override
    public final <B> Maybe<B> zip(Applicative<Function<? super A, ? extends B>, Maybe> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public final <B> Maybe<B> discardL(Applicative<B, Maybe> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public final <B> Maybe<A> discardR(Applicative<B, Maybe> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public abstract <B> Maybe<B> flatMap(Function<? super A, ? extends Monad<B, Maybe>> f);

    @Override
    public abstract <B, App extends Applicative> Applicative<Maybe<B>, App> traverse(
            Function<? super A, ? extends Applicative<B, App>> fn,
            Function<? super Traversable<B, Maybe>, ? extends Applicative<? extends Traversable<B, Maybe>, App>> pure);

    /**
     * If this value is present, accept it by <code>consumer</code>; otherwise, do nothing.
     *
     * @param consumer the consumer
     * @return the same Maybe instance
     */
    public final Maybe<A> peek(Consumer<A> consumer) {
        return fmap(a -> {
            consumer.accept(a);
            return a;
        });
    }

    /**
     * Convenience static factory method for creating a {@link Maybe} from an {@link Either}. If <code>either</code> is
     * a right value, wrap the value in a <code>just</code> and return it; otherwise, return {@link #nothing()}.
     *
     * @param either the either instance
     * @param <A>    the potential right value
     * @return "Just" the right value, or nothing
     */
    public static <A> Maybe<A> fromEither(Either<?, A> either) {
        return either.match(constantly(nothing()), Maybe::just);
    }

    /**
     * Convenience static factory method for creating a {@link Maybe} from an {@link Optional}.
     *
     * @param optional the optional
     * @param <A>      the optional parameter type
     * @return the equivalent Maybe instance
     */
    public static <A> Maybe<A> fromOptional(Optional<? extends A> optional) {
        return optional.<A>map(id()).map(Maybe::just).orElse(Maybe.nothing());
    }

    /**
     * Lift a potentially null value into {@link Maybe}. If <code>a</code> is not null, returns <code>just(a)</code>;
     * otherwise, returns {@link #nothing()}.
     *
     * @param a   the potentially null value
     * @param <A> the value parameter type
     * @return "Just" the value, or nothing
     */
    public static <A> Maybe<A> maybe(A a) {
        return a == null ? nothing() : just(a);
    }

    /**
     * Lift a non-null value into {@link Maybe}. This differs from {@link Maybe#maybe} in that the value *must* be
     * non-null; if it is null, a {@link NullPointerException} is thrown.
     *
     * @param a   the non-null value
     * @param <A> the value parameter type
     * @return "Just" the value
     * @throws NullPointerException if a is null
     */
    public static <A> Maybe<A> just(A a) {
        if (a == null)
            throw new NullPointerException();
        return new Just<>(a);
    }

    @SuppressWarnings("unchecked")
    public static <A> Maybe<A> nothing() {
        return Nothing.INSTANCE;
    }

    private static final class Just<A> extends Maybe<A> {

        private final A a;

        private Just(A a) {
            this.a = a;
        }

        @Override
        public A orElseGet(Supplier<A> otherSupplier) {
            return a;
        }

        @Override
        public <B> Maybe<B> flatMap(Function<? super A, ? extends Monad<B, Maybe>> f) {
            return f.apply(a).coerce();
        }

        @Override
        public <B, App extends Applicative> Applicative<Maybe<B>, App> traverse(
                Function<? super A, ? extends Applicative<B, App>> fn,
                Function<? super Traversable<B, Maybe>, ? extends Applicative<? extends Traversable<B, Maybe>, App>> pure) {
            return fn.apply(a).fmap(Just::new);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Just && Objects.equals(this.a, ((Just) other).a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }

        @Override
        public String toString() {
            return "Just " + a;
        }
    }

    private static final class Nothing<A> extends Maybe<A> {
        private static final Nothing INSTANCE = new Nothing();

        private Nothing() {
        }

        @Override
        @SuppressWarnings("unchecked")
        public <B> Maybe<B> flatMap(Function<? super A, ? extends Monad<B, Maybe>> f) {
            return nothing();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <B, App extends Applicative> Applicative<Maybe<B>, App> traverse(
                Function<? super A, ? extends Applicative<B, App>> fn,
                Function<? super Traversable<B, Maybe>, ? extends Applicative<? extends Traversable<B, Maybe>, App>> pure) {
            return (Applicative<Maybe<B>, App>) pure.apply(nothing());
        }

        @Override
        public A orElseGet(Supplier<A> otherSupplier) {
            return otherSupplier.get();
        }

        @Override
        public String toString() {
            return "Nothing";
        }
    }
}
