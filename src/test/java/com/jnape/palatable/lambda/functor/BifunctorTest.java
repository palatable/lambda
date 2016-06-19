package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import org.junit.Test;
import testsupport.applicatives.InvocationRecordingBifunctor;

import java.util.concurrent.atomic.AtomicReference;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Identity.id;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BifunctorTest {

    @Test
    public void biMapLUsesIdentityForRightBiMapFunction() {
        AtomicReference<MonadicFunction> rightInvocation = new AtomicReference<>();
        Bifunctor<String, Integer> bifunctor = new InvocationRecordingBifunctor<>(new AtomicReference<>(), rightInvocation);
        bifunctor.biMapL(String::toUpperCase);
        assertThat(rightInvocation.get(), is(id()));
    }

    @Test
    public void biMapRUsesIdentityForLeftBiMapFunction() {
        AtomicReference<MonadicFunction> leftInvocation = new AtomicReference<>();
        Bifunctor<String, Integer> bifunctor = new InvocationRecordingBifunctor<>(leftInvocation, new AtomicReference<>());
        bifunctor.biMapR(String::valueOf);
        assertThat(leftInvocation.get(), is(id()));
    }
}