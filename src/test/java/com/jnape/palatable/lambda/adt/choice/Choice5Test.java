package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.FunctorLaws;

import static com.jnape.palatable.lambda.adt.choice.Choice5.a;
import static com.jnape.palatable.lambda.adt.choice.Choice5.b;
import static com.jnape.palatable.lambda.adt.choice.Choice5.c;
import static com.jnape.palatable.lambda.adt.choice.Choice5.d;
import static com.jnape.palatable.lambda.adt.choice.Choice5.e;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Choice5Test {

    private Choice5<Integer, String, Boolean, Double, Character> a;
    private Choice5<Integer, String, Boolean, Double, Character> b;
    private Choice5<Integer, String, Boolean, Double, Character> c;
    private Choice5<Integer, String, Boolean, Double, Character> d;
    private Choice5<Integer, String, Boolean, Double, Character> e;

    @Before
    public void setUp() {
        a = a(1);
        b = b("two");
        c = c(true);
        d = d(4d);
        e = e('z');
    }

    @TestTraits({FunctorLaws.class})
    public Choice5<String, Integer, Boolean, Character, Double> testSubjectA() {
        return Choice5.a("foo");
    }

    @TestTraits({FunctorLaws.class})
    public Choice5<String, Integer, Boolean, Character, Double> testSubjectB() {
        return Choice5.b(1);
    }

    @TestTraits({FunctorLaws.class})
    public Choice5<String, Integer, Boolean, Character, Double> testSubjectC() {
        return Choice5.c(true);
    }

    @TestTraits({FunctorLaws.class})
    public Choice5<String, Integer, Boolean, Character, Double> testSubjectD() {
        return Choice5.d('a');
    }

    @TestTraits({FunctorLaws.class})
    public Choice5<String, Integer, Boolean, Character, Double> testSubjectE() {
        return Choice5.e(2d);
    }

    @Test
    public void convergeStaysInChoice() {
        assertEquals(Choice4.a(1), a.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.b("two"), b.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.c(true), c.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.d(4d), d.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.b("z"), e.converge(e -> Choice4.b(e.toString())));
    }

    @Test
    public void bifunctorProperties() {
        assertEquals(a, a.biMap(d -> -d, Character::toUpperCase));
        assertEquals(b, b.biMap(d -> -d, Character::toUpperCase));
        assertEquals(c, c.biMap(d -> -d, Character::toUpperCase));
        assertEquals(d(-4D), d.biMap(d -> -d, Character::toUpperCase));
        assertEquals(e('Z'), e.biMap(d -> -d, Character::toUpperCase));
    }
}