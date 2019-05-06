package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.Effect.effect;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class EffectTest {

    @Test
    public void covariantReturns() {
        List<Object> results = new ArrayList<>();

        Effect<String> effect       = results::add;
        Effect<Object> diMapL       = effect.diMapL(Object::toString);
        Effect<Object> contraMap    = effect.contraMap(Object::toString);
        Effect<Object> compose      = effect.compose(Object::toString);
        Effect<String> stringEffect = effect.discardR(constantly("1"));
        Effect<String> andThen      = effect.andThen(effect);

        effect.accept("1");
        diMapL.accept("2");
        contraMap.accept("3");
        compose.accept("4");
        stringEffect.accept("5");
        andThen.accept("6");

        assertEquals(asList("1", "2", "3", "4", "5", "6", "6"), results);
    }

    @Test
    public void staticFactoryMethods() {
        AtomicInteger counter = new AtomicInteger();

        Effect<Unit> runnableEffect = effect(counter::incrementAndGet);
        runnableEffect.apply(UNIT).unsafePerformIO();
        assertEquals(1, counter.get());

        Effect<AtomicInteger> fnEffect = effect(AtomicInteger::incrementAndGet);
        fnEffect.apply(counter).unsafePerformIO();
        assertEquals(2, counter.get());
    }
}