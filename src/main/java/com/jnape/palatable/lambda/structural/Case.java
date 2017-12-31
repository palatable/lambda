package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.SingletonHList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into1.into1;

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

    public static <A, B, R> Total<Tuple2<A, B>, R> of(Fn2<? super A, ? super B, ? extends R> fn) {
        return new Total<>(into(fn.toBiFunction()));
    }

    public static <A, R> Total<SingletonHList<A>, R> of(CatchAll aPredicate,
                                                        Fn1<A, R> fn) {
        return new Total<>(into1(fn));
    }

    //todo: overload that explicitly takes Fn<Fields, R> ?

    public static <A, R> Partial<SingletonHList<A>, R> of(Predicate<A> pred,
                                                          Fn1<A, R> fn) {

        return new Partial<>(pred.contraMap(HCons::head), into1(fn));
    }

    public static <A, B, R> Total<Tuple2<A, B>, R> of(CatchAll __,
                                                      CatchAll ___,
                                                      Fn2<? super A, ? super B, ? extends R> fn) {
        return new Total<>(into(fn.toBiFunction()));
    }

    public static <A, B, R> Partial<Tuple2<A, B>, R> of(Predicate<? super A> aPredicate,
                                                        Predicate<? super B> bPredicate,
                                                        Fn2<? super A, ? super B, ? extends R> fn) {
        return new Partial<>(t -> aPredicate.test(t._1()) && bPredicate.test(t._2()),
                             into(fn.toBiFunction()));
    }
}
