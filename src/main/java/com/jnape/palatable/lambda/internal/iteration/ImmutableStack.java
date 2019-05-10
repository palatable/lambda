package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.adt.Maybe;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

abstract class ImmutableStack<A> implements Iterable<A> {

    final ImmutableStack<A> push(A a) {
        return new Node<>(a, this);
    }

    abstract Maybe<A> head();

    abstract ImmutableStack<A> tail();

    final boolean isEmpty() {
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
        Maybe<A> head() {
            return Maybe.nothing();
        }

        @Override
        ImmutableStack<A> tail() {
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
        Maybe<A> head() {
            return Maybe.just(head);
        }

        @Override
        ImmutableStack<A> tail() {
            return tail;
        }
    }
}
