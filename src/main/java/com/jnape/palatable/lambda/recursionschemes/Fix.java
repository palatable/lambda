package com.jnape.palatable.lambda.recursionschemes;

import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.recursionschemes.builtin.Anamorphism;
import com.jnape.palatable.lambda.recursionschemes.builtin.Catamorphism;
import com.jnape.palatable.lambda.recursionschemes.builtin.Histomorphism;
import com.jnape.palatable.lambda.recursionschemes.builtin.Hylomorphism;
import com.jnape.palatable.lambda.recursionschemes.builtin.Paramorphism;
import com.jnape.palatable.lambda.recursionschemes.builtin.Zygomorphism;

import java.util.Objects;

/**
 * A type-level encoding of the least fixed point of a given functor; that is, given a <code>{@link
 * Functor}&lt;X&gt;</code> <code>f</code> and a value <code>x</code> of type <code>X</code>, <code>x</code> is the
 * least fixed point of <code>f</code> if, and only if, for all functions <code>fn</code>, <code>f.fmap(fn) ==
 * f</code>.
 * <p>
 * This encoding is foundational to the recursion schemes contained in this package, as it provides a generic,
 * arbitrarily deep recursive type signature corresponding to inductive and co-inductive types.
 * <p>
 * For more information, read about
 * <a href="https://www.schoolofhaskell.com/user/bartosz/understanding-algebras" target="_top">Fix</a>.
 *
 * @param <F>       the functor unification type
 * @param <Unfixed> the type corresponding to the unfixed functor
 * @see Anamorphism
 * @see Catamorphism
 * @see Histomorphism
 * @see Hylomorphism
 * @see Paramorphism
 * @see Zygomorphism
 */
@FunctionalInterface
public interface Fix<F extends Functor, Unfixed extends Functor<?, ? extends F>> {

    /**
     * Unfix the currently fixed functor.
     *
     * @return the unfixed functor
     */
    Unfixed unfix();

    /**
     * Fix a {@link Functor} f.
     *
     * @param unfixed   the unfixed functor
     * @param <F>       the functor unification type
     * @param <Unfixed> the type corresponding to the unfixed functor
     * @return the fixed functor
     */
    static <F extends Functor, Unfixed extends Functor<? extends Fix<F, ?>, F>> Fix<F, Unfixed> fix(Unfixed unfixed) {
        return new Fix<F, Unfixed>() {
            @Override
            public Unfixed unfix() {
                return unfixed;
            }

            @Override
            public boolean equals(Object obj) {
                return (obj instanceof Fix) && Objects.equals(unfixed, ((Fix) obj).unfix());
            }

            @Override
            public int hashCode() {
                return 31 * Objects.hashCode(unfixed);
            }

            @Override
            public String toString() {
                return "fix(" + unfixed + ")";
            }
        };
    }
}

