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

    /**
     * Rotate the first seven values of this product one slot to the left.
     *
     * @return the left-rotated product
     */
    default Product7<_2, _3, _4, _5, _6, _7, _1> rotateL7() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_2, _3, _4, _5, _6, _7, _1));
    }

    /**
     * Rotate the first seven values of this product one slot to the right.
     *
     * @return the right-rotated product
     */
    default Product7<_7, _1, _2, _3, _4, _5, _6> rotateR7() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_7, _1, _2, _3, _4, _5, _6));
    }

    @Override
    default Product7<_2, _3, _4, _5, _6, _1, _7> rotateL6() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_2, _3, _4, _5, _6, _1, _7));
    }

    @Override
    default Product7<_6, _1, _2, _3, _4, _5, _7> rotateR6() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_6, _1, _2, _3, _4, _5, _7));
    }

    @Override
    default Product7<_2, _3, _4, _5, _1, _6, _7> rotateL5() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_2, _3, _4, _5, _1, _6, _7));
    }

    @Override
    default Product7<_5, _1, _2, _3, _4, _6, _7> rotateR5() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_5, _1, _2, _3, _4, _6, _7));
    }

    @Override
    default Product7<_2, _3, _4, _1, _5, _6, _7> rotateL4() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_2, _3, _4, _1, _5, _6, _7));
    }

    @Override
    default Product7<_4, _1, _2, _3, _5, _6, _7> rotateR4() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_4, _1, _2, _3, _5, _6, _7));
    }

    @Override
    default Product7<_2, _3, _1, _4, _5, _6, _7> rotateL3() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_2, _3, _1, _4, _5, _6, _7));
    }

    @Override
    default Product7<_3, _1, _2, _4, _5, _6, _7> rotateR3() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_3, _1, _2, _4, _5, _6, _7));
    }

    @Override
    default Product7<_2, _1, _3, _4, _5, _6, _7> invert() {
        return into((_1, _2, _3, _4, _5, _6, _7) -> product(_2, _1, _3, _4, _5, _6, _7));
    }

    /**
     * Static factory method for creating a generic {@link Product7}.
     *
     * @param _1   the first slot
     * @param _2   the second slot
     * @param _3   the third slot
     * @param _4   the fourth slot
     * @param _5   the fifth slot
     * @param _6   the sixth slot
     * @param _7   the seventh slot
     * @param <_1> the first slot type
     * @param <_2> the second slot type
     * @param <_3> the third slot type
     * @param <_4> the fourth slot type
     * @param <_5> the fifth slot type
     * @param <_6> the sixth slot type
     * @param <_7> the seventh slot type
     * @return the {@link Product7}
     */
    static <_1, _2, _3, _4, _5, _6, _7> Product7<_1, _2, _3, _4, _5, _6, _7> product(_1 _1, _2 _2, _3 _3, _4 _4, _5 _5,
                                                                                     _6 _6, _7 _7) {
        return new Product7<_1, _2, _3, _4, _5, _6, _7>() {
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
        };
    }
}
