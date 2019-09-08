package com.jnape.palatable.lambda.adt.hmap;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.SingletonHList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.builtin.fn2.Both;
import com.jnape.palatable.lambda.functor.Cartesian;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.optics.Lens;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.optics.lenses.HMapLens.valueAt;

/**
 * A lens that focuses on the {@link HList heterogeneous list} of values pointed at by one or more
 * {@link TypeSafeKey typesafe keys} that must all exist in the same {@link HMap} to be collectively extracted. Note
 * that if any of the keys is absent in the map, the result will be {@link Maybe#nothing()}.
 *
 * @param <Values> the {@link HList} of values to focus on
 * @see TypeSafeKey
 */
public interface Schema<Values extends HList> extends Lens.Simple<HMap, Maybe<Values>> {

    /**
     * Add a new {@link TypeSafeKey} to the head of this {@link Schema}.
     *
     * @param key         the new head key
     * @param <A>         the value the head key focuses on
     * @param <NewValues> the new {@link HCons} of values
     * @return the updated {@link Schema}
     */
    @SuppressWarnings({"unchecked", "RedundantTypeArguments"})
    default <A, NewValues extends HCons<A, Values>> Schema<NewValues> add(TypeSafeKey<?, A> key) {
        Lens<HMap, HMap, Maybe<NewValues>, Maybe<NewValues>> lens = Lens.both(this, valueAt(key))
                .<Maybe<NewValues>>mapA(into((maybeValues, maybeA) -> maybeValues
                        .zip(maybeA.fmap(a -> values -> (NewValues) values.cons(a)))))
                .<Maybe<NewValues>>mapB(Both.both(maybeNewValues -> maybeNewValues.fmap(HCons<A, Values>::tail),
                                                  maybeNewValues -> maybeNewValues.fmap(HCons<A, Values>::head)));
        return new Schema<NewValues>() {
            @Override
            public <CoP extends Profunctor<?, ?, ? extends Cartesian<?, ?, ?>>,
                    CoF extends Functor<?, ? extends Functor<?, ?>>,
                    FB extends Functor<Maybe<NewValues>, ? extends CoF>,
                    FT extends Functor<HMap, ? extends CoF>,
                    PAFB extends Profunctor<Maybe<NewValues>, FB, ? extends CoP>,
                    PSFT extends Profunctor<HMap, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
                return lens.apply(pafb);
            }
        };
    }

    /**
     * Create a {@link Schema} from a single {@link TypeSafeKey}.
     *
     * @param key the {@link TypeSafeKey}
     * @param <A> the type of value the key focuses on
     * @return the {@link Schema}
     */
    static <A> Schema<SingletonHList<A>> schema(TypeSafeKey<?, A> key) {
        Lens<HMap, HMap, Maybe<SingletonHList<A>>, Maybe<SingletonHList<A>>> lens = valueAt(key)
                .mapA(ma -> ma.fmap(HList::singletonHList))
                .mapB(maybeSingletonA -> maybeSingletonA.fmap(HCons::head));
        return new Schema<SingletonHList<A>>() {
            @Override
            public <CoP extends Profunctor<?, ?, ? extends Cartesian<?, ?, ?>>,
                    CoF extends Functor<?, ? extends Functor<?, ?>>,
                    FB extends Functor<Maybe<SingletonHList<A>>, ? extends CoF>,
                    FT extends Functor<HMap, ? extends CoF>,
                    PAFB extends Profunctor<Maybe<SingletonHList<A>>, FB, ? extends CoP>,
                    PSFT extends Profunctor<HMap, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
                return lens.apply(pafb);
            }
        };
    }

    /**
     * Create a {@link Schema} from two {@link TypeSafeKey TypeSafeKeys}.
     *
     * @param aKey the first {@link TypeSafeKey}
     * @param bKey the second {@link TypeSafeKey}
     * @param <A>  the type of value the first key focuses on
     * @param <B>  the type of value the second key focuses on
     * @return the {@link Schema}
     */
    static <A, B> Schema<Tuple2<A, B>> schema(TypeSafeKey<?, A> aKey,
                                              TypeSafeKey<?, B> bKey) {
        return schema(bKey).add(aKey);
    }

    /**
     * Create a {@link Schema} from three {@link TypeSafeKey TypeSafeKeys}.
     *
     * @param aKey the first {@link TypeSafeKey}
     * @param bKey the second {@link TypeSafeKey}
     * @param cKey the third {@link TypeSafeKey}
     * @param <A>  the type of value the first key focuses on
     * @param <B>  the type of value the second key focuses on
     * @param <C>  the type of value the third key focuses on
     * @return the {@link Schema}
     */
    static <A, B, C> Schema<Tuple3<A, B, C>> schema(TypeSafeKey<?, A> aKey,
                                                    TypeSafeKey<?, B> bKey,
                                                    TypeSafeKey<?, C> cKey) {
        return schema(bKey, cKey).add(aKey);
    }

    /**
     * Create a {@link Schema} from four {@link TypeSafeKey TypeSafeKeys}.
     *
     * @param aKey the first {@link TypeSafeKey}
     * @param bKey the second {@link TypeSafeKey}
     * @param cKey the third {@link TypeSafeKey}
     * @param dKey the fourth {@link TypeSafeKey}
     * @param <A>  the type of value the first key focuses on
     * @param <B>  the type of value the second key focuses on
     * @param <C>  the type of value the third key focuses on
     * @param <D>  the type of value the fourth key focuses on
     * @return the {@link Schema}
     */
    static <A, B, C, D> Schema<Tuple4<A, B, C, D>> schema(TypeSafeKey<?, A> aKey,
                                                          TypeSafeKey<?, B> bKey,
                                                          TypeSafeKey<?, C> cKey,
                                                          TypeSafeKey<?, D> dKey) {
        return schema(bKey, cKey, dKey).add(aKey);
    }

    /**
     * Create a {@link Schema} from five {@link TypeSafeKey TypeSafeKeys}.
     *
     * @param aKey the first {@link TypeSafeKey}
     * @param bKey the second {@link TypeSafeKey}
     * @param cKey the third {@link TypeSafeKey}
     * @param dKey the fourth {@link TypeSafeKey}
     * @param eKey the fifth {@link TypeSafeKey}
     * @param <A>  the type of value the first key focuses on
     * @param <B>  the type of value the second key focuses on
     * @param <C>  the type of value the third key focuses on
     * @param <D>  the type of value the fourth key focuses on
     * @param <E>  the type of value the fifth key focuses on
     * @return the {@link Schema}
     */
    static <A, B, C, D, E> Schema<Tuple5<A, B, C, D, E>> schema(TypeSafeKey<?, A> aKey,
                                                                TypeSafeKey<?, B> bKey,
                                                                TypeSafeKey<?, C> cKey,
                                                                TypeSafeKey<?, D> dKey,
                                                                TypeSafeKey<?, E> eKey) {
        return schema(bKey, cKey, dKey, eKey).add(aKey);
    }

    /**
     * Create a {@link Schema} from six {@link TypeSafeKey TypeSafeKeys}.
     *
     * @param aKey the first {@link TypeSafeKey}
     * @param bKey the second {@link TypeSafeKey}
     * @param cKey the third {@link TypeSafeKey}
     * @param dKey the fourth {@link TypeSafeKey}
     * @param eKey the fifth {@link TypeSafeKey}
     * @param fKey the sixth {@link TypeSafeKey}
     * @param <A>  the type of value the first key focuses on
     * @param <B>  the type of value the second key focuses on
     * @param <C>  the type of value the third key focuses on
     * @param <D>  the type of value the fourth key focuses on
     * @param <E>  the type of value the fifth key focuses on
     * @param <F>  the type of value the sixth key focuses on
     * @return the {@link Schema}
     */
    static <A, B, C, D, E, F> Schema<Tuple6<A, B, C, D, E, F>> schema(TypeSafeKey<?, A> aKey,
                                                                      TypeSafeKey<?, B> bKey,
                                                                      TypeSafeKey<?, C> cKey,
                                                                      TypeSafeKey<?, D> dKey,
                                                                      TypeSafeKey<?, E> eKey,
                                                                      TypeSafeKey<?, F> fKey) {
        return schema(bKey, cKey, dKey, eKey, fKey).add(aKey);
    }

    /**
     * Create a {@link Schema} from seven {@link TypeSafeKey TypeSafeKeys}.
     *
     * @param aKey the first {@link TypeSafeKey}
     * @param bKey the second {@link TypeSafeKey}
     * @param cKey the third {@link TypeSafeKey}
     * @param dKey the fourth {@link TypeSafeKey}
     * @param eKey the fifth {@link TypeSafeKey}
     * @param fKey the sixth {@link TypeSafeKey}
     * @param gKey the seventh {@link TypeSafeKey}
     * @param <A>  the type of value the first key focuses on
     * @param <B>  the type of value the second key focuses on
     * @param <C>  the type of value the third key focuses on
     * @param <D>  the type of value the fourth key focuses on
     * @param <E>  the type of value the fifth key focuses on
     * @param <F>  the type of value the sixth key focuses on
     * @param <G>  the type of value the seventh key focuses on
     * @return the {@link Schema}
     */
    static <A, B, C, D, E, F, G> Schema<Tuple7<A, B, C, D, E, F, G>> schema(TypeSafeKey<?, A> aKey,
                                                                            TypeSafeKey<?, B> bKey,
                                                                            TypeSafeKey<?, C> cKey,
                                                                            TypeSafeKey<?, D> dKey,
                                                                            TypeSafeKey<?, E> eKey,
                                                                            TypeSafeKey<?, F> fKey,
                                                                            TypeSafeKey<?, G> gKey) {
        return schema(bKey, cKey, dKey, eKey, fKey, gKey).add(aKey);
    }

    /**
     * Create a {@link Schema} from eight {@link TypeSafeKey TypeSafeKeys}.
     *
     * @param aKey the first {@link TypeSafeKey}
     * @param bKey the second {@link TypeSafeKey}
     * @param cKey the third {@link TypeSafeKey}
     * @param dKey the fourth {@link TypeSafeKey}
     * @param eKey the fifth {@link TypeSafeKey}
     * @param fKey the sixth {@link TypeSafeKey}
     * @param gKey the seventh {@link TypeSafeKey}
     * @param hKey the eighth {@link TypeSafeKey}
     * @param <A>  the type of value the first key focuses on
     * @param <B>  the type of value the second key focuses on
     * @param <C>  the type of value the third key focuses on
     * @param <D>  the type of value the fourth key focuses on
     * @param <E>  the type of value the fifth key focuses on
     * @param <F>  the type of value the sixth key focuses on
     * @param <G>  the type of value the seventh key focuses on
     * @param <H>  the type of value the eighth key focuses on
     * @return the {@link Schema}
     */
    static <A, B, C, D, E, F, G, H> Schema<Tuple8<A, B, C, D, E, F, G, H>> schema(TypeSafeKey<?, A> aKey,
                                                                                  TypeSafeKey<?, B> bKey,
                                                                                  TypeSafeKey<?, C> cKey,
                                                                                  TypeSafeKey<?, D> dKey,
                                                                                  TypeSafeKey<?, E> eKey,
                                                                                  TypeSafeKey<?, F> fKey,
                                                                                  TypeSafeKey<?, G> gKey,
                                                                                  TypeSafeKey<?, H> hKey) {
        return schema(bKey, cKey, dKey, eKey, fKey, gKey, hKey).add(aKey);
    }
}
