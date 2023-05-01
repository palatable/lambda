package testsupport.functions;

import com.jnape.palatable.lambda.functions.Fn2;

import static java.lang.String.format;

public class ExplainFold {

    public static Fn2<String, String, String> explainFold() {
        return (x, y) -> format("(%s + %s)", x, y);
    }
}
