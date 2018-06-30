package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.specialized.checked.CheckedRunnable;
import com.jnape.palatable.lambda.functions.specialized.checked.CheckedSupplier;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Apply;
import com.jnape.palatable.lambda.functor.Bind;
import com.jnape.palatable.lambda.functor.BoundedBifunctor;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Peek2.peek2;

/**
 * A {@link Monad} of the evaluation outcome of an expression that might throw. Try/catch/finally semantics map to
 * <code>trying</code>/<code>catching</code>/<code>ensuring</code>, respectively.
 *
 * @param <T> the {@link Throwable} type that may have been thrown by the expression
 * @param <A> the possibly successful expression result
 * @see Either
 */
public abstract class Try<T extends Throwable, A> implements Monad<A, Try<T, ?>>, Traversable<A, Try<T, ?>>, BoundedBifunctor<T, A, Throwable, Object, Try<?, ?>>, CoProduct2<T, A, Try<T, A>> {

    private Try() {
    }

    /**
     * Catch any instance of <code>throwableType</code> and map it to a success value.
     *
     * @param throwableType the {@link Throwable} (sub)type to be caught
     * @param recoveryFn    the function mapping the {@link Throwable} to the result
     * @param <S>           the {@link Throwable} (sub)type
     * @return a new {@link Try} instance around either the original successful result or the mapped result
     */
    @SuppressWarnings("unchecked")
    public final <S extends T> Try<T, A> catching(Class<S> throwableType, Function<? super S, ? extends A> recoveryFn) {
        return catching(throwableType::isInstance, t -> recoveryFn.apply((S) t));
    }

    /**
     * Catch any thrown <code>T</code> satisfying <code>predicate</code> and map it to a success value.
     *
     * @param predicate  the predicate
     * @param recoveryFn the function mapping the {@link Throwable} to the result
     * @return a new {@link Try} instance around either the original successful result or the mapped result
     */
    public final Try<T, A> catching(Function<? super T, Boolean> predicate,
                                    Function<? super T, ? extends A> recoveryFn) {
        return match(t -> predicate.apply(t) ? success(recoveryFn.apply(t)) : failure(t), Try::success);
    }

    /**
     * Run the provided runnable regardless of whether this is a success or a failure (the {@link Try} analog to
     * <code>finally</code>.
     * <p>
     * If the runnable runs successfully, the result is preserved as is. If the runnable itself throws, and the result
     * was a success, the result becomes a failure over the newly-thrown {@link Throwable}. If the result was a failure
     * over some {@link Throwable} <code>t1</code>, and the runnable throws a new {@link Throwable} <code>t2</code>, the
     * result is a failure over <code>t1</code> with <code>t2</code> added to <code>t1</code> as a suppressed exception.
     *
     * @param runnable the runnable block of code to execute
     * @return the same {@link Try} instance if runnable completes successfully; otherwise, a {@link Try} conforming to
     * rules above
     */
    public final Try<T, A> ensuring(CheckedRunnable<T> runnable) {
        return match(t -> peek2(t::addSuppressed, __ -> {}, trying(runnable))
                             .biMapL(constantly(t))
                             .flatMap(constantly(failure(t))),
                     a -> trying(runnable).fmap(constantly(a)));
    }

    /**
     * If this is a success, return the wrapped value. Otherwise, apply the {@link Throwable} to <code>fn</code> and
     * return the result.
     *
     * @param fn the function mapping the potential {@link Throwable} <code>T</code> to <code>A</code>
     * @return a success value
     */
    public final A recover(Function<? super T, ? extends A> fn) {
        return match(fn, id());
    }

    /**
     * If this is a failure, return the wrapped value. Otherwise, apply the success value to <code>fn</code> and return
     * the result.
     *
     * @param fn the function mapping the potential <code>A</code> to <code>T</code>
     * @return a failure value
     */
    public final T forfeit(Function<? super A, ? extends T> fn) {
        return match(id(), fn);
    }

    /**
     * If this is a success value, return it. Otherwise, rethrow the captured failure.
     *
     * @return possibly the success value
     * @throws T the possible failure
     */
    public abstract A orThrow() throws T;

    /**
     * If this is a success, wrap the value in a {@link Maybe#just} and return it. Otherwise, return {@link
     * Maybe#nothing()}.
     *
     * @return {@link Maybe} the success value
     */
    public final Maybe<A> toMaybe() {
        return match(__ -> nothing(), Maybe::just);
    }

    /**
     * If this is a success, wrap the value in a {@link Either#right} and return it. Otherwise, return the {@link
     * Throwable} in an {@link Either#left}.
     *
     * @return {@link Either} the success value or the {@link Throwable}
     */
    public final Either<T, A> toEither() {
        return toEither(id());
    }

