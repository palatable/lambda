package com.jnape.palatable.lambda.iteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Any.any;
import static java.util.Collections.singletonList;

public final class PredicatedDroppingIterable<A> implements Iterable<A> {
    private final List<Function<? super A, Boolean>> predicates;
    private final Iterable<A>                        as;

    public PredicatedDroppingIterable(Function<? super A, Boolean> predicate, Iterable<A> as) {
        List<Function<? super A, Boolean>> predicates = new ArrayList<>(singletonList(predicate));

        while (as instanceof PredicatedDroppingIterable) {
            PredicatedDroppingIterable<A> nested = (PredicatedDroppingIterable<A>) as;
            as = nested.as;
            predicates.addAll(nested.predicates);
        }
        this.predicates = predicates;
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        Function<? super A, Boolean> metaPredicate = a -> any(p -> p.apply(a), predicates);
        return new PredicatedDroppingIterator<>(metaPredicate, as.iterator());
    }
}
