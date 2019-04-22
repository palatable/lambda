package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.functor.builtin.Tagged;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.optics.Optic.optic;
import static org.junit.Assert.assertEquals;

public class OpticTest {

    @Test
    public void monomorphize() {
        Optic<Tagged<?, ?>, Identity<?>, String, String, String, String>        optic        = optic(pafb -> pafb);
        Fn1<Tagged<String, Identity<String>>, Tagged<String, Identity<String>>> monomorphize = optic.monomorphize();
        assertEquals(new Identity<>("foo"), monomorphize.apply(new Tagged<>(new Identity<>("foo"))).unTagged());
    }

    @Test
    public void reframe() {
        Optic<Profunctor<?, ?, ?>, Functor<?, ?>, String, String, String, String> optic    = optic(pafb -> pafb);
        Optic<Fn1<?, ?>, Identity<?>, String, String, String, String>             reframed = Optic.reframe(optic);
        assertEquals(new Identity<>("foo"),
                     reframed.<Fn1<?, ?>, Identity<?>, Identity<String>, Identity<String>,
                             Fn1<String, Identity<String>>,
                             Fn1<String, Identity<String>>>apply(constantly(new Identity<>("foo"))).apply("bar"));
    }

    @Test
    public void adapt() {
        Optic<Profunctor<?, ?, ?>, Functor<?, ?>, String, String, String, String> optic  = optic(pafb -> pafb);
        Optic.Simple<Profunctor<?, ?, ?>, Functor<?, ?>, String, String>          simple = Optic.Simple.adapt(optic);

        assertEquals("foo",
                     simple.<Fn1<?, ?>, Identity<?>, Identity<String>, Identity<String>,
                             Fn1<String, Identity<String>>,
                             Fn1<String, Identity<String>>>apply(constantly(new Identity<>("foo")))
                             .apply("bar").runIdentity());
    }
}