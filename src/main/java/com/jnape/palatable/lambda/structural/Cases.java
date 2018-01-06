package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.structural.Case.of;
import static com.jnape.palatable.lambda.structural.CatchAll.__;
import static com.jnape.palatable.lambda.structural.Match.partial;
import static com.jnape.palatable.lambda.structural.Match.total;
import static com.jnape.palatable.lambda.structural.Struct.struct;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class Cases {

    private Cases() {
    }

    public static void main(String[] args) {

        class Foo implements Struct._2<String, Integer> {
            String getFoo() {
                return "hi";
            }

            Integer getBar() {
                return 1;
            }

            @Override
            public Tuple2<String, Integer> unapply() {
                return tuple(getFoo(), getBar());
            }
        }

        Foo foo = new Foo();

//        Maybe<String> match = struct(foo::getFoo, foo::getBar).match(cases(of(__, eq(1), (foo11, bar1) -> foo11),
//                                                                           of(__, eq(1), (foo11, bar1) -> foo11)));
    }

    public static <Fields extends HCons, R> Match.Total<Fields, R> cases(Case.Partial<Fields, R> partialCase,
                                                                         Case.Total<Fields, R> totalCase) {
        return cases(partialCase).or(cases(totalCase));
    }

    public static <Fields extends HCons, R> Match.Total<Fields, R> cases(Case.Partial<Fields, R> partialCase1,
                                                                         Case.Partial<Fields, R> partialCase2,
                                                                         Case.Total<Fields, R> totalCase) {
        return cases(partialCase1, partialCase2).or(cases(totalCase));
    }

    public static <Fields extends HCons, R> Match.Total<Fields, R> cases(Case.Partial<Fields, R> partialCase1,
                                                                         Case.Partial<Fields, R> partialCase2,
                                                                         Case.Partial<Fields, R> partialCase3,
                                                                         Case.Total<Fields, R> totalCase) {
        return cases(partialCase1, partialCase2, partialCase3).or(cases(totalCase));
    }

    public static <Fields extends HCons, R> Match.Total<Fields, R> cases(Case.Partial<Fields, R> partialCase1,
                                                                         Case.Partial<Fields, R> partialCase2,
                                                                         Case.Partial<Fields, R> partialCase3,
                                                                         Case.Partial<Fields, R> partialCase4,
                                                                         Case.Total<Fields, R> totalCase) {
        return cases(partialCase1, partialCase2, partialCase3, partialCase4).or(cases(totalCase));
    }

    public static <Fields extends HCons, R> Match.Total<Fields, R> cases(Case.Partial<Fields, R> partialCase1,
                                                                         Case.Partial<Fields, R> partialCase2,
                                                                         Case.Partial<Fields, R> partialCase3,
                                                                         Case.Partial<Fields, R> partialCase4,
                                                                         Case.Partial<Fields, R> partialCase5,
                                                                         Case.Total<Fields, R> totalCase) {
        return cases(partialCase1, partialCase2, partialCase3, partialCase4, partialCase5).or(cases(totalCase));
    }

    public static <Fields extends HCons, R> Match.Total<Fields, R> cases(Case.Partial<Fields, R> partialCase1,
                                                                         Case.Partial<Fields, R> partialCase2,
                                                                         Case.Partial<Fields, R> partialCase3,
                                                                         Case.Partial<Fields, R> partialCase4,
                                                                         Case.Partial<Fields, R> partialCase5,
                                                                         Case.Partial<Fields, R> partialCase6,
                                                                         Case.Total<Fields, R> totalCase) {
        return cases(partialCase1, partialCase2, partialCase3, partialCase4, partialCase5, partialCase6).or(cases(totalCase));
    }

    public static <Fields extends HCons, R> Match.Total<Fields, R> cases(Case.Partial<Fields, R> partialCase1,
                                                                         Case.Partial<Fields, R> partialCase2,
                                                                         Case.Partial<Fields, R> partialCase3,
                                                                         Case.Partial<Fields, R> partialCase4,
                                                                         Case.Partial<Fields, R> partialCase5,
                                                                         Case.Partial<Fields, R> partialCase6,
                                                                         Case.Partial<Fields, R> partialCase7,
                                                                         Case.Total<Fields, R> totalCase) {
        return total(asList(partialCase1, partialCase2, partialCase3, partialCase4, partialCase5, partialCase6, partialCase7),
                     totalCase);
    }

    public static <Fields extends HCons, R> Match.Total<Fields, R> cases(Case.Partial<Fields, R> partialCase1,
                                                                         Case.Partial<Fields, R> partialCase2,
                                                                         Case.Partial<Fields, R> partialCase3,
                                                                         Case.Partial<Fields, R> partialCase4,
                                                                         Case.Partial<Fields, R> partialCase5,
                                                                         Case.Partial<Fields, R> partialCase6,
                                                                         Case.Partial<Fields, R> partialCase7,
                                                                         Case.Partial<Fields, R> partialCase8,
                                                                         Case.Total<Fields, R> totalCase) {
        return cases(partialCase1, partialCase2, partialCase3, partialCase4, partialCase5, partialCase6, partialCase7, partialCase8)
                .or(cases(totalCase));
    }

    public static <Fields extends HCons, R> Match.Total<Fields, R> cases(Case.Total<Fields, R> totalCase) {
        return total(emptyList(), totalCase);
    }

    @SafeVarargs
    public static <Fields extends HCons, R> Match.Partial<Fields, R> cases(Case.Partial<Fields, R> partialCase,
                                                                           Case.Partial<Fields, R>... more) {
        return partial(cons(partialCase, asList(more)));
    }

}
