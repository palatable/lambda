package com.jnape.palatable.lambda.internal;

import com.jnape.palatable.lambda.adt.Maybe;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

/**
 * Internal API. Use at your own peril.
 */
public abstract class ImmutableQueue<A> implements Iterable<A> {

    public abstract ImmutableQueue<A> pushFront(A a);

    public abstract ImmutableQueue<A> pushBack(A a);

    public abstract Maybe<A> head();

    public abstract ImmutableQueue<A> tail();

    public abstract ImmutableQueue<A> concat(ImmutableQueue<A> other);

    public final boolean isEmpty() {
        return head().fmap(constantly(false)).orElse(true);
    }

    public static <A> ImmutableQueue<A> singleton(A a) {
        return new NonEmpty<>(ImmutableStack.<A>empty().push(a), ImmutableStack.empty());
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
        public ImmutableQueue<A> pushFront(A a) {
            return new NonEmpty<>(ImmutableStack.<A>empty().push(a), ImmutableStack.empty());
        }

        @Override
        public ImmutableQueue<A> pushBack(A a) {
            return pushFront(a);
        }

        @Override
        public ImmutableQueue<A> concat(ImmutableQueue<A> other) {
            return other;
        }

        @Override
        public Maybe<A> head() {
            return Maybe.nothing();
        }

        @Override
        public ImmutableQueue<A> tail() {
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
        public ImmutableQueue<A> pushFront(A a) {
            return new NonEmpty<>(outbound.push(a), inbound);
        }

        @Override
        public ImmutableQueue<A> pushBack(A a) {
            return new NonEmpty<>(outbound, inbound.push(a));
        }

        @Override
        public ImmutableQueue<A> concat(ImmutableQueue<A> other) {
            return new NonEmpty<>(outbound, foldLeft(ImmutableStack::push, inbound, other));
        }

        @Override
        public Maybe<A> head() {
            return outbound.head();
        }

        @Override
        public ImmutableQueue<A> tail() {
            ImmutableStack<A> outTail = outbound.tail();
            if (!outTail.isEmpty())
                return new NonEmpty<>(outTail, inbound);

            ImmutableStack<A> newOutbound = foldLeft(ImmutableStack::push, ImmutableStack.empty(), inbound);
            return newOutbound.isEmpty() ? empty() : new NonEmpty<>(newOutbound, ImmutableStack.empty());
        }
    }
}
