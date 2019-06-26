package testsupport.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.invocation.Invocation;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.internal.util.MockUtil.getInvocationContainer;

public class ZeroInvocationsMatcher<T> extends BaseMatcher<T> {
    @Override
    public boolean matches(Object item) {
        try {
            verifyNoMoreInteractions(item);
            return true;
        } catch (NoInteractionsWanted unexpectedInteractions) {
            return false;
        } catch (NotAMockException notVerifiable) {
            throw new AssertionError("Can't do verifications on non-mocked objects.");
        }
    }

    @Override
    public void describeMismatch(Object item, final Description description) {
        description.appendText("had these: ");
        for (Invocation invocation : getInvocationContainer(item).getInvocations())
            description.appendText(invocation.toString());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("no interactions with mock");
    }

    public static <T> Matcher<T> wasNeverInteractedWith() {
        return new ZeroInvocationsMatcher<>();
    }
}