    /**
     * If this is a success, wrap the value in a {@link Either#right} and return it. Otherwise, apply the mapping
     * function to the failure {@link Throwable}, re-wrap it in an {@link Either#left}, and return it.
     *
     * @param fn  the mapping function
     * @param <L> the {@link Either} left parameter type
     * @return {@link Either} the success value or the mapped left value
     */
    public final <L> Either<L, A> toEither(Function<? super T, ? extends L> fn) {
        return match(fn.andThen(Either::left), Either::right);
    }

    @Override
    public <B> Try<T, B> fmap(Function<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    @Override
    public <B> Try<T, B> flatMap(Function<? super A, ? extends Bind<B, Try<T, ?>>> f) {
        return match(Try::failure, a -> f.apply(a).coerce());
    }

    @Override
    public <B> Try<T, B> pure(B b) {
        return success(b);
    }

    @Override
    public <B> Try<T, B> zip(Apply<Function<? super A, ? extends B>, Try<T, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public <B> Try<T, B> discardL(Applicative<B, Try<T, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <B> Try<T, A> discardR(Applicative<B, Try<T, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B, App extends Applicative, TravB extends Traversable<B, Try<T, ?>>, AppB extends Applicative<B, App>, AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Function<? super A, ? extends AppB> fn, Function<? super TravB, ? extends AppTrav> pure) {
        return match(t -> pure.apply((TravB) failure(t)),
                     a -> fn.apply(a).fmap(Try::success).<TravB>fmap(Applicative::coerce).coerce());
    }

    @Override
    public <U extends Throwable, D> Try<U, D> biMap(Function<? super T, ? extends U> lFn,
                                                    Function<? super A, ? extends D> rFn) {
        return match(t -> failure(lFn.apply(t)), a -> success(rFn.apply(a)));
    }

    @Override
    public <U extends Throwable> Try<U, A> biMapL(Function<? super T, ? extends U> fn) {
        return (Try<U, A>) BoundedBifunctor.super.<U>biMapL(fn);
    }

    @Override
    public <B> Try<T, B> biMapR(Function<? super A, ? extends B> fn) {
        return (Try<T, B>) BoundedBifunctor.super.<B>biMapR(fn);
    }

    /**
     * Static factory method for creating a success value.
     *
     * @param a   the wrapped value
     * @param <T> the failure parameter type
     * @param <A> the success parameter type
     * @return a success value of a
     */
    public static <T extends Throwable, A> Try<T, A> success(A a) {
        return new Success<>(a);
    }

    /**
     * Static factory method for creating a failure value.
     *
     * @param t   the wrapped {@link Throwable}
     * @param <T> the failure parameter type
     * @param <A> the success parameter type
     * @return a failure value of t
     */
    public static <T extends Throwable, A> Try<T, A> failure(T t) {
        return new Failure<>(t);
    }

    /**
     * Execute <code>supplier</code>, returning a success <code>A</code> or a failure of the thrown {@link Throwable}.
     *
     * @param supplier the supplier
     * @param <T>      the possible {@link Throwable} type
     * @param <A>      the possible success type
     * @return a new {@link Try} around either a successful A result or the thrown {@link Throwable}
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable, A> Try<T, A> trying(CheckedSupplier<T, A> supplier) {
        try {
            return success(supplier.get());
        } catch (Throwable t) {
            return failure((T) t);
        }
    }

    /**
     * Execute <code>runnable</code>, returning a success {@link Unit} or a failure of the thrown {@link Throwable}.
     *
     * @param runnable the runnable
     * @param <T>      the possible {@link Throwable} type
     * @return a new {@link Try} around either a successful {@link Unit} result or the thrown {@link Throwable}
     */
    public static <T extends Throwable> Try<T, Unit> trying(CheckedRunnable<T> runnable) {
        return trying(() -> {
            runnable.run();
            return UNIT;
        });
    }

    private static final class Failure<T extends Throwable, A> extends Try<T, A> {
        private final T t;

        private Failure(T t) {
            this.t = t;
        }

        @Override
        public A orThrow() throws T {
            throw t;
        }

        @Override
        public <R> R match(Function<? super T, ? extends R> aFn, Function<? super A, ? extends R> bFn) {
            return aFn.apply(t);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Failure && Objects.equals(t, ((Failure) other).t);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t);
        }

        @Override
        public String toString() {
            return "Failure{" +
                    "t=" + t +
                    '}';
        }
    }

    private static final class Success<T extends Throwable, A> extends Try<T, A> {
        private final A a;

        private Success(A a) {
            this.a = a;
        }

        @Override
        public A orThrow() throws T {
            return a;
        }

        @Override
        public <R> R match(Function<? super T, ? extends R> aFn, Function<? super A, ? extends R> bFn) {
            return bFn.apply(a);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Success && Objects.equals(a, ((Success) other).a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }

        @Override
        public String toString() {
            return "Success{" +
                    "a=" + a +
                    '}';
        }
    }
}
