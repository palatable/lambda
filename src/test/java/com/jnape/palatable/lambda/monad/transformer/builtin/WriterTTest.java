package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monad.transformer.builtin.WriterT.writerT;
import static com.jnape.palatable.lambda.monoid.builtin.Join.join;
import static com.jnape.palatable.lambda.monoid.builtin.Trivial.trivial;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.WriterTMatcher.whenEvaluatedWith;
import static testsupport.matchers.WriterTMatcher.whenExecutedWith;
import static testsupport.matchers.WriterTMatcher.whenRunWith;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class WriterTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadWriterLaws.class, MonadRecLaws.class})
    public Equivalence<WriterT<String, Identity<?>, Integer>> testSubject() {
        return equivalence(writerT(new Identity<>(tuple(2, ""))), writerT -> writerT.runWriterT(join()));
    }

    @Test
    public void accumulationUsesProvidedMonoid() {
        assertThat(writerT(new Identity<>(tuple(1, "foo")))
                           .discardR(WriterT.tell(new Identity<>("bar")))
                           .flatMap(x -> writerT(new Identity<>(tuple(x + 1, "baz")))),
                   whenRunWith(join(), equalTo(new Identity<>(tuple(2, "foobarbaz")))));
    }

    @Test
    public void eval() {
        assertThat(writerT(new Identity<>(tuple(1, "foo"))),
                   whenEvaluatedWith(join(), equalTo(new Identity<>(1))));
    }

    @Test
    public void exec() {
        assertThat(writerT(new Identity<>(tuple(1, "foo"))),
                   whenExecutedWith(join(), equalTo(new Identity<>("foo"))));
    }

    @Test
    public void tell() {
        assertThat(WriterT.tell(new Identity<>("")),
                   whenRunWith(join(), equalTo(new Identity<>(tuple(UNIT, "")))));
    }

    @Test
    public void listen() {
        assertThat(WriterT.listen(new Identity<>(1)),
                   whenRunWith(join(), equalTo(new Identity<>(tuple(1, "")))));
    }

    @Test
    public void staticPure() {
        assertThat(WriterT.<String, Identity<?>>pureWriterT(pureIdentity()).apply(1),
                   whenRunWith(join(), equalTo(new Identity<>(tuple(1, "")))));
    }

    @Test
    public void staticLift() {
        assertThat(WriterT.<String>liftWriterT().apply(new Identity<>(1)),
                   whenRunWith(join(), equalTo(new Identity<>(tuple(1, "")))));
    }

    @Test(timeout = 500)
    public void composedZip() {
        CountDownLatch latch = new CountDownLatch(2);
        IO<Unit> countdownAndAwait = io(() -> {
            latch.countDown();
            latch.await();
        });
        WriterT<Unit, IO<?>, Unit> lifted = WriterT.<Unit>liftWriterT().apply(countdownAndAwait);
        lifted.discardL(lifted)
                .<IO<Tuple2<Unit, Unit>>>runWriterT(trivial())
                .unsafePerformAsyncIO(Executors.newFixedThreadPool(2))
                .join();
    }
}