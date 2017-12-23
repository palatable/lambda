package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.jnape.palatable.lambda.lens.Lens.simpleLens;

/**
 * Lenses that operate on {@link Collection}s.
 */
public final class CollectionLens {

    private CollectionLens() {
    }

    /**
     * Convenience static factory method for creating a lens that focuses on a copy of a <code>Collection</code>, given
     * a function that creates the copy. Useful for composition to avoid mutating a <code>Collection</code> reference.
     *
     * @param copyFn the copying function
     * @param <X>    the collection element type
     * @param <CX>   the type of the collection
     * @return a lens that focuses on a copy of CX
     */
    public static <X, CX extends Collection<X>> Lens.Simple<CX, CX> asCopy(Function<? super CX, ? extends CX> copyFn) {
        return simpleLens(copyFn, (__, copy) -> copy);
    }

    /**
     * Convenience static factory method for creating a lens that focuses on an arbitrary {@link Collection} as a
     * {@link Set}.
     *
     * @param <X>  the collection element type
     * @param <CX> the type of the collection
     * @return a lens that focuses on a Collection as a Set
     * @deprecated in favor of lawful {@link CollectionLens#asSet(Function)}
     */
    @Deprecated
    public static <X, CX extends Collection<X>> Lens.Simple<CX, Set<X>> asSet() {
        return simpleLens(HashSet::new, (xsL, xsS) -> {
            xsL.retainAll(xsS);
            return xsL;
        });
    }

    /**
     * Convenience static factory method for creating a lens that focuses on an arbitrary {@link Collection} as a
     * {@link Set}.
     *
     * @param copyFn the copying function
     * @param <X>    the collection element type
     * @param <CX>   the type of the collection
     * @return a lens that focuses on a Collection as a Set
     */
    public static <X, CX extends Collection<X>> Lens.Simple<CX, Set<X>> asSet(
            Function<? super CX, ? extends CX> copyFn) {
        return simpleLens(HashSet::new, (xsL, xsS) -> {
            Set<X> missing = new HashSet<>(xsS);
            missing.removeAll(xsL);
            CX updated = copyFn.apply(xsL);
            updated.addAll(missing);
            updated.retainAll(xsS);
            return updated;
        });
    }

    /**
     * Convenience static factory method for creating a lens that focuses on a Collection as a Stream.
     *
     * @param <X>  the collection element type
     * @param <CX> the type of the collection
     * @return a lens that focuses on a Collection as a stream.
     * @deprecated in favor of lawful {@link CollectionLens#asStream(Function)}
     */
    @Deprecated
    public static <X, CX extends Collection<X>> Lens.Simple<CX, Stream<X>> asStream() {
        return simpleLens(Collection::stream, (xsL, xsS) -> {
            xsL.clear();
            xsS.forEach(xsL::add);
            return xsL;
        });
    }

    /**
     * Convenience static factory method for creating a lens that focuses on a Collection as a Stream.
     * <p>
     * Note that this lens is effectively lawful, though difficult to prove given the intrinsically
     * stateful and inequitable nature of {@link Stream}.
     *
     * @param copyFn the copying function
     * @param <X>    the collection element type
     * @param <CX>   the type of the collection
     * @return a lens that focuses on a Collection as a stream.
     */
    public static <X, CX extends Collection<X>> Lens.Simple<CX, Stream<X>> asStream(
            Function<? super CX, ? extends CX> copyFn) {
        return simpleLens(Collection::stream, (xsL, xsS) -> {
            CX updated = copyFn.apply(xsL);
            updated.clear();
            xsS.forEach(updated::add);
            return updated;
        });
    }
}
