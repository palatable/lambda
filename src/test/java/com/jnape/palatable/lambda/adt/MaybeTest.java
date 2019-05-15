package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.TraversableLaws;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Eq.eq;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(Traits.class)
public class MaybeTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, TraversableLaws.class, MonadLaws.class})
    public Subjects<Maybe<Integer>> testSubject() {
        return subjects(Maybe.nothing(), just(1));
    }

    @Test(expected = NullPointerException.class)
    public void justMustBeNonNull() {
        just(null);
    }

    @Test
    public void nothingReusesInstance() {
        assertSame(Maybe.nothing(), Maybe.nothing());
    }

    @Test
    public void maybeAllowsNull() {
        assertEquals(just(1), Maybe.maybe(1));
        assertEquals(Maybe.nothing(), Maybe.maybe(null));
    }

    @Test
    public void orElseGet() {
        assertEquals((Integer) 1, just(1).orElseGet(() -> -1));
        assertEquals(-1, Maybe.nothing().orElseGet(() -> -1));
    }

    @Test
    public void orElse() {
        assertEquals((Integer) 1, just(1).orElse(-1));
        assertEquals(-1, Maybe.nothing().orElse(-1));
    }

    @Test
    public void filter() {
        assertEquals(just(1), just(1).filter(eq(1)));
        assertEquals(nothing(), just(0).filter(eq(1)));
        assertEquals(nothing(), nothing().filter(eq(1)));
    }

    @Test
    public void toOptional() {
        assertEquals(Optional.of(1), just(1).toOptional());
        assertEquals(Optional.empty(), Maybe.nothing().toOptional());
    }

    @Test
    public void fromOptional() {
        assertEquals(just(1), Maybe.fromOptional(Optional.of(1)));
        assertEquals(Maybe.nothing(), Maybe.fromOptional(Optional.empty()));
    }

    @Test
    public void toEither() {
        assertEquals(right(1), just(1).toEither(() -> "empty"));
        assertEquals(left("empty"), nothing().toEither(() -> "empty"));
    }

    @Test
    public void fromEither() {
        assertEquals(just(1), Maybe.fromEither(right(1)));
        assertEquals(nothing(), Maybe.fromEither(left("failure")));
    }

    @Test
    public void peek() {
        AtomicInteger ref = new AtomicInteger(0);
        assertEquals(just(1), just(1).peek(constantly(io(ref::incrementAndGet))));
        assertEquals(1, ref.get());

        assertEquals(nothing(), nothing().peek(constantly(io(ref::incrementAndGet))));
        assertEquals(1, ref.get());
    }

    @Test
    public void justOrThrow() {
        just(1).orElseThrow(IllegalStateException::new);
    }

    @Test(expected = IllegalStateException.class)
    public void nothingOrThrow() {
        nothing().orElseThrow(IllegalStateException::new);
    }

    @Test
    public void divergesIntoChoice3() {
        assertEquals(Choice3.a(UNIT), nothing().diverge());
        assertEquals(Choice3.b(1), just(1).diverge());
    }

    @Test
    public void projectsIntoTuple2() {
        assertEquals(tuple(just(UNIT), nothing()), nothing().project());
        assertEquals(tuple(nothing(), just(1)), just(1).project());
    }

    @Test
    public void invertsIntoChoice2() {
        assertEquals(Choice2.b(UNIT), nothing().invert());
        assertEquals(Choice2.a(1), just(1).invert());
    }

    @Test
    public void lazyZip() {
        assertEquals(just(2), just(1).<Integer>lazyZip(lazy(() -> just(x -> x + 1))).value());
        assertEquals(nothing(), nothing().lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
    }
}