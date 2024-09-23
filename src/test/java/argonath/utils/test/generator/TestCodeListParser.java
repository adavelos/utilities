package argonath.utils.test.generator;

import argonath.reflector.generator.codelist.CodeList;
import argonath.reflector.generator.codelist.CodeLists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCodeListParser {

    @Test
    void testCodeListParser() {
        CodeList<String> codeList1 = CodeLists.parseCodeListData("CL1", "C1, C2,C3, C4");
        // assert that code list 1 has 4 items
        Assertions.assertEquals(4, codeList1.items().size());
        // asser that key=CL1
        Assertions.assertEquals("CL1", codeList1.key());
        // assert that code list 1 has items C1, C2, C3, C4
        Assertions.assertTrue(codeList1.items().stream().allMatch(item -> item.getCode().matches("C[1-4]")));

        // with descriptions
        CodeList<String> codeList2 = CodeLists.parseCodeListData("CL2", "C1(C1-desc), C2(C2-desc),C3(C3-desc), C4(C4-desc)");
        // assert that code list 2 has 4 items
        Assertions.assertEquals(4, codeList2.items().size());
        // asser that key=CL2
        Assertions.assertEquals("CL2", codeList2.key());
        // assert that code list 2 has items C1, C2, C3, C4
        Assertions.assertTrue(codeList2.items().stream().allMatch(item -> item.getCode().matches("C[1-4]")));
        // assert that code list 2 has descriptions C1-desc, C2-desc, C3-desc, C4-desc
        Assertions.assertTrue(codeList2.items().stream().allMatch(item -> item.getDescription().matches("C[1-4]-desc")));

        // code list line with error (missing close parenthesis)
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                CodeLists.parseCodeListData("CL1", "C1(desc, C2(desc2), C3(desc3)")
        );

        // code list line with error (missing open parenthesis)
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                CodeLists.parseCodeListData("CL1", "C1desc), C2(desc2), C3(desc3)")
        );

        // code list line with error (more than one open parenthesis)
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                CodeLists.parseCodeListData("CL1", "C1(desc()), C2(desc2), C3(desc3)")
        );
    }
}
