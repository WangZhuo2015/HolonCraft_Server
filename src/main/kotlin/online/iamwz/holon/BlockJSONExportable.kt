package online.iamwz.holon

/**
 * interface for classes which can provide block definitions.
 */
interface BlockJSONExportable{
    /**
     * convert kotlin model class to JSON string
     * @param holon: Holon object it belonged to
     */
    fun toBlocklyJSON(holon: Holon): List<String>
}

/**
 * interface for classes which can provide block generators.
 */
interface CodeGeneratorExportable{

    /**
     * convert kotlin model class to code generator code in JavaScript
     * @param langType target programming language
     */
    fun toCodeGen(langType:LanguageType):List<String>
}