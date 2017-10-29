package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.lens.Lens.lens;
import static com.jnape.palatable.lambda.lens.Lens.simpleLens;
import static com.jnape.palatable.lambda.lens.lenses.MaybeLens.unLiftA;

/**
 * Lenses that operate on {@link List}s.
 */
public final class ListLens {

    private ListLens() {
    }

    /**
     * Convenience static factory method for creating a lens over a copy of a list. Useful for composition to avoid
     * mutating a list reference.
     *
     * @param <X> the list element type
     * @return a lens that focuses on copies of lists
     */
    public static <X> Lens.Simple<List<X>, List<X>> asCopy() {
        return simpleLens(ArrayList::new, (xs, ys) -> ys);
    }

    /**
     * Convenience static factory method for creating a lens that focuses on an element in a list at a particular index.
     * Wraps result in a {@link Maybe} to handle null values or indexes that fall outside of list boundaries.
     *
     * @param index the index to focus on
     * @param <X>   the list element type
     * @return Maybe the element at the index
     */
    public static <X> Lens<List<X>, List<X>, Maybe<X>, X> elementAt(int index) {
        return lens(xs -> maybe(xs.size() > index ? xs.get(index) : null),
                    (xs, x) -> {
                        if (xs.size() > index)
                            xs.set(index, x);
                        return xs;
                    });
    }

    /**
     * Convenience static factory method for creating a lens that focuses on an element in a list at a particular index,
     * returning <code>defaultValue</code> if there is no value at that index.
     *
     * @param index        the index to focus on
     * @param defaultValue the value to use if there is no element at index
     * @param <X>          the list element type
     * @return the element at the index, or defaultValue
     */
    @SuppressWarnings("unchecked")
    public static <X> Lens.Simple<List<X>, X> elementAt(int index, X defaultValue) {
        return unLiftA(elementAt(index), defaultValue)::apply;
    }
}
