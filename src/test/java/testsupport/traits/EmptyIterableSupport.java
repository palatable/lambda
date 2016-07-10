package testsupport.traits;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.traits.Trait;

import java.util.ArrayList;

public class EmptyIterableSupport implements Trait<Fn1<Iterable, ?>> {

    @Override
    public void test(Fn1<Iterable, ?> testSubject) {
        try {
            testSubject.apply(new ArrayList());
        } catch (Exception e) {
            throw new AssertionError("Expected support for empty iterable arguments", e);
        }
    }
}
