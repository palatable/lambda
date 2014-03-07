package testsupport.traits;

import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.ArrayList;

public class EmptyIterableSupport implements Trait<MonadicFunction<Iterable, Iterable>> {

    @Override
    public void test(MonadicFunction<Iterable, Iterable> testSubject) {
        try {
            testSubject.apply(new ArrayList());
        } catch (Exception e) {
            throw new AssertionError("Expected support for empty iterable arguments", e);
        }
    }
}
