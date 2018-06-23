package com.jnape.palatable.lambda.zipper;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monad.builtin.Cont;
import com.jnape.palatable.lambda.traversable.LambdaIterable;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Focus.focus;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Next.next;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Reset.reset;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Shift.shift;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Iterate.iterate;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;

public abstract class Zipper<A, TA extends Traversable<A, ?>>
        implements CoProduct2<TA, Tuple2<A, Function<Maybe<A>, Zipper<A, TA>>>, Zipper<A, TA>> {

    private Zipper() {
    }

    public static <A, T extends Traversable, TA extends Traversable<A, T>> Zipper<A, TA> zipper(TA ta) {
        return reset(ta.<A, Cont<Zipper<A, TA>, ?>, TA, Cont<Zipper<A, TA>, A>, Cont<Zipper<A, TA>, TA>>traverse(
                a -> shift(k -> new Cont<>(g -> g.apply(hole(a, maybeA -> k.apply(maybeA.orElse(a)))))),
                ta_ -> new Cont<>(g -> g.apply(ta_))).flatMap(trav -> new Cont<>(g -> g.apply(top(trav)))));
    }

    public static <A, TA extends Traversable<A, ?>> Zipper<A, TA> top(TA ta) {
        return new Top<>(ta);
    }

    public static <A, TA extends Traversable<A, ?>> Zipper<A, TA> hole(A a,
                                                                       Function<? super Maybe<A>, Zipper<A, TA>> nextFn) {
        return new Hole<>(a, fn1(nextFn));
    }

    public static void main(String[] args) {
        Zipper<Integer, LambdaIterable<Integer>> zipper = zipper(LambdaIterable.wrap(take(100000, iterate(x -> x + 1, 0))));

        System.out.println(focus(times(99_998, next(), zipper)));

        System.out.println(focus(zipper));
    }

    private static final class Top<A, TA extends Traversable<A, ?>> extends Zipper<A, TA> {
        private final TA ta;

        private Top(TA ta) {
            this.ta = ta;
        }

        @Override
        public <R> R match(Function<? super TA, ? extends R> aFn,
                           Function<? super Tuple2<A, Function<Maybe<A>, Zipper<A, TA>>>, ? extends R> bFn) {
            return aFn.apply(ta);
        }
    }

    private static final class Hole<A, TA extends Traversable<A, ?>> extends Zipper<A, TA> {
        private final A                            a;
        private final Fn1<Maybe<A>, Zipper<A, TA>> nextFn;

        private Hole(A a, Fn1<Maybe<A>, Zipper<A, TA>> nextFn) {
            this.a = a;
            this.nextFn = nextFn;
        }

        @Override
        public <R> R match(Function<? super TA, ? extends R> aFn,
                           Function<? super Tuple2<A, Function<Maybe<A>, Zipper<A, TA>>>, ? extends R> bFn) {
            return bFn.apply(tuple(a, nextFn));
        }
    }

}
