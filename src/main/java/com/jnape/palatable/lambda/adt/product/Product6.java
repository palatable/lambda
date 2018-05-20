package com.jnape.palatable.lambda.adt.product;

import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn6;

/**
 * A product with six values.
 *
 * @param <_1> The first element type
 * @param <_2> The second element type
 * @param <_3> The third element type
 * @param <_4> The fourth element type
 * @param <_5> The fifth element type
 * @param <_6> The sixth element type
 * @see Product2
 * @see Tuple6
 */
public interface Product6<_1, _2, _3, _4, _5, _6> extends Product5<_1, _2, _3, _4, _5> {

    /**
     * Retrieve the sixth element.
     *
     * @return the sixth element
     */
    _6 _6();

    /**
     * Destructure and apply this product to a function accepting the same number of arguments as this product's
     * slots. This can be thought of as a kind of dual to uncurrying a function and applying a product to it.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the destructured product to the function
     */
    default <R> R into(Fn6<? super _1, ? super _2, ? super _3, ? super _4, ? super _5, ? super _6, ? extends R> fn) {
        return Product5.super.<Fn1<? super _6, ? extends R>>into(fn).apply(_6());
    }
}
