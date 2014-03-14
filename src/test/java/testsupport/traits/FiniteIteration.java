package testsupport.traits;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.traitor.traits.Trait;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static testsupport.matchers.FiniteIterableMatcher.finitelyIterable;

public class FiniteIteration implements Trait<MonadicFunction<Iterable, Iterable>> {

    @Override
    public void test(MonadicFunction<Iterable, Iterable> testSubject) {
        Iterable result = testSubject.apply(asList(1, 2, 3));
        assertThat(result, is(finitelyIterable()));
    }
}

