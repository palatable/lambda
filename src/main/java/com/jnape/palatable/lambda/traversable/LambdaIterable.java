package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Flatten.flatten;
import static com.jnape.palatable.lambda.functions.builtin.fn2.CartesianProduct.cartesianProduct;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight.foldRight;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

/**
 * Wrap an {@link Iterable} in a {@link Traversable} such that {@link Traversable#traverse(Function, Function)} applies
 * its computation against each element of the wrapped {@link Iterable}. Returns the result of <code>pure</code> if the
 * wrapped {@link Iterable} is empty.
 *
 * @param <A> the Iterable element type
 */
public final class LambdaIterable<A> implements Monad<A, LambdaIterable>, Traversable<A, LambdaIterable> {
    private final Iterable<A> as;

    @SuppressWarnings("unchecked")
    private LambdaIterable(Iterable<? extends A> as) {
        this.as = (Iterable<A>) as;
    }

    /**
     * Unwrap the underlying {@link Iterable}.
     *
     * @return the wrapped Iterable
     */
    public Iterable<A> unwrap() {
        return as;
    }

    @Override
    public <B> LambdaIterable<B> fmap(Function<? super A, ? extends B> fn) {
        return wrap(map(fn, as));
    }

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
    @SuppressWarnings("Convert2MethodRef")
    public <B> LambdaIterable<B> zip(Applicative<Function<? super A, ? extends B>, LambdaIterable> appFn) {
        return wrap(map(into((f, x) -> f.apply(x)),
                        cartesianProduct(appFn.<LambdaIterable<Function<? super A, ? extends B>>>coerce().unwrap(), as)));
    }

    @Override
    public <B> LambdaIterable<B> discardL(Applicative<B, LambdaIterable> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <B> LambdaIterable<A> discardR(Applicative<B, LambdaIterable> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <B> LambdaIterable<B> flatMap(Function<? super A, ? extends Monad<B, LambdaIterable>> f) {
        return wrap(flatten(map(a -> f.apply(a).<LambdaIterable<B>>coerce().unwrap(), as)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B, App extends Applicative, TravB extends Traversable<B, LambdaIterable>, AppB extends Applicative<B, App>, AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Function<? super A, ? extends AppB> fn,
            Function<? super TravB, ? extends AppTrav> pure) {
        return foldRight((a, appTrav) -> (AppTrav) appTrav.<TravB>zip(fn.apply(a).fmap(b -> bs -> (TravB) wrap(cons(b, ((LambdaIterable<B>) bs).unwrap())))),
                         (AppTrav) pure.apply((TravB) LambdaIterable.<B>empty()),
                         as);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof LambdaIterable) {
            Iterator<A> xs = as.iterator();
            Iterator ys = ((LambdaIterable) other).as.iterator();

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
     * Wrap an {@link Iterable} in a <code>TraversableIterable</code>.
     *
     * @param as  the Iterable
     * @param <A> the Iterable element type
     * @return the Iterable wrapped in a TraversableIterable
     */
    public static <A> LambdaIterable<A> wrap(Iterable<? extends A> as) {
        return new LambdaIterable<>(as);
    }

    /**
     * Construct an empty <code>TraversableIterable</code> by wrapping {@link java.util.Collections#emptyList()}.
     *
     * @param <A> the Iterable element type
     * @return a TraversableIterable wrapping Collections.emptyList()
     */
    public static <A> LambdaIterable<A> empty() {
        return wrap(emptyList());
    }
}
