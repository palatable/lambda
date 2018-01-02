package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.SingletonHList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;

import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

public interface Struct<Fields extends HCons> {

    Fields unapply();

    default <R> Maybe<R> match(Match.Partial<Fields, R> match) {
        return match.apply(this.unapply());
    }

    default <R> R match(Match.Total<Fields, R> match) {
        return match.apply(this.unapply());
    }

    static <Fields extends HCons> Struct<Fields> struct(Fields fields) {
        return () -> fields;
    }

    static <Fields extends HCons> Struct<Fields> struct(Supplier<Fields> fieldsSupplier) {
        return fieldsSupplier::get;
    }

    static <A, B> Struct._2<A, B> struct(Supplier<A> aSupplier,
                                         Supplier<B> bSupplier) {
        return () -> tuple(aSupplier.get(), bSupplier.get());
    }

    static <A, B, C> Struct._3<A, B, C> struct(Supplier<A> aSupplier,
                                               Supplier<B> bSupplier,
                                               Supplier<C> cSupplier) {
        return () -> tuple(aSupplier.get(), bSupplier.get(), cSupplier.get());
    }

    static <A, B, C, D> Struct._4<A, B, C, D> struct(Supplier<A> aSupplier,
                                                     Supplier<B> bSupplier,
                                                     Supplier<C> cSupplier,
                                                     Supplier<D> dSupplier) {
        return () -> tuple(aSupplier.get(), bSupplier.get(), cSupplier.get(), dSupplier.get());
    }

    static <A, B, C, D, E> Struct._5<A, B, C, D, E> struct(Supplier<A> aSupplier,
                                                           Supplier<B> bSupplier,
                                                           Supplier<C> cSupplier,
                                                           Supplier<D> dSupplier,
                                                           Supplier<E> eSupplier) {
        return () -> tuple(aSupplier.get(), bSupplier.get(), cSupplier.get(), dSupplier.get(), eSupplier.get());
    }

    static <A, B, C, D, E, F> Struct._6<A, B, C, D, E, F> struct(Supplier<A> aSupplier,
                                                                 Supplier<B> bSupplier,
                                                                 Supplier<C> cSupplier,
                                                                 Supplier<D> dSupplier,
                                                                 Supplier<E> eSupplier,
                                                                 Supplier<F> fSupplier) {
        return () -> tuple(aSupplier.get(), bSupplier.get(), cSupplier.get(), dSupplier.get(), eSupplier.get(), fSupplier.get());
    }

    static <A, B, C, D, E, F, G> Struct._7<A, B, C, D, E, F, G> struct(Supplier<A> aSupplier,
                                                                       Supplier<B> bSupplier,
                                                                       Supplier<C> cSupplier,
                                                                       Supplier<D> dSupplier,
                                                                       Supplier<E> eSupplier,
                                                                       Supplier<F> fSupplier,
                                                                       Supplier<G> gSupplier) {
        return () -> tuple(aSupplier.get(), bSupplier.get(), cSupplier.get(), dSupplier.get(), eSupplier.get(), fSupplier.get(), gSupplier.get());
    }

    static <A, B, C, D, E, F, G, H> Struct._8<A, B, C, D, E, F, G, H> struct(Supplier<A> aSupplier,
                                                                             Supplier<B> bSupplier,
                                                                             Supplier<C> cSupplier,
                                                                             Supplier<D> dSupplier,
                                                                             Supplier<E> eSupplier,
                                                                             Supplier<F> fSupplier,
                                                                             Supplier<G> gSupplier,
                                                                             Supplier<H> hSupplier) {
        return () -> tuple(aSupplier.get(), bSupplier.get(), cSupplier.get(), dSupplier.get(), eSupplier.get(), fSupplier.get(), gSupplier.get(),
                           hSupplier.get());
    }

    interface _1<A> extends Struct<SingletonHList<A>> {
    }

    interface _2<A, B> extends Struct<Tuple2<A, B>> {
    }

    interface _3<A, B, C> extends Struct<Tuple3<A, B, C>> {
    }

    interface _4<A, B, C, D> extends Struct<Tuple4<A, B, C, D>> {
    }

    interface _5<A, B, C, D, E> extends Struct<Tuple5<A, B, C, D, E>> {
    }

    interface _6<A, B, C, D, E, F> extends Struct<Tuple6<A, B, C, D, E, F>> {
    }

    interface _7<A, B, C, D, E, F, G> extends Struct<Tuple7<A, B, C, D, E, F, G>> {
    }

    interface _8<A, B, C, D, E, F, G, H> extends Struct<Tuple8<A, B, C, D, E, F, G, H>> {
    }
}
