package argonath.reflector.types.simple;

import argonath.reflector.types.builtin.DateTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;

class BuiltInSimpleTypes {
    static SimpleType<String> STRING = new SimpleType<>() {

        @Override
        public String toString(String object) {
            return object;
        }

        @Override
        public String fromString(String string) {
            return string;
        }
    };

    static SimpleType<Integer> INTEGER = new SimpleType<>() {

        @Override
        public String toString(Integer object) {
            return object.toString();
        }

        @Override
        public Integer fromString(String string) {
            return Integer.parseInt(string);
        }
    };

    static SimpleType<Float> FLOAT = new SimpleType<>() {

        @Override
        public String toString(Float object) {
            return object.toString();
        }

        @Override
        public Float fromString(String string) {
            return Float.parseFloat(string);
        }
    };

    static SimpleType<Double> DOUBLE = new SimpleType<>() {

        @Override
        public String toString(Double object) {
            return object.toString();
        }

        @Override
        public Double fromString(String string) {
            return Double.parseDouble(string);
        }
    };

    static SimpleType<Long> LONG = new SimpleType<>() {

        @Override
        public String toString(Long object) {
            return object.toString();
        }

        @Override
        public Long fromString(String string) {
            return Long.parseLong(string);
        }
    };

    static SimpleType<Boolean> BOOLEAN = new SimpleType<>() {

        @Override
        public String toString(Boolean object) {
            return object.toString();
        }

        @Override
        public Boolean fromString(String string) {
            return Boolean.parseBoolean(string);
        }
    };

    static SimpleType<LocalDate> LOCAL_DATE = new SimpleType<>() {

        @Override
        public String toString(LocalDate object) {
            return DateTime.toString(object);
        }

        @Override
        public LocalDate fromString(String string) {
            return DateTime.fromStringToDate(string);
        }
    };

    static SimpleType<LocalDateTime> LOCAL_DATE_TIME = new SimpleType<>() {

        @Override
        public String toString(LocalDateTime object) {
            return DateTime.toString(object);
        }

        @Override
        public LocalDateTime fromString(String string) {
            return DateTime.fromStringToDateTime(string);
        }
    };

    // offset date time
    static SimpleType<OffsetDateTime> OFFSET_DATE_TIME = new SimpleType<>() {

        @Override
        public String toString(OffsetDateTime object) {
            return DateTime.toString(object);
        }

        @Override
        public OffsetDateTime fromString(String string) {
            return DateTime.fromStringToOffsetDateTime(string);
        }
    };

    static SimpleType<OffsetTime> OFFSET_TIME = new SimpleType<>() {

        @Override
        public String toString(OffsetTime object) {
            return DateTime.toString(object);
        }

        @Override
        public OffsetTime fromString(String string) {
            return DateTime.fromStringToOffsetTime(string);
        }
    };

    // zoned date time
    static SimpleType<ZonedDateTime> ZONED_DATE_TIME = new SimpleType<>() {

        @Override
        public String toString(ZonedDateTime object) {
            return DateTime.toString(object);
        }

        @Override
        public ZonedDateTime fromString(String string) {
            return DateTime.fromStringToZonedDateTime(string);
        }
    };

    static SimpleType<BigInteger> BIG_INTEGER = new SimpleType<>() {

        @Override
        public String toString(BigInteger object) {
            return object.toString();
        }

        @Override
        public BigInteger fromString(String string) {
            return new BigInteger(string);
        }
    };

    static SimpleType<BigDecimal> BIG_DECIMAL = new SimpleType<>() {

        @Override
        public String toString(BigDecimal object) {
            return object.toString();
        }

        @Override
        public BigDecimal fromString(String string) {
            return new BigDecimal(string);
        }
    };

    static SimpleType<Character> CHARACTER = new SimpleType<>() {

        @Override
        public String toString(Character object) {
            return object.toString();
        }

        @Override
        public Character fromString(String string) {
            return string.charAt(0);
        }
    };

    static SimpleType<Byte> BYTE = new SimpleType<>() {

        @Override
        public String toString(Byte object) {
            return object.toString();
        }

        @Override
        public Byte fromString(String string) {
            return Byte.parseByte(string);
        }
    };

    static SimpleType<Short> SHORT = new SimpleType<>() {

        @Override
        public String toString(Short object) {
            return object.toString();
        }

        @Override
        public Short fromString(String string) {
            return Short.parseShort(string);
        }
    };

    static SimpleType<byte[]> BYTE_ARRAY = new SimpleType<>() {

        @Override
        public String toString(byte[] object) {
            return new String(object);
        }

        @Override
        public byte[] fromString(String string) {
            return string.getBytes();
        }
    };
}
