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

import static com.jnape.palatable.lambda.adt.choice.Choice4.a;
import static com.jnape.palatable.lambda.adt.choice.Choice4.b;
import static com.jnape.palatable.lambda.adt.choice.Choice4.c;
import static com.jnape.palatable.lambda.adt.choice.Choice4.d;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Choice4Test {

    private Choice4<Integer, String, Boolean, Double> a;
    private Choice4<Integer, String, Boolean, Double> b;
    private Choice4<Integer, String, Boolean, Double> c;
    private Choice4<Integer, String, Boolean, Double> d;

    @Before
    public void setUp() {
        a = a(1);
        b = b("two");
        c = c(true);
        d = d(4D);
    }

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, BifunctorLaws.class})
    public Subjects<Choice4<String, Integer, Boolean, Character>> testSubjects() {
        return subjects(a("foo"), b(1), c(true), d('a'));
    }

    @Test
    public void convergeStaysInChoice() {
        assertEquals(Choice3.a(1), a.converge(d -> Choice3.b(d.toString())));
        assertEquals(Choice3.b("two"), b.converge(d -> Choice3.b(d.toString())));
        assertEquals(Choice3.c(true), c.converge(d -> Choice3.b(d.toString())));
        assertEquals(Choice3.b("4.0"), d.converge(d -> Choice3.b(d.toString())));
    }

    @Test
    public void divergeStaysInChoice() {
        assertEquals(Choice5.a(1), a.diverge());
        assertEquals(Choice5.b("two"), b.diverge());
        assertEquals(Choice5.c(true), c.diverge());
        assertEquals(Choice5.d(4D), d.diverge());
    }
}