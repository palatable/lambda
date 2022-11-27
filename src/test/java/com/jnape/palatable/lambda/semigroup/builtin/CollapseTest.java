package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.builtin.fn1.Constantly;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Zip.zip;
import static com.jnape.palatable.lambda.monoid.builtin.And.and;
import static com.jnape.palatable.lambda.monoid.builtin.Join.join;
import static com.jnape.palatable.lambda.monoid.builtin.Or.or;
import static com.jnape.palatable.lambda.semigroup.builtin.Absent.absent;
import static com.jnape.palatable.lambda.semigroup.builtin.Collapse.collapse;
import static org.junit.Assert.assertEquals;

public class CollapseTest {

    @Test
    public void semigroup() {
        Semigroup<String> join = join();
        Semigroup<Integer> add = Integer::sum;

        Collapse<String, Integer> collapse = collapse();
        assertEquals(tuple("foobar", 3), collapse.apply(join, add, tuple("foo", 1), tuple("bar", 2)));
    }

    @Test(timeout = 200)
    public void foldLeftShortCircuits() {
        Semigroup<Tuple2<Boolean, Boolean>> collapse = collapse(and(), or());
        Iterable<Tuple2<Boolean, Boolean>> tuples = zip(cons(false, repeat(true)),
                                                        cons(true, repeat(false)));
        Tuple2<Boolean, Boolean> booleanBooleanTuple2 = collapse.foldLeft(tuple(true, false),
                                                                          tuples);
        assertEquals(tuple(false, true),
                     booleanBooleanTuple2);
    }

    @Test(timeout = 200)
    public void foldRightShortCircuits() {
        Semigroup<Maybe<Unit>> absent = absent(Constantly::constantly);
        Semigroup<Tuple2<Maybe<Unit>, Maybe<Unit>>> collapse = collapse(absent, absent);

        Lazy<Tuple2<Maybe<Unit>, Maybe<Unit>>> maybeLazy = collapse
                .foldRight(tuple(just(UNIT), just(UNIT)),
                           repeat(tuple(nothing(), nothing())));
        Tuple2<Maybe<Unit>, Maybe<Unit>> result = maybeLazy.value();
        assertEquals(tuple(nothing(), nothing()), result);

        result = collapse
                .foldRight(tuple(nothing(), nothing()),
                           repeat(tuple(just(UNIT), just(UNIT)))).value();
        assertEquals(tuple(nothing(), nothing()), result);
    }
}