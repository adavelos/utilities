package argonath.utils.test.generator;

import argonath.reflector.generator.FieldSelector;
import argonath.reflector.generator.ObjectGenerator;
import argonath.reflector.generator.codelist.GlobalCodeLists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static argonath.reflector.generator.Generators.*;
import static argonath.reflector.generator.model.ObjectSpecs.create;

class TestCodeListGenerator {
    @Test
    void testCodeListGenerator() {
        GlobalCodeLists.clear(); // reset code list data
        GlobalCodeLists.load("default.codelists.specs");
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withCodeListFile("test.codelists.specs") // contains only CL2
                // top level
                .withSpecs(FieldSelector.ofPath("/a"), create(Integer.class).generator(codeListValue(Integer.class, "CL1", true)))
                .withSpecs(FieldSelector.ofPath("/aDesc"), create(String.class).generator(codeListDescription("CL1")))
                .withSpecs(FieldSelector.ofPath("/c"), create(String.class).generator(codeListValue(String.class, "CL2", true)))
                .withSpecs(FieldSelector.ofPath("/cDesc"), create(String.class).generator(codeListDescription("CL2")))
                .withSpecs(FieldSelector.ofPath("/intArray"), create(Integer.class).generator(intCodeListValue("CL3", true)).size(3, 3))
                .withSpecs(FieldSelector.ofPath("/stringArray"), create(String.class).generator(stringCodeListValue("CL4", false)).size(4, 4))
                // single inner
                .withSpecs(FieldSelector.ofPath("/inner/ia"), create(Integer.class).generator(codeListValue(Integer.class, "CL1", true)))
                .withSpecs(FieldSelector.ofPath("/inner/iaDesc"), create(Integer.class).generator(codeListValue(Integer.class, "CL1", true)))
                .withSpecs(FieldSelector.ofPath("/inner/ic"), create(String.class).generator(codeListValue(String.class, "CL2", true)))
                .withSpecs(FieldSelector.ofPath("/inner/icDesc"), create(String.class).generator(codeListDescription("CL2")))
                // multi inner
                .withSpecs(FieldSelector.ofPath("/innerList/ia"), create(Integer.class).generator(codeListValue(Integer.class, "CL3", true)))
                .withSpecs(FieldSelector.ofPath("/innerList/iaDesc"), create(String.class).generator(codeListDescription("CL3")))
                .withSpecs(FieldSelector.ofPath("/innerList/ic"), create(String.class).generator(codeListValue(String.class, "CL4", true)))
                .withSpecs(FieldSelector.ofPath("/innerList/icDesc"), create(String.class).generator(codeListDescription("CL4")))
                .generate();

        executeAssertions(obj);

    }

    @Test
    void testCodeListGeneratorString() {
        GlobalCodeLists.clear(); // reset code list data
        GlobalCodeLists.load("default.codelists.specs");
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withCodeListFile("test.codelists.specs") // contains only CL2
                // top level
                .withSpecs("/a=M|codeList(CL1,true)")
                .withSpecs("/aDesc=M|codeListDescription(CL1)")
                .withSpecs("/c=M|codeList(CL2,true)")
                .withSpecs("/cDesc=M|codeListDescription(CL2)")
                .withSpecs("/intArray=M|size(3,3)|codeList(CL3,true)")
                .withSpecs("/stringArray=M|size(4,4)|codeList(CL4,false)")
                // single inner
                .withSpecs("/inner/ia=M|codeList(CL1,true)")
                .withSpecs("/inner/iaDesc=M|codeList(CL1,true)")
                .withSpecs("/inner/ic=M|codeList(CL2,true)")
                .withSpecs("/inner/icDesc=M|codeListDescription(CL2)")
                // multi inner
                .withSpecs("/innerList/ia=M|codeList(CL3,true)")
                .withSpecs("/innerList/iaDesc=M|codeListDescription(CL3)")
                .withSpecs("/innerList/ic=M|codeList(CL4,true)")
                .withSpecs("/innerList/icDesc=M|codeListDescription(CL4)")
                .generate();

        executeAssertions(obj);
    }

    private void executeAssertions(TestClass obj) {
        // assert that 'a' has values from CL1 (1,2,3,4)
        Assertions.assertTrue(Set.of(1, 2, 3, 4).contains(obj.a));
        // assert that 'aDesc' has null value
        Assertions.assertNull(obj.aDesc);
        // assert that 'c' has values from CL2 (C1, C2, C3, C4)
        Assertions.assertTrue(Set.of("C1", "C2", "C3", "C4").contains(obj.c));
        // assert that 'cDesc' has values from CL2 descriptions ('CL2-desc', 'CL2-desc', 'CL2-desc', 'CL2-desc')
        Assertions.assertTrue(Set.of("cl2-1-desc", "cl2-2-desc", "cl2-3-desc", "cl2-4-desc").contains(obj.cDesc));
        // assert that 'intArray' has values from CL3 (10, 20, 100,1000)
        obj.intArray.forEach(i -> Assertions.assertTrue(Set.of(10, 100, 500, 6000).contains(i)));
        // assert that 'stringArray' has values from CL4 (AFE,FTR,VFF,PO4) without repetition
        obj.stringArray.forEach(s -> Assertions.assertTrue(Set.of("AFE", "FTR", "VFF", "PO4").contains(s)));
        // Assertions.assertEquals(4, new HashSet<>(obj.intArray).size());

        // assert that 'inner.ia' has values from CL1 (1,2,3,4)
        Assertions.assertTrue(Set.of(1, 2, 3, 4).contains(obj.inner.ia));
        // assert that 'inner.ic' has values from CL2 (C1, C2, C3, C4)
        Assertions.assertTrue(Set.of("C1", "C2", "C3", "C4").contains(obj.inner.ic));
        // assert that 'inner.icDesc' has values from CL2 descriptions ('CL2-desc', 'CL2-desc', 'CL2-desc', 'CL2-desc')
        Assertions.assertTrue(Set.of("cl2-1-desc", "cl2-2-desc", "cl2-3-desc", "cl2-4-desc").contains(obj.inner.icDesc));

        // assert that 'innerList.ia' has values from CL3 (10, 20, 100,1000)
        obj.innerList.forEach(inner -> Assertions.assertTrue(Set.of(10, 100, 500, 6000).contains(inner.ia)));
        // assert that 'innerList.iaDesc' has values from CL3 descriptions ('CL3-desc', 'CL3-desc', 'CL3-desc', 'CL3-desc')
        obj.innerList.forEach(inner -> Assertions.assertTrue(Set.of("cl3-1-desc", "cl3-2-desc", "cl3-3-desc", "cl3-4-desc").contains(inner.iaDesc)));
    }


    private static class TestClass {
        private Integer a;

        private String aDesc;
        private String c;

        private String cDesc;
        private List<Integer> intArray;
        private List<String> stringArray;

        private InnerClass inner;
        private List<InnerClass> innerList;
    }

    private static class InnerClass {
        private Integer ia;

        private String iaDesc;
        private String ic;

        private String icDesc;
    }
}