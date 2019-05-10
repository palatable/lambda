package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.Deforesting;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Collections.emptyList;

@RunWith(Traits.class)
public class UnioningIterableTest {
    @TestTraits({Deforesting.class})
    public Subjects<Fn1<Iterable<Integer>, Iterable<Integer>>> testSubject() {
        return subjects(xs -> new UnioningIterable<>(emptyList(), xs),
                        xs -> new UnioningIterable<>(xs, emptyList()),
                        xs -> new UnioningIterable<>(repeat(1), xs),
                        xs -> new UnioningIterable<>(xs, repeat(1)));
    }

}