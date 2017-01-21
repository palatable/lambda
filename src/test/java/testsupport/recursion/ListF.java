package testsupport.recursion;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;

public abstract class ListF<A, B> implements Functor<B, ListF<A, ?>>, CoProduct2<ListF.Nil<A, B>, ListF.Cons<A, B>> {

    public static <A, B> ListF<A, B> nil() {
        return new Nil<>();
    }

    public static <A, B> ListF<A, B> cons(A head, B tail) {
        return new Cons<>(head, tail);
    }

    public static final class Nil<A, B> extends ListF<A, B> {

        @Override
        @SuppressWarnings("unchecked")
        public <C> ListF<A, C> fmap(Function<? super B, ? extends C> fn) {
            return (Nil<A, C>) this;
        }

        @Override
        public <R> R match(Function<? super Nil<A, B>, ? extends R> aFn,
                           Function<? super Cons<A, B>, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    public static final class Cons<A, B> extends ListF<A, B> {

        private final A head;
        private final B tail;

        private Cons(A head, B tail) {
            this.head = head;
            this.tail = tail;
        }

        public A head() {
            return head;
        }

        public B tail() {
            return tail;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <C> ListF<A, C> fmap(Function<? super B, ? extends C> fn) {
            return new Cons<>(head, fn.apply(tail));
        }

        @Override
        public <R> R match(Function<? super Nil<A, B>, ? extends R> aFn,
                           Function<? super Cons<A, B>, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }
}
