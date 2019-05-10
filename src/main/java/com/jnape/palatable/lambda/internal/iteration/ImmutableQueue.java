package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.adt.Maybe;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

abstract class ImmutableQueue<A> implements Iterable<A> {

    abstract ImmutableQueue<A> pushFront(A a);

    abstract ImmutableQueue<A> pushBack(A a);

    abstract Maybe<A> head();

    abstract ImmutableQueue<A> tail();

    abstract ImmutableQueue<A> concat(ImmutableQueue<A> other);

    final boolean isEmpty() {
        return head().fmap(constantly(false)).orElse(true);
    }

    @Override
    public Iterator<A> iterator() {
        return new Iterator<A>() {
            private ImmutableQueue<A> queue = ImmutableQueue.this;

            @Override
            public boolean hasNext() {
                return !queue.isEmpty();
            }

            @Override
            public A next() {
                A next = queue.head().orElseThrow(NoSuchElementException::new);
                queue = queue.tail();
                return next;
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <A> ImmutableQueue<A> empty() {
        return (ImmutableQueue<A>) Empty.INSTANCE;
    }

    private static final class Empty<A> extends ImmutableQueue<A> {
        private static final Empty<?> INSTANCE = new Empty<>();

        @Override
        ImmutableQueue<A> pushFront(A a) {
            return new NonEmpty<>(ImmutableStack.<A>empty().push(a), ImmutableStack.empty());
        }

        @Override
        ImmutableQueue<A> pushBack(A a) {
            return pushFront(a);
        }

        @Override
        ImmutableQueue<A> concat(ImmutableQueue<A> other) {
            return other;
        }

        @Override
        Maybe<A> head() {
            return Maybe.nothing();
        }

        @Override
        ImmutableQueue<A> tail() {
            return this;
        }
    }

    private static final class NonEmpty<A> extends ImmutableQueue<A> {
        private final ImmutableStack<A> outbound;
        private final ImmutableStack<A> inbound;

        private NonEmpty(ImmutableStack<A> outbound, ImmutableStack<A> inbound) {
            this.outbound = outbound;
            this.inbound = inbound;
        }

        @Override
        ImmutableQueue<A> pushFront(A a) {
            return new NonEmpty<>(outbound.push(a), inbound);
        }

        @Override
        ImmutableQueue<A> pushBack(A a) {
            return new NonEmpty<>(outbound, inbound.push(a));
        }

        @Override
        ImmutableQueue<A> concat(ImmutableQueue<A> other) {
            return new NonEmpty<>(outbound, foldLeft(ImmutableStack::push, inbound, other));
        }

        @Override
        Maybe<A> head() {
            return outbound.head();
        }

        @Override
        ImmutableQueue<A> tail() {
            ImmutableStack<A> outTail = outbound.tail();
            if (!outTail.isEmpty())
                return new NonEmpty<>(outTail, inbound);

            ImmutableStack<A> newOutbound = foldLeft(ImmutableStack::push, ImmutableStack.empty(), inbound);
            return newOutbound.isEmpty() ? empty() : new NonEmpty<>(newOutbound, ImmutableStack.empty());
        }
    }
}
