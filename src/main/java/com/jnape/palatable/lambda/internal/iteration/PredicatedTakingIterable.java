package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static java.util.Collections.singletonList;

public final class PredicatedTakingIterable<A> implements Iterable<A> {
    private final List<Fn1<? super A, ? extends Boolean>> predicates;
    private final Iterable<A>                             as;

    public PredicatedTakingIterable(Fn1<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        List<Fn1<? super A, ? extends Boolean>> predicates = new ArrayList<>(singletonList(predicate));
        while (as instanceof PredicatedTakingIterable<A> nested) {
            predicates.addAll(0, nested.predicates);
            as = nested.as;
        }
        this.predicates = predicates;
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        Fn1<? super A, ? extends Boolean> metaPredicate = a -> all(p -> p.apply(a), predicates);
        return new PredicatedTakingIterator<>(metaPredicate, as.iterator());
    }
}
