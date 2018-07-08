package testsupport.matchers;

import com.jnape.palatable.lambda.functions.builtin.fn2.Eq;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import testsupport.time.InstantRecordingClock;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;

import static com.jnape.palatable.lambda.functions.builtin.fn2.All.all;
import static com.jnape.palatable.lambda.functions.builtin.fn2.InGroupsOf.inGroupsOf;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Slide.slide;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Zip.zip;
import static java.time.Duration.between;

public final class RateLimitedIterationMatcher<A> extends TypeSafeMatcher<Iterable<A>> {
    private final Iterable<A>           elements;
    private final Duration              delay;
    private final InstantRecordingClock clock;
    private final Long                  limit;

    public RateLimitedIterationMatcher(Long limit, Duration delay, Iterable<A> elements, InstantRecordingClock clock) {
        this.elements = elements;
        this.delay = delay;
        this.clock = clock;
        this.limit = limit;
    }

    @Override
    protected boolean matchesSafely(Iterable<A> xs) {
        xs.forEach(__ -> clock.saveLastInstant());

        Boolean enoughDelay = all(d -> d.toNanos() > delay.toNanos(), map(boundaries -> {
            Iterator<Instant> it = boundaries.iterator();
            Instant first = it.next();
            Instant second = it.next();
            return between(first, second);
        }, slide(2, map(instants -> instants.iterator().next(), inGroupsOf(limit.intValue(), clock.instants())))));

        Boolean sameElements = all(Eq.<A>eq().uncurry(), zip(elements, xs));

        return enoughDelay && sameElements;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Iterated elements " + toCollection(ArrayList::new, elements) + " with at least " + delay.toMillis() + "ms between groups of " + limit);
    }

    @Override
    protected void describeMismatchSafely(Iterable<A> item, Description mismatchDescription) {
        mismatchDescription.appendText("Iterated elements "
                                               + toCollection(ArrayList::new, item)
                                               + " with the following delays between groups: "
                                               + toCollection(ArrayList::new, map(instants -> instants.iterator().next(), inGroupsOf(limit.intValue(), clock.instants()))));
    }

    public static <A> RateLimitedIterationMatcher<A> iteratesAccordingToRateLimit(Long limit, Duration duration,
                                                                                  Iterable<A> elements,
                                                                                  InstantRecordingClock clock) {
        return new RateLimitedIterationMatcher<>(limit, duration, elements, clock);
    }
}
