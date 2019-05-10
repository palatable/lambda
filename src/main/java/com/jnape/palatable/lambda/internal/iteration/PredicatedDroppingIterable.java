package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Any.any;
import static java.util.Collections.singletonList;

public final class PredicatedDroppingIterable<A> implements Iterable<A> {
    private final List<Fn1<? super A, ? extends Boolean>> predicates;
    private final Iterable<A>                             as;

    public PredicatedDroppingIterable(Fn1<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        List<Fn1<? super A, ? extends Boolean>> predicates = new ArrayList<>(singletonList(predicate));
        while (as instanceof PredicatedDroppingIterable) {
            PredicatedDroppingIterable<A> nested = (PredicatedDroppingIterable<A>) as;
            as = nested.as;
            predicates.addAll(0, nested.predicates);
        }
        this.predicates = predicates;
        this.as = as;
    }

    @Override
    public Iterator<A> iterator() {
        Fn1<? super A, ? extends Boolean> metaPredicate = a -> any(p -> p.apply(a), predicates);
        return new PredicatedDroppingIterator<>(metaPredicate, as.iterator());
    }
}
