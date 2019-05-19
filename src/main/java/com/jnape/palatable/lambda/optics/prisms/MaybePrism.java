package com.jnape.palatable.lambda.optics.prisms;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.optics.Prism;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.optics.Prism.prism;
import static com.jnape.palatable.lambda.optics.Prism.simplePrism;

/**
 * {@link Prism Prisms} for {@link Maybe}.
 */
public final class MaybePrism {

    private MaybePrism() {
    }

    /**
     * A {@link Prism} that focuses on present values in a {@link Maybe}.
     *
     * @param <A> {@link Maybe} the input value
     * @param <B> {@link Maybe} the output value
     * @return the {@link Prism}
     */
    public static <A, B> Prism<Maybe<A>, Maybe<B>, A, B> _just() {
        return prism(maybeA -> maybeA.toEither(Maybe::nothing), Maybe::just);
    }

    /**
     * A {@link Prism} that focuses on absent values in a {@link Maybe}, for symmetry.
     *
     * @param <A> {@link Maybe} the input and output value
     * @return the {@link Prism}
     */
    public static <A> Prism.Simple<Maybe<A>, Unit> _nothing() {
        return simplePrism(CoProduct2::projectA, constantly(nothing()));
    }
}
