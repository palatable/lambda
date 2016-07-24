package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public final class ListLens {

    private ListLens() {
    }

    public static <X> Lens.Simple<List<X>, List<X>> asCopy() {
        return simpleLens(ArrayList::new, (xs, ys) -> ys);
    }

    public static <X> Lens.Simple<List<X>, X> at(int index) {
        return simpleLens(xs -> xs.size() > index ? xs.get(index) : null,
                          (xs, x) -> {
                              if (xs.size() > index)
                                  xs.set(index, x);
                              return xs;
                          });
    }
}
