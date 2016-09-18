package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;

/**
 * HList indexes representing a value at arbitrary depth in some compatible HList. HList compatibility requires
 * identical element types up to and including the target element, but thereafter is unconstrained in length and element
 * type.
 *
 * @param <Target>     the target element type
 * @param <TargetList> type of compatible HList
 */
public abstract class Index<Target, TargetList extends HCons<?, ?>> {

    private Index() {
    }

    /**
     * Nest this index deeper by one element.
     *
     * @param <NewHead> the type of the preceding element
     * @return an index at the same Target, nested one level deep
     */
    public final <NewHead> Index<Target, HCons<NewHead, ? extends TargetList>> after() {
        return new N<>(this);
    }

    /**
     * Retrieve the value at this index in hList.
     *
     * @param hList the hList
     * @return the value at this index
     */
    public abstract Target get(TargetList hList);

    /**
     * Set a new value of the same type at this index in an {@link HList}.
     *
     * @param newElement the new value
     * @param hList      the HList
     * @return the updated HList
     */
    public abstract TargetList set(Target newElement, TargetList hList);

    /**
     * Create a root index for a head value of type <code>Target</code>.
     *
     * @param <Target> the type of the value to get
     * @return the root index
     */
    public static <Target> Index<Target, HCons<Target, ?>> index() {
        return Z.instance();
    }

    private static final class Z<Target> extends Index<Target, HCons<Target, ?>> {

        private static final Z INSTANCE = new Z();

        @Override
        public Target get(HCons<Target, ?> hList) {
            return hList.head();
        }

        @Override
        public HCons<Target, ?> set(Target newElement, HCons<Target, ?> hList) {
            return hList.tail().cons(newElement);
        }

        @SuppressWarnings("unchecked")
        public static <Target> Z<Target> instance() {
            return (Z<Target>) INSTANCE;
        }
    }

    private static final class N<Target, Head, List extends HCons<?, ?>, PreviousIndex extends Index<Target, List>> extends Index<Target, HCons<Head, ? extends List>> {

        private final PreviousIndex previousIndex;

        private N(PreviousIndex previousIndex) {
            this.previousIndex = previousIndex;
        }

        @Override
        public Target get(HCons<Head, ? extends List> hList) {
            return previousIndex.get(hList.tail());
        }

        @Override
        @SuppressWarnings("unchecked")
        public HCons<Head, ? extends List> set(Target newElement, HCons<Head, ? extends List> hList) {
            return (HCons<Head, ? extends List>) previousIndex.set(newElement, hList.tail()).cons(hList.head());
        }
    }
}
