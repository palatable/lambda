package com.jnape.palatable.lambda.internal;

import com.jnape.palatable.lambda.adt.Maybe;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * Internal API. Use at your own peril.
 */
public abstract class ImmutableStack<A> implements Iterable<A> {

    public final ImmutableStack<A> push(A a) {
        return new Node<>(a, this);
    }

    public abstract Maybe<A> head();

    public abstract ImmutableStack<A> tail();

    public final boolean isEmpty() {
        return head().fmap(constantly(false)).orElse(true);
    }

    @Override
    public Iterator<A> iterator() {
        return new Iterator<A>() {
            private ImmutableStack<A> stack = ImmutableStack.this;

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public A next() {
                A next = stack.head().orElseThrow(NoSuchElementException::new);
                stack = stack.tail();
                return next;
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <A> ImmutableStack<A> empty() {
        return (ImmutableStack<A>) Empty.INSTANCE;
    }

    private static final class Empty<A> extends ImmutableStack<A> {
        private static final Empty<?> INSTANCE = new Empty<>();

        @Override
        public Maybe<A> head() {
            return Maybe.nothing();
        }

        @Override
        public ImmutableStack<A> tail() {
            return this;
        }
    }

    private static final class Node<A> extends ImmutableStack<A> {
        private final A                 head;
        private final ImmutableStack<A> tail;

        public Node(A head, ImmutableStack<A> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public Maybe<A> head() {
            return Maybe.just(head);
        }

        @Override
        public ImmutableStack<A> tail() {
            return tail;
        }
    }
}
