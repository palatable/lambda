package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functor.builtin.Lazy;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * An interface representing applicative functors - functors that can have their results combined with other functors
 * of the same instance in a context-free manner.
 * <p>
 * The same rules that apply to <code>Functor</code> apply to <code>Applicative</code>, along with the following
 * additional 4 laws:
 * <ul>
 * <li>identity: <code>v.zip(pureId).equals(v)</code></li>
 * <li>composition: <code>w.zip(v.zip(u.zip(pureCompose))).equals((w.zip(v)).zip(u))</code></li>
 * <li>homomorphism: <code>pureX.zip(pureF).equals(pureFx)</code></li>
 * <li>interchange: <code>pureY.zip(u).equals(u.zip(pure(f -&gt; f.apply(y))))</code></li>
 * </ul>
 * As with <code>Functor</code>, <code>Applicative</code> instances that do not satisfy all of the functor laws, as well
 * as the above applicative laws, are not well-behaved and often break down in surprising ways.
 * <p>
 * For more information, read about
 * <a href="https://en.wikipedia.org/wiki/Applicative_functor" target="_top">Applicative Functors</a>.
 *
 * @param <A>   The type of the parameter
 * @param <App> The unification parameter to more tightly type-constrain Applicatives to themselves
 */
public interface Applicative<A, App extends Applicative> extends Functor<A, App> {

    /**
     * Lift the value <code>b</code> into this applicative functor.
     *
     * @param b   the value
     * @param <B> the type of the returned applicative's parameter
     * @return an instance of this applicative over b
     */
    <B> Applicative<B, App> pure(B b);

    /**
     * Given another instance of this applicative over a mapping function, "zip" the two instances together using
     * whatever application semantics the current applicative supports.
     *
     * @param appFn the other applicative instance
     * @param <B>   the resulting applicative parameter type
     * @return the mapped applicative
     */
    <B> Applicative<B, App> zip(Applicative<Function<? super A, ? extends B>, App> appFn);

    /**
     * Given a {@link Lazy lazy} instance of this applicative over a mapping function, "zip" the two instances together
     * using whatever application semantics the current applicative supports. This is useful for applicatives that
     * support lazy evaluation and early termination.
     *
     * @param lazyAppFn the lazy other applicative instance
     * @param <B>       the resulting applicative parameter type
     * @return the mapped applicative
     * @see com.jnape.palatable.lambda.adt.Maybe
     * @see com.jnape.palatable.lambda.adt.Either
     */
    default <B> Lazy<? extends Applicative<B, App>> lazyZip(
            Lazy<Applicative<Function<? super A, ? extends B>, App>> lazyAppFn) {
        return lazyAppFn.fmap(this::zip);
    }

    @Override
    default <B> Applicative<B, App> fmap(Function<? super A, ? extends B> fn) {
        return zip(pure(fn));
    }

    /**
     * Sequence both this <code>Applicative</code> and <code>appB</code>, discarding this <code>Applicative's</code>
     * result and returning <code>appB</code>. This is generally useful for sequentially performing side-effects.
     *
     * @param appB the other Applicative
     * @param <B>  the type of the returned Applicative's parameter
     * @return appB
     */
    default <B> Applicative<B, App> discardL(Applicative<B, App> appB) {
        return appB.zip(fmap(constantly(id())));
    }

    /**
     * Sequence both this <code>Applicative</code> and <code>appB</code>, discarding <code>appB's</code> result and
     * returning this <code>Applicative</code>. This is generally useful for sequentially performing side-effects.
     *
     * @param appB the other Applicative
     * @param <B>  the type of appB's parameter
     * @return this Applicative
     */
    default <B> Applicative<A, App> discardR(Applicative<B, App> appB) {
        return appB.zip(fmap(constantly()));
    }

    /**
     * Convenience method for coercing this applicative instance into another concrete type. Unsafe.
     *
     * @param <Concrete> the concrete applicative instance to coerce this applicative to
     * @return the coerced applicative
     */
    @SuppressWarnings("unchecked")
    default <Concrete extends Applicative<A, App>> Concrete coerce() {
        return (Concrete) this;
    }
}
