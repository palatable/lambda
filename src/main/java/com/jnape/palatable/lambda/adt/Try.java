package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SideEffect;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.internal.Runtime.throwChecked;

/**
 * A {@link Monad} of the evaluation outcome of an expression that might throw. Try/catch/finally semantics map to
 * <code>trying</code>/<code>catching</code>/<code>ensuring</code>, respectively.
 *
 * @param <A> the possibly successful expression result
 * @see Either
 */
public abstract class Try<A> implements Monad<A, Try<?>>, Traversable<A, Try<?>>, CoProduct2<Throwable, A, Try<A>> {

    private Try() {
    }

    /**
     * Catch any instance of <code>throwableType</code> and map it to a success value.
     *
     * @param <S>           the {@link Throwable} (sub)type
     * @param throwableType the {@link Throwable} (sub)type to be caught
     * @param recoveryFn    the function mapping the {@link Throwable} to the result
     * @return a new {@link Try} instance around either the original successful result or the mapped result
     */
    @SuppressWarnings("unchecked")
    public final <S extends Throwable> Try<A> catching(Class<S> throwableType,
                                                       Fn1<? super S, ? extends A> recoveryFn) {
        return catching(throwableType::isInstance, t -> recoveryFn.apply((S) t));
    }

    /**
     * Catch any thrown <code>T</code> satisfying <code>predicate</code> and map it to a success value.
     *
     * @param predicate  the predicate
     * @param recoveryFn the function mapping the {@link Throwable} to the result
     * @return a new {@link Try} instance around either the original successful result or the mapped result
     */
    public final Try<A> catching(Fn1<? super Throwable, ? extends Boolean> predicate,
                                 Fn1<? super Throwable, ? extends A> recoveryFn) {
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
     * @param sideEffect the runnable block of code to execute
     * @return the same {@link Try} instance if runnable completes successfully; otherwise, a {@link Try} conforming to
     * rules above
     */
    public final Try<A> ensuring(SideEffect sideEffect) {
        return match(t -> trying(sideEffect)
                             .<Try<A>>fmap(constantly(failure(t)))
                             .recover(t2 -> {
                                 t.addSuppressed(t2);
                                 return failure(t);
                             }),
                     a -> trying(sideEffect).fmap(constantly(a)));
    }

    /**
     * If this is a success, return the wrapped value. Otherwise, apply the {@link Throwable} to <code>fn</code> and
     * return the result.
     *
     * @param fn the function mapping the potential {@link Throwable} <code>T</code> to <code>A</code>
     * @return a success value
     */
    public final A recover(Fn1<? super Throwable, ? extends A> fn) {
        return match(fn, id());
    }

    /**
     * If this is a failure, return the wrapped value. Otherwise, apply the success value to <code>fn</code> and return
     * the result.
     *
     * @param fn the function mapping the potential <code>A</code> to <code>T</code>
     * @return a failure value
     */
    public final Throwable forfeit(Fn1<? super A, ? extends Throwable> fn) {
        return match(id(), fn);
    }

    /**
     * If this is a success value, return it. Otherwise, rethrow the captured failure.
     *
     * @param <T> a declarable exception type used for catching checked exceptions
     * @return possibly the success value
     * @throws T anything that the call site may want to explicitly catch or indicate could be thrown
     */
    public final <T extends Throwable> A orThrow() throws T {
        try {
            return orThrow(id());
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    /**
     * If this is a success value, return it. Otherwise, transform the captured failure with <code>fn</code> and throw
     * the result.
     *
     * @param fn  the {@link Throwable} transformation
     * @param <T> the type of the thrown {@link Throwable}
     * @return possibly the success value
     * @throws T the transformation output
     */
    public abstract <T extends Throwable> A orThrow(Fn1<? super Throwable, ? extends T> fn) throws T;


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
    public final Either<Throwable, A> toEither() {
        return toEither(id());
    }

    /**
     * If this is a success, wrap the value in a {@link Either#right} and return it. Otherwise, apply the mapping
     * function to the failure {@link Throwable}, re-wrap it in an {@link Either#left}, and return it.
     *
     * @param <L> the {@link Either} left parameter type
     * @param fn  the mapping function
     * @return {@link Either} the success value or the mapped left value
     */
    public final <L> Either<L, A> toEither(Fn1<? super Throwable, ? extends L> fn) {
        return match(fn.fmap(Either::left), Either::right);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Try<B> fmap(Fn1<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Try<B> flatMap(Fn1<? super A, ? extends Monad<B, Try<?>>> f) {
        return match(Try::failure, a -> f.apply(a).coerce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Try<B> pure(B b) {
        return success(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Try<B> zip(Applicative<Fn1<? super A, ? extends B>, Try<?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<Try<B>> lazyZip(Lazy<? extends Applicative<Fn1<? super A, ? extends B>, Try<?>>> lazyAppFn) {
        return match(f -> lazy(failure(f)),
                     s -> lazyAppFn.fmap(tryF -> tryF.<B>fmap(f -> f.apply(s)).coerce()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Try<B> discardL(Applicative<B, Try<?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Try<A> discardR(Applicative<B, Try<?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <B, App extends Applicative<?, App>, TravB extends Traversable<B, Try<?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super A, ? extends Applicative<B, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return match(t -> pure.apply((TravB) failure(t)),
                     a -> fn.apply(a).fmap(Try::success).<TravB>fmap(Applicative::coerce).coerce());
    }

    /**
     * Static factory method for creating a success value.
     *
     * @param a   the wrapped value
     * @param <A> the success parameter type
     * @return a success value of a
     */
    public static <A> Try<A> success(A a) {
        return new Success<>(a);
    }

    /**
     * Static factory method for creating a failure value.
     *
     * @param t   the {@link Throwable}
     * @param <A> the success parameter type
     * @return a failure value of t
     */
    public static <A> Try<A> failure(Throwable t) {
        return new Failure<>(t);
    }

    /**
     * Execute <code>supplier</code>, returning a success <code>A</code> or a failure of the thrown {@link Throwable}.
     *
     * @param supplier the supplier
     * @param <A>      the possible success type
     * @return a new {@link Try} around either a successful A result or the thrown {@link Throwable}
     */
    public static <A> Try<A> trying(Fn0<? extends A> supplier) {
        try {
            return success(supplier.apply());
        } catch (Throwable t) {
            return failure(t);
        }
    }

    /**
     * Execute <code>runnable</code>, returning a success {@link Unit} or a failure of the thrown {@link Throwable}.
     *
     * @param sideEffect the runnable
     * @return a new {@link Try} around either a successful {@link Unit} result or the thrown {@link Throwable}
     */
    public static Try<Unit> trying(SideEffect sideEffect) {
        return trying(() -> {
            IO.io(sideEffect).unsafePerformIO();
            return UNIT;
        });
    }

    /**
     * Given a <code>{@link Fn0}&lt;{@link AutoCloseable}&gt;</code> <code>aSupplier</code> and an {@link Fn1}
     * <code>fn</code>, apply <code>fn</code> to the result of <code>aSupplier</code>, ensuring that the result has its
     * {@link AutoCloseable#close() close} method invoked, regardless of the outcome.
     * <p>
     * If the resource creation process throws, the function body throws, or the
     * {@link AutoCloseable#close() close method} throws, the result is a failure. If both the function body and the
     * {@link AutoCloseable#close() close method} throw, the result is a failure over the function body
     * {@link Throwable} with the {@link AutoCloseable#close() close method} {@link Throwable} added as a
     * {@link Throwable#addSuppressed(Throwable) suppressed} {@link Throwable}. If only the
     * {@link AutoCloseable#close() close method} throws, the result is a failure over that {@link Throwable}.
     * <p>
     * Note that <code>withResources</code> calls can be nested, in which case all of the above specified exception
     * handling applies, where closing the previously created resource is considered part of the body of the next
     * <code>withResources</code> calls, and {@link Throwable Throwables} are considered suppressed in the same manner.
     * Additionally, {@link AutoCloseable#close() close methods} are invoked in the inverse order of resource creation.
     * <p>
     * This is {@link Try}'s equivalent of
     * <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html" target="_top">
     * try-with-resources</a>, introduced in Java 7.
     *
     * @param fn0 the resource supplier
     * @param fn  the function body
     * @param <A> the resource type
     * @param <B> the function return type
     * @return a {@link Try} representing the result of the function's application to the resource
     */
    @SuppressWarnings("try")
    public static <A extends AutoCloseable, B> Try<B> withResources(
            Fn0<? extends A> fn0,
            Fn1<? super A, ? extends Try<? extends B>> fn) {
        return trying(() -> {
            try (A resource = fn0.apply()) {
                return fn.apply(resource).<B>fmap(upcast());
            }
        }).flatMap(id());
    }

    /**
     * Convenience overload of {@link Try#withResources(Fn0, Fn1) withResources} that cascades dependent resource
     * creation via nested calls.
     *
     * @param fn0 the first resource supplier
     * @param bFn the dependent resource function
     * @param fn  the function body
     * @param <A> the first resource type
     * @param <B> the second resource type
     * @param <C> the function return type
     * @return a {@link Try} representing the result of the function's application to the dependent resource
     */
    public static <A extends AutoCloseable, B extends AutoCloseable, C> Try<C> withResources(
            Fn0<? extends A> fn0,
            Fn1<? super A, ? extends B> bFn,
            Fn1<? super B, ? extends Try<? extends C>> fn) {
        return withResources(fn0, a -> withResources(() -> bFn.apply(a), fn::apply));
    }

    /**
     * Convenience overload of {@link Try#withResources(Fn0, Fn1, Fn1) withResources} that
     * cascades
     * two dependent resource creations via nested calls.
     *
     * @param fn0 the first resource supplier
     * @param bFn the second resource function
     * @param cFn the final resource function
     * @param fn  the function body
     * @param <A> the first resource type
     * @param <B> the second resource type
     * @param <C> the final resource type
     * @param <D> the function return type
     * @return a {@link Try} representing the result of the function's application to the final dependent resource
     */
    public static <A extends AutoCloseable, B extends AutoCloseable, C extends AutoCloseable, D> Try<D> withResources(
            Fn0<? extends A> fn0,
            Fn1<? super A, ? extends B> bFn,
            Fn1<? super B, ? extends C> cFn,
            Fn1<? super C, ? extends Try<? extends D>> fn) {
        return withResources(fn0, bFn, b -> withResources(() -> cFn.apply(b), fn::apply));
    }

    private static final class Failure<A> extends Try<A> {
        private final Throwable t;

        private Failure(Throwable t) {
            this.t = t;
        }

        @Override
        public <T extends Throwable> A orThrow(Fn1<? super Throwable, ? extends T> fn) throws T {
            throw fn.apply(t);
        }

        @Override
        public <R> R match(Fn1<? super Throwable, ? extends R> aFn, Fn1<? super A, ? extends R> bFn) {
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

    private static final class Success<A> extends Try<A> {
        private final A a;

        private Success(A a) {
            this.a = a;
        }

        @Override
        public <T extends Throwable> A orThrow(Fn1<? super Throwable, ? extends T> fn) {
            return a;
        }

        @Override
        public <R> R match(Fn1<? super Throwable, ? extends R> aFn, Fn1<? super A, ? extends R> bFn) {
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
