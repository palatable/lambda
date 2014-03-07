package testsupport.functions;

import com.jnape.palatable.lambda.DyadicFunction;

import static java.lang.String.format;

public class ExplainFold {

    public static DyadicFunction<String, String, String> explainFold() {
        return new DyadicFunction<String, String, String>() {
            @Override
            public String apply(String acc, String x) {
                return format("(%s + %s)", acc, x);
            }
        };
    }
}
