package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.hlist.Tuple4;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A generalization of the coproduct of four types <code>A</code>, <code>B</code>, <code>C</code>, and <code>D</code>.
 *
 * @param <A> a type parameter representing the first possible type of this coproduct
 * @param <B> a type parameter representing the second possible type of this coproduct
 * @param <C> a type parameter representing the third possible type of this coproduct
 * @param <D> a type parameter representing the fourth possible type of this coproduct
 * @see CoProduct2
 */
@FunctionalInterface
public interface CoProduct4<A, B, C, D, CP4 extends CoProduct4<A, B, C, D, ?>> {

    /**
     * Type-safe convergence requiring a match against all potential types.
     *
     * @param aFn morphism <code>A -&gt; R</code>
     * @param bFn morphism <code>B -&gt; R</code>
     * @param cFn morphism <code>C -&gt; R</code>
     * @param dFn morphism <code>D -&gt; R</code>
     * @param <R> result type
     * @return the result of applying the appropriate morphism from whichever type is represented by this coproduct to R
     * @see CoProduct2#match(Function, Function)
     */
    <R> R match(Function<? super A, ? extends R> aFn,
                Function<? super B, ? extends R> bFn,
                Function<? super C, ? extends R> cFn,
                Function<? super D, ? extends R> dFn);

    /**
     * Diverge this coproduct by introducing another possible type that it could represent.
     *
     * @param <E> the additional possible type of this coproduct
     * @return a Coproduct5&lt;A, B, C, D, E&gt;
     * @see CoProduct2#diverge()
     */
    default <E> CoProduct5<A, B, C, D, E, ? extends CoProduct5<A, B, C, D, E, ?>> diverge() {
        return new CoProduct5<A, B, C, D, E, CoProduct5<A, B, C, D, E, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                               Function<? super E, ? extends R> eFn) {
                return CoProduct4.this.match(aFn, bFn, cFn, dFn);
            }
        };
    }

    /**
     * Converge this coproduct down to a lower order coproduct by mapping the last possible type into an earlier
     * possible type.
     *
     * @param convergenceFn function from last possible type to earlier type
     * @return a coproduct of the initial types without the terminal type
     * @see CoProduct3#converge
     */
    default CoProduct3<A, B, C, ? extends CoProduct3<A, B, C, ?>> converge(
            Function<? super D, ? extends CoProduct3<A, B, C, ?>> convergenceFn) {
        return match(a -> new CoProduct3<A, B, C, CoProduct3<A, B, C, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn) {
                return aFn.apply(a);
            }
        }, b -> new CoProduct3<A, B, C, CoProduct3<A, B, C, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn) {
                return bFn.apply(b);
            }
        }, c -> new CoProduct3<A, B, C, CoProduct3<A, B, C, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn) {
                return cFn.apply(c);
            }
        }, convergenceFn);
    }

    /**
     * Project this coproduct onto a tuple.
     *
     * @return a tuple of the coproduct projection
     * @see CoProduct2#project()
     */
    default Tuple4<Optional<A>, Optional<B>, Optional<C>, Optional<D>> project() {
        return match(a -> tuple(Optional.of(a), Optional.empty(), Optional.empty(), Optional.empty()),
                     b -> tuple(Optional.empty(), Optional.of(b), Optional.empty(), Optional.empty()),
                     c -> tuple(Optional.empty(), Optional.empty(), Optional.of(c), Optional.empty()),
                     d -> tuple(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(d)));
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the first slot value.
     *
     * @return an optional value representing the projection of the "a" type index
     */
    default Optional<A> projectA() {
        return project()._1();
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the second slot value.
     *
     * @return an optional value representing the projection of the "b" type index
     */
    default Optional<B> projectB() {
        return project()._2();
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the third slot value.
     *
     * @return an optional value representing the projection of the "c" type index
     */
    default Optional<C> projectC() {
        return project()._3();
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the fourth slot value.
     *
     * @return an optional value representing the projection of the "d" type index
     */
    default Optional<D> projectD() {
        return project()._4();
    }

    /**
     * Embed this coproduct inside another value; that is, given morphisms from this coproduct to <code>R</code>, apply
     * the appropriate morphism to this coproduct as a whole. Like {@link CoProduct4#match}, but without unwrapping the
     * value.
     *
     * @param aFn morphism <code>A v B v C v D -&gt; R</code>, applied in the <code>A</code> case
     * @param bFn morphism <code>A v B v C v D -&gt; R</code>, applied in the <code>B</code> case
     * @param cFn morphism <code>A v B v C v D -&gt; R</code>, applied in the <code>C</code> case
     * @param dFn morphism <code>A v B v C v D -&gt; R</code>, applied in the <code>D</code> case
     * @param <R> result type
     * @return the result of applying the appropriate morphism to this coproduct
     */
    @SuppressWarnings("unchecked")
    default <R> R embed(Function<? super CP4, ? extends R> aFn,
                        Function<? super CP4, ? extends R> bFn,
                        Function<? super CP4, ? extends R> cFn,
                        Function<? super CP4, ? extends R> dFn) {
        return match(constantly(aFn.apply((CP4) this)),
                     constantly(bFn.apply((CP4) this)),
                     constantly(cFn.apply((CP4) this)),
                     constantly(dFn.apply((CP4) this)));
    }

}
