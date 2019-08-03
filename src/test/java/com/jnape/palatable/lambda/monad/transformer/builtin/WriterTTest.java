package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.Equivalence;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;
import testsupport.traits.MonadWriterLaws;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.WriterT.writerT;
import static com.jnape.palatable.lambda.monoid.builtin.Join.join;
import static org.junit.Assert.assertEquals;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class WriterTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadWriterLaws.class, MonadRecLaws.class})
    public Equivalence<WriterT<String, Identity<?>, Integer>> testSubject() {
        return equivalence(writerT(new Identity<>(tuple(2, ""))), writerT -> writerT.runWriterT(join()));
    }

    @Test
    public void accumulationUsesProvidedMonoid() {
        Identity<Tuple2<Integer, String>> result = writerT(new Identity<>(tuple(1, "foo")))
                .discardR(WriterT.tell(new Identity<>("bar")))
                .flatMap(x -> writerT(new Identity<>(tuple(x + 1, "baz"))))
                .runWriterT(join());

        assertEquals(new Identity<>(tuple(2, "foobarbaz")), result);
    }

    @Test
    public void tell() {
        assertEquals(new Identity<>(tuple(UNIT, "")),
                     WriterT.tell(new Identity<>("")).runWriterT(join()));
    }

    @Test
    public void listen() {
        assertEquals(new Identity<>(tuple(1, "")),
                     WriterT.<String, Identity<?>, Integer>listen(new Identity<>(1)).runWriterT(join()));
    }

    @Test
    public void staticPure() {
        WriterT<String, Identity<?>, Integer> apply = WriterT.<String, Identity<?>>pureWriterT(pureIdentity()).apply(1);
        assertEquals(new Identity<>(tuple(1, "")),
                     apply.runWriterT(join()));
    }

    @Test
    public void staticLift() {
        WriterT<String, Identity<?>, Integer> apply = WriterT.<String>liftWriterT().apply(new Identity<>(1));
        assertEquals(new Identity<>(tuple(1, "")),
                     apply.runWriterT(join()));
    }
}