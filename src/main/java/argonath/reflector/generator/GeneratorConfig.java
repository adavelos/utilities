package argonath.reflector.generator;

import argonath.reflector.config.Configuration;
import argonath.reflector.generator.codelist.CodeList;
import argonath.reflector.generator.codelist.CodeLists;
import argonath.reflector.generator.codelist.GlobalCodeLists;
import argonath.reflector.generator.model.ObjectSpecs;
import argonath.reflector.generator.model.SpecsExpressionParser;
import argonath.reflector.reflection.TypeExplorer;
import argonath.reflector.registry.TypeRegistry;
import argonath.utils.WcMatcher;

import java.util.HashMap;
import java.util.Map;

public class GeneratorConfig {

    // Specs
    private Map<String, ObjectSpecs<?>> specsByPath;
    private Map<String, ObjectSpecs<?>> specsByFieldName;
    private Map<String, ObjectSpecs<?>> specsByFieldNameWc;
    private TypeRegistry<ObjectSpecs<?>> specsByType;
    // Values
    private Map<String, Object> valuesByPath;
    private Map<String, Object> valuesByFieldName;
    private Map<String, Object> valuesByFieldNameWc;
    private TypeRegistry<Object> valuesByType;

    private GeneratorStrategy strategy;

    private Map<String, CodeList<?>> codeLists;

    private boolean lenient;

    private Long seed;


    public GeneratorConfig(GeneratorStrategy strategy) {
        this.specsByPath = new HashMap<>();
        this.specsByFieldName = new HashMap<>();
        this.specsByFieldNameWc = new HashMap<>();
        this.specsByType = new TypeRegistry<>(false);
        this.valuesByPath = new HashMap<>();
        this.valuesByFieldName = new HashMap<>();
        this.valuesByFieldNameWc = new HashMap<>();
        this.valuesByType = new TypeRegistry<>(false);
        this.strategy = strategy;
        this.seed = null;
        this.lenient = Configuration.isLenient();
        this.codeLists = GlobalCodeLists.all();
    }

    public GeneratorConfig() {
        this(GeneratorStrategy.defaultStrategy());
    }

    public static GeneratorConfig defaultConfig() {
        return new GeneratorConfig(GeneratorStrategy.defaultStrategy());
    }

