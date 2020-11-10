package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.functor.builtin.Writer;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.functor.builtin.Writer.*;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.*;
import static com.jnape.palatable.lambda.monoid.builtin.AddAll.addAll;
import static com.jnape.palatable.lambda.monoid.builtin.Join.join;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.matchers.IOMatcher.yieldsValue;
import static testsupport.matchers.IterateTMatcher.*;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class IterateTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadRecLaws.class})
    public Subjects<Equivalence<IterateT<Identity<?>, Integer>>> testSubjects() {
        Fn1<IterateT<Identity<?>, ?>, Object> toCollection = iterateT -> iterateT
                .<Collection<Object>, Identity<Collection<Object>>>fold(
                        (as, a) -> {
                            as.add(a);
                            return new Identity<>(as);
                        },
                        new Identity<>(new ArrayList<>()))
                .runIdentity();
        return subjects(equivalence(empty(pureIdentity()), toCollection),
                        equivalence(singleton(new Identity<>(0)), toCollection),
                        equivalence(IterateT.<Identity<?>, Integer>empty(pureIdentity()).cons(new Identity<>(1)),
                                    toCollection),
                        equivalence(IterateT.<Identity<?>, Integer>empty(pureIdentity()).snoc(new Identity<>(1)),
                                    toCollection),
                        equivalence(singleton(new Identity<>(0)).concat(singleton(new Identity<>(1))),
                                    toCollection),
                        equivalence(unfold(x -> new Identity<>(x <= 100 ? just(tuple(x, x + 1)) : nothing()),
                                           new Identity<>(0)),
                                    toCollection)
        );
    }

    @Test
    public void emptyHasNoElements() {
        assertEquals(new Identity<>(nothing()),
                     IterateT.<Identity<?>, Integer>empty(pureIdentity())
                             .<Identity<Maybe<Tuple2<Integer, IterateT<Identity<?>, Integer>>>>>runIterateT());
    }

    @Test
    public void singletonHasOneElement() {
        assertThat(singleton(new Identity<>(1)), iterates(1));
    }

    @Test
    public void unfolding() {
        assertThat(unfold(x -> new Identity<>(just(x).filter(lte(3)).fmap(y -> tuple(y, y + 1))), new Identity<>(1)),
                   iteratesAll(asList(1, 2, 3)));
    }

    @Test
    public void consAddsElementToFront() {
        assertThat(singleton(new Identity<>(1)).cons(new Identity<>(0)), iterates(0, 1));
    }

    @Test
    public void snocAddsElementToBack() {
        assertThat(singleton(new Identity<>(1)).snoc(new Identity<>(2)), iterates(1, 2));
    }

    @Test
    public void concatsTwoIterateTs() {
        IterateT<Identity<?>, Integer> front = singleton(new Identity<>(0)).snoc(new Identity<>(1));
        IterateT<Identity<?>, Integer> back  = singleton(new Identity<>(2)).snoc(new Identity<>(3));

        assertThat(front.concat(back), iterates(0, 1, 2, 3));
        assertThat(IterateT.<Identity<?>, Integer>empty(pureIdentity()).concat(back), iterates(2, 3));
        assertThat(front.concat(empty(pureIdentity())), iterates(0, 1));
        assertThat(IterateT.<Identity<?>, Integer>empty(pureIdentity()).concat(empty(pureIdentity())), isEmpty());
        assertThat(singleton(new Identity<>(1))
                           .concat(unfold(x -> new Identity<>(nothing()), new Identity<>(0)))
                           .concat(singleton(new Identity<>(2))),
                   iterates(1, 2));
    }

    @Test
    public void ofIteratesElements() {
        assertEquals(tuple(6, asList(1, 2, 3)),
                     IterateT.<Writer<List<Integer>, ?>, Integer>of(listen(1), listen(2), listen(3))
                             .<Integer, Writer<List<Integer>, Integer>>fold(
                                     (x, y) -> writer(tuple(x + y, singletonList(y))), listen(0))
                             .runWriter(addAll(ArrayList::new)));
    }

    @Test
    public void fromIterator() {
        IterateT<IO<?>, Integer> it = IterateT.fromIterator(asList(1, 2, 3).iterator());
        assertThat(it.<ArrayList<Integer>, IO<ArrayList<Integer>>>toCollection(ArrayList::new),
                   yieldsValue(equalTo(asList(1, 2, 3))));
        assertThat(it.<ArrayList<Integer>, IO<ArrayList<Integer>>>toCollection(ArrayList::new),
                   yieldsValue(equalTo(emptyList())));
    }

    @Test
    public void fold() {
        assertEquals(tuple(6, asList(1, 2, 3)),
                     IterateT.<Writer<List<Integer>, ?>, Integer>of(listen(1), listen(2), listen(3))
                             .<Integer, Writer<List<Integer>, Integer>>fold(
                                     (x, y) -> writer(tuple(x + y, singletonList(y))), listen(0))
                             .runWriter(addAll(ArrayList::new)));
    }

    @Test
    public void foldCut() {
        assertEquals(tuple(3, "012"),
                     IterateT.of(writer(tuple(1, "1")),
                                 writer(tuple(2, "2")),
                                 writer(tuple(3, "3")))
                             .<Integer, Writer<String, Integer>>foldCut(
                                     (x, y) -> listen(y == 2 ? terminate(x + y) : recurse(x + y)),
                                     writer(tuple(0, "0")))
                             .runWriter(join()));
    }

    @Test
    public void zipUsesCartesianProduct() {
        assertThat(IterateT.of(new Identity<>(1), new Identity<>(2), new Identity<>(3))
                           .zip(IterateT.of(new Identity<>(x -> x + 1), new Identity<>(x -> x - 1))),
                   iterates(2, 3, 4, 0, 1, 2));
    }

    @Test(timeout = 1000)
    public void zipsInParallel() {
        CountDownLatch latch = new CountDownLatch(2);
        singleton(io(() -> {
            latch.countDown();
            latch.await();
            return 0;
        })).<Integer>zip(singleton(io(() -> {
            latch.countDown();
            latch.await();
            return x -> x + 1;
        }))).<IO<Maybe<Tuple2<Integer, IterateT<IO<?>, Integer>>>>>runIterateT()
                .unsafePerformAsyncIO()
                .join();
    }

    @Test
    public void toCollection() {
        assertEquals(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                     unfold(x -> new Identity<>(x <= 10 ? just(tuple(x, x + 1)) : nothing()), new Identity<>(1))
                             .<List<Integer>, Identity<List<Integer>>>toCollection(ArrayList::new)
                             .runIdentity());
    }

    @Test
    public void forEach() {
        assertEquals(tuple(UNIT, asList(1, 2, 3)),
                     IterateT.<Writer<List<Integer>, ?>, Integer>empty(pureWriter())
                             .cons(listen(3))
                             .cons(listen(2))
                             .cons(listen(1))
                             .<Writer<List<Integer>, Unit>>forEach(x -> tell(singletonList(x)))
                             .runWriter(addAll(ArrayList::new)));
    }

    @Test
    public void foldLargeNumberOfElements() {
        IterateT<Identity<?>, Integer> largeIterateT = times(STACK_EXPLODING_NUMBER,
                                                             it -> it.cons(new Identity<>(1)),
                                                             empty(pureIdentity()));
        assertEquals(new Identity<>(STACK_EXPLODING_NUMBER),
                     largeIterateT.fold((x, y) -> new Identity<>(x + y), new Identity<>(0)));
    }

    @Test
    public void stackSafetyForStrictMonads() {
        IterateT<Identity<?>, Integer> hugeStrictIterateT =
                unfold(x -> new Identity<>(x <= STACK_EXPLODING_NUMBER ? just(tuple(x, x + 1)) : nothing()),
                       new Identity<>(1));
        Identity<Integer> fold = hugeStrictIterateT.fold((x, y) -> new Identity<>(x + y), new Identity<>(0));
        assertEquals(new Identity<>(1250025000), fold);
    }

    @Test
    public void stackSafetyForNonStrictMonads() {
        IterateT<Lazy<?>, Integer> hugeNonStrictIterateT =
                unfold(x -> lazy(() -> x <= 50_000 ? just(tuple(x, x + 1)) : nothing()), lazy(0));
        Lazy<Integer> fold = hugeNonStrictIterateT.fold((x, y) -> lazy(() -> x + y), lazy(0));
        assertEquals((Integer) 1250025000, fold.value());
    }

    @Test
    public void concatIsStackSafe() {
        IterateT<Identity<?>, Integer> bigIterateT = times(10_000, xs -> xs.concat(singleton(new Identity<>(1))),
                                                           singleton(new Identity<>(0)));
        assertEquals(new Identity<>(10_000),
                     bigIterateT.fold((x, y) -> new Identity<>(x + y), new Identity<>(0)));
    }

    @Test
    public void staticPure() {
        assertEquals(new Identity<>(singletonList(1)),
                     pureIterateT(pureIdentity())
                         .<Integer, IterateT<Identity<?>, Integer>>apply(1)
                         .<List<Integer>, Identity<List<Integer>>>toCollection(ArrayList::new));
    }

    @Test
    public void staticLift() {
        assertEquals(new Identity<>(singletonList(1)),
                     liftIterateT()
                         .<Integer, Identity<?>, IterateT<Identity<?>, Integer>>apply(new Identity<>(1))
                         .<List<Integer>, Identity<List<Integer>>>toCollection(ArrayList::new));
    }

    @Test
    public void trampolineMRecursesBreadth() {
        IterateT<Identity<?>, Integer> firstFour = of(new Identity<>(1), new Identity<>(2), new Identity<>(3), new Identity<>(4));
        IterateT<Identity<?>, Integer> trampolined = firstFour
                .trampolineM(x -> (x % 3 == 0 && (x < 30))
                                  ? of(new Identity<>(terminate(x + 10)), new Identity<>(recurse(x + 11)), new Identity<>(recurse(x + 12)), new Identity<>(recurse(x + 13)))
                                  : singleton(new Identity<>(terminate(x))));
        assertThat(trampolined, iterates(1, 2, 13, 14, 25, 26, 37, 38, 39, 40, 28, 16, 4));
    }
}