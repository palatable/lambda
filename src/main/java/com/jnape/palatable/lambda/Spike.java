package com.jnape.palatable.lambda;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functions.Fn6;
import com.jnape.palatable.lambda.functions.Fn7;
import com.jnape.palatable.lambda.functions.Fn8;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.structural.Struct;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn2.fn2;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.structural.Case.of;
import static com.jnape.palatable.lambda.structural.Cases.cases;
import static com.jnape.palatable.lambda.structural.Matcher.$;
import static com.jnape.palatable.lambda.structural.Matcher.__;
import static com.jnape.palatable.lambda.structural.Matchers.$just;
import static com.jnape.palatable.lambda.structural.Matchers.$right;
import static com.jnape.palatable.lambda.structural.Struct.struct;
import static java.lang.Integer.parseInt;

public class Spike {

    public static <A, R, App extends Applicative, AppR extends Applicative<R, App>> AppR liftA(Applicative<A, App> appA,
                                                                                               Function<? super A, ? extends R> fn) {
        return appA.<R>fmap(fn).coerce();
    }

    public static <A, B, R, App extends Applicative, AppR extends Applicative<R, App>> AppR liftA(
            Applicative<A, App> appA,
            Applicative<B, App> appB,
            BiFunction<? super A, ? super B, ? extends R> fn) {
        return appB.<R>zip(appA.fmap(fn2(fn))).coerce();
    }

    public static <A, B, C, R, App extends Applicative, AppR extends Applicative<R, App>> AppR liftA(
            Applicative<A, App> appA,
            Applicative<B, App> appB,
            Applicative<C, App> appC,
            Fn3<? super A, ? super B, ? super C, ? extends R> fn) {
        return appC.<R>zip(liftA(appA, appB, fn.toBiFunction())).coerce();
    }

    public static <A, B, C, D, R, App extends Applicative, AppR extends Applicative<R, App>> AppR liftA(
            Applicative<A, App> appA,
            Applicative<B, App> appB,
            Applicative<C, App> appC,
            Applicative<D, App> appD,
            Fn4<? super A, ? super B, ? super C, ? super D, ? extends R> fn) {
        return appD.<R>zip(liftA(appA, appB, appC, fn)).coerce();
    }

    public static <A, B, C, D, E, R, App extends Applicative, AppR extends Applicative<R, App>> AppR liftA(
            Applicative<A, App> appA,
            Applicative<B, App> appB,
            Applicative<C, App> appC,
            Applicative<D, App> appD,
            Applicative<E, App> appE,
            Fn5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends R> fn) {
        return appE.<R>zip(liftA(appA, appB, appC, appD, fn)).coerce();
    }

    public static <A, B, C, D, E, F, R, App extends Applicative, AppR extends Applicative<R, App>> AppR liftA(
            Applicative<A, App> appA,
            Applicative<B, App> appB,
            Applicative<C, App> appC,
            Applicative<D, App> appD,
            Applicative<E, App> appE,
            Applicative<F, App> appF,
            Fn6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends R> fn) {
        return appF.<R>zip(liftA(appA, appB, appC, appD, appE, fn)).coerce();
    }


    public static <A, B, C, D, E, F, G, R, App extends Applicative, AppR extends Applicative<R, App>> AppR liftA(
            Applicative<A, App> appA,
            Applicative<B, App> appB,
            Applicative<C, App> appC,
            Applicative<D, App> appD,
            Applicative<E, App> appE,
            Applicative<F, App> appF,
            Applicative<G, App> appG,
            Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends R> fn) {
        return appG.<R>zip(liftA(appA, appB, appC, appD, appE, appF, fn)).coerce();
    }

    public static <A, B, C, D, E, F, G, H, R, App extends Applicative, AppR extends Applicative<R, App>> AppR liftA(
            Applicative<A, App> appA,
            Applicative<B, App> appB,
            Applicative<C, App> appC,
            Applicative<D, App> appD,
            Applicative<E, App> appE,
            Applicative<F, App> appF,
            Applicative<G, App> appG,
            Applicative<H, App> appH,
            Fn8<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? extends R> fn) {
        return appH.<R>zip(liftA(appA, appB, appC, appD, appE, appF, appG, fn)).coerce();
    }


    public static void main(String[] args) {
        class Foo implements Struct._4<String, Maybe<Integer>, String, Maybe<Integer>> {

            @Override
            public Tuple4<String, Maybe<Integer>, String, Maybe<Integer>> unapply() {
                return tuple(getBar(), getBaz(), getBar().toUpperCase(), getBaz().fmap(x -> x * 2));
            }

            public String getBar() {
                return "123";
            }

            public Maybe<Integer> getBaz() {
                return just(100);
            }
        }

        Foo foo = new Foo();

        String match1 = struct(() -> Either.<String, Integer>right(2), () -> "foo")
                .match(cases(of($right(1), __(), (x, y) -> x + y),
                             of($(), $(), (x, y) -> x + y)));

        System.out.println(match1);

        Integer match = struct(foo::getBar, foo::getBaz).match(cases(
                of($(eq("123")), $just(), (bar1, baz1) -> parseInt(bar1) + baz1),
                of($(eq("foo")), $(), (bar, baz) -> 1),
                of($(eq("foo")), $(just(1)), (bar, baz) -> baz.orElse(-2)),
                of($(eq("foo")), $just(1), (bar, baz) -> -2),
                of($(), $(), (a, b) -> -1)));

        System.out.println("match = " + match);
    }
}
