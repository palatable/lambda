package com.jnape.palatable.lambda.adt.product;

import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn8;

/**
 * A product with eight values.
 *
 * @param <_1> The first element type
 * @param <_2> The second element type
 * @param <_3> The third element type
 * @param <_4> The fourth element type
 * @param <_5> The fifth element type
 * @param <_6> The sixth element type
 * @param <_7> The seventh element type
 * @param <_8> The eighth element type
 * @see Product2
 * @see Tuple8
 */
public interface Product8<_1, _2, _3, _4, _5, _6, _7, _8> extends Product7<_1, _2, _3, _4, _5, _6, _7> {

    /**
     * Retrieve the eighth element.
     *
     * @return the eighth element
     */
    _8 _8();

    /**
     * Destructure and apply this product to a function accepting the same number of arguments as this product's
     * slots. This can be thought of as a kind of dual to uncurrying a function and applying a product to it.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the destructured product to the function
     */
    default <R> R into(
            Fn8<? super _1, ? super _2, ? super _3, ? super _4, ? super _5, ? super _6, ? super _7, ? super _8, ? extends R> fn) {
        return Product7.super.<Fn1<? super _8, ? extends R>>into(fn).apply(_8());
    }

    /**
     * Rotate all eight values of this product one slot to the left.
     *
     * @return the left-rotated product
     */
    default Product8<_2, _3, _4, _5, _6, _7, _8, _1> rotateL8() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_2, _3, _4, _5, _6, _7, _8, _1));
    }

    /**
     * Rotate all eight values of this product one slot to the right.
     *
     * @return the right-rotated product
     */
    default Product8<_8, _1, _2, _3, _4, _5, _6, _7> rotateR8() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_8, _1, _2, _3, _4, _5, _6, _7));
    }

    @Override
    default Product8<_2, _3, _4, _5, _6, _7, _1, _8> rotateL7() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_2, _3, _4, _5, _6, _7, _1, _8));
    }

    @Override
    default Product8<_7, _1, _2, _3, _4, _5, _6, _8> rotateR7() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_7, _1, _2, _3, _4, _5, _6, _8));
    }

    @Override
    default Product8<_2, _3, _4, _5, _6, _1, _7, _8> rotateL6() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_2, _3, _4, _5, _6, _1, _7, _8));
    }

    @Override
    default Product8<_6, _1, _2, _3, _4, _5, _7, _8> rotateR6() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_6, _1, _2, _3, _4, _5, _7, _8));
    }

    @Override
    default Product8<_2, _3, _4, _5, _1, _6, _7, _8> rotateL5() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_2, _3, _4, _5, _1, _6, _7, _8));
    }

    @Override
    default Product8<_5, _1, _2, _3, _4, _6, _7, _8> rotateR5() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_5, _1, _2, _3, _4, _6, _7, _8));
    }

    @Override
    default Product8<_2, _3, _4, _1, _5, _6, _7, _8> rotateL4() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_2, _3, _4, _1, _5, _6, _7, _8));
    }

    @Override
    default Product8<_4, _1, _2, _3, _5, _6, _7, _8> rotateR4() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_4, _1, _2, _3, _5, _6, _7, _8));
    }

    @Override
    default Product8<_2, _3, _1, _4, _5, _6, _7, _8> rotateL3() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_2, _3, _1, _4, _5, _6, _7, _8));
    }

    @Override
    default Product8<_3, _1, _2, _4, _5, _6, _7, _8> rotateR3() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_3, _1, _2, _4, _5, _6, _7, _8));
    }

    @Override
    default Product8<_2, _1, _3, _4, _5, _6, _7, _8> invert() {
        return into((_1, _2, _3, _4, _5, _6, _7, _8) -> product(_2, _1, _3, _4, _5, _6, _7, _8));
    }

    /**
     * Static factory method for creating a generic {@link Product8}.
     *
     * @param _1   the first slot
     * @param _2   the second slot
     * @param _3   the third slot
     * @param _4   the fourth slot
     * @param _5   the fifth slot
     * @param _6   the sixth slot
     * @param _7   the seventh slot
     * @param _8   the eighth slot
     * @param <_1> the first slot type
     * @param <_2> the second slot type
     * @param <_3> the third slot type
     * @param <_4> the fourth slot type
     * @param <_5> the fifth slot type
     * @param <_6> the sixth slot type
     * @param <_7> the seventh slot type
     * @param <_8> the eighth slot type
     * @return the {@link Product8}
     */
    static <_1, _2, _3, _4, _5, _6, _7, _8> Product8<_1, _2, _3, _4, _5, _6, _7, _8> product(_1 _1, _2 _2, _3 _3, _4 _4,
                                                                                             _5 _5, _6 _6, _7 _7,
                                                                                             _8 _8) {
        return new Product8<_1, _2, _3, _4, _5, _6, _7, _8>() {
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

            @Override
            public _5 _5() {
                return _5;
            }

            @Override
            public _6 _6() {
                return _6;
            }

            @Override
            public _7 _7() {
                return _7;
            }

            @Override
            public _8 _8() {
                return _8;
            }
        };
    }
}
