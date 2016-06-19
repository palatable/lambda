package com.jnape.palatable.lambda.applicative;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import org.junit.Test;
import testsupport.applicatives.InvocationRecordingBiFunctor;

import java.util.concurrent.atomic.AtomicReference;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Identity.id;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BiFunctorTest {

    @Test
    public void biMapLUsesIdentityForRightBiMapFunction() {
        AtomicReference<MonadicFunction> rightInvocation = new AtomicReference<>();
        BiFunctor<String, Integer> biFunctor = new InvocationRecordingBiFunctor<>(new AtomicReference<>(), rightInvocation);
        biFunctor.biMapL(String::toUpperCase);
        assertThat(rightInvocation.get(), is(id()));
    }

    @Test
    public void biMapRUsesIdentityForLeftBiMapFunction() {
        AtomicReference<MonadicFunction> leftInvocation = new AtomicReference<>();
        BiFunctor<String, Integer> biFunctor = new InvocationRecordingBiFunctor<>(leftInvocation, new AtomicReference<>());
        biFunctor.biMapR(String::valueOf);
        assertThat(leftInvocation.get(), is(id()));
    }
}