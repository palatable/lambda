package com.jnape.palatable.lambda.adt.product;

import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn4;

/**
 * A product with four values.
 *
 * @param <_1> The first element type
 * @param <_2> The second element type
 * @param <_3> The third element type
 * @param <_4> The fourth element type
 * @see Product2
 * @see Tuple4
 */
public interface Product4<_1, _2, _3, _4> extends Product3<_1, _2, _3> {

    /**
     * Retrieve the fourth element.
     *
     * @return the fourth element
     */
    _4 _4();

    /**
     * Destructure and apply this product to a function accepting the same number of arguments as this product's
     * slots. This can be thought of as a kind of dual to uncurrying a function and applying a product to it.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the destructured product to the function
     */
    default <R> R into(Fn4<? super _1, ? super _2, ? super _3, ? super _4, ? extends R> fn) {
        return Product3.super.into(fn).apply(_4());
    }
}
