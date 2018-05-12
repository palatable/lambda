package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link Set}s.
 */
public final class SetLens {

    private SetLens() {
    }

    /**
     * A lens that focuses on whether a {@link Set} contains some value <code>a</code>. Note that <code>copyFn</code> is
     * used to avoid mutating the {@link Set} in question.
     *
     * @param copyFn the copy function
     * @param a      the value in question
     * @param <A>    the value type
     * @param <SetA> the set to focus on
     * @return a lens that focuses on a value's inclusion in a given {@link Set}
     */
    public static <A, SetA extends Set<A>> Lens.Simple<SetA, Boolean> contains(
            Function<? super SetA, ? extends SetA> copyFn, A a) {
        return simpleLens(setA -> setA.contains(a),
                          (setA, include) -> {
                              SetA copy = copyFn.apply(setA);
                              if (include) copy.add(a);
                              else copy.remove(a);
                              return copy;
                          });
    }

    /**
     * A lens that focuses on whether a {@link Set} contains some value <code>a</code>. Like {@link #contains(Function,
     * Object)} but with an implicit copy function that produces <code>{@link HashSet}s</code>.
     *
     * @param a   the value in question
     * @param <A> the value type
     * @return a lens that focuses on a value's inclusion in a given {@link Set}
     */
    public static <A> Lens.Simple<Set<A>, Boolean> contains(A a) {
        return contains(HashSet::new, a);
    }
}
