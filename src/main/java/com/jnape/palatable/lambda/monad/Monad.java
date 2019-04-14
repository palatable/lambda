package com.jnape.palatable.lambda.monad;

import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * Monads are {@link Applicative} functors that support a flattening operation to unwrap <code>M&lt;M&lt;A&gt;&gt;
 * -&gt; M&lt;A&gt;</code>. This flattening operation, coupled with {@link Applicative#zip(Applicative)}, gives rise to
 * {@link Monad#flatMap(Function)}, a binding operation that maps the carrier value to a new monad instance in the same
 * category, and then unwraps the outer layer.
 * <p>
 * In addition to the applicative laws, there are 3 specific monad laws that monads should obey:
 * <ul>
 * <li>left identity: <code>m.pure(a).flatMap(fn).equals(fn.apply(a))</code></li>
 * <li>right identity: <code>m.flatMap(m::pure).equals(m)</code></li>
 * <li>associativity: <code>m.flatMap(f).flatMap(g).equals(m.flatMap(a -&gt; f.apply(a).flatMap(g)))</code></li>
 * </ul>
 * <p>
 * For more information, read about
 * <a href="https://en.wikipedia.org/wiki/Monad_(functional_programming)" target="_top">Monads</a>.
 *
 * @param <A> the type of the parameter
 * @param <M> the unification parameter to more tightly type-constrain Monads to themselves
 */
public interface Monad<A, M extends Monad<?, M>> extends Applicative<A, M> {

    /**
     * Chain dependent computations that may continue or short-circuit based on previous results.
     *
     * @param f   the dependent computation over A
     * @param <B> the resulting monad parameter type
     * @return the new monad instance
     */
    <B> Monad<B, M> flatMap(Function<? super A, ? extends Monad<B, M>> f);

    /**
     * {@inheritDoc}
     */
    @Override
    <B> Monad<B, M> pure(B b);

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> Monad<B, M> fmap(Function<? super A, ? extends B> fn) {
        return flatMap(fn.andThen(this::pure));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> Monad<B, M> zip(Applicative<Function<? super A, ? extends B>, M> appFn) {
        return appFn.<Monad<Function<? super A, ? extends B>, M>>coerce().flatMap(this::fmap);
    }

    /**
     * {@inheritDoc}
     * @param lazyAppFn
     */
    @Override
    default <B> Lazy<? extends Monad<B, M>> lazyZip(
            Lazy<? extends Applicative<Function<? super A, ? extends B>, M>> lazyAppFn) {
        return Applicative.super.lazyZip(lazyAppFn).fmap(Applicative<B, M>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> Monad<B, M> discardL(Applicative<B, M> appB) {
        return Applicative.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> Monad<A, M> discardR(Applicative<B, M> appB) {
        return Applicative.super.discardR(appB).coerce();
    }

    /**
     * Convenience static method equivalent to {@link Monad#flatMap(Function) flatMap}{@link Id#id() (id())};
     *
     * @param mma  the outer monad
     * @param <M>  the monad type
     * @param <A>  the nested type parameter
     * @param <MA> the nested monad
     * @return the nested monad
     */
    static <M extends Monad<?, M>, A, MA extends Monad<A, M>> MA join(Monad<? extends MA, M> mma) {
        return mma.flatMap(id()).coerce();
    }
}