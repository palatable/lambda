package com.jnape.palatable.lambda;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.structural.Match;
import com.jnape.palatable.lambda.structural.Struct;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.structural.Case.of;
import static com.jnape.palatable.lambda.structural.Cases.cases;
import static com.jnape.palatable.lambda.structural.CatchAll.__;
import static com.jnape.palatable.lambda.structural.Struct.struct;

public class SPike {

    public static void main(String[] args) {

        class Foo implements Struct._4<String, Integer, String, Integer> {

            @Override
            public Tuple4<String, Integer, String, Integer> unapply() {
                return HList.tuple(getBar(), getBaz(), getBar().toUpperCase(), getBaz() * 2);
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

        Maybe<String> apply = match.apply(struct.unapply());

        System.out.println("blah = " + blah);


    }
}
