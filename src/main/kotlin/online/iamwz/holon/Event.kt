package online.iamwz.holon

import java.util.ArrayList
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * kotlin model for events
 */
@XmlRootElement(name = "event")
class Event {
    /**
     * name of a event
     */
    @set:XmlElement
    var name: String? = null
        set(value) {
            val newValue = value?.replace(" ", "_")
            field = newValue
        }

    /**
     * parameters of a event
     */
    @set:XmlElement
    var parameters: String? = null


    /**
     * message for blockly blocks
     */
    @set:XmlElement
    var message: String? = null

    override fun toString(): String {
        return """
             name = $name
             parameters = $parameters
             """.trimIndent()
    }

    private val properties = mapOf(
        name to "name",
        parameters to "parameters"
    )

    fun toBlocklyJSON(holon: Holon):List<String>  {
        return listOf("""
            {"type":"service_$name","message0":"$message","args0":[{"type":"input_value","name":"sensor","check":"${holon.name}"}],"output":"Boolean","colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent())
    }


    /**
     * generate toolbox item for this event
     */
    fun toToolboxConfig(): String {
        return properties.map { "\'<block type=holon_\"${it.key}\"></block>\'" }.reduce { x, y -> "$x\n$y" }
    }

}