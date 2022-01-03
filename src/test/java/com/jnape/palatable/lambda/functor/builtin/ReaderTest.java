package com.jnape.palatable.lambda.functor.builtin;

import static com.jnape.palatable.lambda.functor.builtin.Reader.listen;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static testsupport.traits.Equivalence.equivalence;

import org.junit.runner.RunWith;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;

import testsupport.traits.ApplicativeLaws;
import testsupport.traits.Equivalence;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadReaderLaws;
import testsupport.traits.MonadRecLaws;

@RunWith(Traits.class)
public class ReaderTest {
    @TestTraits({ FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadRecLaws.class,
            MonadReaderLaws.class })
    public Subjects<Equivalence<Reader<String, ?>>> testSubject() {
        return subjects(equivalence(listen("test"), reader -> reader.eval("test")));
    }
}
