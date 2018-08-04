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

    /**
     * Rotate the first five values of this product one slot to the left.
     *
     * @return the left-rotated product
     */
    default Product5<_2, _3, _4, _5, _1> rotateL5() {
        return into((_1, _2, _3, _4, _5) -> product(_2, _3, _4, _5, _1));
    }

    /**
     * Rotate the first five values of this product one slot to the right.
     *
     * @return the right-rotated product
     */
    default Product5<_5, _1, _2, _3, _4> rotateR5() {
        return into((_1, _2, _3, _4, _5) -> product(_5, _1, _2, _3, _4));
    }

    @Override
    default Product5<_2, _3, _4, _1, _5> rotateL4() {
        return into((_1, _2, _3, _4, _5) -> product(_2, _3, _4, _1, _5));
    }

    @Override
    default Product5<_4, _1, _2, _3, _5> rotateR4() {
        return into((_1, _2, _3, _4, _5) -> product(_4, _1, _2, _3, _5));
    }

    @Override
    default Product5<_2, _3, _1, _4, _5> rotateL3() {
        return into((_1, _2, _3, _4, _5) -> product(_2, _3, _1, _4, _5));
    }

    @Override
    default Product5<_3, _1, _2, _4, _5> rotateR3() {
        return into((_1, _2, _3, _4, _5) -> product(_3, _1, _2, _4, _5));
    }

    @Override
    default Product5<_2, _1, _3, _4, _5> invert() {
        return into((_1, _2, _3, _4, _5) -> product(_2, _1, _3, _4, _5));
    }

    /**
     * Static factory method for creating a generic {@link Product5}.
     *
     * @param _1   the first slot
     * @param _2   the second slot
     * @param _3   the third slot
     * @param _4   the fourth slot
     * @param _5   the fifth slot
     * @param <_1> the first slot type
     * @param <_2> the second slot type
     * @param <_3> the third slot type
     * @param <_4> the fourth slot type
     * @param <_5> the fifth slot type
     * @return the {@link Product5}
     */
    static <_1, _2, _3, _4, _5> Product5<_1, _2, _3, _4, _5> product(_1 _1, _2 _2, _3 _3, _4 _4, _5 _5) {
        return new Product5<_1, _2, _3, _4, _5>() {
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
        };
    }
}
