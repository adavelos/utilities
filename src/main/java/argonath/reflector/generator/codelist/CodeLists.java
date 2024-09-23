package argonath.reflector.generator.codelist;

import argonath.utils.ConfigurationLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads code list data from a file and parses it into a map of code lists.
 * It is used either by the GlobalCodeLists to load code lists globally or by the Generator API to assign code lists to
 * fields through a Code List Generator.
 * <p/>
 * Code List generators loaded from Specs File are always String generators (conversion to simple types is automatically done within the generator).
 * <p/>
 * The code list file line must have the following format:
 * Code List with descriptions:
 * - codeListName1=code1(desc1), code2(desc2), code3(desc3)
 * Code List without descriptions:
 * - codeListName2=code1, code2, code3
 * Note: Spaces are automatically trimmed.
 */
public class CodeLists {
    private CodeLists() {
    }

    public static Map<String, CodeList<String>> load(String codeListFileName) {
        Map<String, CodeList<String>> codeListData = new HashMap<>();
        Map<String, String> loadedData = ConfigurationLoader.load(codeListFileName);
        loadedData.entrySet().forEach(entry -> {
            String key = entry.getKey();
            CodeList<String> codeList = parseCodeListData(key, entry.getValue());
            codeListData.put(key, codeList);
        });
        return codeListData;
    }

    public static CodeList<String> parseCodeListData(String key, String data) {
        String[] items = data.split(",");
        List<CodeListItem<String>> codeListItems = new ArrayList<>();
        for (String item : items) {
            String[] parts = item.split("\\(");
            String code = parts[0].trim();
            if (parts.length > 2 || // more than one open parenthesis
                    parts.length == 1 && item.contains(")") ||  // no open parenthesis
                    parts.length == 2 && !item.trim().endsWith(")")) { // no close parenthesis
                throw new IllegalArgumentException("Invalid code list item: " + item);
            }
            if (parts.length != 2) {
                codeListItems.add(new CodeListItem<>(item.trim(), null));
                continue;
            }
            String descHolder = parts[1].trim();
            String desc = descHolder.substring(0, descHolder.length() - 1).trim();
            codeListItems.add(new CodeListItem<>(code, desc));
        }
        return new CodeList<>(key, codeListItems);
    }
}
