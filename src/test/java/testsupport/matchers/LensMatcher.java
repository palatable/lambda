package testsupport.matchers;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.lens.Lens;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.HashSet;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Empty.empty;
import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static com.jnape.palatable.lambda.functions.builtin.fn2.CartesianProduct.cartesianProduct;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;

public class LensMatcher<S, A> extends BaseMatcher<Lens<S, S, A, A>> {

    private final Iterable<Tuple2<S, A>> combinations;

    private LensMatcher(Iterable<Tuple2<S, A>> combinations) {
        this.combinations = combinations;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean matches(Object other) {
        if (!(other instanceof Lens))
            return false;

        Lens<S, S, A, A> lens = (Lens<S, S, A, A>) other;
        return youGetBackWhatYouPutIn(lens)
                && puttingBackWhatYouGotChangesNothing(lens)
                && settingTwiceIsEquivalentToSettingOnce(lens);
    }

    @Override
    public void describeTo(Description description) {
        throw new UnsupportedOperationException();
    }

    private boolean youGetBackWhatYouPutIn(Lens<S, S, A, A> lens) {
        return all(into((s, b) -> view(lens, set(lens, b, s)).equals(b)), combinations);
    }

    private boolean puttingBackWhatYouGotChangesNothing(Lens<S, S, A, A> lens) {
        return all(into((s, b) -> set(lens, view(lens, s), s).equals(s)), combinations);
    }

    private boolean settingTwiceIsEquivalentToSettingOnce(Lens<S, S, A, A> lens) {
        return all(into((s, b) -> set(lens, b, set(lens, b, s)).equals(set(lens, b, s))), combinations);
    }

    public static <S, A> LensMatcher<S, A> isLawfulForAllSAndB(Iterable<S> ss, Iterable<A> bs) {
        return new LensMatcher<>(cartesianProduct(ss, bs));
    }
}
