package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.SingletonHList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into1.into1;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;

public abstract class Case {

    private Case() {
    }

    public static final class Partial<Fields extends HCons, R> extends Case implements Fn1<Fields, Maybe<R>> {

        private final Predicate<Fields>   pred;
        private final Function<Fields, R> fn;

        private Partial(Predicate<Fields> pred, Function<Fields, R> fn) {
            this.pred = pred;
            this.fn = fn;
        }

        @Override
        public Maybe<R> apply(Fields fields) {
            return just(fields).filter(pred).fmap(fn);
        }
    }

    public static final class Total<Fields extends HCons, R> extends Case implements Fn1<Fields, R> {
        private final Function<Fields, R> fn;

        private Total(Function<Fields, R> fn) {
            this.fn = fn;
        }

        @Override
        public R apply(Fields fields) {
            return fn.apply(fields);
        }
    }

    public static <A, R> Total<SingletonHList<A>, R> of(Function<? super A, ? extends R> fn) {
        return new Total<>(into1(fn));
    }

    public static <A, B, R> Total<Tuple2<A, B>, R> of(BiFunction<? super A, ? super B, ? extends R> fn) {
        return new Total<>(into(fn));
    }

    public static <A, B, C, R> Total<Tuple3<A, B, C>, R> of(Fn3<? super A, ? super B, ? super C, ? extends R> fn) {
        return new Total<>(into3(fn));
    }

    public static <A, B, C, D, R> Total<Tuple4<A, B, C, D>, R> of(
            Fn4<? super A, ? super B, ? super C, ? super D, ? extends R> fn) {
        return new Total<>(into4(fn));
    }


    public static <A, R> Total<SingletonHList<A>, R> of(CatchAll __, Function<? super A, ? extends R> fn) {
        return of(fn);
    }

    public static <A, B, R> Total<Tuple2<A, B>, R> of(CatchAll __,
                                                      CatchAll ___,
                                                      BiFunction<? super A, ? super B, ? extends R> fn) {
        return of(fn);
    }

    public static <A, B, C, R> Total<Tuple3<A, B, C>, R> of(CatchAll __,
                                                            CatchAll ___,
                                                            CatchAll ____,
                                                            Fn3<? super A, ? super B, ? super C, ? extends R> fn) {
        return Case.<A, B, C, R>of(fn);
    }

    public static <A, B, C, D, R> Total<Tuple4<A, B, C, D>, R> of(CatchAll __,
                                                                  CatchAll ___,
                                                                  CatchAll ____,
                                                                  CatchAll _____,
                                                                  Fn4<? super A, ? super B, ? super C, ? super D, ? extends R> fn) {
        return Case.<A, B, C, D, R>of(fn);
    }


    public static <A, R> Partial<SingletonHList<A>, R> of(Predicate<A> pred,
                                                          Fn1<A, R> fn) {
        return new Partial<>(pred.contraMap(HCons::head), into1(fn));
    }

//    public static <A, B, R> Partial<Tuple2<A, B>, R> of(Predicate<? super A> aPredicate,
//                                                        Predicate<? super B> bPredicate,
//                                                        BiFunction<? super A, ? super B, ? extends R> fn) {
//        return new Partial<>(t -> aPredicate.test(t._1()) && bPredicate.test(t._2()), into(fn));
//    }

    public static <A, APrime, B, BPrime, R> Partial<Tuple2<A, B>, R>of(Predicate<? super A> x,
                                                                       Predicate<? super B> y,
                                                                       BiFunction<? super A, ? super B, ? extends R> z) {
        throw new UnsupportedOperationException();
    }

    public static <A, APrime, B, BPrime, R> Partial<Tuple2<A, B>, R> of(
            DestructuringPredicate<? super A, ? extends APrime> aPredicate,
            DestructuringPredicate<? super B, ? extends BPrime> bPredicate,
            BiFunction<? super APrime, ? super BPrime, ? extends R> fn) {
        return new Partial<>(t -> aPredicate.test(t._1()) && bPredicate.test(t._2()),
                             into((a, b) -> fn.apply(aPredicate.destructure(a).orElse(null),
                                                     bPredicate.destructure(b).orElse(null))));
    }

    public static <A, B, C, R> Partial<Tuple3<A, B, C>, R> of(Predicate<? super A> aPredicate,
                                                              Predicate<? super B> bPredicate,
                                                              Predicate<? super C> cPredicate,
                                                              Fn3<? super A, ? super B, ? super C, ? extends R> fn) {
        return new Partial<>(t -> aPredicate.test(t._1()) && bPredicate.test(t._2()) && cPredicate.test(t._3()),
                             into3(fn));
    }

    @FunctionalInterface
    public static interface DestructuringPredicate<A, B> extends Predicate<A> {
        Maybe<B> destructure(A a);

        @Override
        default Boolean apply(A a) {
            return destructure(a).fmap(constantly(true)).orElse(false);
        }
    }

    public static <A, APrime, B, C, R> Partial<Tuple3<A, B, C>, R> of(
            DestructuringPredicate<? super A, ? extends APrime> aPredicate,
            Predicate<? super B> bPredicate,
            Predicate<? super C> cPredicate,
            Fn3<? super APrime, ? super B, ? super C, ? extends R> fn) {
        return new Partial<>(t -> aPredicate.test(t._1()) && bPredicate.test(t._2()) && cPredicate.test(t._3()),
                             into3((ap, b, c) -> fn.apply(aPredicate.destructure(ap).orElseGet(null), b, c)));
    }

    public static <A, APrime, B, BPrime, C, R> Partial<Tuple3<A, B, C>, R> of(
            DestructuringPredicate<? super A, ? extends APrime> aPredicate,
            DestructuringPredicate<? super B, ? extends BPrime> bPredicate,
            Predicate<? super C> cPredicate,
            Fn3<? super APrime, ? super BPrime, ? super C, ? extends R> fn) {
        return new Partial<>(t -> aPredicate.test(t._1()) && bPredicate.test(t._2()) && cPredicate.test(t._3()),
                             into3((a, b, c) -> fn.apply(aPredicate.destructure(a).orElseGet(null),
                                                         bPredicate.destructure(b).orElseGet(null),
                                                         c)));
    }

    public static <A, B, C, D, R> Partial<Tuple4<A, B, C, D>, R> of(Predicate<? super A> aPredicate,
                                                                    Predicate<? super B> bPredicate,
                                                                    Predicate<? super C> cPredicate,
                                                                    Predicate<? super D> dPredicate,
                                                                    Fn4<? super A, ? super B, ? super C, ? super D, ? extends R> fn) {
        return new Partial<>(t -> aPredicate.test(t._1()) && bPredicate.test(t._2()) && cPredicate.test(t._3()) && dPredicate.test(t._4()),
                             into4(fn));
    }

}
