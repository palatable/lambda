package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * A heterogeneous list supporting arbitrary depth type-safety via a linearly recursive type signature. Note that due to
 * its rapidly expanding type signature, specializations exist up to certain depths to minimize typing overhead.
 *
 * @param <Head> The head element type
 * @param <Tail> The encoded recursive tail HList type
 */
public abstract class HList<Head, Tail extends HList<?, ?>> {

    private HList() {
    }

    public abstract <NewHead> HCons<NewHead, ? extends HList<Head, Tail>> cons(NewHead newHead);

    public static HNil nil() {
        return HNil.INSTANCE;
    }

    public static <Head, Tail extends HList<?, ?>> HCons<Head, Tail> cons(Head head, Tail tail) {
        return new HCons<>(head, tail);
    }

    public static <Head> HCons1<Head> list(Head head) {
        return new HCons1<>(head);
    }

    public static <H1, H2> HCons2<H1, H2> list(H1 h1, H2 h2) {
        return list(h2).cons(h1);
    }

    public static <H1, H2, H3> HCons3<H1, H2, H3> list(H1 h1, H2 h2, H3 h3) {
        return list(h2, h3).cons(h1);
    }

    public static <H1, H2, H3, H4> HCons4<H1, H2, H3, H4> list(H1 h1, H2 h2, H3 h3, H4 h4) {
        return list(h2, h3, h4).cons(h1);
    }

    public static <H1, H2, H3, H4, H5> HCons5<H1, H2, H3, H4, H5> list(H1 h1, H2 h2, H3 h3, H4 h4, H5 h5) {
        return list(h2, h3, h4, h5).cons(h1);
    }

    public static final class HCons5<A, B, C, D, E> extends HCons<A, HCons4<B, C, D, E>> {
        private HCons5(A a, HCons4<B, C, D, E> tail) {
            super(a, tail);
        }

        @Override
        public <NewHead> HCons<NewHead, HCons5<A, B, C, D, E>> cons(NewHead newHead) {
            return new HCons<>(newHead, this);
        }
    }

    public static final class HCons4<A, B, C, D> extends HCons<A, HCons3<B, C, D>> {
        private HCons4(A a, HCons3<B, C, D> tail) {
            super(a, tail);
        }

        @Override
        public <NewHead> HCons5<NewHead, A, B, C, D> cons(NewHead newHead) {
            return new HCons5<>(newHead, this);
        }
    }

    public static final class HCons3<A, B, C> extends HCons<A, HCons2<B, C>> {
        private HCons3(A a, HCons2<B, C> tail) {
            super(a, tail);
        }

        @Override
        public <NewHead> HCons4<NewHead, A, B, C> cons(NewHead newHead) {
            return new HCons4<>(newHead, this);
        }
    }

    public static final class HCons2<A, B> extends HCons<A, HCons1<B>> {
        public HCons2(A a, HCons1<B> tail) {
            super(a, tail);
        }

        @Override
        public <NewHead> HCons3<NewHead, A, B> cons(NewHead newHead) {
            return new HCons3<>(newHead, this);
        }
    }

    public static final class HCons1<Head> extends HCons<Head, HNil> {
        private HCons1(Head head) {
            super(head, nil());
        }

        @Override
        public <NewHead> HCons2<NewHead, Head> cons(NewHead newHead) {
            return new HCons2<>(newHead, this);
        }
    }

    public static class HCons<Head, Tail extends HList<?, ?>> extends HList<Head, Tail> implements Functor<Head> {
        private final Head head;
        private final Tail tail;

        private HCons(Head head, Tail tail) {
            this.head = head;
            this.tail = tail;
        }

        public Head head() {
            return head;
        }

        public Tail tail() {
            return tail;
        }

        @Override
        public <NewHead> HCons<NewHead, ? extends HCons<Head, Tail>> cons(NewHead newHead) {
            return new HCons<>(newHead, this);
        }

        @Override
        public <NewHead> HCons<NewHead, Tail> fmap(MonadicFunction<? super Head, ? extends NewHead> fn) {
            return new HCons<>(fn.apply(head), tail);
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof HCons) {
                HCons that = (HCons) other;
                boolean sameHead = this.head.equals(that.head);
                boolean sameTail = this.tail.equals(that.tail);
                return sameHead && sameTail;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 31 * head.hashCode() + tail.hashCode();
        }

        @Override
        public String toString() {
            return "HCons{" +
                    "head=" + head +
                    ", tail=" + tail +
                    '}';
        }
    }

    public static final class HNil extends HList<Void, HNil> {
        private static final HNil INSTANCE = new HNil();

        @Override
        public <NewHead> HCons<NewHead, HNil> cons(NewHead newHead) {
            return new HCons<>(newHead, this);
        }

        @Override
        public String toString() {
            return "HNil{}";
        }
    }
}
