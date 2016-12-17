package testsupport.traits;

import com.jnape.palatable.traitor.traits.Trait;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Size.size;
import static com.jnape.palatable.lambda.functions.builtin.fn1.ToCollection.toCollection;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Filter.filter;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public final class CoProductProjections implements Trait<Class<?>> {

    @Override
    public void test(Class<?> coProductType) {
        List<Method> declaredMethods = asList(coProductType.getDeclaredMethods());
        Iterable<Method> staticFactoryMethods = filter(m -> m.getName().length() == 1, declaredMethods);
        List<String> staticFactoryMethodNames = toCollection(ArrayList::new, map(Method::getName, staticFactoryMethods));
        Collections.sort(staticFactoryMethodNames);

        if (size(staticFactoryMethodNames) == 0)
            fail("Couldn't find any static constructors for type " + coProductType.getSimpleName());

        assertIndividualProjections(coProductType, staticFactoryMethods);
        assertFullProjection(coProductType, staticFactoryMethods, staticFactoryMethodNames);
    }

    private void assertFullProjection(Class<?> coProductType, Iterable<Method> staticFactoryMethods,
                                      List<String> staticFactoryMethodNames) {
        staticFactoryMethods.forEach(sfm -> {
            try {
                String value = "value";
                Object instance = sfm.invoke(null, value);
                Method project = coProductType.getDeclaredMethod("project");
                Object tuple = project.invoke(instance);
                for (int i = 0; i < staticFactoryMethodNames.size(); i++) {
                    Object slot = tuple.getClass().getDeclaredMethod("_" + (i + 1)).invoke(tuple);
                    if (i == staticFactoryMethodNames.indexOf(sfm.getName()))
                        assertEquals(format("Assertion %1$s.%2$s(\"%3$s\")._%4$s() == Optional.of(\"%3$s\")",
                                            coProductType.getSimpleName(),
                                            sfm.getName(),
                                            value,
                                            i + 1), Optional.of(value), slot);
                    else
                        assertEquals(format("Assertion %1$s.%2$s(\"%3$s\")._%4$s() == Optional.empty()",
                                            coProductType.getSimpleName(),
                                            sfm.getName(),
                                            value,
                                            i + 1), Optional.empty(), slot);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Trait " + getClass().getSimpleName() + " is broken: " + e.getMessage());
            } catch (NoSuchMethodException e) {
                fail(coProductType.getSimpleName() + " is missing an expected method: " + e.getMessage());
            }
        });
    }

    private void assertIndividualProjections(Class<?> coProductType, Iterable<Method> staticFactoryMethods) {
        staticFactoryMethods.forEach(sfm -> {
            try {
                String value = "value";
                Object instance = sfm.invoke(null, value);
                Method project = coProductType.getDeclaredMethod("project" + sfm.getName().toUpperCase());
                assertEquals(format("Assertion %1$s.%2$s(\"%3$s\").%4$s() == Optional.of(\"%3$s\")",
                                    coProductType.getSimpleName(),
                                    sfm.getName(),
                                    value,
                                    project.getName()), Optional.of(value), project.invoke(instance));
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Trait " + getClass().getSimpleName() + " is broken: " + e.getMessage());
            } catch (NoSuchMethodException e) {
                fail(coProductType.getSimpleName() + " is missing an expected method: " + e.getMessage());
            }
        });
    }
}
