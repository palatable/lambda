package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.Equivalence;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;
import testsupport.traits.MonadWriterLaws;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functor.builtin.Writer.listen;
import static com.jnape.palatable.lambda.functor.builtin.Writer.tell;
import static com.jnape.palatable.lambda.functor.builtin.Writer.writer;
import static com.jnape.palatable.lambda.monoid.builtin.Join.join;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.WriterTMatcher.whenRunWith;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class WriterTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadRecLaws.class, MonadWriterLaws.class})
    public Subjects<Equivalence<Writer<String, ?>>> testSubject() {
        Fn1<Writer<String, ?>, Object> runWriter = w -> w.runWriter(join());
        return subjects(equivalence(tell("foo"), runWriter),
                        equivalence(listen(1), runWriter),
                        equivalence(writer(tuple(1, "foo")), runWriter));
    }

    @Test
    public void toWriterT() {
        assertThat(writer(tuple(1, "foo")).toWriterT(), whenRunWith(join(), equalTo(new Identity<>(tuple(1, "foo")))));
    }

    @Test
    public void tellListenInteraction() {
        assertEquals(tuple(1, "hello, world!"),
                     tell("hello, ")
                             .discardL(listen(1))
                             .discardR(tell("world!"))
                             .runWriter(join()));
    }
}