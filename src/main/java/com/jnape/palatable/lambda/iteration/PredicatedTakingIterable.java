package com.jnape.palatable.lambda.iteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static java.util.Collections.singletonList;

public final class PredicatedTakingIterable<A> implements Iterable<A> {
    private final List<Function<? super A, ? extends Boolean>> predicates;
    private final Iterable<A>                                  as;

    public PredicatedTakingIterable(Function<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        List<Function<? super A, ? extends Boolean>> predicates = new ArrayList<>(singletonList(predicate));
        while (as instanceof PredicatedTakingIterable) {
            PredicatedTakingIterable<A> nested = (PredicatedTakingIterable<A>) as;
            predicates.addAll(0, nested.predicates);
            as = nested.as;
        }
        this.predicates = predicates;
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        Function<? super A, ? extends Boolean> metaPredicate = a -> all(p -> p.apply(a), predicates);
        return new PredicatedTakingIterator<>(metaPredicate, as.iterator());
    }
}
