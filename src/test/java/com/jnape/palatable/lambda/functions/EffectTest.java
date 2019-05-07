package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.io.IO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.Effect.effect;
import static com.jnape.palatable.lambda.functions.Effect.fromConsumer;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class EffectTest {

    @Test
    public void covariantReturns() {
        List<Object> results = new ArrayList<>();

        Effect<String> effect       = fromConsumer(results::add);
        Effect<Object> diMapL       = effect.diMapL(Object::toString);
        Effect<Object> contraMap    = effect.contraMap(Object::toString);
        Effect<String> stringEffect = effect.discardR(constantly("1"));

        effect.apply("1").unsafePerformIO();
        diMapL.apply("2").unsafePerformIO();
        contraMap.apply("3").unsafePerformIO();
        stringEffect.apply("4").unsafePerformIO();

        assertEquals(asList("1", "2", "3", "4"), results);
    }

    @Test
    public void staticFactoryMethods() {
        AtomicInteger counter = new AtomicInteger();

        Effect<Unit> runnableEffect = effect(counter::incrementAndGet);
        runnableEffect.apply(UNIT).unsafePerformIO();
        assertEquals(1, counter.get());

        Effect<AtomicInteger> fnEffect = Effect.fromConsumer(AtomicInteger::incrementAndGet);
        fnEffect.apply(counter).unsafePerformIO();
        assertEquals(2, counter.get());
    }

    @Test
    public void toConsumer() {
        @SuppressWarnings("RedundantTypeArguments") Effect<List<String>> addFoo   = l -> IO.<Unit>io(() -> l.add("foo"));
        Consumer<List<String>>                                           consumer = addFoo.toConsumer();
        ArrayList<String>                                                list     = new ArrayList<>();
        consumer.accept(list);
        assertEquals(singletonList("foo"), list);
    }
}