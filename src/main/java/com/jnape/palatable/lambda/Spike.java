package com.jnape.palatable.lambda;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.specialized.Predicate;
import com.jnape.palatable.lambda.structural.Case;
import com.jnape.palatable.lambda.structural.Match;
import com.jnape.palatable.lambda.structural.Struct;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.structural.Case.of;
import static com.jnape.palatable.lambda.structural.Cases.cases;
import static com.jnape.palatable.lambda.structural.CatchAll.__;
import static com.jnape.palatable.lambda.structural.Struct.struct;

public class Spike {

    public static <A> Case.DestructuringPredicate<Maybe<A>, A> $just(Predicate<A> predicate) {
        return m -> m.filter(predicate);
    }

    public static <A> Case.DestructuringPredicate<Maybe<A>, A> $just(A a) {
        return $just(eq(a));
    }

    public static void main(String[] args) {

        class Foo implements Struct._4<String, Integer, String, Integer> {

            @Override
            public Tuple4<String, Integer, String, Integer> unapply() {
                return tuple(getBar(), getBaz(), getBar().toUpperCase(), getBaz() * 2);
            }

            public String getBar() {
                return "foo";
            }

            public Integer getBaz() {
                return 1;
            }
        }

        Foo foo = new Foo();


        Struct._3<String, String, Integer> struct = struct(foo::getBar, foo::getBar, foo::getBaz);
        Match.Partial<Tuple3<String, String, Integer>, String> match = cases(of(eq("foo"), __, __, (x, y, z) -> x));
        Maybe<String> blah = struct.match(match);

        Case.DestructuringPredicate<Maybe<Integer>, Integer> aPredicate = $just(eq(1));
        Case.DestructuringPredicate<Maybe<Integer>, Integer> bPredicate = $just(eq(2));
        Case.Partial<Tuple2<Maybe<Integer>, Maybe<Integer>>, Integer> of = of(aPredicate, bPredicate, (x, y) -> x + y);
        Maybe<Integer> result = cases(of)
                .apply(tuple(just(1), just(2)));
        System.out.println(result);

        Maybe<String> apply = match.apply(struct.unapply());

        System.out.println("blah = " + blah);


    }
}
