package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Empty;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.internal.iteration.TrampoliningIterator;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;

import java.util.Iterator;
import java.util.Objects;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Flatten.flatten;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

/**
 * Extension point for {@link Iterable} to adapt lambda core types like {@link Monad} and {@link Traversable}.
 *
 * @param <A> the {@link Iterable} element type
 * @see LambdaMap
 */
public final class LambdaIterable<A> implements
        MonadRec<A, LambdaIterable<?>>,
        Traversable<A, LambdaIterable<?>> {

    private final Iterable<A> as;

    @SuppressWarnings("unchecked")
    private LambdaIterable(Iterable<? extends A> as) {
        this.as = (Iterable<A>) as;
    }

    /**
     * Unwrap the underlying {@link Iterable}.
     *
     * @return the wrapped {@link Iterable}
     */
    public Iterable<A> unwrap() {
        return as;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> LambdaIterable<B> fmap(Fn1<? super A, ? extends B> fn) {
        return wrap(map(fn, as));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> LambdaIterable<B> pure(B b) {
        return wrap(singleton(b));
    }

    /**
     * {@inheritDoc}
     * <p>
     * In this case, calculate the cartesian product of applications of all functions in <code>appFn</code> to all
     * values wrapped by this {@link LambdaIterable}.
     *
     * @param appFn the other applicative instance
     * @param <B>   the new parameter type
     * @return the zipped LambdaIterable
     */
    @Override
    public <B> LambdaIterable<B> zip(Applicative<Fn1<? super A, ? extends B>, LambdaIterable<?>> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<LambdaIterable<B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, LambdaIterable<?>>> lazyAppFn) {
        return Empty.empty(as)
               ? lazy(LambdaIterable.empty())
               : MonadRec.super.lazyZip(lazyAppFn).fmap(Monad<B, LambdaIterable<?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> LambdaIterable<B> discardL(Applicative<B, LambdaIterable<?>> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> LambdaIterable<A> discardR(Applicative<B, LambdaIterable<?>> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> LambdaIterable<B> flatMap(Fn1<? super A, ? extends Monad<B, LambdaIterable<?>>> f) {
        return wrap(flatten(map(a -> f.apply(a).<LambdaIterable<B>>coerce().unwrap(), as)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> LambdaIterable<B> trampolineM(
            Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, LambdaIterable<?>>> fn) {
        return flatMap(a -> wrap(() -> new TrampoliningIterator<>(
                x -> fn.apply(x)
                        .<LambdaIterable<RecursiveResult<A, B>>>coerce()
                        .unwrap(),
                a)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <B, App extends Applicative<?, App>, TravB extends Traversable<B, LambdaIterable<?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super A, ? extends Applicative<B, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
        return FoldRight.<A, AppTrav>foldRight(
                (a, lazyAppTrav) -> (Lazy<AppTrav>) fn.apply(a)
                        .lazyZip(lazyAppTrav.<Applicative<Fn1<? super B, ? extends TravB>, App>>fmap(appTrav -> appTrav
                                .fmap(travB -> b -> (TravB) wrap(cons(b, ((LambdaIterable<B>) travB).unwrap()))))),
                lazy(() -> pure.apply((TravB) empty())),
                as
        ).value();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof LambdaIterable) {
            Iterator<A> xs = as.iterator();
            Iterator<?> ys = ((LambdaIterable<?>) other).as.iterator();

            while (xs.hasNext() && ys.hasNext())
                if (!Objects.equals(xs.next(), ys.next()))
                    return false;

            return xs.hasNext() == ys.hasNext();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(as);
    }

    /**
     * Wrap an {@link Iterable} in a {@link LambdaIterable}.
     *
     * @param as  the Iterable
     * @param <A> the Iterable element type
     * @return the Iterable wrapped in a {@link LambdaIterable}
     */
    public static <A> LambdaIterable<A> wrap(Iterable<? extends A> as) {
        return new LambdaIterable<>(as);
    }

    /**
     * Construct an empty {@link LambdaIterable} by wrapping {@link java.util.Collections#emptyList()}.
     *
     * @param <A> the Iterable element type
     * @return an empty {@link LambdaIterable}
     */
    public static <A> LambdaIterable<A> empty() {
        return wrap(emptyList());
    }

    /**
     * The canonical {@link Pure} instance for {@link LambdaIterable}.
     *
     * @return the {@link Pure} instance
     */
    public static Pure<LambdaIterable<?>> pureLambdaIterable() {
        return new Pure<LambdaIterable<?>>() {
            @Override
            public <A> LambdaIterable<A> checkedApply(A a) throws Throwable {
                return wrap(singleton(a));
            }
        };
    }
}
