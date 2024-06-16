package argonath.utils.reflector.reader.expression;

import java.util.Collections;
import java.util.List;

public class Method {
    private boolean identity;
    private String name;
    private List<String> args;

    public Method(String name, List<String> args, boolean identity) {
        this.name = name;
        this.args = args;
        this.identity = identity;
    }

    public static Method identity() {
        return new Method(null, Collections.emptyList(), true);
    }

    public String name() {
        return name;
    }

    public List<String> args() {
        return args;
    }

    public boolean isIdentity() {
        return identity;
    }


    public interface Token {
    }

    public static class ValueToken implements Token {
        private String value;

        public ValueToken(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    public static class MethodToken implements Token {
        private Method method;

        public MethodToken(Method method) {
            this.method = method;
        }

        public Method method() {
            return method;
        }
    }
}
