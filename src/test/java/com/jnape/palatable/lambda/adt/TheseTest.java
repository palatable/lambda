package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.TraversableLaws;

import static com.jnape.palatable.lambda.adt.These.a;
import static com.jnape.palatable.lambda.adt.These.b;
import static com.jnape.palatable.lambda.adt.These.both;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;

@RunWith(Traits.class)
public class TheseTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, TraversableLaws.class, BifunctorLaws.class})
    public Subjects<These<String, Integer>> testSubject() {
        return subjects(a("foo"), b(1), both("foo", 1));
    }
}