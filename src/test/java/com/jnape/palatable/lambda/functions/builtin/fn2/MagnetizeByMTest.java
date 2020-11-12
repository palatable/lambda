package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;
import testsupport.matchers.IterableMatcher;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.CycleM.cycleM;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Drop.drop;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GTE.gte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.MagnetizeByM.magnetizeByM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn2.NthM.nthM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.singleton;
import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.matchers.IterateTMatcher.isEmpty;
import static testsupport.matchers.IterateTMatcher.iterates;

public class MagnetizeByMTest {
    @Test
    public void magnetizesEmpty() {
        assertThat(magnetizeByM(GTE.<Integer>gte(), empty(pureIdentity())), isEmpty());
    }

    @Test
    public void magnetizesSingleton() {
        List<IterateT<Identity<?>, Integer>> actual = magnetizeByM(gte(), singleton(new Identity<>(1)))
                .<List<IterateT<Identity<?>, Integer>>, Identity<List<IterateT<Identity<?>, Integer>>>>toCollection(ArrayList::new)
                .runIdentity();
        assertThat(actual, contains(iterates(1)));
    }

    @Test
    public void magnetizesElementsInSeveralGroups() {
        List<IterateT<Identity<?>, Integer>> actual = magnetizeByM(gte(), fromIterable(pureIdentity(), asList(1, 2, 3, 2, 2, 3, 2, 1)))
                .<List<IterateT<Identity<?>, Integer>>, Identity<List<IterateT<Identity<?>, Integer>>>>toCollection(ArrayList::new)
                .runIdentity();
        assertThat(actual, contains(iterates(1, 2, 3),
                                    iterates(2, 2, 3),
                                    iterates(2),
                                    iterates(1)));
    }

    @Test
    public void magnetizesLargeGroups() {
        IterateT<Identity<?>, Integer> numbers = cycleM(takeM(10_000, naturalNumbersM(pureIdentity())));
        Identity<Maybe<IterateT<Identity<?>, Integer>>> maybeIdentity = nthM(3, magnetizeByM(gte(), numbers)).coerce();
        List<Integer> thirdGroup = maybeIdentity
                .runIdentity()
                .orElseThrow(AssertionError::new)
                .<List<Integer>, Identity<List<Integer>>>toCollection(ArrayList::new)
                .runIdentity();
        assertThat(thirdGroup, hasSize(10_000));
        assertThat(take(3, thirdGroup), IterableMatcher.iterates(1, 2, 3));
        assertThat(drop(9_997, thirdGroup), IterableMatcher.iterates(9_998, 9_999, 10_000));
    }

    @Test
    public void magnetizesLotsOfSmallGroups() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        Identity<Maybe<IterateT<Identity<?>, Integer>>> group = nthM(STACK_EXPLODING_NUMBER, magnetizeByM(lt(), numbers)).coerce();
        IterateT<Identity<?>, Integer> actual = group.runIdentity().orElseThrow(AssertionError::new);
        assertThat(actual, iterates(STACK_EXPLODING_NUMBER));
    }

    private static <M extends MonadRec<?, M>, A> IterateT<M, A> fromIterable(Pure<M> pureM, Iterable<A> as) {
        return foldLeft(IterateT::snoc, empty(pureM), map(pureM::<A, MonadRec<A, M>>apply, as));
    }
}