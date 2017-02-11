package com.jnape.palatable.lambda.recursionschemes;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functor.Functor;
import fix.Coalgebra;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Anamorphism.ana;

/**
 * The free monad. <code>Free</code> can be thought of as adding the capabilities of a pattern functor to a functor
 * that has no zero construction -- that is, a functor that has no constructions for which <code>f.fmap(fn) ==
 * f</code>.
 * <p>
 * Consider the example where some functor <code>f</code> has no zero construction; fixing <code>f</code> is rendered
 * impossible by construction, since there can be no terminating expression. Conversely, {@link Free#pure} provides an
 * escape-hatch to terminate the expression recursion, allowing construction of fix-like expressions for functors that
 * otherwise would not support this.
 *
 * @param <A>     the type of the expression-terminating pure value
 * @param <F>     the functor unification type
 * @param <FreeF> the type corresponding to the functor that wraps Free
 */
public final class Free<A, F extends Functor, FreeF extends Functor<?, F>> implements CoProduct2<A, FreeF> {
    private final Choice2<A, FreeF> choice;

    private Free(Choice2<A, FreeF> choice) {
        this.choice = choice;
    }

    /**
     * Given a {@link Coalgebra}, recursively fold this <code>Free</code> into a {@link Fix}. Note that this is only
     * achievable for pattern functors, as the pure anamorphism will only terminate once a zero construction is reached.
     *
     * @param coalgebra the coalgebra to use to convert a pure value into a Fix
     * @param <FA>      the functor type the coalgebra unwraps the value from
     * @return the fix version of this free
     */
    @SuppressWarnings("unchecked")
    public <FA extends Functor<A, F>> Fix<F, ? extends Functor<? extends Fix<F, ?>, F>> fix(
            Coalgebra<A, FA> coalgebra) {
        return match(a -> ana(coalgebra, a),
                     freeF -> Fix.fix(freeF.fmap(x1 -> ((Free<A, F, FreeF>) x1).fix(coalgebra))));
    }

    @Override
    public <R> R match(Function<? super A, ? extends R> aFn, Function<? super FreeF, ? extends R> bFn) {
        return choice.match(aFn, bFn);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Free && choice.equals(((Free) other).choice);
    }

    @Override
    public int hashCode() {
        return choice.hashCode();
    }

    /**
     * The recursive expression in free construction, using a functor.
     *
     * @param f       the functor
     * @param <A>     the pure parameter type
     * @param <F>     the functor unification type
     * @param <FreeF> the type corresponding to the functor that wraps Free
     * @return an instance of Free over this functor
     */
    public static <A, F extends Functor, FreeF extends Functor<? extends Free<A, F, ?>, F>> Free<A, F, FreeF> free(
            FreeF f) {
        return new Free<>(b(f));
    }

    /**
     * The terminating expression in free construction, using a value.
     *
     * @param a       the pure value
     * @param <A>     the pure parameter type
     * @param <F>     the functor unification type
     * @param <FreeF> the type corresponding to the functor that wraps Free
     * @return an instance of Free over this value
     */
    public static <A, F extends Functor, FreeF extends Functor<?, F>> Free<A, F, FreeF> pure(A a) {
        return new Free<>(a(a));
    }
}
