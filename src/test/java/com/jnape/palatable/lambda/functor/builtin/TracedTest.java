package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import com.jnape.palatable.lambda.functions.specialized.Cokleisli;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EquatableW;
import testsupport.traits.ComonadLaws;
import testsupport.traits.FunctorLaws;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.specialized.Cokleisli.cokleisli;
import static com.jnape.palatable.lambda.functor.builtin.Traced.traced;
import static junit.framework.TestCase.assertEquals;

@RunWith(Traits.class)
public class TracedTest {
    private Monoid<Object> objectMonoid = Monoid.monoid((o1, o2) -> o1, new Object());
    private Monoid<String> stringMonoid = Monoid.monoid((s1, s2) -> s1 + s2, "");
    private Monoid<Integer> productMonoid = Monoid.monoid((i1, i2) -> i1 * i2, 1);

    @TestTraits({FunctorLaws.class, ComonadLaws.class})
    public EquatableW<Traced<Object, Monoid<Object>, ?>, ?> testSubject() {
        Traced<Object, Monoid<Object>, Object> traced = traced(constantly(new Object()), objectMonoid);
        return new EquatableW<>(traced, t -> tuple(t.extract(), t.runTrace(new Object())));
    }

    @Test
    public void tracedStringEmpty() {
        Traced<String, Monoid<String>, String> tracer = traced(Id.id(), stringMonoid);
        assertEquals(tracer.extract(), "");
    }

    @Test
    public void tracedStringAppends() {
        Traced<String, Monoid<String>, String> tracer = traced(Id.id(), stringMonoid);
        Cokleisli<String, String, Traced<String, Monoid<String>, ?>> cokleisli = cokleisli(t2 -> t2.extract() + " world");
        Cokleisli<String, String, Traced<String, Monoid<String>, ?>> cokleisli1 = cokleisli(t1 -> t1.extract() + "hello");
        String applied = cokleisli1.andThen(cokleisli).apply(tracer);
        assertEquals(applied, "hello world");
    }

    @Test
    public void tracedProductStartsFromOne() {
        Traced<Integer, Monoid<Integer>, Integer> tracer = traced(i -> i + 2, productMonoid);
        Integer extract = tracer.extract();
        Integer i = 3;
        assertEquals(i, extract);
    }

    @Test
    public void tracedProductAppendingStartsFromOne() {
        Traced<Integer, Monoid<Integer>, Integer> tracer = traced(i -> i + 1, productMonoid);
        Cokleisli<Integer, Integer, Traced<Integer, Monoid<Integer>, ?>> cokleisli = cokleisli((Fn1<Traced<Integer, Monoid<Integer>, Integer>, Integer>) t1 -> t1.runTrace(2) * 2);
        Cokleisli<Integer, Integer, Traced<Integer, Monoid<Integer>, ?>> cokleisli1 = cokleisli((Fn1<Traced<Integer, Monoid<Integer>, Integer>, Integer>) t2 -> t2.runTrace(3) + 3);
        Integer applied = (cokleisli).andThen(cokleisli1).apply(tracer);
        Integer i = 17;
        assertEquals(i, applied);
    }
}
