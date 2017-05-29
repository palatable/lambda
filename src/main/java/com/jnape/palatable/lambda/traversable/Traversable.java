package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;

/**
 * An interface for a class of data structures that can be "traversed from left to right" in a structure-preserving
 * way, successively applying some applicative computation to each element and collapsing the results into a single
 * resulting applicative.
 * <p>
 * The same rules that apply to <code>Functor</code> apply to <code>Traversable</code>, along with the following
 * additional 3 laws:
 * <ul>
 * <li>naturality: <code>t.apply(trav.traverse(f, pure).&lt;Object&gt;fmap(id()).coerce())
 * .equals(trav.traverse(t.compose(f), pure2).&lt;Object&gt;fmap(id()).coerce())</code></li>
 * <li>identity: <code>trav.traverse(Identity::new, x -&gt; new Identity&lt;&gt;(x)).equals(new
 * Identity&lt;&gt;(trav)</code></li>
 * <li>composition: <code>trav.traverse(f.andThen(x -&gt; x.fmap(g)).andThen(Compose::new), x -&gt;
 * new Compose&lt;&gt;(new Identity&lt;&gt;(new Identity&lt;&gt;(x)))).equals(new Compose&lt;Identity, Identity,
 * Traversable&lt;Object, Trav&gt;&gt;(trav.traverse(f, x -&gt; new Identity&lt;&gt;(x)).fmap(t -&gt;
 * t.traverse(g, x -&gt; new Identity&lt;&gt;(x)))))</code></li>
 * </ul>
 * <p>
 * For more information, read about
 * <a href="https://hackage.haskell.org/package/base-4.9.1.0/docs/Data-Traversable.html" target="_top">Traversables</a>.
 *
 * @param <A> The type of the parameter
 * @param <T> The unification parameter
 */
public interface Traversable<A, T extends Traversable> extends Functor<A, T> {

    /**
     * Apply <code>fn</code> to each element of this traversable from left to right, and collapse the results into
     * a single resulting applicative, potentially with the assistance of the applicative's pure function.
     *
     * @param fn    the function to apply
     * @param pure  the applicative pure function
     * @param <B>   the resulting element type
     * @param <App> the result applicative type
     * @return the traversed Traversable, wrapped inside an applicative
     */
    <B, App extends Applicative> Applicative<? extends Traversable<B, T>, App> traverse(
            Function<? super A, ? extends Applicative<B, App>> fn,
            Function<? super Traversable<B, T>, ? extends Applicative<? extends Traversable<B, T>, App>> pure);

    @Override
    <B> Traversable<B, T> fmap(Function<? super A, ? extends B> fn);
}
