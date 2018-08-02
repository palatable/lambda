package com.jnape.palatable.lambda.adt.product;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * The minimal shape of the combination of two potentially distinctly typed values, supporting destructuring via
 * explicitly named indexing methods, as well as via a combining function.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Product_type">products</a>.
 *
 * @param <_1> The first element type
 * @param <_2> The second element type
 * @see Tuple2
 */
public interface Product2<_1, _2> extends Map.Entry<_1, _2> {

    /**
     * Retrieve the first element.
     *
     * @return the first element
     */
    _1 _1();

    /**
     * Retrieve the second element.
     *
     * @return the second element
     */
    _2 _2();

    /**
     * Destructure and apply this product to a function accepting the same number of arguments as this product's
     * slots. This can be thought of as a kind of dual to uncurrying a function and applying a product to it.
     *
     * @param fn  the function to apply
     * @param <R> the return type of the function
     * @return the result of applying the destructured product to the function
     */
    default <R> R into(BiFunction<? super _1, ? super _2, ? extends R> fn) {
        return fn.apply(_1(), _2());
    }

    /**
     * Flip the slots of this {@link Product2}.
     *
     * @return the flipped {@link Product2}
     */
    default Product2<_2, _1> invert() {
        return into((_1, _2) -> product(_2, _1));
    }

    @Override
    default _1 getKey() {
        return _1();
    }

    @Override
    default _2 getValue() {
        return _2();
    }

    @Override
    default _2 setValue(_2 value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Static factory method for creating a generic {@link Product2}.
     *
     * @param _1   the first slot
     * @param _2   the second slot
     * @param <_1> the first slot type
     * @param <_2> the second slot type
     * @return the {@link Product2}
     */
    static <_1, _2> Product2<_1, _2> product(_1 _1, _2 _2) {
        return new Product2<_1, _2>() {
            @Override
            public _1 _1() {
                return _1;
            }

            @Override
            public _2 _2() {
                return _2;
            }
        };
    }
}
