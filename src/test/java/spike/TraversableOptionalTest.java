package spike;

import com.jnape.palatable.lambda.traversable.TraversableOptional;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.FunctorLaws;
import testsupport.traits.TraversableLaws;

import java.util.Optional;

import static com.jnape.palatable.traitor.framework.Subjects.subjects;

@RunWith(Traits.class)
public class TraversableOptionalTest {

    @TestTraits({FunctorLaws.class, TraversableLaws.class})
    public Subjects<TraversableOptional<Integer>> testSubject() {
        return subjects(TraversableOptional.empty(), TraversableOptional.wrap(Optional.of(1)));
    }
}