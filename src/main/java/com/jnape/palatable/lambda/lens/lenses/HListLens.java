package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.Index;
import com.jnape.palatable.lambda.lens.Lens;

import static com.jnape.palatable.lambda.adt.hlist.HList.cons;
import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link HList}s.
 */
public final class HListLens {

    /**
     * Focus invariantly on the element at the specified {@link Index} in an {@link HList}.
     *
     * @param index  the index of the element to focus on
     * @param <E>    the element type
     * @param <List> the HList under focus
     * @return a lens focusing on the element at index
     */
    public static <E, List extends HCons<?, ?>> Lens.Simple<List, E> elementAt(Index<E, List> index) {
        return simpleLens(index::get, (l, e) -> index.set(e, l));
    }

    /**
     * Focus on the head of an {@link HList}.
     *
     * @param <Head> the head element type
     * @param <Tail> the tail HList type
     * @return a lens that focuses on the head of an HList
     */
    public static <Head, Tail extends HList<?, ?>> Lens.Simple<HCons<Head, Tail>, Head> head() {
        return simpleLens(HCons::head, (hCons, newHead) -> cons(newHead, hCons.tail()));
    }

    /**
     * Focus on the tail of an {@link HList}.
     *
     * @param <Head> the head element type
     * @param <Tail> the tail HList type
     * @return a lens that focuses on the tail of an HList
     */
    public static <Head, Tail extends HList<?, ?>> Lens.Simple<HCons<Head, Tail>, Tail> tail() {
        return simpleLens(HCons::tail, (hCons, newTail) -> cons(hCons.head(), newTail));
    }
}
