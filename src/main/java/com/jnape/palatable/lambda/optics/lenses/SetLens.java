package com.jnape.palatable.lambda.optics.lenses;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Lens;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.jnape.palatable.lambda.optics.Lens.simpleLens;

/**
 * Lenses that operate on {@link Set}s.
 */
public final class SetLens {

    private SetLens() {
    }

    /**
     * A lens that focuses on whether a {@link Set} contains all values in <code>as</code>. Note that
     * <code>copyFn</code> is used to avoid mutating the {@link Set} in question.
     *
     * @param copyFn the copy function
     * @param as     the values in question
     * @param <A>    the value type
     * @param <SetA> the set to focus on
     * @return a lens that focuses on the inclusion of a {@link Collection} of values in a given {@link Set}
     */
    public static <A, SetA extends Set<A>> Lens.Simple<SetA, SetA> intersection(
            Fn1<? super SetA, ? extends SetA> copyFn,
            Set<A> as) {
        return simpleLens(setA -> {
                              SetA intersection = copyFn.apply(setA);
                              intersection.retainAll(as);
                              return intersection;
                          },
                          (setA, setB) -> {
                              SetA copy = copyFn.apply(setA);
                              copy.retainAll(setB);
                              copy.retainAll(as);
                              return copy;
                          });
    }

    /**
     * A lens that focuses on whether a {@link Set} contains some value <code>a</code>. Like
     * {@link #intersection(Fn1, Set)} )} but with an implicit copy function that produces
     * <code>{@link HashSet}s</code>.
     *
     * @param as  the values in question
     * @param <A> the value type
     * @return a lens that focuses on the inclusion of a {@link Collection} of values in a given {@link Set}
     */
    public static <A> Lens.Simple<Set<A>, Set<A>> intersection(Set<A> as) {
        return intersection(HashSet::new, as);
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
    public static <A, SetA extends Set<A>> Lens.Simple<SetA, Boolean> contains(Fn1<? super SetA, ? extends SetA> copyFn,
                                                                               A a) {
        return simpleLens(setA -> setA.contains(a),
                          (setA, include) -> {
                              SetA copy = copyFn.apply(setA);
                              if (include) copy.add(a);
                              else copy.remove(a);
                              return copy;
                          });
    }

    /**
     * A lens that focuses on whether a {@link Set} contains some value <code>a</code>. Like
     * {@link #contains(Fn1, Object)} but with an implicit copy function that produces <code>{@link HashSet}s</code>.
     *
     * @param a   the value in question
     * @param <A> the value type
     * @return a lens that focuses on a value's inclusion in a given {@link Set}
     */
    public static <A> Lens.Simple<Set<A>, Boolean> contains(A a) {
        return contains(HashSet::new, a);
    }
}
