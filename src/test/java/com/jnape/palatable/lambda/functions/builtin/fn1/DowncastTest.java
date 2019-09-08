package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.Functor;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Downcast.downcast;

public class DowncastTest {

    @Test
    @SuppressWarnings("unused")
    public void safeDowncast() {
        CharSequence charSequence = "123";
        String       s            = downcast(charSequence);

        Functor<Integer, Maybe<?>> maybeInt = nothing();
        Maybe<Integer>             cast     = downcast(maybeInt);
    }

    @Test(expected = ClassCastException.class)
    @SuppressWarnings({"JavacQuirks", "unused"})
    public void unsafeDowncast() {
        CharSequence charSequence = "123";
        Integer      explosion    = downcast(charSequence);
    }
}