package com.jnape.palatable.lambda.adt.product;

import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.functions.Fn3;

/**
 * A product with three values.
 *
 * @param <_1> The first element type
 * @param <_2> The second element type
 * @param <_3> The third element type
 * @see Product2
 * @see Tuple3
 */
public interface Product3<_1, _2, _3> extends Product2<_1, _2> {

    /**
     * Retrieve the third element.
     *
     * @return the third element
     */
    _3 _3();

    /**
     * Destructure and apply this product to a function accepting the same number of arguments as this product's
     * slots. This can be thought of as a kind of dual to uncurrying a function and applying a product to it.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the destructured product to the function
     */
    default <R> R into(Fn3<? super _1, ? super _2, ? super _3, ? extends R> fn) {
        return Product2.super.into(fn).apply(_3());
    }

    /**
     * Rotate the first three values of this product one slot to the left.
     *
     * @return the left-rotated product
     */
    default Product3<_2, _3, _1> rotateL3() {
        return into((_1, _2, _3) -> product(_2, _3, _1));
    }

    /**
     * Rotate the first three values of this product one slot to the right.
     *
     * @return the right-rotated product
     */
    default Product3<_3, _1, _2> rotateR3() {
        return into((_1, _2, _3) -> product(_3, _1, _2));
    }

    @Override
    default Product3<_2, _1, _3> invert() {
        return into((_1, _2, _3) -> product(_2, _1, _3));
    }

    /**
     * Static factory method for creating a generic {@link Product3}.
     *
     * @param _1   the first slot
     * @param _2   the second slot
     * @param _3   the third slot
     * @param <_1> the first slot type
     * @param <_2> the second slot type
     * @param <_3> the third slot type
     * @return the {@link Product3}
     */
    static <_1, _2, _3> Product3<_1, _2, _3> product(_1 _1, _2 _2, _3 _3) {
        return new Product3<_1, _2, _3>() {
            @Override
            public _1 _1() {
                return _1;
            }

            @Override
            public _2 _2() {
                return _2;
            }

            @Override
            public _3 _3() {
                return _3;
            }
        };
    }
}
