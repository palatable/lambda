package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.specialized.Noop.noop;
import static junit.framework.TestCase.assertTrue;

public class EffectTest {

    @Test
    @SuppressWarnings("unused")
    public void covariantReturns() {
        Effect<String> effect = noop();
        Effect<Object> diMapL = effect.diMapL(constantly("1"));
        Effect<Object> contraMap = effect.contraMap(constantly("1"));
        Effect<Object> compose = effect.compose(constantly("1"));
        Effect<String> stringEffect = effect.discardR(constantly("1"));
        Effect<String> andThen = effect.andThen((Consumer<? super String>) noop());
    }


    @Test
    public void contraMapPreservesEffect() {
        AtomicBoolean i = new AtomicBoolean();

        Effect<Unit> originalEffect = __ -> i.set(true);
        Effect<Unit> mappedEffect = originalEffect.contraMap(id());

        mappedEffect.apply(UNIT).unsafePerformIO();

        assertTrue(i.get());
    }
}