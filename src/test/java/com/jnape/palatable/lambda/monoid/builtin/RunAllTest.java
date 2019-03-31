package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monoid.builtin.RunAll.runAll;
import static org.junit.Assert.assertEquals;

public class RunAllTest {

    @Test
    public void monoid() {
        Monoid<Integer> add = Monoid.monoid((x, y) -> x + y, 0);
        assertEquals((Integer) 3, runAll(add).apply(io(1), io(2)).unsafePerformIO());
        assertEquals((Integer) 0, runAll(add).identity().unsafePerformIO());
    }
}