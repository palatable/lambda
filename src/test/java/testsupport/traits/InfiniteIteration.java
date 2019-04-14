package testsupport.traits;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.traits.Trait;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static testsupport.matchers.FiniteIterableMatcher.finitelyIterable;

public class InfiniteIteration implements Trait<Fn1<Iterable<?>, Iterable<?>>> {

    @Override
    public void test(Fn1<Iterable<?>, Iterable<?>> testSubject) {
        Iterable<?> result = testSubject.apply(asList(1, 2, 3));
        assertThat(result, is(not(finitelyIterable())));
    }
}
