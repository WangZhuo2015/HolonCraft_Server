/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online.iamwz.holon

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 *
 * @author zhuo wang
 */
@XmlRootElement(name = "service")
class Service:BlockJSONExportable, CodeGeneratorExportable{
    /**
     *
     */
    @set:XmlElement
    var name: String? = null
        set(value) {
            val newValue = value?.replace(" ", "_")
            field = newValue
        }

    /**
     *  full parameter string
     */
    @set:XmlElement
    var parameters: String? = null
        set(value) {
            field = value
        }

    /**
     *  splited parameters
     */
    @set:XmlElement
    var parametersList: List<String>? = null
        get() = parameters?.split(",") ?: listOf<String>()

//    @set:XmlElement
//    var parameterTypes: ArrayList<String>? = null

    @set:XmlElement
    var url: String? = null

    /**
     * this property is for path optimization
     */
    @set:XmlElement
    var cost: String? = null

    @set:XmlElement
    var returns = false

    /**
     * block message
     */
    @set:XmlElement
    var message: String? = null


    /**
     * this field is for block shape
     */
    @set:XmlElement
    var type: ServiceType = ServiceType.ACTION

    /**
     * this field is for type check
     */
    @set:XmlElement
    var returnType: String? = null
        get() {
            return if (field == null) {
                ""
            } else {
                field
            }
        }

    override fun toCodeGen(langType: LanguageType): List<String> {
        return CustomBlocks.getPredefinedCodeGenerator(listOf(name!!))
    }

    override fun toString(): String {
        return """
             name = $name
             url = $url
             cost = $cost
             parameters = $parameters
             returns = $returns
             """.trimIndent()
    }

    private fun parametersToJSON(holon: Holon): String {
        //"check": "String"
        //TODO: parameter type check
        return parametersList?.map {
            when {
                it.contains("percentage") -> ParameterBuilder.percentageParameter(it)
                it.contains("device") -> ParameterBuilder.deviceParameter(holon)
                it.startsWith("dropdown") -> ParameterBuilder.dropdownParameters(it)
                else -> ""
            }
        }!!.reduce { x, y -> "$x,$y" } ?: ""
    }

    override fun toBlocklyJSON(holon: Holon): List<String>  {
        return if (type == ServiceType.VALUE) {
            listOf("""
                    {"type":"service_$name","message0":"$message","args0":[${parametersToJSON(holon)}],"output": "$returnType","colour":230,"tooltip":"","helpUrl":""}
                """.trimIndent())
        } else {
            listOf(
            """
                {"type":"service_$name","message0":"$message","args0":[${parametersToJSON(holon)}],"previousStatement":null,"nextStatement":null,"colour":230,"tooltip":"","helpUrl":""}
            """.trimIndent())
        }
    }
}


/**
 * add basic data to service
 */
fun initService(
    service: Service,
    name: String,
    message: String,
    cost: String = "1",
    url: String = "service url",
    parameters: String,
    returns: String = "",
    type:ServiceType = ServiceType.ACTION,
    returnType: String? = null
) {
    service.name = name
    service.message = message
    service.cost = cost
    service.url = url
    service.parameters = parameters
    service.returns = false
    service.type = type
    service.returnType = returnType
}