package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A generalization of the coproduct of four types.
 *
 * @param <A> the first possible type
 * @param <B> the second possible type
 * @param <C> the third possible type
 * @param <D> the fourth possible type
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
     * @return a {@link CoProduct3}&lt;A, B, C&gt;
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
     * Project this coproduct onto a product.
     *
     * @return a product of the coproduct projection
     * @see CoProduct2#project()
     */
    default Product4<Maybe<A>, Maybe<B>, Maybe<C>, Maybe<D>> project() {
        return match(a -> tuple(just(a), nothing(), nothing(), nothing()),
                     b -> tuple(nothing(), just(b), nothing(), nothing()),
                     c -> tuple(nothing(), nothing(), just(c), nothing()),
                     d -> tuple(nothing(), nothing(), nothing(), just(d)));
    }

    /**
     * Convenience method for projecting this coproduct onto a product and then extracting the first slot value.
     *
     * @return an optional value representing the projection of the "a" type index
     */
    default Maybe<A> projectA() {
        return project()._1();
    }

    /**
     * Convenience method for projecting this coproduct onto a product and then extracting the second slot value.
     *
     * @return an optional value representing the projection of the "b" type index
     */
    default Maybe<B> projectB() {
        return project()._2();
    }

    /**
     * Convenience method for projecting this coproduct onto a product and then extracting the third slot value.
     *
     * @return an optional value representing the projection of the "c" type index
     */
    default Maybe<C> projectC() {
        return project()._3();
    }

    /**
     * Convenience method for projecting this coproduct onto a product and then extracting the fourth slot value.
     *
     * @return an optional value representing the projection of the "d" type index
     */
    default Maybe<D> projectD() {
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
        return this.<Fn1<CP4, R>>match(constantly(fn1(aFn)),
                                       constantly(fn1(bFn)),
                                       constantly(fn1(cFn)),
                                       constantly(fn1(dFn)))
                .apply((CP4) this);
    }

}
