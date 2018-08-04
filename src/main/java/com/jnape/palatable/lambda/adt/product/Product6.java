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

    /**
     * Rotate the first six values of this product one slot to the left.
     *
     * @return the left-rotated product
     */
    default Product6<_2, _3, _4, _5, _6, _1> rotateL6() {
        return into((_1, _2, _3, _4, _5, _6) -> product(_2, _3, _4, _5, _6, _1));
    }

    /**
     * Rotate the first six values of this product one slot to the right.
     *
     * @return the right-rotated product
     */
    default Product6<_6, _1, _2, _3, _4, _5> rotateR6() {
        return into((_1, _2, _3, _4, _5, _6) -> product(_6, _1, _2, _3, _4, _5));
    }

    @Override
    default Product6<_2, _3, _4, _5, _1, _6> rotateL5() {
        return into((_1, _2, _3, _4, _5, _6) -> product(_2, _3, _4, _5, _1, _6));
    }

    @Override
    default Product6<_5, _1, _2, _3, _4, _6> rotateR5() {
        return into((_1, _2, _3, _4, _5, _6) -> product(_5, _1, _2, _3, _4, _6));
    }

    @Override
    default Product6<_2, _3, _4, _1, _5, _6> rotateL4() {
        return into((_1, _2, _3, _4, _5, _6) -> product(_2, _3, _4, _1, _5, _6));
    }

    @Override
    default Product6<_4, _1, _2, _3, _5, _6> rotateR4() {
        return into((_1, _2, _3, _4, _5, _6) -> product(_4, _1, _2, _3, _5, _6));
    }

    @Override
    default Product6<_2, _3, _1, _4, _5, _6> rotateL3() {
        return into((_1, _2, _3, _4, _5, _6) -> product(_2, _3, _1, _4, _5, _6));
    }

    @Override
    default Product6<_3, _1, _2, _4, _5, _6> rotateR3() {
        return into((_1, _2, _3, _4, _5, _6) -> product(_3, _1, _2, _4, _5, _6));
    }

    @Override
    default Product6<_2, _1, _3, _4, _5, _6> invert() {
        return into((_1, _2, _3, _4, _5, _6) -> product(_2, _1, _3, _4, _5, _6));
    }

    /**
     * Static factory method for creating a generic {@link Product6}.
     *
     * @param _1   the first slot
     * @param _2   the second slot
     * @param _3   the third slot
     * @param _4   the fourth slot
     * @param _5   the fifth slot
     * @param _6   the sixth slot
     * @param <_1> the first slot type
     * @param <_2> the second slot type
     * @param <_3> the third slot type
     * @param <_4> the fourth slot type
     * @param <_5> the fifth slot type
     * @param <_6> the sixth slot type
     * @return the {@link Product6}
     */
    static <_1, _2, _3, _4, _5, _6> Product6<_1, _2, _3, _4, _5, _6> product(_1 _1, _2 _2, _3 _3, _4 _4, _5 _5,
                                                                             _6 _6) {
        return new Product6<_1, _2, _3, _4, _5, _6>() {
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
        };
    }
}
