package com.jnape.palatable.lambda.adt.coproduct;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A generalization of the coproduct of five types <code>A</code>, <code>B</code>, <code>C</code>, <code>D</code>, and
 * <code>E</code>.
 *
 * @param <A> a type parameter representing the first possible type of this coproduct
 * @param <B> a type parameter representing the second possible type of this coproduct
 * @param <C> a type parameter representing the third possible type of this coproduct
 * @param <D> a type parameter representing the fourth possible type of this coproduct
 * @param <E> a type parameter representing the fifth possible type of this coproduct
 * @see CoProduct2
 */
@FunctionalInterface
public interface CoProduct5<A, B, C, D, E, CP5 extends CoProduct5<A, B, C, D, E, ?>> {

    /**
     * Type-safe convergence requiring a match against all potential types.
     *
     * @param aFn morphism <code>A -&gt; R</code>
     * @param bFn morphism <code>B -&gt; R</code>
     * @param cFn morphism <code>C -&gt; R</code>
     * @param dFn morphism <code>D -&gt; R</code>
     * @param eFn morphism <code>E -&gt; R</code>
     * @param <R> result type
     * @return the result of applying the appropriate morphism from whichever type is represented by this coproduct to R
     * @see CoProduct2#match(Function, Function)
     */
    <R> R match(Function<? super A, ? extends R> aFn,
                Function<? super B, ? extends R> bFn,
                Function<? super C, ? extends R> cFn,
                Function<? super D, ? extends R> dFn,
                Function<? super E, ? extends R> eFn);

    /**
     * Converge this coproduct down to a lower order coproduct by mapping the last possible type into an earlier
     * possible type.
     *
     * @param convergenceFn morphism <code>E -&gt; {@link CoProduct4}&lt;A, B, C, D&gt;</code>
     * @return a CoProduct4&lt;A, B, C, D&gt;
     */
    default CoProduct4<A, B, C, D, ? extends CoProduct4<A, B, C, D, ?>> converge(
            Function<? super E, ? extends CoProduct4<A, B, C, D, ?>> convergenceFn) {
        return match(a -> new CoProduct4<A, B, C, D, CoProduct4<A, B, C, D, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn) {
                return aFn.apply(a);
            }
        }, b -> new CoProduct4<A, B, C, D, CoProduct4<A, B, C, D, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn) {
                return bFn.apply(b);
            }
        }, c -> new CoProduct4<A, B, C, D, CoProduct4<A, B, C, D, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn) {
                return cFn.apply(c);
            }
        }, d -> new CoProduct4<A, B, C, D, CoProduct4<A, B, C, D, ?>>() {
            @Override
            public <R> R match(Function<? super A, ? extends R> aFn, Function<? super B, ? extends R> bFn,
                               Function<? super C, ? extends R> cFn, Function<? super D, ? extends R> dFn) {
                return dFn.apply(d);
            }
        }, convergenceFn::apply);
    }

    /**
     * Project this coproduct onto a tuple.
     *
     * @return a tuple of the coproduct projection
     * @see CoProduct2#project()
     */
    default Tuple5<Maybe<A>, Maybe<B>, Maybe<C>, Maybe<D>, Maybe<E>> project() {
        return match(a -> tuple(just(a), nothing(), nothing(), nothing(), nothing()),
                     b -> tuple(nothing(), just(b), nothing(), nothing(), nothing()),
                     c -> tuple(nothing(), nothing(), just(c), nothing(), nothing()),
                     d -> tuple(nothing(), nothing(), nothing(), just(d), nothing()),
                     e -> tuple(nothing(), nothing(), nothing(), nothing(), just(e)));
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the first slot value.
     *
     * @return an optional value representing the projection of the "a" type index
     */
    default Maybe<A> projectA() {
        return project()._1();
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the second slot value.
     *
     * @return an optional value representing the projection of the "b" type index
     */
    default Maybe<B> projectB() {
        return project()._2();
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the third slot value.
     *
     * @return an optional value representing the projection of the "c" type index
     */
    default Maybe<C> projectC() {
        return project()._3();
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the fourth slot value.
     *
     * @return an optional value representing the projection of the "d" type index
     */
    default Maybe<D> projectD() {
        return project()._4();
    }

    /**
     * Convenience method for projecting this coproduct onto a tuple and then extracting the fifth slot value.
     *
     * @return an optional value representing the projection of the "e" type index
     */
    default Maybe<E> projectE() {
        return project()._5();
    }

    /**
     * Embed this coproduct inside another value; that is, given morphisms from this coproduct to <code>R</code>, apply
     * the appropriate morphism to this coproduct as a whole. Like {@link CoProduct5#match}, but without unwrapping the
     * value.
     *
     * @param aFn morphism <code>A v B v C v D v E -&gt; R</code>, applied in the <code>A</code> case
     * @param bFn morphism <code>A v B v C v D v E -&gt; R</code>, applied in the <code>B</code> case
     * @param cFn morphism <code>A v B v C v D v E -&gt; R</code>, applied in the <code>C</code> case
     * @param dFn morphism <code>A v B v C v D v E -&gt; R</code>, applied in the <code>D</code> case
     * @param eFn morphism <code>A v B v C v D v E -&gt; R</code>, applied in the <code>E</code> case
     * @param <R> result type
     * @return the result of applying the appropriate morphism to this coproduct
     */
    @SuppressWarnings("unchecked")
    default <R> R embed(Function<? super CP5, ? extends R> aFn,
                        Function<? super CP5, ? extends R> bFn,
                        Function<? super CP5, ? extends R> cFn,
                        Function<? super CP5, ? extends R> dFn,
                        Function<? super CP5, ? extends R> eFn) {
        return this.<Fn1<CP5, R>>match(constantly(fn1(aFn)),
                                       constantly(fn1(bFn)),
                                       constantly(fn1(cFn)),
                                       constantly(fn1(dFn)),
                                       constantly(fn1(eFn)))
                .apply((CP5) this);
    }
}
