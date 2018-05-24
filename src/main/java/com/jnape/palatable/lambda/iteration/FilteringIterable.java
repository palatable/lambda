package com.jnape.palatable.lambda.iteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static java.util.Collections.singletonList;

public final class FilteringIterable<A> implements Iterable<A> {
    private final List<Function<? super A, Boolean>> predicates;
    private final Iterable<A>                        as;

    public FilteringIterable(Function<? super A, Boolean> predicate, Iterable<A> as) {
        List<Function<? super A, Boolean>> predicates = new ArrayList<>(singletonList(predicate));
        while (as instanceof FilteringIterable) {
            FilteringIterable<A> nested = (FilteringIterable<A>) as;
            predicates.addAll(0, nested.predicates);
            as = nested.as;
        }
        this.predicates = predicates;
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        Function<? super A, Boolean> metaPredicate = a -> all(p -> p.apply(a), predicates);
        return new FilteringIterator<>(metaPredicate, as.iterator());
    }
}
