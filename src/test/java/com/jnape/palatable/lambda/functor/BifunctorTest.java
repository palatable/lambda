package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.Fn1;
import org.junit.Test;
import testsupport.applicatives.InvocationRecordingBifunctor;

import java.util.concurrent.atomic.AtomicReference;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BifunctorTest {

    @Test
    public void biMapLUsesIdentityForRightBiMapFunction() {
        AtomicReference<Fn1<?, ?>> rightInvocation = new AtomicReference<>();
        Bifunctor<String, Integer, InvocationRecordingBifunctor<?, ?>> bifunctor =
                new InvocationRecordingBifunctor<>(new AtomicReference<>(), rightInvocation);
        bifunctor.biMapL(String::toUpperCase);
        assertThat(rightInvocation.get(), is(id()));
    }

    @Test
    public void biMapRUsesIdentityForLeftBiMapFunction() {
        AtomicReference<Fn1<?, ?>> leftInvocation = new AtomicReference<>();
        Bifunctor<String, Integer, InvocationRecordingBifunctor<?, ?>> bifunctor =
                new InvocationRecordingBifunctor<>(leftInvocation, new AtomicReference<>());
        bifunctor.biMapR(String::valueOf);
        assertThat(leftInvocation.get(), is(id()));
    }
}