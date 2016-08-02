package com.jnape.palatable.lambda.functor;

import org.junit.Test;
import testsupport.applicatives.InvocationRecordingProfunctor;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProfunctorTest {

    @Test
    public void diMapLUsesIdentityForRightDiMapFunction() {
        AtomicReference<Function> rightInvocation = new AtomicReference<>();
        Profunctor<String, Integer> profunctor = new InvocationRecordingProfunctor<>(new AtomicReference<>(), rightInvocation);
        profunctor.diMapL(Object::toString);
        assertThat(rightInvocation.get(), is(id()));
    }

    @Test
    public void diMapRUsesIdentityForLeftDiMapFunction() {
        AtomicReference<Function> leftInvocation = new AtomicReference<>();
        Profunctor<String, Integer> profunctor = new InvocationRecordingProfunctor<>(leftInvocation, new AtomicReference<>());
        profunctor.diMapR(String::valueOf);
        assertThat(leftInvocation.get(), is(id()));
    }
}