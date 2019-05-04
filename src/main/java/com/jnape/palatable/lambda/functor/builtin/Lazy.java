package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn0.fn0;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;

/**
 * A {@link Monad} representing a lazily-computed value. Stack-safe.
 *
 * @param <A> the value type
 */
public abstract class Lazy<A> implements Monad<A, Lazy<?>>, Traversable<A, Lazy<?>> {

    private Lazy() {
    }

    /**
     * Returns the value represented by this lazy computation.
     *
     * @return the value
     */
    public abstract A value();

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<B> flatMap(Function<? super A, ? extends Monad<B, Lazy<?>>> f) {
        @SuppressWarnings("unchecked") Lazy<Object> source = (Lazy<Object>) this;
        @SuppressWarnings({"unchecked", "RedundantCast"})
        Function<Object, Lazy<Object>> flatMap = (Function<Object, Lazy<Object>>) (Object) f;
        return new Compose<>(source, flatMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <B, App extends Applicative<?, App>, TravB extends Traversable<B, Lazy<?>>,
            AppB extends Applicative<B, App>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Function<? super A, ? extends AppB> fn,
                                                                      Function<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(value()).fmap(b -> (TravB) lazy(b)).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> Lazy<B> pure(B b) {
        return lazy(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> Lazy<B> fmap(Function<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<B> zip(Applicative<Function<? super A, ? extends B>, Lazy<?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> Lazy<B> discardL(Applicative<B, Lazy<?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> Lazy<A> discardR(Applicative<B, Lazy<?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Lazy<?> && Objects.equals(value(), ((Lazy<?>) other).value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value());
    }

    @Override
    public String toString() {
        return "Lazy{value=" + value() + "}";
    }

    /**
     * Lift a pure value into a lazy computation.
     *
     * @param value the value
     * @param <A>   the value type
     * @return the new {@link Lazy}
     */
    public static <A> Lazy<A> lazy(A value) {
        return lazy(() -> value);
    }

    /**
     * Wrap a computation in a lazy computation.
     *
     * @param supplier the computation
     * @param <A>      the value type
     * @return the new {@link Lazy}
     */
    public static <A> Lazy<A> lazy(Supplier<A> supplier) {
        return new Later<>(fn0(supplier));
    }

    private static final class Later<A> extends Lazy<A> {
        private final Fn0<A> fn0;

        private Later(Fn0<A> fn0) {
            this.fn0 = fn0;
        }

        @Override
        public A value() {
            return fn0.apply();
        }
    }

    private static final class Compose<A> extends Lazy<A> {
        private final Lazy<Object>                   source;
        private final Function<Object, Lazy<Object>> flatMap;

        private Compose(Lazy<Object> source,
                        Function<Object, Lazy<Object>> flatMap) {
            this.source = source;
            this.flatMap = flatMap;
        }

        @Override
        public A value() {
            @SuppressWarnings("unchecked") Tuple2<Lazy<Object>, LinkedList<Function<Object, Lazy<Object>>>> tuple =
                    tuple((Lazy<Object>) this, new LinkedList<>());
            @SuppressWarnings("unchecked")
            A a = (A) trampoline(into((source, flatMaps) -> {
                if (source instanceof Compose<?>) {
                    Compose<?> nested = (Compose<?>) source;
                    flatMaps.push(nested.flatMap);
                    return recurse(tuple(nested.source, flatMaps));
                }

                if (flatMaps.isEmpty())
                    return terminate(source.value());

                return recurse(tuple(flatMaps.pop().apply(source.value()), flatMaps));
            }), tuple);

            return a;
        }
    }
}
