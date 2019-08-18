package testsupport.traits;

import com.jnape.palatable.traitor.traits.Trait;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static testsupport.traits.Equivalence.equivalence;

public interface EquivalenceTrait<A> extends Trait<Object> {

    Class<? super A> type();

    void test(Equivalence<A> equivalence);

    @Override
    default void test(Object value) {
        Class<? super A> type = type();
        if (value instanceof Equivalence<?>) {
            if (type.isInstance(((Equivalence<?>) value).getValue())) {
                @SuppressWarnings("unchecked") Equivalence<A> equivalenceC = (Equivalence<A>) value;
                test(equivalenceC);
                return;
            } else {
                throw new ClassCastException("Unable to create " + type.getSimpleName()
                                                     + " surrogate for value of type " + value.getClass());
            }
        }
        if (!type.isInstance(value))
            throw new ClassCastException("Unable to create " + type.getSimpleName() + " surrogate for value of type "
                                                 + value.getClass().getSimpleName());
        @SuppressWarnings("unchecked") A b = (A) value;
        test(equivalence(b, id()));
    }
}
