package com.jnape.palatable.lambda.iterators;

import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class ScanningIterator<A, B> extends ImmutableIterator<B> {

    private final Fn2<? super B, ? super A, ? extends B> scanner;
    private final Iterator<A>                            asIterator;
    private       B                                      b;

    public ScanningIterator(Fn2<? super B, ? super A, ? extends B> scanner, B b,
                            Iterator<A> asIterator) {
        this.scanner = scanner;
        this.b = b;
        this.asIterator = asIterator;
    }

    @Override
    public boolean hasNext() {
        return b != null;
    }

    @Override
    public B next() {
        if (b == null)
            throw new NoSuchElementException();

        B next = b;
        b = asIterator.hasNext() ? scanner.apply(b, asIterator.next()) : null;
        return next;
    }
}
