package com.jnape.palatable.lambda.internal.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.max;

public final class SplicingIterator<A> extends ImmutableIterator<A> {
    private enum Phase {FRONT, SPLICE, BACK}

    private final Iterator<A> original;
    private final Iterator<A> replacement;
    private final int startIndex;
    private int replaceCount;
    private Phase phase;
    private int currentIndex;
    private A nextElement;

    public SplicingIterator(int startIndex, int replaceCount, Iterator<A> replacement, Iterator<A> original) {
        this.original = original;
        this.replacement = replacement;
        this.startIndex = max(0, startIndex);
        this.replaceCount = max(0, replaceCount);
        this.phase = Phase.FRONT;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        if (nextElement == null) {
            nextElement = readNextElement();
        }
        return nextElement != null;
    }

    @Override
    public A next() {
        if (hasNext()) {
            A result = nextElement;
            nextElement = null;
            return result;
        } else {
            throw new NoSuchElementException();
        }
    }

    private A readNextElement() {
        if (phase == Phase.FRONT) {
            if (currentIndex < startIndex && original.hasNext()) {
                currentIndex++;
                return original.next();
            } else {
                phase = Phase.SPLICE;
            }
        }

        if (phase == Phase.SPLICE) {
            if (replacement.hasNext()) {
                return replacement.next();
            } else {
                while (original.hasNext() && replaceCount > 0) {
                    original.next();
                    replaceCount--;
                }
                phase = Phase.BACK;
            }
        }

        if (original.hasNext()) {
            return original.next();
        } else {
            return null;
        }
    }
}
