package com.jnape.palatable.lambda.semigroup;

import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft;
import com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;

/**
 * A <code>Semigroup</code> is a closed, associative category. As closure can be implied by the type signature, and
 * associativity is not enforceable, this is simply represented as a binary operator.
 *
 * @param <A> The element type this Semigroup is formed over
 */
@FunctionalInterface
public interface Semigroup<A> extends Fn2<A, A, A> {

    /**
     * Catamorphism under this semigroup using {@link FoldLeft}, where the binary operator is this semigroup, and the
     * starting accumulator is provided.
     *
     * @param a  the starting accumulator
     * @param as the elements to fold over
     * @return the folded result
     * @see FoldLeft
     */
    default A foldLeft(A a, Iterable<A> as) {
        return FoldLeft.foldLeft(toBiFunction(), a, as);
    }

    /**
     * Catamorphism under this semigroup using {@link FoldRight}, where the binary operator is this semigroup, and the
     * starting accumulator is provided.
     *
     * @param a  the starting accumulator
     * @param as the elements to fold over
     * @return the folded result
     * @see FoldRight
     */
    default Lazy<A> foldRight(A a, Iterable<A> as) {
        return FoldRight.foldRight((y, lazyX) -> lazyX.fmap(x -> apply(x, y)), lazy(a), as);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Semigroup<A> flip() {
        return Fn2.super.flip()::apply;
    }
}
