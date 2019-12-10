package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.io.IO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.jnape.palatable.lambda.functions.Effect.effect;
import static com.jnape.palatable.lambda.functions.Effect.fromConsumer;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Alter.alter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Sequence.sequence;
import static com.jnape.palatable.lambda.functions.specialized.SideEffect.sideEffect;
import static com.jnape.palatable.lambda.io.IO.io;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class EffectTest {

    @Test
    public void covariantReturns() {
        List<Object> results = new ArrayList<>();

        Effect<String> effect       = fromConsumer(results::add);
        Effect<String> diMapL       = effect.diMapL(Object::toString);
        Effect<String> contraMap    = effect.contraMap(Object::toString);
        Effect<String> stringEffect = effect.discardR(constantly("1"));

        assertThat(sequence(asList(effect.apply("1"),
                                   diMapL.apply("2"),
                                   contraMap.apply("3"),
                                   stringEffect.apply("4")),
                            IO::io)
                           .fmap(constantly(results)),
                   yieldsValue(equalTo(asList("1", "2", "3", "4"))));
    }

    @Test
    public void andThen() {
        AtomicInteger         counter = new AtomicInteger();
        Effect<AtomicInteger> inc     = c -> io(sideEffect(c::incrementAndGet));

        assertThat(alter(inc.andThen(inc), counter).fmap(AtomicInteger::get),
                   yieldsValue(equalTo(2)));
    }

    @Test
    public void staticFactoryMethods() {
        AtomicInteger counter = new AtomicInteger();

        Effect<String> sideEffect = effect(counter::incrementAndGet);
        assertThat(sideEffect.apply("foo").flatMap(constantly(io(counter::get))),
                   yieldsValue(equalTo(1)));

        Effect<AtomicInteger> fnEffect = Effect.fromConsumer(AtomicInteger::incrementAndGet);
        assertThat(fnEffect.apply(counter).flatMap(constantly(io(counter::get))),
                   yieldsValue(equalTo(2)));
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