package com.jnape.palatable.lambda.iteration;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

public class CombinatorialIterator<A, B> extends ImmutableIterator<Tuple2<A, B>> {
    private final Iterator<A>     asIterator;
    private final Iterator<B>     bsIterator;
    private final ListIterator<B> doublyLinkedBsIterator;
    private       A               currentA;

    public CombinatorialIterator(Iterator<A> asIterator, Iterator<B> bsIterator) {
        this.asIterator = asIterator;
        this.bsIterator = bsIterator;
        this.doublyLinkedBsIterator = new ArrayList<B>().listIterator();
        currentA = null;
    }

    @Override
    public boolean hasNext() {
        return (moreAs() || currentA != null) && moreBs();
    }

    @Override
    public Tuple2<A, B> next() {
        if (currentA == null)
            currentA = asIterator.next();

        if (bsIterator.hasNext()) {
            doublyLinkedBsIterator.add(bsIterator.next());
            doublyLinkedBsIterator.previous();
        }

        Tuple2<A, B> tuple = tuple(currentA, doublyLinkedBsIterator.next());

        if (moreAs() && !moreBs()) {
            currentA = asIterator.next();
            while (doublyLinkedBsIterator.hasPrevious())
                doublyLinkedBsIterator.previous();
        }

        return tuple;
    }

    private boolean moreAs() {
        return asIterator.hasNext();
    }

    private boolean moreBs() {
        return bsIterator.hasNext() || doublyLinkedBsIterator.hasNext();
    }
}
