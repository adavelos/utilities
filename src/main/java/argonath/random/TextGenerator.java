package argonath.random;

import argonath.utils.Assert;

public class TextGenerator {
    private static final String[] WORD_BANK = {
            "lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit", "sed", "do",
            "eiusmod", "tempor", "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua", "enim",
            "ad", "minim", "veniam", "quis", "nostrud", "exercitation", "ullamco", "laboris", "nisi", "aliquip",
            "ex", "ea", "commodo", "consequat", "duis", "aute", "irure", "in", "reprehenderit", "voluptate",
            "velit", "esse", "cillum", "eu", "fugiat", "nulla", "pariatur", "excepteur", "sint", "occaecat",
            "cupidatat", "non", "proident", "sunt", "culpa", "qui", "officia", "deserunt", "mollit", "anim",
            "id", "est", "laborum", "sed", "perspiciatis", "unde", "omnis", "iste", "natus", "error",
            "voluptatem", "accusantium", "doloremque", "laudantium", "totam", "rem", "aperiam", "eaque", "ipsa",
            "quae", "ab", "illo", "inventore", "veritatis", "quasi", "architecto", "beatae", "vitae", "dicta",
            "explicabo", "nemo", "ipsam", "quia", "voluptas", "aspernatur", "aut", "odit", "fugit", "consequuntur",
            "magni", "dolores", "eos", "ratione", "sequi", "nesciunt", "neque", "porro", "quisquam", "dolorem",
            "adipisci", "numquam", "eius", "modi", "tempora", "incidunt", "magnam", "aliquam", "quaerat", "minima",
            "nostrum", "exercitationem", "ullam", "corporis", "suscipit", "laboriosam", "nisi", "aliquid", "commodi",
            "consequatur", "autem", "vel", "eum", "iure", "reprehenderit", "voluptate", "velit", "esse", "quam",
            "nihil", "molestiae", "illum", "quo", "at", "vero", "accusamus", "iusto", "odio", "dignissimos",
            "ducimus", "blanditiis", "praesentium", "voluptatum", "deleniti", "atque", "corrupti", "quos", "quas",
            "molestias", "excepturi", "obcaecati", "cupiditate", "provident", "similique", "culpa", "officia", "deserunt",
            "mollitia", "animi", "laborum", "dolorum", "fuga", "harum", "quidem", "rerum", "facilis", "expedita",
            "distinctio", "nam", "libero", "tempore", "cum", "soluta", "nobis", "eligendi", "optio", "cumque",
            "impedit", "quo", "minus", "quod", "maxime", "placeat", "facere", "possimus", "assumenda", "repellendus"
    };

    // lorem ipsum
    public static String loremIpsum(int length) {
        Assert.isTrue(length >= 0, "Invalid Argument: Length is negative");
        return loremIpsum(length, length);
    }

    public static String loremIpsum(int minLength, int maxLength) {
        Assert.isTrue(minLength <= maxLength, "Invalid Argument: Min length is greater than Max length");
        int length = RandomNumber.getInteger(minLength, maxLength + 1);
        if (length == 0) {
            return "";
        }
        if (length == 1) {
            int randomInt = RandomNumber.getInteger(0, 26);
            char randomChar = (char) (randomInt + 'a');
            return String.valueOf(randomChar);
        }
        StringBuilder sb = new StringBuilder();
        // while sb.len < length keep building
        while (sb.length() <= length) {
            int remainingLength = length - sb.length();
            String nextWord = WORD_BANK[RandomNumber.getInteger(0, WORD_BANK.length)];
            while (nextWord.length() > remainingLength) {
                nextWord = nextWord.substring(0, remainingLength);
            }
            sb.append(nextWord);
            sb.append(" ");
        }

        if (sb.length() > length) {
            sb.delete(length, sb.length());
        }

        return sb.toString();
    }


}
