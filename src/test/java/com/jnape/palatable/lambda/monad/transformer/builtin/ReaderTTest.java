package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EquatableM;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.ReaderT.readerT;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class ReaderTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public EquatableM<ReaderT<Integer, Identity<?>, ?>, Integer> testSubject() {
        return new EquatableM<>(readerT(Identity::new),
                                readerT -> readerT.runReaderT(1));
    }

    @Test
    public void profunctor() {
        assertEquals(new Identity<>(4),
                     ReaderT.<Integer, Identity<?>, Integer>readerT(Identity::new)
                             .diMap(String::length, x -> x + 1)
                             .runReaderT("123"));
    }

    @Test
    public void mapReaderT() {
        assertEquals(just(3),
                     ReaderT.<String, Identity<?>, String>readerT(Identity::new)
                             .<Identity<String>, Maybe<?>, Integer>mapReaderT(id -> just(id.runIdentity().length()))
                             .runReaderT("foo"));
    }

    @Test
    public void staticPure() {
        ReaderT<String, Identity<?>, Integer> readerT =
                ReaderT.<String, Identity<?>>pureReaderT(pureIdentity()).apply(1);
        assertEquals(new Identity<>(1), readerT.runReaderT("foo"));
    }
}