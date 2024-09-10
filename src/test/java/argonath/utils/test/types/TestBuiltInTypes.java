package argonath.utils.test.types;

import argonath.reflector.config.Configuration;
import argonath.reflector.types.builtin.Collections;
import argonath.reflector.types.builtin.DateTime;
import argonath.reflector.types.builtin.Numbers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TestBuiltInTypes {
    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    public void testBigDecimal() {
        Assertions.assertEquals("123", Numbers.bigDecimalToString(new BigDecimal("123")));
        Assertions.assertEquals("123.456", Numbers.bigDecimalToString(new BigDecimal("123.456")));
        Assertions.assertEquals("123", Numbers.bigIntegerToString(new BigInteger("123")));
        // null cases
        Assertions.assertNull(Numbers.bigDecimalToString(null));
        Assertions.assertNull(Numbers.bigIntegerToString(null));
    }

    @Test
    public void testDates() {
        Assertions.assertEquals("2021-01-01", DateTime.toString(DateTime.fromStringToDate("2021-01-01")));
        Assertions.assertEquals("2021-01-01T00:00:00", DateTime.toString(DateTime.fromStringToDateTime("2021-01-01T00:00:00")));
        Assertions.assertEquals("2021-01-01T00:00:00+02:00", DateTime.toString(DateTime.fromStringToOffsetDateTime("2021-01-01T00:00:00+02:00")));
        Assertions.assertEquals("00:00:00+02:00", DateTime.toString(DateTime.fromStringToOffsetTime("00:00:00+02:00")));
        Assertions.assertEquals("2021-01-01T00:00:00Z", DateTime.toString(DateTime.fromStringToZonedDateTime("2021-01-01T00:00:00Z")));

        Configuration.withConfigFile("default.reflector.properties");
        Assertions.assertEquals("2021-01-01", DateTime.toString(DateTime.fromStringToDate("2021-01-01")));
        Assertions.assertEquals("2021-01-01T00:00:00", DateTime.toString(DateTime.fromStringToDateTime("2021-01-01T00:00:00")));
        Assertions.assertEquals("2021-01-01T00:00:00+02:00", DateTime.toString(DateTime.fromStringToOffsetDateTime("2021-01-01T00:00:00+02:00")));

        // test that we can dynamically change the configuration
        Configuration.withConfigFile("alt.reflector.properties");
        Assertions.assertEquals("2021/01/01", DateTime.toString(DateTime.fromStringToDate("2021/01/01")));


    }

    @Test
    public void testEmptyList() {
        List<?> list = Collections.emptyList();
        Assertions.assertTrue(list instanceof ArrayList, "list is ArrayList");
        Assertions.assertTrue(list.isEmpty(), "list is empty");
    }
}
