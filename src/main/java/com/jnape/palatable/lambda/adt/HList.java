package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.applicative.Functor;
import com.jnape.palatable.lambda.functions.MonadicFunction;

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

    public static <Head> HCons<Head, HNil> singleton(Head head) {
        return cons(head, nil());
    }

    public static <H1, H2> HCons<H1, HCons<H2, HNil>> list(H1 h1, H2 h2) {
        return singleton(h2).cons(h1);
    }

    public static <H1, H2, H3> HCons<H1, HCons<H2, HCons<H3, HNil>>> list(H1 h1, H2 h2, H3 h3) {
        return list(h2, h3).cons(h1);
    }

    public static <H1, H2, H3, H4> HCons<H1, HCons<H2, HCons<H3, HCons<H4, HNil>>>> list(H1 h1, H2 h2, H3 h3,
                                                                                         H4 h4) {
        return list(h2, h3, h4).cons(h1);
    }

    public static <H1, H2, H3, H4, H5> HCons<H1, HCons<H2, HCons<H3, HCons<H4, HCons<H5, HNil>>>>> list(H1 h1, H2 h2,
                                                                                                        H3 h3, H4 h4,
                                                                                                        H5 h5) {
        return list(h2, h3, h4, h5).cons(h1);
    }

    public static final class HCons<Head, Tail extends HList<?, ?>> extends HList<Head, Tail> implements Functor<Head> {
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
        public <NewHead> HCons<NewHead, HCons<Head, Tail>> cons(NewHead newHead) {
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
