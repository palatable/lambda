package com.jnape.palatable.lambda.adt.product;

import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn7;

/**
 * A product with seven values.
 *
 * @param <_1> The first element type
 * @param <_2> The second element type
 * @param <_3> The third element type
 * @param <_4> The fourth element type
 * @param <_5> The fifth element type
 * @param <_6> The sixth element type
 * @param <_7> The seventh element type
 * @see Product2
 * @see Tuple7
 */
public interface Product7<_1, _2, _3, _4, _5, _6, _7> extends Product6<_1, _2, _3, _4, _5, _6> {

    /**
     * Retrieve the seventh element.
     *
     * @return the seventh element
     */
    _7 _7();

    /**
     * Destructure and apply this product to a function accepting the same number of arguments as this product's
     * slots. This can be thought of as a kind of dual to uncurrying a function and applying a product to it.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the destructured product to the function
     */
    default <R> R into(
            Fn7<? super _1, ? super _2, ? super _3, ? super _4, ? super _5, ? super _6, ? super _7, ? extends R> fn) {
        return Product6.super.<Fn1<? super _7, ? extends R>>into(fn).apply(_7());
    }
}
