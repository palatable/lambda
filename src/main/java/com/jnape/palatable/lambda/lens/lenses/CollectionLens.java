package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

public class CollectionLens {

    public static <X, CX extends Collection<X>> Lens.Simple<CX, CX> asCopy(Function<? super CX, ? extends CX> copyFn) {
        return simpleLens(copyFn, (__, copy) -> copy);
    }

    public static <X, CX extends Collection<X>> Lens.Simple<CX, Set<X>> asSet() {
        return simpleLens(HashSet::new, (xsL, xsS) -> {
            xsL.retainAll(xsS);
            return xsL;
        });
    }

    public static <X, CX extends Collection<X>> Lens.Simple<CX, Stream<X>> asStream() {
        return simpleLens(Collection::stream, (xsL, xsS) -> {
            xsL.clear();
            xsS.forEach(xsL::add);
            return xsL;
        });
    }
}
