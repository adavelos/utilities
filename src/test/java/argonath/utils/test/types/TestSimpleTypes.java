package argonath.utils.test.types;

import argonath.reflector.config.Configuration;
import argonath.reflector.types.simple.SimpleType;
import argonath.reflector.types.simple.SimpleTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestSimpleTypes {

    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    public void testSimpleType() {
        assertNotNull(SimpleTypes.simpleType(String.class));
        assertNotNull(SimpleTypes.simpleType(Integer.class));
        assertNull(SimpleTypes.simpleType(List.class));
    }

    @Test
    public void testIsSimpleType() {
        assertTrue(SimpleTypes.isSimpleType(String.class));
        assertTrue(SimpleTypes.isSimpleType(Integer.class));
        assertFalse(SimpleTypes.isSimpleType(List.class));
    }

    @Test
    public void testRegister() {
        SimpleTypes.register(CustomType.class, new CustomTypeSimpleType());
        assertTrue(SimpleTypes.isSimpleType(CustomType.class));
    }

    @Test
    public void testFromToString() {
        SimpleType<String> stringType = SimpleTypes.simpleType(String.class);
        assertEquals("test", stringType.toString(stringType.fromString("test")));

        SimpleType<Integer> integerType = SimpleTypes.simpleType(Integer.class);
        assertEquals("1", integerType.toString(integerType.fromString("1")));

        SimpleType<Float> floatType = SimpleTypes.simpleType(Float.class);
        assertEquals("1.0", floatType.toString(floatType.fromString("1.0")));
        assertEquals("1.0", floatType.toString(floatType.fromString("1f")));

        SimpleType<Double> doubleType = SimpleTypes.simpleType(Double.class);
        assertEquals("1.0", doubleType.toString(doubleType.fromString("1.0")));
        assertEquals("1.0", doubleType.toString(doubleType.fromString("1d")));

        SimpleType<Long> longType = SimpleTypes.simpleType(Long.class);
        assertEquals("1", longType.toString(longType.fromString("1")));

        SimpleType<Character> characterType = SimpleTypes.simpleType(Character.class);
        assertEquals("a", characterType.toString(characterType.fromString("a")));

        SimpleType<Byte> byteType = SimpleTypes.simpleType(Byte.class);
        assertEquals("1", byteType.toString(byteType.fromString("1")));

        SimpleType<Short> shortType = SimpleTypes.simpleType(Short.class);
        assertEquals("1", shortType.toString(shortType.fromString("1")));

        SimpleType<Boolean> booleanType = SimpleTypes.simpleType(Boolean.class);
        assertEquals("true", booleanType.toString(booleanType.fromString("true")));
        assertEquals("false", booleanType.toString(booleanType.fromString("false")));
        assertEquals("false", booleanType.toString(booleanType.fromString("0")));

        // local date
        SimpleType<java.time.LocalDate> localDateType = SimpleTypes.simpleType(java.time.LocalDate.class);
        assertEquals("2021-01-01", localDateType.toString(localDateType.fromString("2021-01-01")));

        // local date time
        SimpleType<java.time.LocalDateTime> localDateTimeType = SimpleTypes.simpleType(java.time.LocalDateTime.class);
        assertEquals("2021-01-01T00:00:00", localDateTimeType.toString(localDateTimeType.fromString("2021-01-01T00:00:00")));

        // offset date time
        SimpleType<java.time.OffsetDateTime> offsetDateTimeType = SimpleTypes.simpleType(java.time.OffsetDateTime.class);
        assertEquals("2021-01-01T00:00:00+02:00", offsetDateTimeType.toString(offsetDateTimeType.fromString("2021-01-01T00:00:00+02:00")));

        // offset time
        SimpleType<java.time.OffsetTime> offsetTimeType = SimpleTypes.simpleType(java.time.OffsetTime.class);
        assertEquals("00:00:00+02:00", offsetTimeType.toString(offsetTimeType.fromString("00:00:00+02:00")));

        // zoned date time
        SimpleType<java.time.ZonedDateTime> zonedDateTimeType = SimpleTypes.simpleType(java.time.ZonedDateTime.class);
        assertEquals("2021-01-01T00:00:00Z", zonedDateTimeType.toString(zonedDateTimeType.fromString("2021-01-01T00:00:00Z")));

        // big integer
        SimpleType<java.math.BigInteger> bigIntegerType = SimpleTypes.simpleType(java.math.BigInteger.class);
        assertEquals("123", bigIntegerType.toString(bigIntegerType.fromString("123")));

        // big decimal
        SimpleType<java.math.BigDecimal> bigDecimalType = SimpleTypes.simpleType(java.math.BigDecimal.class);
        assertEquals("123.456", bigDecimalType.toString(bigDecimalType.fromString("123.456")));
    }

    private static class CustomType {
    }

    private static class CustomTypeSimpleType implements SimpleType<CustomType> {

        @Override
        public String toString(CustomType object) {
            return "CustomType";
        }

        @Override
        public CustomType fromString(String string) {
            return new CustomType();
        }
    }
}