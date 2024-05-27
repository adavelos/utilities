package argonath.utils.reflector.reader.filter;

import argonath.utils.xpath.XPathUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Method {
    private String name;
    private List<String> args;

    private Method(String name, List<String> args) {
        this.name = name;
        this.args = args;
    }

    static Method parse(String methodStr) {
        int openBracket = methodStr.indexOf("(");
        int closeBracket = methodStr.lastIndexOf(")");
        if (openBracket == -1 || closeBracket == -1) {
            return null;
        }
        String methodName = methodStr.substring(0, openBracket);
        String argsStr = methodStr.substring(openBracket + 1, closeBracket);
        String[] split = argsStr.split(",", -1);
        List<String> args = Arrays.asList(split).stream()
                .map(String::trim)
                .map(XPathUtil::stripQuotes)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        return new Method(methodName, args);
    }

    public String name() {
        return name;
    }

    public List<String> args() {
        return args;
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
