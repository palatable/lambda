package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import java.util.function.Consumer;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.specialized.Noop.noop;

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
}