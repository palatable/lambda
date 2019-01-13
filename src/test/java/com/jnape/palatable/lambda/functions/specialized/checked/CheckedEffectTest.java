package com.jnape.palatable.lambda.functions.specialized.checked;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class CheckedEffectTest {

    @Test
    public void assignment() {
        List<Object> results = new ArrayList<>();

        CheckedEffect<Throwable, String> effect = results::add;
        CheckedEffect<Throwable, Object> diMapL = effect.diMapL(Object::toString);
        CheckedEffect<Throwable, Object> contraMap = effect.contraMap(Object::toString);
        CheckedEffect<Throwable, Object> compose = effect.compose(Object::toString);
        CheckedEffect<Throwable, String> stringEffect = effect.discardR(constantly("1"));
        CheckedEffect<Throwable, String> andThen = effect.andThen(effect);

        effect.accept("1");
        diMapL.accept("2");
        contraMap.accept("3");
        compose.accept("4");
        stringEffect.accept("5");
        andThen.accept("6");

        assertEquals(asList("1", "2", "3", "4", "5", "6", "6"), results);
    }
}