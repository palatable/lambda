package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.DropWhile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Any.any;
import static com.jnape.palatable.lambda.functions.builtin.fn2.DropWhile.dropWhile;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static java.util.Collections.singletonList;

public final class PredicatedDroppingIterable<A> implements Iterable<A> {
    private final List<Fn1<? super A, ? extends Boolean>> predicates;
    private final Iterable<A> as;

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
        return new PredicatedDroppingIterator<>(predicates, as.iterator());
    }
}
