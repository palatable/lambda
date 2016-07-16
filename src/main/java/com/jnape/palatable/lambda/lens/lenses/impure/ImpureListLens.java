package com.jnape.palatable.lambda.lens.lenses.impure;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.List;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public final class ImpureListLens {

    private ImpureListLens() {
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
