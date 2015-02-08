package com.jnape.palatable.lambda.applicative;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import org.junit.Test;
import testsupport.applicatives.InvocationRecordingProFunctor;

import java.util.concurrent.atomic.AtomicReference;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Identity.id;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProFunctorTest {

    @Test
    public void diMapLUsesIdentityForRightDiMapFunction() {
        AtomicReference<MonadicFunction> rightInvocation = new AtomicReference<>();
        ProFunctor<String, Integer> proFunctor = new InvocationRecordingProFunctor<>(new AtomicReference<>(), rightInvocation);
        proFunctor.diMapL(Object::toString);
        assertThat(rightInvocation.get(), is(id()));
    }

    @Test
    public void diMapRUsesIdentityForLeftDiMapFunction() {
        AtomicReference<MonadicFunction> leftInvocation = new AtomicReference<>();
        ProFunctor<String, Integer> proFunctor = new InvocationRecordingProFunctor<>(leftInvocation, new AtomicReference<>());
        proFunctor.diMapR(String::valueOf);
        assertThat(leftInvocation.get(), is(id()));
    }
}