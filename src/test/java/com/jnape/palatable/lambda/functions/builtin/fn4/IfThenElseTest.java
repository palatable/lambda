package com.jnape.palatable.lambda.functions.builtin.fn4;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Predicate;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse.ifThenElse;
import static org.junit.Assert.assertEquals;

public class IfThenElseTest {

    @Test
    public void standardLogic() {
        Predicate<Integer>    even = x -> x % 2 == 0;
        Fn1<Integer, Integer> inc  = x -> x + 1;
        Fn1<Integer, Integer> dec  = x -> x - 1;

        assertEquals((Integer) 3, ifThenElse(even, inc, dec, 2));
        assertEquals((Integer) 0, ifThenElse(even, inc, dec, 1));
    }
}