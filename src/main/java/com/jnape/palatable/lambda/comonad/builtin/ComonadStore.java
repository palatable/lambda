package com.jnape.palatable.lambda.comonad.builtin;

import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Store;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Downcast.downcast;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Upcast.upcast;

/**
 * An interface for an Store {@link Comonad}, which can be thought of as providing a lookup function and a current input.
 * A concrete class is provided in {@link Store}.
 *
 * @param <S> the type of the storage and the cursor
 * @param <A> the type of the value produced
 */
public interface ComonadStore<S, A, W extends Comonad<?, W>> extends Comonad<A, W> {
    /**
     * Retrieve a value from a mapped cursor.
     *
     * @param f the relative change to make to the cursor before retrieving a value
     * @return  a retrieved value A
     */
    A peeks(Fn1<? super S, ? extends S> f);

    /**
     * Retrieve a value from a cursor.
     *
     * @param s the cursor to extract the value from
     * @return  a retrieved value A
     */
    default A peek(S s) {
        return peeks(constantly(s));
    };

    /**
     * Retrieve a value from the current cursor.
     *
     * @return  a retrieved value A
     */
    default A pos() {
        return peeks(id());
    }

    /**
     * Produce a new store by providing a relative change to the cursor.
     *
     * This can be implemented using the Comonad implementation as:
     * <code>return downcast(this.extend((Fn1&lt;WA, A&gt;) s -&gt; s.peeks(f)));</code>
     * but is left unimplemented in the interface to prevent conflicts from using `seeks` to implement {@link Comonad#extendImpl}.
     *
     * @param f the relative change to make to the cursor before retrieving a value
     * @return  a new ComonadStore with updated cursor
     */
    ComonadStore<S, A, W> seeks(Fn1<? super S, ? extends S> f);

    /**
     * Produce a new store at a given cursor.
     *
     * @param s the cursor to extract the value from
     * @return  a new ComonadStore with updated cursor
     */
    default ComonadStore<S, A, W> seek(S s) {
        return seeks(constantly(s));
    };

    /**
     * Retrieve a {@link Functor} of value(s) by perturbing the cursor.
     *
     * @param <F> the functor to be mapped over
     * @param f   the function for perturbing the cursor
     * @return    a Functor F of value(s) A
     */
    <F extends Functor<?, F>> Functor<A, F> experiment(Fn1<? super S, ? extends Functor<S, F>> f);

    /**
     * {@inheritDoc}
     */
    @Override
    default A extract() {
        return pos();
    }
}
