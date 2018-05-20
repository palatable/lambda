package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.product.Product6;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A generalization of the coproduct of six types.
 *
 * @param <A> the first possible type
 * @param <B> the second possible type
 * @param <C> the third possible type
 * @param <D> the fourth possible type
 * @param <E> the fifth possible type
 * @param <F> the sixth possible type
 * @see CoProduct2
 */
@FunctionalInterface
public interface CoProduct6<A, B, C, D, E, F, CP6 extends CoProduct6<A, B, C, D, E, F, ?>> {

    /**
     * Type-safe convergence requiring a match against all potential types.
     *
     * @param aFn morphism <code>A -&gt; R</code>
     * @param bFn morphism <code>B -&gt; R</code>
     * @param cFn morphism <code>C -&gt; R</code>
     * @param dFn morphism <code>D -&gt; R</code>
     * @param eFn morphism <code>E -&gt; R</code>
     * @param fFn morphism <code>F -&gt; R</code>
     * @param <R> result type
     * @return the result of applying the appropriate morphism from whichever type is represented by this coproduct to R
     * @see CoProduct2#match(Function, Function)
     */
    <R> R match(Function<? super A, ? extends R> aFn,
                Function<? super B, ? extends R> bFn,
                Function<? super C, ? extends R> cFn,
                Function<? super D, ? extends R> dFn,
                Function<? super E, ? extends R> eFn,
                Function<? super F, ? extends R> fFn);

    /**
     * Diverge this coproduct by introducing another possible type that it could represent.
     *
     * @param <G> the additional possible type of this coproduct
     * @return a Coproduct7&lt;A, B, C, D, E, F, G&gt;
     * @see CoProduct2#diverge()
     */
    default <G> CoProduct7<A, B, C, D, E, F, G, ? extends CoProduct7<A, B, C, D, E, F, G, ?>> diverge() {
        return new CoProduct7<A, B, C, D, E, F, G, CoProduct7<A, B, C, D, E, F, G, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn,
                               Function<? super E, ? extends R> eFn, Function<? super F, ? extends R> fFn,
                               Function<? super G, ? extends R> gFn) {
                return CoProduct6.this.match(aFn, bFn, cFn, dFn, eFn, fFn);
            }
        };
    }

    /**
     * Converge this coproduct down to a lower order coproduct by mapping the last possible type into an earlier
     * possible type.
     *
     * @param convergenceFn morphism <code>F -&gt; {@link CoProduct5}&lt;A, B, C, D, E&gt;</code>
     * @return a {@link CoProduct5}&lt;A, B, C, D, E&gt;
     */
    default CoProduct5<A, B, C, D, E, ? extends CoProduct5<A, B, C, D, E, ?>> converge(
            Function<? super F, ? extends CoProduct5<A, B, C, D, E, ?>> convergenceFn) {
        return match(Choice5::a, Choice5::b, Choice5::c, Choice5::d, Choice5::e, convergenceFn::apply);
    }

    /**
     * Project this coproduct onto a product.
     *
     * @return a product of the coproduct projection
     * @see CoProduct2#project()
     */
    default Product6<Maybe<A>, Maybe<B>, Maybe<C>, Maybe<D>, Maybe<E>, Maybe<F>> project() {
        return match(a -> tuple(just(a), nothing(), nothing(), nothing(), nothing(), nothing()),
                     b -> tuple(nothing(), just(b), nothing(), nothing(), nothing(), nothing()),
                     c -> tuple(nothing(), nothing(), just(c), nothing(), nothing(), nothing()),
                     d -> tuple(nothing(), nothing(), nothing(), just(d), nothing(), nothing()),
                     e -> tuple(nothing(), nothing(), nothing(), nothing(), just(e), nothing()),
                     f -> tuple(nothing(), nothing(), nothing(), nothing(), nothing(), just(f)));
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
     * Convenience method for projecting this coproduct onto a product and then extracting the fifth slot value.
     *
     * @return an optional value representing the projection of the "e" type index
     */
    default Maybe<E> projectE() {
        return project()._5();
    }

    /**
     * Convenience method for projecting this coproduct onto a product and then extracting the sixth slot value.
     *
     * @return an optional value representing the projection of the "f" type index
     */
    default Maybe<F> projectF() {
        return project()._6();
    }

    /**
     * Embed this coproduct inside another value; that is, given morphisms from this coproduct to <code>R</code>, apply
     * the appropriate morphism to this coproduct as a whole. Like {@link CoProduct6#match}, but without unwrapping the
     * value.
     *
     * @param aFn morphism <code>A v B v C v D v E v F -&gt; R</code>, applied in the <code>A</code> case
     * @param bFn morphism <code>A v B v C v D v E v F -&gt; R</code>, applied in the <code>B</code> case
     * @param cFn morphism <code>A v B v C v D v E v F -&gt; R</code>, applied in the <code>C</code> case
     * @param dFn morphism <code>A v B v C v D v E v F -&gt; R</code>, applied in the <code>D</code> case
     * @param eFn morphism <code>A v B v C v D v E v F -&gt; R</code>, applied in the <code>E</code> case
     * @param fFn morphism <code>A v B v C v D v E v F -&gt; R</code>, applied in the <code>F</code> case
     * @param <R> result type
     * @return the result of applying the appropriate morphism to this coproduct
     */
    @SuppressWarnings("unchecked")
    default <R> R embed(Function<? super CP6, ? extends R> aFn,
                        Function<? super CP6, ? extends R> bFn,
                        Function<? super CP6, ? extends R> cFn,
                        Function<? super CP6, ? extends R> dFn,
                        Function<? super CP6, ? extends R> eFn,
                        Function<? super CP6, ? extends R> fFn) {
        return this.<Fn1<CP6, R>>match(constantly(fn1(aFn)),
                                       constantly(fn1(bFn)),
                                       constantly(fn1(cFn)),
                                       constantly(fn1(dFn)),
                                       constantly(fn1(eFn)),
                                       constantly(fn1(fFn)))
                .apply((CP6) this);
    }
}
