package com.jnape.palatable.lambda.adt.product;

import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
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
        return Product3.super.<Fn1<? super _4, ? extends R>>into(fn).apply(_4());
    }

    /**
     * Rotate the first four values of this product one slot to the left.
     *
     * @return the left-rotated product
     */
    default Product4<_2, _3, _4, _1> rotateL4() {
        return into((_1, _2, _3, _4) -> product(_2, _3, _4, _1));
    }

    /**
     * Rotate the first four values of this product one slot to the right.
     *
     * @return the right-rotated product
     */
    default Product4<_4, _1, _2, _3> rotateR4() {
        return into((_1, _2, _3, _4) -> product(_4, _1, _2, _3));
    }

    @Override
    default Product4<_2, _3, _1, _4> rotateL3() {
        return into((_1, _2, _3, _4) -> product(_2, _3, _1, _4));
    }

    @Override
    default Product4<_3, _1, _2, _4> rotateR3() {
        return into((_1, _2, _3, _4) -> product(_3, _1, _2, _4));
    }

    @Override
    default Product4<_2, _1, _3, _4> invert() {
        return into((_1, _2, _3, _4) -> product(_2, _1, _3, _4));
    }

    /**
     * Static factory method for creating a generic {@link Product4}.
     *
     * @param _1   the first slot
     * @param _2   the second slot
     * @param _3   the third slot
     * @param _4   the fourth slot
     * @param <_1> the first slot type
     * @param <_2> the second slot type
     * @param <_3> the third slot type
     * @param <_4> the fourth slot type
     * @return the {@link Product4}
     */
    static <_1, _2, _3, _4> Product4<_1, _2, _3, _4> product(_1 _1, _2 _2, _3 _3, _4 _4) {
        return new Product4<_1, _2, _3, _4>() {
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

            @Override
            public _4 _4() {
                return _4;
            }
        };
    }
}
