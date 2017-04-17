package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;

import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Choice2Test {

    private Choice2<Integer, Boolean> a;
    private Choice2<Integer, Boolean> b;

    @Before
    public void setUp() {
        a = a(1);
        b = b(true);
    }

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, BifunctorLaws.class})
    public Subjects<Choice2<String, Integer>> testSubjects() {
        return subjects(a("foo"), b(1));
    }

    @Test
    public void divergeStaysInChoice() {
        assertEquals(Choice3.a(1), a.diverge());
        assertEquals(Choice3.b(true), b.diverge());
    }
}