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
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.lens.lenses.HMapLens.valueAt;

/**
 * A lens that focuses on the {@link HList heterogeneous list} of values pointed at by one or more
 * {@link TypeSafeKey typesafe keys} that must all exist in the same {@link HMap} to be collectively extracted. Note
 * that if any of the keys is absent in the map, the result will be {@link Maybe#nothing()}.
 *
 * @param <Values> the {@link HList} of values to focus on
 * @see TypeSafeKey
 */
public interface Schema<Values extends HCons<?, ?>> extends Lens.Simple<HMap, Maybe<Values>> {

    @SuppressWarnings({"unchecked", "RedundantTypeArguments"})
    default <A, NewValues extends HCons<A, Values>> Schema<NewValues> add(TypeSafeKey<?, A> key) {
        Lens<HMap, HMap, Maybe<NewValues>, Maybe<NewValues>> lens = Lens.both(this, valueAt(key))
                .<Maybe<NewValues>>mapA(into((maybeValues, maybeA) -> maybeValues
                        .zip(maybeA.fmap(a -> values -> (NewValues) values.cons(a)))))
                .<Maybe<NewValues>>mapB(Both.both(maybeNewValues -> maybeNewValues.fmap(HCons<A, Values>::tail),
                                                  maybeNewValues -> maybeNewValues.fmap(HCons<A, Values>::head)));
        return new Schema<NewValues>() {
            @Override
            public <F extends Functor<?, F>, FT extends Functor<HMap, F>, FB extends Functor<Maybe<NewValues>, F>>
            FT apply(Function<? super Maybe<NewValues>, ? extends FB> fn, HMap hmap) {
                return lens.apply(fn, hmap);
            }
        };
    }

    static <A> Schema<SingletonHList<A>> schema(TypeSafeKey<?, A> key) {
        Lens<HMap, HMap, Maybe<SingletonHList<A>>, Maybe<SingletonHList<A>>> lens = valueAt(key)
                .mapA(ma -> ma.fmap(HList::singletonHList))
                .mapB(maybeSingletonA -> maybeSingletonA.fmap(HCons::head));
        return new Schema<SingletonHList<A>>() {
            @Override
            public <F extends Functor<?, F>, FT extends Functor<HMap, F>,
                    FB extends Functor<Maybe<SingletonHList<A>>, F>> FT apply(
                    Function<? super Maybe<SingletonHList<A>>, ? extends FB> fn, HMap hmap) {
                return lens.apply(fn, hmap);
            }
        };
    }

    static <A, B> Schema<Tuple2<A, B>> schema(TypeSafeKey<?, A> aKey,
                                              TypeSafeKey<?, B> bKey) {
        return schema(bKey).add(aKey);
    }

    static <A, B, C> Schema<Tuple3<A, B, C>> schema(TypeSafeKey<?, A> aKey,
                                                    TypeSafeKey<?, B> bKey,
                                                    TypeSafeKey<?, C> cKey) {
        return schema(bKey, cKey).add(aKey);
    }

    static <A, B, C, D> Schema<Tuple4<A, B, C, D>> schema(TypeSafeKey<?, A> aKey,
                                                          TypeSafeKey<?, B> bKey,
                                                          TypeSafeKey<?, C> cKey,
                                                          TypeSafeKey<?, D> dKey) {
        return schema(bKey, cKey, dKey).add(aKey);
    }


    static <A, B, C, D, E> Schema<Tuple5<A, B, C, D, E>> schema(TypeSafeKey<?, A> aKey,
                                                                TypeSafeKey<?, B> bKey,
                                                                TypeSafeKey<?, C> cKey,
                                                                TypeSafeKey<?, D> dKey,
                                                                TypeSafeKey<?, E> eKey) {
        return schema(bKey, cKey, dKey, eKey).add(aKey);
    }

    static <A, B, C, D, E, F> Schema<Tuple6<A, B, C, D, E, F>> schema(TypeSafeKey<?, A> aKey,
                                                                      TypeSafeKey<?, B> bKey,
                                                                      TypeSafeKey<?, C> cKey,
                                                                      TypeSafeKey<?, D> dKey,
                                                                      TypeSafeKey<?, E> eKey,
                                                                      TypeSafeKey<?, F> fKey) {
        return schema(bKey, cKey, dKey, eKey, fKey).add(aKey);
    }

    static <A, B, C, D, E, F, G> Schema<Tuple7<A, B, C, D, E, F, G>> schema(TypeSafeKey<?, A> aKey,
                                                                            TypeSafeKey<?, B> bKey,
                                                                            TypeSafeKey<?, C> cKey,
                                                                            TypeSafeKey<?, D> dKey,
                                                                            TypeSafeKey<?, E> eKey,
                                                                            TypeSafeKey<?, F> fKey,
                                                                            TypeSafeKey<?, G> gKey) {
        return schema(bKey, cKey, dKey, eKey, fKey, gKey).add(aKey);
    }

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
