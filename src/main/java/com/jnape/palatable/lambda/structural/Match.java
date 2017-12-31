package com.jnape.palatable.lambda.structural;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.functions.builtin.fn1.CatMaybes.catMaybes;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Head.head;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.monoid.builtin.Concat.concat;

public abstract class Match {
    private Match() {
    }

    public static <Fields extends HList.HCons, R> Partial<Fields, R> partial(
            Iterable<Case.Partial<Fields, R>> partialCases) {
        return new Partial<>(partialCases);
    }

    public static <Fields extends HList.HCons, R> Total<Fields, R> total(Iterable<Case.Partial<Fields, R>> partialCases,
                                                                         Case.Total<Fields, R> totalCase) {
        return new Total<>(partialCases, totalCase);
    }

    public static final class Partial<Fields extends HList.HCons, R> implements Fn1<Fields, Maybe<R>> {
        private final Iterable<Case.Partial<Fields, R>> cases;

        private Partial(Iterable<Case.Partial<Fields, R>> cases) {
            this.cases = cases;
        }

        @Override
        public Maybe<R> apply(Fields fields) {
            return head(catMaybes(map(c -> c.apply(fields), cases)));
        }

        public Partial<Fields, R> or(Partial<Fields, R> other) {
            return new Partial<>(concat(cases, other.cases));
        }

        public Total<Fields, R> or(Total<Fields, R> other) {
            return new Total<>(concat(cases, other.partialCases), other.totalCase);
        }
    }

    public static final class Total<Fields extends HList.HCons, R> implements Fn1<Fields, R> {
        private final Iterable<Case.Partial<Fields, R>> partialCases;
        private final Case.Total<Fields, R>             totalCase;

        private Total(Iterable<Case.Partial<Fields, R>> partialCases,
                      Case.Total<Fields, R> totalCase) {
            this.partialCases = partialCases;
            this.totalCase = totalCase;
        }

        @Override
        public R apply(Fields fields) {
            return partial(partialCases).apply(fields).orElseGet(() -> totalCase.apply(fields));
        }
    }
}
