package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.adt.product.Product8;
import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A generalization of the coproduct of eight types.
 *
 * @param <A>   the first possible type
 * @param <B>   the second possible type
 * @param <C>   the third possible type
 * @param <D>   the fourth possible type
 * @param <E>   the fifth possible type
 * @param <F>   the sixth possible type
 * @param <G>   the seventh possible type
 * @param <H>   the eighth possible type
 * @param <CP8> the recursive type of this coproduct (used for embedding)
 * @see CoProduct2
 */
@FunctionalInterface
public interface CoProduct8<A, B, C, D, E, F, G, H, CP8 extends CoProduct8<A, B, C, D, E, F, G, H, ?>> {

    /**
     * Type-safe convergence requiring a match against all potential types.
     *
     * @param <R> result type
     * @param aFn morphism <code>A -&gt; R</code>
     * @param bFn morphism <code>B -&gt; R</code>
     * @param cFn morphism <code>C -&gt; R</code>
     * @param dFn morphism <code>D -&gt; R</code>
     * @param eFn morphism <code>E -&gt; R</code>
     * @param fFn morphism <code>F -&gt; R</code>
     * @param gFn morphism <code>G -&gt; R</code>
     * @param hFn morphism <code>H -&gt; R</code>
     * @return the result of applying the appropriate morphism from whichever type is represented by this coproduct to R
     * @see CoProduct2#match(Fn1, Fn1)
     */
    <R> R match(Fn1<? super A, ? extends R> aFn,
                Fn1<? super B, ? extends R> bFn,
                Fn1<? super C, ? extends R> cFn,
                Fn1<? super D, ? extends R> dFn,
                Fn1<? super E, ? extends R> eFn,
                Fn1<? super F, ? extends R> fFn,
                Fn1<? super G, ? extends R> gFn,
                Fn1<? super H, ? extends R> hFn);

    /**
     * Converge this coproduct down to a lower order coproduct by mapping the last possible type into an earlier
     * possible type.
     *
     * @param convergenceFn morphism <code>G -&gt; {@link CoProduct6}&lt;A, B, C, D, E, F, G&gt;</code>
     * @return a {@link CoProduct7}&lt;A, B, C, D, E, F, G&gt;
     */
    default CoProduct7<A, B, C, D, E, F, G, ? extends CoProduct7<A, B, C, D, E, F, G, ?>> converge(
            Fn1<? super H, ? extends CoProduct7<A, B, C, D, E, F, G, ?>> convergenceFn) {
        return match(Choice7::a, Choice7::b, Choice7::c, Choice7::d, Choice7::e, Choice7::f, Choice7::g,
                     convergenceFn::apply);
    }

    /**
     * Project this coproduct onto a product.
     *
     * @return a product of the coproduct projection
     * @see CoProduct2#project()
     */
    default Product8<Maybe<A>, Maybe<B>, Maybe<C>, Maybe<D>, Maybe<E>, Maybe<F>, Maybe<G>, Maybe<H>> project() {
        return match(a -> tuple(just(a), nothing(), nothing(), nothing(), nothing(), nothing(), nothing(), nothing()),
                     b -> tuple(nothing(), just(b), nothing(), nothing(), nothing(), nothing(), nothing(), nothing()),
                     c -> tuple(nothing(), nothing(), just(c), nothing(), nothing(), nothing(), nothing(), nothing()),
                     d -> tuple(nothing(), nothing(), nothing(), just(d), nothing(), nothing(), nothing(), nothing()),
                     e -> tuple(nothing(), nothing(), nothing(), nothing(), just(e), nothing(), nothing(), nothing()),
                     f -> tuple(nothing(), nothing(), nothing(), nothing(), nothing(), just(f), nothing(), nothing()),
                     g -> tuple(nothing(), nothing(), nothing(), nothing(), nothing(), nothing(), just(g), nothing()),
                     h -> tuple(nothing(), nothing(), nothing(), nothing(), nothing(), nothing(), nothing(), just(h)));
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
     * Convenience method for projecting this coproduct onto a product and then extracting the seventh slot value.
     *
     * @return an optional value representing the projection of the "g" type index
     */
    default Maybe<G> projectG() {
        return project()._7();
    }

    /**
     * Convenience method for projecting this coproduct onto a product and then extracting the eighth slot value.
     *
     * @return an optional value representing the projection of the "h" type index
     */
    default Maybe<H> projectH() {
        return project()._8();
    }

    /**
     * Embed this coproduct inside another value; that is, given morphisms from this coproduct to <code>R</code>, apply
     * the appropriate morphism to this coproduct as a whole. Like {@link CoProduct8#match}, but without unwrapping the
     * value.
     *
     * @param <R> result type
     * @param aFn morphism <code>A v B v C v D v E v F v G v H -&gt; R</code>, applied in the <code>A</code> case
     * @param bFn morphism <code>A v B v C v D v E v F v G v H -&gt; R</code>, applied in the <code>B</code> case
     * @param cFn morphism <code>A v B v C v D v E v F v G v H -&gt; R</code>, applied in the <code>C</code> case
     * @param dFn morphism <code>A v B v C v D v E v F v G v H -&gt; R</code>, applied in the <code>D</code> case
     * @param eFn morphism <code>A v B v C v D v E v F v G v H -&gt; R</code>, applied in the <code>E</code> case
     * @param fFn morphism <code>A v B v C v D v E v F v G v H -&gt; R</code>, applied in the <code>F</code> case
     * @param gFn morphism <code>A v B v C v D v E v F v G v H -&gt; R</code>, applied in the <code>G</code> case
     * @param hFn morphism <code>A v B v C v D v E v F v G v H -&gt; R</code>, applied in the <code>H</code> case
     * @return the result of applying the appropriate morphism to this coproduct
     */
    @SuppressWarnings("unchecked")
    default <R> R embed(Fn1<? super CP8, ? extends R> aFn,
                        Fn1<? super CP8, ? extends R> bFn,
                        Fn1<? super CP8, ? extends R> cFn,
                        Fn1<? super CP8, ? extends R> dFn,
                        Fn1<? super CP8, ? extends R> eFn,
                        Fn1<? super CP8, ? extends R> fFn,
                        Fn1<? super CP8, ? extends R> gFn,
                        Fn1<? super CP8, ? extends R> hFn) {
        return this.<Fn1<CP8, R>>match(constantly(fn1(aFn)),
                                       constantly(fn1(bFn)),
                                       constantly(fn1(cFn)),
                                       constantly(fn1(dFn)),
                                       constantly(fn1(eFn)),
                                       constantly(fn1(fFn)),
                                       constantly(fn1(gFn)),
                                       constantly(fn1(hFn)))
                .apply((CP8) this);
    }
}
