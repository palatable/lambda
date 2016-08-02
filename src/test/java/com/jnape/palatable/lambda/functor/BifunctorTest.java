package com.jnape.palatable.lambda.functor;

import org.junit.Test;
import testsupport.applicatives.InvocationRecordingBifunctor;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BifunctorTest {

    @Test
    public void biMapLUsesIdentityForRightBiMapFunction() {
        AtomicReference<Function> rightInvocation = new AtomicReference<>();
        Bifunctor<String, Integer> bifunctor = new InvocationRecordingBifunctor<>(new AtomicReference<>(), rightInvocation);
        bifunctor.biMapL(String::toUpperCase);
        assertThat(rightInvocation.get(), is(id()));
    }

    @Test
    public void biMapRUsesIdentityForLeftBiMapFunction() {
        AtomicReference<Function> leftInvocation = new AtomicReference<>();
        Bifunctor<String, Integer> bifunctor = new InvocationRecordingBifunctor<>(leftInvocation, new AtomicReference<>());
        bifunctor.biMapR(String::valueOf);
        assertThat(leftInvocation.get(), is(id()));
    }
}