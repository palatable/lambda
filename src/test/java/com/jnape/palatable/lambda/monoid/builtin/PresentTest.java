package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.monoid.builtin.Present.present;
import static org.junit.Assert.assertEquals;

public class PresentTest {

    @Test
    public void monoid() {
        Present<Integer> present = present();
        Semigroup<Integer> addition = (x, y) -> x + y;

        assertEquals(Optional.of(3), present.apply(addition, Optional.of(1), Optional.of(2)));
        assertEquals(Optional.of(1), present.apply(addition, Optional.empty(), Optional.of(1)));
        assertEquals(Optional.of(1), present.apply(addition, Optional.of(1), Optional.empty()));
        assertEquals(Optional.empty(), present.apply(addition, Optional.empty(), Optional.empty()));
    }
}