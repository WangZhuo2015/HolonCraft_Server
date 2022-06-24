package online.iamwz.holon

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * kotlin model for Holon Ontology
 */
@XmlRootElement(name = "holon")
class Holon: BlockJSONExportable,CodeGeneratorExportable {
    /**
     * unique id for holon
     */
    val holonId: String?
        get() = "${type.name}_$name"

    /**
     * name of a holon
     */
    @set:XmlElement
    var name: String? = null
    set(value) {
        val newValue = value?.replace(" ", "_")
        field=newValue
    }

    /**
     * IP address of a holon
     */
    @set:XmlElement
    var address: String? = null

    /**
     * communicate port of a holon
     */
    @set:XmlElement
    var port: String? = null

    /**
     * services supported by this holon
     */
    @set:XmlElement
    var services: ArrayList<Service?>? = null

    /**
     * events can be triggered by this holon
     */
    @set:XmlElement
    var events: ArrayList<Event?>? = null

    /**
     * a timestamp
     */
    @set:XmlElement
    var timestamp: String? = null

    /**
     * mobility of a holon
     */
    @set:XmlElement
    var mobility: String? = null

    /**
     * reliability of the device
     */
    @set:XmlElement
    var reliability: String? = null

    /**
     * temperature of the device
     */
    @set:XmlElement
    var temperature: String? = null

    /**
     * messaging protocol of the device
     */
    @set:XmlElement
    var messagingProtocol: String? = null

    /**
     * parent node of this holon
     */
    @set:XmlElement
    var hasParent: Holon? = null

    /**
     *  children nodes of this holon
     */
    @set:XmlElement
    var hasChildren: ArrayList<Holon>? = null

    /**
     *  this property is used to differentiate between device types. Currently, we have two
     *  types of devices, sensors and actuators.
     */
    @set:XmlElement
    var type: HolonType = HolonType.SENSOR

    /**
     *  This property will be used by the service provided by the device to set the type-check mark.
     */
    @set:XmlElement
    var modelType: String? = null
    get() {
        return if (field == null)
            name
        else
            field
    }

    override fun toString(): String {
        return """
             name = $name
             address = $address
             port = $port
             timestamp = $timestamp
             mobility = $mobility
             reliability = $reliability
             temperature = $temperature
             services = $services
             
             """.trimIndent()
    }

    private val properties = mapOf(
        name to "name",
        address to "address",
        port to "port",
        timestamp to "timestamp",
        mobility to "mobility",
        reliability to "reliability",
        temperature to "temperature"
    )

    override fun toBlocklyJSON(holon: Holon): List<String> {
        val nameJSON = """
{"holonId":"$holonId","type":"${type.str}_$name","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"$name"}],"output":"$modelType","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        //get blocks of services and events
        val serviceJSONArray:List<String> = services?.map { it!!.toBlocklyJSON(this)[0] } ?: listOf()
        val eventsJSONArray:List<String> = events?.map { it!!.toBlocklyJSON(this)[0] } ?: listOf()
        val list = mutableListOf(nameJSON)
        list.addAll(serviceJSONArray)
        list.addAll(eventsJSONArray)
        return list
    }


    fun toToolboxConfig():String{
        return properties.map {"\'<block type=\"${it.key}\"></block>\'"}.reduce{ x,y-> "$x\n$y" }
    }

    override fun toCodeGen(langType:LanguageType):List<String> {
        return listOf<String>("""
            Blockly.Python['device_$name'] = function (block) {
                // TODO: Assemble Python into code variable.
                var code = 'device["$name"]';
                // TODO: Change ORDER_NONE to the correct strength.
                return [code, Blockly.Python.ORDER_NONE];
            };
        """.trimIndent())
    }

}

enum class LanguageType{
    Python, JavaScript
}

/**
 * add basic properties to a holon
 */
fun initHolonDevice(
    holon: Holon,
    name: String,
    address: String = "192.168.0.1",
    port: String = "20",
    reliability: String = "BestEffort",
    mobility: String = "movable",
    temperature: String = "22",
    messagingProtocol: String = "HTTP",
    type: HolonType,
    service: ArrayList<Service?>?,
    events: ArrayList<Event?>?,
    modelType: String? = null
) {
    holon.name = name
    holon.address = address
    holon.port = port
    holon.reliability = reliability
    holon.mobility = mobility
    holon.temperature = temperature
    holon.messagingProtocol = messagingProtocol
    holon.type = type
    holon.services = service
    holon.events = events
    holon.modelType = modelType
}