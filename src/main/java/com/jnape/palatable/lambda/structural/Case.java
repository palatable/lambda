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
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functions.Fn6;
import com.jnape.palatable.lambda.functions.Fn7;
import com.jnape.palatable.lambda.functions.Fn8;
import com.jnape.palatable.lambda.structural.Matchers.Any;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into1.into1;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into3.into3;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into4.into4;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into5.into5;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into6.into6;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into7.into7;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into8.into8;

public abstract class Case {

    private Case() {
    }

    public static final class Partial<Fields extends HCons, R> extends Case implements Fn1<Fields, Maybe<R>> {

        private final Function<Fields, Maybe<R>> fn;

        private Partial(Function<Fields, Maybe<R>> fn) {
            this.fn = fn;
        }

        @Override
        public Maybe<R> apply(Fields fields) {
            return just(fields).flatMap(fn);
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

    public static <A, B, C, D, E, R> Total<Tuple5<A, B, C, D, E>, R> of(
            Fn5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends R> fn) {
        return new Total<>(into5(fn));
    }

    public static <A, B, C, D, E, F, R> Total<Tuple6<A, B, C, D, E, F>, R> of(
            Fn6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends R> fn) {
        return new Total<>(into6(fn));
    }

    public static <A, B, C, D, E, F, G, R> Total<Tuple7<A, B, C, D, E, F, G>, R> of(
            Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends R> fn) {
        return new Total<>(into7(fn));
    }

    public static <A, B, C, D, E, F, G, H, R> Total<Tuple8<A, B, C, D, E, F, G, H>, R> of(
            Fn8<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? extends R> fn) {
        return new Total<>(into8(fn));
    }

    @SuppressWarnings("unused")
    public static <A, R> Total<SingletonHList<A>, R> of(Any<A> __, Function<? super A, ? extends R> fn) {
        return of(fn);
    }

    @SuppressWarnings("unused")
    public static <A, B, R> Total<Tuple2<A, B>, R> of(Any<A> __,
                                                      Any<B> ___,
                                                      BiFunction<? super A, ? super B, ? extends R> fn) {
        return of(fn);
    }

    @SuppressWarnings({"RedundantTypeArguments", "unused"})
    public static <A, B, C, R> Total<Tuple3<A, B, C>, R> of(Any<A> __,
                                                            Any<B> ___,
                                                            Any<C> ____,
                                                            Fn3<? super A, ? super B, ? super C, ? extends R> fn) {
        return Case.<A, B, C, R>of(fn);
    }

    @SuppressWarnings({"RedundantTypeArguments", "unused"})
    public static <A, B, C, D, R> Total<Tuple4<A, B, C, D>, R> of(Any __,
                                                                  Any ___,
                                                                  Any ____,
                                                                  Any _____,
                                                                  Fn4<? super A, ? super B, ? super C, ? super D, ? extends R> fn) {
        return Case.<A, B, C, D, R>of(fn);
    }

    @SuppressWarnings({"RedundantTypeArguments", "unused"})
    public static <A, B, C, D, E, R> Total<Tuple5<A, B, C, D, E>, R> of(Any __,
                                                                        Any ___,
                                                                        Any ____,
                                                                        Any _____,
                                                                        Any ______,
                                                                        Fn5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends R> fn) {
        return Case.<A, B, C, D, E, R>of(fn);
    }

    @SuppressWarnings({"RedundantTypeArguments", "unused"})
    public static <A, B, C, D, E, F, R> Total<Tuple6<A, B, C, D, E, F>, R> of(Any __,
                                                                              Any ___,
                                                                              Any ____,
                                                                              Any _____,
                                                                              Any ______,
                                                                              Any _______,
                                                                              Fn6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends R> fn) {
        return Case.<A, B, C, D, E, F, R>of(fn);
    }

    @SuppressWarnings({"RedundantTypeArguments", "unused"})
    public static <A, B, C, D, E, F, G, R> Total<Tuple7<A, B, C, D, E, F, G>, R> of(Any __,
                                                                                    Any ___,
                                                                                    Any ____,
                                                                                    Any _____,
                                                                                    Any ______,
                                                                                    Any _______,
                                                                                    Any ________,
                                                                                    Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends R> fn) {
        return Case.<A, B, C, D, E, F, G, R>of(fn);
    }

    @SuppressWarnings({"RedundantTypeArguments", "unused"})
    public static <A, B, C, D, E, F, G, H, R> Total<Tuple8<A, B, C, D, E, F, G, H>, R> of(Any __,
                                                                                          Any ___,
                                                                                          Any ____,
                                                                                          Any _____,
                                                                                          Any ______,
                                                                                          Any _______,
                                                                                          Any ________,
                                                                                          Any _________,
                                                                                          Fn8<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? extends R> fn) {
        return Case.<A, B, C, D, E, F, G, H, R>of(fn);
    }


    public static <A, APrime, R> Partial<SingletonHList<A>, R> of(Matcher<? super A, ? extends APrime> aMatcher,
                                                                  Function<? super APrime, ? extends R> body) {
        return new Partial<>(into1(a -> aMatcher.apply(a).fmap(body)));
    }

    public static <A, APrime, B, BPrime, R> Partial<Tuple2<A, B>, R> of(
            Matcher<? super A, ? extends APrime> aMatcher,
            Matcher<? super B, ? extends BPrime> bMatcher,
            BiFunction<? super APrime, ? super BPrime, ? extends R> body) {
        return new Partial<>(into((a, b) -> aMatcher.apply(a).flatMap(aPrime -> bMatcher.apply(b).fmap(bPrime -> body.apply(aPrime, bPrime)))));
    }

    public static <A, APrime, B, BPrime, C, CPrime, R> Partial<Tuple3<A, B, C>, R> of(
            Matcher<? super A, ? extends APrime> aMatcher,
            Matcher<? super B, ? extends BPrime> bMatcher,
            Matcher<? super C, ? extends CPrime> cMatcher,
            Fn3<? super APrime, ? super BPrime, ? super CPrime, ? extends R> body) {
        return new Partial<>(into3((a, b, c) -> aMatcher.apply(a)
                .flatMap(aPrime -> bMatcher.apply(b)
                        .flatMap(bPrime -> cMatcher.apply(c)
                                .fmap(cPrime -> body.apply(aPrime, bPrime, cPrime))))));
    }

    public static <A, APrime, B, BPrime, C, CPrime, D, DPrime, R> Partial<Tuple4<A, B, C, D>, R> of(
            Matcher<? super A, ? extends APrime> aMatcher,
            Matcher<? super B, ? extends BPrime> bMatcher,
            Matcher<? super C, ? extends CPrime> cMatcher,
            Matcher<? super D, ? extends DPrime> dMatcher,
            Fn4<? super APrime, ? super BPrime, ? super CPrime, ? super DPrime, ? extends R> body) {
        return new Partial<>(into4((a, b, c, d) -> aMatcher.apply(a)
                .flatMap(aPrime -> bMatcher.apply(b)
                        .flatMap(bPrime -> cMatcher.apply(c)
                                .flatMap(cPrime -> dMatcher.apply(d)
                                        .fmap(dPrime -> body.apply(aPrime, bPrime, cPrime, dPrime)))))));
    }


    public static <A, APrime, B, BPrime, C, CPrime, D, DPrime, E, EPrime, R> Partial<Tuple5<A, B, C, D, E>, R> of(
            Matcher<? super A, ? extends APrime> aMatcher,
            Matcher<? super B, ? extends BPrime> bMatcher,
            Matcher<? super C, ? extends CPrime> cMatcher,
            Matcher<? super D, ? extends DPrime> dMatcher,
            Matcher<? super E, ? extends EPrime> eMatcher,
            Fn5<? super APrime, ? super BPrime, ? super CPrime, ? super DPrime, ? super EPrime, ? extends R> body) {
        return new Partial<>(into5((a, b, c, d, e) -> aMatcher.apply(a)
                .flatMap(aPrime -> bMatcher.apply(b)
                        .flatMap(bPrime -> cMatcher.apply(c)
                                .flatMap(cPrime -> dMatcher.apply(d)
                                        .flatMap(dPrime -> eMatcher.apply(e)
                                                .fmap(ePrime -> body.apply(aPrime, bPrime, cPrime, dPrime, ePrime))))))));
    }


    public static <A, APrime, B, BPrime, C, CPrime, D, DPrime, E, EPrime, F, FPrime, R> Partial<Tuple6<A, B, C, D, E, F>, R> of(
            Matcher<? super A, ? extends APrime> aMatcher,
            Matcher<? super B, ? extends BPrime> bMatcher,
            Matcher<? super C, ? extends CPrime> cMatcher,
            Matcher<? super D, ? extends DPrime> dMatcher,
            Matcher<? super E, ? extends EPrime> eMatcher,
            Matcher<? super F, ? extends FPrime> fMatcher,
            Fn6<? super APrime, ? super BPrime, ? super CPrime, ? super DPrime, ? super EPrime, ? super FPrime, ? extends R> body) {
        //todo: for-comprehensions, lest any consumers needing to flatMap 6 levels deep instead choose death's sweet embrace
        return new Partial<>(into6((a, b, c, d, e, f) -> aMatcher.apply(a)
                .flatMap(aPrime -> bMatcher.apply(b)
                        .flatMap(bPrime -> cMatcher.apply(c)
                                .flatMap(cPrime -> dMatcher.apply(d)
                                        .flatMap(dPrime -> eMatcher.apply(e)
                                                .flatMap(ePrime -> fMatcher.apply(f)
                                                        .fmap(fPrime -> body.apply(aPrime, bPrime, cPrime, dPrime, ePrime, fPrime)))))))));
    }

    public static <A, APrime, B, BPrime, C, CPrime, D, DPrime, E, EPrime, F, FPrime, G, GPrime, R> Partial<Tuple7<A, B, C, D, E, F, G>, R> of(
            Matcher<? super A, ? extends APrime> aMatcher,
            Matcher<? super B, ? extends BPrime> bMatcher,
            Matcher<? super C, ? extends CPrime> cMatcher,
            Matcher<? super D, ? extends DPrime> dMatcher,
            Matcher<? super E, ? extends EPrime> eMatcher,
            Matcher<? super F, ? extends FPrime> fMatcher,
            Matcher<? super G, ? extends GPrime> gMatcher,
            Fn7<? super APrime, ? super BPrime, ? super CPrime, ? super DPrime, ? super EPrime, ? super FPrime, ? super GPrime, ? extends R> body) {
        return new Partial<>(into7((a, b, c, d, e, f, g) -> aMatcher.apply(a)
                .flatMap(aPrime -> bMatcher.apply(b)
                        .flatMap(bPrime -> cMatcher.apply(c)
                                .flatMap(cPrime -> dMatcher.apply(d)
                                        .flatMap(dPrime -> eMatcher.apply(e)
                                                .flatMap(ePrime -> fMatcher.apply(f)
                                                        .flatMap(fPrime -> gMatcher.apply(g)
                                                                .fmap(gPrime -> body.apply(aPrime, bPrime, cPrime, dPrime, ePrime, fPrime, gPrime))))))))));
    }

    public static <A, APrime, B, BPrime, C, CPrime, D, DPrime, E, EPrime, F, FPrime, G, GPrime, H, HPrime, R> Partial<Tuple8<A, B, C, D, E, F, G, H>, R> of(
            Matcher<? super A, ? extends APrime> aMatcher,
            Matcher<? super B, ? extends BPrime> bMatcher,
            Matcher<? super C, ? extends CPrime> cMatcher,
            Matcher<? super D, ? extends DPrime> dMatcher,
            Matcher<? super E, ? extends EPrime> eMatcher,
            Matcher<? super F, ? extends FPrime> fMatcher,
            Matcher<? super G, ? extends GPrime> gMatcher,
            Matcher<? super H, ? extends HPrime> hMatcher,
            Fn8<? super APrime, ? super BPrime, ? super CPrime, ? super DPrime, ? super EPrime, ? super FPrime, ? super GPrime, ? super HPrime, ? extends R> body) {
        return new Partial<>(into8((a, b, c, d, e, f, g, h) -> aMatcher.apply(a)
                .flatMap(aPrime -> bMatcher.apply(b)
                        .flatMap(bPrime -> cMatcher.apply(c)
                                .flatMap(cPrime -> dMatcher.apply(d)
                                        .flatMap(dPrime -> eMatcher.apply(e)
                                                .flatMap(ePrime -> fMatcher.apply(f)
                                                        .flatMap(fPrime -> gMatcher.apply(g)
                                                                .flatMap(gPrime -> hMatcher.apply(h)
                                                                        .fmap(hPrime -> body.apply(aPrime, bPrime, cPrime, dPrime, ePrime, fPrime, gPrime, hPrime)))))))))));
    }

}
