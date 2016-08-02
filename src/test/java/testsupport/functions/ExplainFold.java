package testsupport.functions;

import java.util.function.BiFunction;

import static java.lang.String.format;

public class ExplainFold {

    public static BiFunction<String, String, String> explainFold() {
        return (acc, x) -> format("(%s + %s)", acc, x);
    }
}
