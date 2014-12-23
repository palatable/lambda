package testsupport.functions;

import com.jnape.palatable.lambda.functions.DyadicFunction;

import static java.lang.String.format;

public class ExplainFold {

    public static DyadicFunction<String, String, String> explainFold() {
        return (acc, x) -> format("(%s + %s)", acc, x);
    }
}
