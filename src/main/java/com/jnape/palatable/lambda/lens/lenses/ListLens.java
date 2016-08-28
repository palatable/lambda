package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jnape.palatable.lambda.lens.Lens.lens;
import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

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
     * Wraps result in an Optional to handle null values or indexes that fall outside of list boundaries.
     *
     * @param index the index to focus on
     * @param <X>   the list element type
     * @return an Optional wrapping the element at the index
     */
    public static <X> Lens<List<X>, List<X>, Optional<X>, X> at(int index) {
        return lens(xs -> Optional.ofNullable(xs.size() > index ? xs.get(index) : null),
                    (xs, x) -> {
                        if (xs.size() > index)
                            xs.set(index, x);
                        return xs;
                    });
    }
}
