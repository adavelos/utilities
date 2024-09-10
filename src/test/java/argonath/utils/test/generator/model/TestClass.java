package argonath.utils.test.generator.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestClass {
    // Primitive types
    public int intField;
    public float floatField;
    public double doubleField;
    public long longField;
    public char charField;
    public byte byteField;
    public short shortField;
    public boolean booleanField;

    // Wrapper classes
    public Integer integerWrapperField;
    public Float floatWrapperField;
    public Double doubleWrapperField;
    public Long longWrapperField;
    public Character charWrapperField;
    public Byte byteWrapperField;
    public Short shortWrapperField;
    public Boolean booleanWrapperField;

    // Other types
    public String stringField;
    public LocalDate localDateField;
    public LocalDateTime localDateTimeField;
    public OffsetDateTime offsetDateTimeField;
    public OffsetTime offsetTimeField;
    public ZonedDateTime zonedDateTimeField;
    public BigInteger bigIntegerField;
    public BigDecimal bigDecimalField;
    public byte[] byteArrayField;

    // Simple Iterables
    public List<String> stringListField;
    public Set<String> stringSetField;

    public Map<String, String> stringMapField;

    public int[] intArrayField;
    public short[] shortArrayField;
    public boolean[] booleanArrayField;
    public float[] floatArrayField;
    public double[] doubleArrayField;
    public char[] charArrayField;

    public String[] stringArrayField;

    // Complex Iterables
    public List<InnerTestClass> innerTestClassList;
    public Set<InnerTestClass> innerTestClassSet;
    public InnerTestClass[] innerTestClassArray;

    public Map<String, InnerTestClass> innerTestClassMap;

    // Advanced Structures (require implementation of Type explorer)
    public List<List<List<String>>> nestedStringListField;
    public Map<String, Map<String, Map<String, Map<String, Integer>>>> nestedMapField;
    public List<String>[] stringListArrayField;
    public Map<String, List<String>>[] stringMapArrayField;
    public List<? extends Number> wildcardListField;
    public GenericClass<String> genericClassField;

    // 2-d and 3-d Arrays
    public int[][] intArrayField2D;
    public int[][][] intArrayField3D;
    public String[][] stringArrayField2D;
    public String[][][] stringArrayField3D;

    // Interface & Implementation
    public TestInterface testInterfaceField;
    public TestClassWithInterface testClassWithInterfaceField;
}