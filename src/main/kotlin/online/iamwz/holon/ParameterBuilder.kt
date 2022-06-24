package online.iamwz.holon

/**
 * This class for build the JSON string of a parameter
 */
object ParameterBuilder {
    /**
     * builder percentage block
     * @param parameters parameter string
     */
    fun percentageParameter(parameters: String): String {
        return """
            {"type":"field_number","name":"percentage","value":0,"min":0,"max":100,"precision":0.1}
        """.trimIndent()
    }

    /**
     * OptionName: option
     * @param holon Holon
     */
    fun deviceParameter(holon: Holon): String {
        return """
{"type":"input_value","name":"device","check":"${holon.modelType}"}
        """.trimIndent()
    }

    /**
     * build a dropdown option
     * @param parameters parameter string
     */
    fun dropdownParameters(parameters: String): String {
        val parameterList = parameters
            .removePrefix("dropdown(")
            .removeSuffix(")")
            .split("/")
            .map { Pair(it, "OPTION_" + it.uppercase()) }

        return """{"type":"field_dropdown","name":"dropdown","options":[${dropOptionListJSON(parameterList)}]}
        """.trimIndent()
    }

    private fun dropOptionListJSON(list: List<Pair<String, String>>): String {
        return list.map { dropOptionJSON(it) }.reduce { x, y -> "$x,$y" }
    }

    private fun dropOptionJSON(pair: Pair<String, String>): String {
        return "[\"${pair.first}\",\"${pair.second}\"]"
    }
}