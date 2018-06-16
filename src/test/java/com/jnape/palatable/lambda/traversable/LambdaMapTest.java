package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.FunctorLaws;
import testsupport.traits.TraversableLaws;

import java.util.HashMap;

import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Collections.singletonMap;

@RunWith(Traits.class)
public class LambdaMapTest {

    @TestTraits({FunctorLaws.class, TraversableLaws.class})
    public Subjects<LambdaMap<Integer, String>> testSubject() {
        return subjects(LambdaMap.empty(),
                        LambdaMap.wrap(singletonMap(1, "foo")),
                        LambdaMap.wrap(new HashMap<Integer, String>() {{
                            put(1, "foo");
                            put(2, "bar");
                            put(3, "baz");
                        }}));
    }
}