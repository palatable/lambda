package testsupport.exceptions;

public class OutOfScopeException extends RuntimeException {

    public OutOfScopeException(String s) {
        super(s);
    }

    public static OutOfScopeException outOfScope() {
        return new OutOfScopeException("Unexpected invocation of unimplemented method");
    }
}
