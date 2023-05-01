package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Iterate.iterate;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight.foldRight;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.functions.ExplainFold.explainFold;

@RunWith(Traits.class)
public class FoldRightTest {

    @TestTraits({EmptyIterableSupport.class})
    public Fn1<Iterable<Object>, Iterable<Object>> createTestSubject() {
        return foldRight((o, objects) -> objects, lazy(singletonList(new Object()))).fmap(Lazy::value);
    }

    @Test
    public void foldRightAccumulatesRightToLeft() {
        assertThat(foldRight((a, lazyAcc) -> lazyAcc.fmap(acc -> explainFold().apply(a, acc)),
                             lazy("5"),
                             asList("1", "2", "3", "4"))
                           .value(),
                   is("(1 + (2 + (3 + (4 + 5))))")
        );
    }

    @Test
    public void stackSafe() {
        Lazy<Integer> lazy = foldRight((x, lazyY) -> x < STACK_EXPLODING_NUMBER ? lazyY.fmap(y -> y) : lazy(x),
                                       lazy(0),
                                       iterate(x -> x + 1, 0));

        assertEquals(STACK_EXPLODING_NUMBER, lazy.value());
    }
}
