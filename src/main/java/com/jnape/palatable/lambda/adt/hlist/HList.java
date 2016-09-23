package com.jnape.palatable.lambda.adt.hlist;

import java.util.Objects;

/**
 * An immutable heterogeneous list supporting arbitrary depth type-safety via a linearly recursive type signature. Note
 * that due to its rapidly expanding type signature, specializations exist up to certain depths to minimize typing
 * overhead.
 *
 * @param <Head> The head element type
 * @param <Tail> The encoded recursive tail HList type
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 */
public abstract class HList<Head, Tail extends HList<?, ?>> {

    private HList() {
    }

    /**
     * Cons an element onto the front of this HList.
     *
     * @param newHead   the new head element
     * @param <NewHead> the new head type
     * @return the updated HList
     */
    public abstract <NewHead> HCons<NewHead, ? extends HList<Head, Tail>> cons(NewHead newHead);

    @Override
    public final String toString() {
        StringBuilder body = new StringBuilder("HList{");

        HList next = this;
        while (next != HNil.INSTANCE) {
            HCons hCons = (HCons) next;
            body.append(" ").append(hCons.head).append(" ");
            next = hCons.tail;
            if (next != HNil.INSTANCE)
                body.append("::");
        }

        return body.append("}").toString();
    }

    /**
     * Static factory method for creating empty HLists.
     *
     * @return an empty HList
     */
    public static HNil nil() {
        return HNil.INSTANCE;
    }

    /**
     * Static factory method for creating an HList from the given head and tail.
     *
     * @param head   the head element
     * @param tail   the tail HList
     * @param <Head> the head type
     * @param <Tail> the tail type
     * @return the newly created HList
     */
    public static <Head, Tail extends HList<?, ?>> HCons<Head, Tail> cons(Head head, Tail tail) {
        return new HCons<>(head, tail);
    }

    /**
     * Static factory method for creating a singleton HList.
     *
     * @param head   the head element
     * @param <Head> the head element type
     * @return the singleton HList
     */
    public static <Head> SingletonHList<Head> singletonHList(Head head) {
        return new SingletonHList<>(head);
    }

    /**
     * Static factory method for creating a 2-element HList.
     *
     * @param _1   the head element
     * @param _2   the second element
     * @param <_1> the head element type
     * @param <_2> the second element type
     * @return the 2-element HList
     * @see Tuple2
     */
    @SuppressWarnings("JavaDoc")
    public static <_1, _2> Tuple2<_1, _2> tuple(_1 _1, _2 _2) {
        return singletonHList(_2).cons(_1);
    }

    /**
     * Static factory method for creating a 3-element HList.
     *
     * @param _1   the head element
     * @param _2   the second element
     * @param _3   the third element
     * @param <_1> the head element type
     * @param <_2> the second element type
     * @param <_3> the third element type
     * @return the 3-element HList
     * @see Tuple3
     */
    @SuppressWarnings("JavaDoc")
    public static <_1, _2, _3> Tuple3<_1, _2, _3> tuple(_1 _1, _2 _2, _3 _3) {
        return tuple(_2, _3).cons(_1);
    }

    /**
     * Static factory method for creating a 4-element HList.
     *
     * @param _1   the head element
     * @param _2   the second element
     * @param _3   the third element
     * @param _4   the fourth element
     * @param <_1> the head element type
     * @param <_2> the second element type
     * @param <_3> the third element type
     * @param <_4> the fourth element type
     * @return the 4-element HList
     * @see Tuple4
     */
    @SuppressWarnings("JavaDoc")
    public static <_1, _2, _3, _4> Tuple4<_1, _2, _3, _4> tuple(_1 _1, _2 _2, _3 _3, _4 _4) {
        return tuple(_2, _3, _4).cons(_1);
    }

    /**
     * Static factory method for creating a 5-element HList.
     *
     * @param _1   the head element
     * @param _2   the second element
     * @param _3   the third element
     * @param _4   the fourth element
     * @param _5   the fifth element
     * @param <_1> the head element type
     * @param <_2> the second element type
     * @param <_3> the third element type
     * @param <_4> the fourth element type
     * @param <_5> the fifth element type
     * @return the 5-element HList
     * @see Tuple5
     */
    @SuppressWarnings("JavaDoc")
    public static <_1, _2, _3, _4, _5> Tuple5<_1, _2, _3, _4, _5> tuple(_1 _1, _2 _2, _3 _3, _4 _4, _5 _5) {
        return tuple(_2, _3, _4, _5).cons(_1);
    }

    /**
     * The consing of a head element to a tail <code>HList</code>.
     *
     * @param <Head> the head element type
     * @param <Tail> the HList tail type
     */
    public static class HCons<Head, Tail extends HList<?, ?>> extends HList<Head, Tail> {
        private final Head head;
        private final Tail tail;

        HCons(Head head, Tail tail) {
            this.head = head;
            this.tail = tail;
        }

        /**
         * The head element of the <code>HList</code>.
         *
         * @return the head element
         */
        public Head head() {
            return head;
        }

        /**
         * The remaining tail of the <code>HList</code>; returns an HNil if this is the last element.
         *
         * @return the tail
         */
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
                return this.head.equals(that.head)
                        && this.tail.equals(that.tail);
            }
            return false;
        }

        @Override
        public final int hashCode() {
            return 31 * Objects.hashCode(head) + tail.hashCode();
        }
    }

    /**
     * The empty <code>HList</code>.
     */
    public static final class HNil extends HList<Void, HNil> {
        private static final HNil INSTANCE = new HNil();

        private HNil() {
        }

        @Override
        public <Head> SingletonHList<Head> cons(Head head) {
            return new SingletonHList<>(head);
        }
    }
}
