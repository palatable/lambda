package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import org.junit.Test;
import testsupport.applicatives.InvocationRecordingProfunctor;

import java.util.concurrent.atomic.AtomicReference;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Identity.id;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProfunctorTest {

    @Test
    public void diMapLUsesIdentityForRightDiMapFunction() {
        AtomicReference<MonadicFunction> rightInvocation = new AtomicReference<>();
        Profunctor<String, Integer> profunctor = new InvocationRecordingProfunctor<>(new AtomicReference<>(), rightInvocation);
        profunctor.diMapL(Object::toString);
        assertThat(rightInvocation.get(), is(id()));
    }

    @Test
    public void diMapRUsesIdentityForLeftDiMapFunction() {
        AtomicReference<MonadicFunction> leftInvocation = new AtomicReference<>();
        Profunctor<String, Integer> profunctor = new InvocationRecordingProfunctor<>(leftInvocation, new AtomicReference<>());
        profunctor.diMapR(String::valueOf);
        assertThat(leftInvocation.get(), is(id()));
    }
}