    public GeneratorConfig withStrategy(GeneratorStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public <T> GeneratorConfig withSpecs(FieldSelector selector, ObjectSpecs<T> specs) {

        // Since there is a single map for each type of selector, we need to merge the specs to support the usage below:
        // Example: ObjectGenerator
        //    .create(MyClass.class)
        //    .withSpecs(FieldSelector.path("a.b.c"), ObjectSpecs.ofGenerator(Generators.randomString("an4..6")))
        //    .withSpecs(FieldSelector.path("a.b.c"), ObjectSpecs.ofOptionality(true))
        //    .withSpecs(FieldSelector.path("a.b.c"), ObjectSpecs.ofCardinality(1, 2))
        //    .generate();

        // Of course, we can equivalently use the same selector type in the above example with a single method:
        // Example: ObjectGenerator
        //    .create(MyClass.class)
        //    .withSpecs(FieldSelector.path("a.b.c"), ObjectSpecs.of(Generators.randomString("an4..6"), true, 1, 2))

        // Note: in case one part of the specs is already defined for a path, the later declaration has precedence:
        // Example: ObjectGenerator
        //    .create(MyClass.class)
        //    .withSpecs(FieldSelector.path("a.b.c"), ObjectSpecs.ofGenerator(Generators.randomString("an4..6"))
        //    .withSpecs(FieldSelector.path("a.b.c"), ObjectSpecs.ofGenerator(Generators.randomString("an1..20")))

        switch (selector.type()) {
            case PATH:
                merge(specsByPath, selector.expression(), specs);
                break;
            case FIELD_NAME:
                merge(specsByFieldName, selector.expression(), specs);
                break;
            case FIELD_NAME_WC:
                merge(specsByFieldNameWc, selector.expression(), specs);
                break;
            case TYPE:
                merge(specsByType, selector.clazz(), specs);
                break;
        }
        return this;
    }

    public GeneratorConfig withSpecsFile(String filename) {
        Map<String, ObjectSpecs<?>> specs = SpecsExpressionParser.parseSpecs(filename);
        specs.forEach((k, v) -> withSpecs(FieldSelector.ofPath(k), v)); // all keys in specs file are referring to paths
        return this;
    }

    @SuppressWarnings("unchecked")
    private <T> void merge(TypeRegistry<ObjectSpecs<?>> specsMap, Class<?> clazz, ObjectSpecs<T> specs) {
        ObjectSpecs<?> rawSpecs = specsMap.get(clazz);
        if (rawSpecs == null) {
            specsMap.register(clazz, specs);
            return;
        }
        Class<?> elementClass = TypeExplorer.elementType(rawSpecs.getClass());
        if (!elementClass.equals(clazz)) {
            throw new IllegalArgumentException("Specs for " + elementClass + " cannot be merged with " + clazz);
        }

        ObjectSpecs<T> curSpecs = (ObjectSpecs<T>) rawSpecs;
        specs = ObjectSpecs.merge(curSpecs, specs);

        specsMap.register(clazz, specs);
    }

    @SuppressWarnings("unchecked")
    private <T> void merge(Map<String, ObjectSpecs<?>> specsMap, String expression, ObjectSpecs<?> specs) {
        ObjectSpecs<?> rawSpecs = specsMap.get(expression);
        if (rawSpecs == null) {
            specsMap.put(expression, specs);
            return;
        }

        Class<?> class1 = TypeExplorer.elementType(rawSpecs.getClass());
        Class<?> class2 = TypeExplorer.elementType(specs.getClass());
        if (class1.equals(class2)) {
            throw new IllegalArgumentException("Specs for " + class1 + " cannot be merged with " + class2);
        }

        ObjectSpecs<T> merge1 = (ObjectSpecs<T>) rawSpecs;
        ObjectSpecs<T> merge2 = (ObjectSpecs<T>) specs;
        specs = ObjectSpecs.merge(merge1, merge2);

        specsMap.put(expression, specs);
    }

    // with value
    public <T> GeneratorConfig withValue(FieldSelector selector, T value) {
        switch (selector.type()) {
            case PATH:
                this.valuesByPath.put(selector.expression(), value);
                break;
            case FIELD_NAME:
                this.valuesByFieldName.put(selector.expression(), value);
                break;
            case FIELD_NAME_WC:
                this.valuesByFieldNameWc.put(selector.expression(), value);
                break;
            case TYPE:
                this.valuesByType.register(selector.clazz(), value);
                break;
        }
        return this;
    }

    public GeneratorConfig withConfigFile(String filename) {
        Configuration.withConfigFile(filename);
        return this;
    }

    public GeneratorConfig withCodeListFile(String filename) {
        Map<String, CodeList<String>> loadedCodeLists = CodeLists.load(filename);
        codeLists.putAll(loadedCodeLists);
        return this;
    }

    public GeneratorConfig withConfigProperty(String property, String value) {
        Configuration.withConfigProperty(property, value);
        return this;
    }

    public GeneratorConfig withSeed(Long seed) {
        this.seed = seed;
        return this;
    }

    public GeneratorConfig ignore(FieldSelector selector) {
        withSpecs(selector, ObjectSpecs.create(Object.class).ignore());
        return this;
    }

    public GeneratorConfig withAllMandatory() {
        withStrategy(GeneratorStrategy.ALL);
        return this;
    }

    public GeneratorConfig setLenient() {
        this.lenient = true;
        return this;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public GeneratorStrategy strategy() {
        return this.strategy;
    }

    @SuppressWarnings("java:S1452")
    public ObjectSpecs<?> specs(GeneratorContext.Element element, GeneratorContext ctx) {
        ObjectSpecs<?> result = null;

        result = this.specsByPath.get(ctx.path());
        if (result != null) {
            return result;
        }

        result = this.specsByFieldName.get(element.name());
        if (result != null) {
            return result;
        }

        for (Map.Entry<String, ObjectSpecs<?>> entry : this.specsByFieldNameWc.entrySet()) {
            if (WcMatcher.match(element.name(), entry.getKey())) {
                result = entry.getValue();
                return result;
            }
        }

        result = this.specsByType.match(element.typeClass());
        if (result != null) {
            return result;
        }

        return null;
    }

    public Object value(GeneratorContext.Element element, GeneratorContext ctx) {
        // same with specs but check the 'byValue' maps
        Object value = this.valuesByPath.get(ctx.path());
        if (value != null) {
            return value;
        }
        value = this.valuesByFieldName.get(element.name());
        if (value != null) {
            return value;
        }
        value = this.valuesByFieldNameWc.keySet().stream()
                .filter(key -> WcMatcher.match(element.name(), key))
                .findFirst()
                .map(this.valuesByFieldNameWc::get)
                .orElse(null);
        if (value != null) {
            return value;
        }
        value = this.valuesByType.match(element.typeClass());

        return value;
    }

    public Long seed() {
        return this.seed;
    }

    @SuppressWarnings("java:S1452")
    public CodeList<?> getCodeList(String key) {
        return codeLists.get(key);
    }

}
