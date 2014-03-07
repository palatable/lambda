package testsupport.traits;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.traitor.traits.Trait;

import static com.jnape.palatable.lambda.staticfactory.IterableFactory.iterable;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static testsupport.matchers.FiniteIterableMatcher.finitelyIterable;

public class InfiniteIteration implements Trait<MonadicFunction<Iterable, Iterable>> {

    @Override
    public void test(MonadicFunction<Iterable, Iterable> testSubject) {
        Iterable result = testSubject.apply(iterable(1, 2, 3));
        assertThat(result, is(not(finitelyIterable())));
    }
}

