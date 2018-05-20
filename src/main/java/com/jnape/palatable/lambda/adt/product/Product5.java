package com.jnape.palatable.lambda.adt.product;

import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn5;

/**
 * A product with five values.
 *
 * @param <_1> The first element type
 * @param <_2> The second element type
 * @param <_3> The third element type
 * @param <_4> The fourth element type
 * @param <_5> The fifth element type
 * @see Product2
 * @see Tuple5
 */
public interface Product5<_1, _2, _3, _4, _5> extends Product4<_1, _2, _3, _4> {

    /**
     * Retrieve the fifth element.
     *
     * @return the fifth element
     */
    _5 _5();

    /**
     * Destructure and apply this product to a function accepting the same number of arguments as this product's
     * slots. This can be thought of as a kind of dual to uncurrying a function and applying a product to it.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the destructured product to the function
     */
    default <R> R into(Fn5<? super _1, ? super _2, ? super _3, ? super _4, ? super _5, ? extends R> fn) {
        return Product4.super.<Fn1<? super _5, ? extends R>>into(fn).apply(_5());
    }
}
