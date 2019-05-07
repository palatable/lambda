package testsupport.functions;

import com.jnape.palatable.lambda.functions.Fn2;

import java.util.function.BiFunction;

import static java.lang.String.format;

public class ExplainFold {

    public static Fn2<String, String, String> explainFold() {
        return (acc, x) -> format("(%s + %s)", acc, x);
    }
}
