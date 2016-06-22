package com.jnape.palatable.lambda.adt.hlist;

import java.util.Objects;

/**
 * A heterogeneous list supporting arbitrary depth type-safety via a linearly recursive type signature. Note that due to
 * its rapidly expanding type signature, specializations exist up to certain depths to minimize typing overhead.
 *
 * @param <Head> The head element type
 * @param <Tail> The encoded recursive tail HList type
 * @see Singleton
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
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

    public static <Head> Singleton<Head> singleton(Head head) {
        return new Singleton<>(head);
    }

    public static <_1, _2> Tuple2<_1, _2> tuple(_1 _1, _2 _2) {
        return singleton(_2).cons(_1);
    }

    public static <_1, _2, _3> Tuple3<_1, _2, _3> tuple(_1 _1, _2 _2, _3 _3) {
        return tuple(_2, _3).cons(_1);
    }

    public static <_1, _2, _3, _4> Tuple4<_1, _2, _3, _4> tuple(_1 _1, _2 _2, _3 _3, _4 _4) {
        return tuple(_2, _3, _4).cons(_1);
    }

    public static <_1, _2, _3, _4, _5> Tuple5<_1, _2, _3, _4, _5> tuple(_1 _1, _2 _2, _3 _3, _4 _4, _5 _5) {
        return tuple(_2, _3, _4, _5).cons(_1);
    }

    public static class HCons<Head, Tail extends HList<?, ?>> extends HList<Head, Tail> {
        private final Head head;
        private final Tail tail;

        HCons(Head head, Tail tail) {
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
        public final boolean equals(Object other) {
            if (other instanceof HCons) {
                HCons that = (HCons) other;
                boolean sameHead = this.head.equals(that.head);
                boolean sameTail = this.tail.equals(that.tail);
                return sameHead && sameTail;
            }
            return false;
        }

        @Override
        public final int hashCode() {
            return 31 * Objects.hashCode(head) + tail.hashCode();
        }

        @Override
        public final String toString() {
            return "HCons{" +
                    "head=" + head +
                    ", tail=" + tail +
                    '}';
        }
    }

    public static final class HNil extends HList<Void, HNil> {
        private static final HNil INSTANCE = new HNil();

        @Override
        public <Head> Singleton<Head> cons(Head head) {
            return new Singleton<>(head);
        }

        @Override
        public String toString() {
            return "HNil{}";
        }
    }
}
