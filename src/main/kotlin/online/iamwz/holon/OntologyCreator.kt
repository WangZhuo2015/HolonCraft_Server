package online.iamwz.holon

import org.semanticweb.HermiT.Reasoner
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.InferenceType
import org.semanticweb.owlapi.reasoner.OWLReasoner
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * OntologyCreator
 */
object OntologyCreator {
    private var holonOntology: OWLOntology? = null
    private var dataFactory: OWLDataFactory? = null
    private var reasoner: OWLReasoner? = null
    private var manager: OWLOntologyManager? = null
    private var ontologyIRI: IRI? = null

    private val hasParameter = "hasParameter"
    private val hasValue = "hasValue"

    /**
     * create a new OWL ontology from a holon kotlin object
     */
    @Throws(OWLOntologyCreationException::class, OWLOntologyStorageException::class, FileNotFoundException::class)
    fun creatOntology(holon: Holon): OWLOntology? {
        val holonId = holon.name
        val holonIp = holon.address
        val holonMobility = holon.mobility
        val holonReliability = holon.reliability
        val holonTemperature = holon.temperature
        val holonMessagingProtocol = holon.messagingProtocol

        //String holonTimestamp = holon.getTimestamp();
        val holonServices = holon.services
        manager = OWLManager.createOWLOntologyManager()
        val file = File("./src/main/resources/smart_home.owx")

        //loading the ontology
        holonOntology = manager!!.loadOntologyFromOntologyDocument(file)
        dataFactory = holonOntology!!.owlOntologyManager.owlDataFactory
        val rf: OWLReasonerFactory = Reasoner.ReasonerFactory()
        reasoner = rf.createReasoner(holonOntology)
        reasoner!!.precomputeInferences(InferenceType.CLASS_HIERARCHY)
        ontologyIRI = holonOntology!!.ontologyID.ontologyIRI.get()

        //holon individual         
        val holonIndividual = addOWLClassAssertionAxiom("Holon", holonId)

        //holon parameters Individuals
        val holonIDIndividual = addOWLClassAssertionAxiom("HolonID", "$holonId-id")
        val holonIPIndividual = addOWLClassAssertionAxiom("HolonIP", "$holonId-ip")
        val holonMobilityIndividual = addOWLClassAssertionAxiom("Mobility", "$holonId-mobility")
        val holonTemperatureIndividual = addOWLClassAssertionAxiom("Temperature", "$holonId-temperature")
        val holonReliabilityIndividual = addOWLClassAssertionAxiom("Reliability", "$holonId-reliability")
        val holonMessagingProtocolIndividual =
            addOWLClassAssertionAxiom("MessagingProtocol", "$holonId-messagingProtocol")
        val holonTypeIndividual = addOWLClassAssertionAxiom("HolonType", "$holonId-type")
        val holonModelTypeIndividual = addOWLClassAssertionAxiom("HolonModelType", "$holonId-model_type")

        //holon hasParameter
        addObjectAxiom(hasParameter, holonIndividual, holonIDIndividual)
        addObjectAxiom(hasParameter, holonIndividual, holonIPIndividual)
        addObjectAxiom(hasParameter, holonIndividual, holonMobilityIndividual)
        addObjectAxiom(hasParameter, holonIndividual, holonTemperatureIndividual)
        addObjectAxiom(hasParameter, holonIndividual, holonReliabilityIndividual)
        addObjectAxiom(hasParameter, holonIndividual, holonMessagingProtocolIndividual)
        addObjectAxiom(hasParameter, holonIndividual, holonTypeIndividual)
        addObjectAxiom(hasParameter, holonIndividual, holonModelTypeIndividual)

        //holon parameters' values
        addDataAxiom(hasValue, holonId, holonIDIndividual, "string")
        addDataAxiom(hasValue, holonIp, holonIPIndividual, "string")
        addDataAxiom(hasValue, holonMobility, holonMobilityIndividual, "string")
        addDataAxiom(hasValue, holonTemperature, holonTemperatureIndividual, "string")
        addDataAxiom(hasValue, holonMessagingProtocol, holonMessagingProtocolIndividual, "string")

        //reliability profile
        if (holonReliability.equals("BestEffort", ignoreCase = true)) {
            var bestEffortIndividual: OWLNamedIndividual? = null
            val bestEffortClass = dataFactory!!.getOWLClass(IRI.create(ontologyIRI.toString() + "#Best_Effort"))
            val instances = reasoner!!.getInstances(bestEffortClass, false)
            val iterIndividual = instances.entities().iterator()
            if (iterIndividual.hasNext()) {
                bestEffortIndividual = iterIndividual.next()
                addObjectAxiom("hasProfile", holonReliabilityIndividual, bestEffortIndividual)
            }
        }
        for (service in holonServices!!) {
            val serviceIndividual = addOWLClassAssertionAxiom("Service", service!!.name)
            addDataAxiom("message", service!!.message, serviceIndividual, "string")
            addDataAxiom("name", service!!.name, serviceIndividual, "string")
            addDataAxiom("cost", service!!.cost, serviceIndividual, "string")
            addDataAxiom("url", service!!.url, serviceIndividual, "string")
            addDataAxiom("type", service!!.type.toString(), serviceIndividual, "string")
            addDataAxiom("return_type", service!!.returnType, serviceIndividual, "string")
            addDataAxiom("returns", service!!.returns.toString() + "", serviceIndividual, "boolean")
            addDataAxiom("parameters", service!!.parameters, serviceIndividual, "string")
            addObjectAxiom("providesService", holonIndividual, serviceIndividual)
        }
//                  File holonFile = new File("holons/" + holonId + ".owl");
//          FileOutputStream out = new FileOutputStream(holonFile);
//          manager.saveOntology(holonOntology, out);
        manager!!.saveOntology(holonOntology)
        return holonOntology
    }

    /**
     * Create OWLClassAssertionAxiom
     *
     * @param clazz
     * @param ind
     * @return
     */
    private fun addOWLClassAssertionAxiom(clazz: String, ind: String?): OWLNamedIndividual {
        val owlClass = dataFactory!!.getOWLClass(IRI.create(ontologyIRI.toString() + "#" + clazz))
        val individual = dataFactory!!.getOWLNamedIndividual(IRI.create(ontologyIRI.toString() + "#" + ind))
        val ax = dataFactory!!.getOWLClassAssertionAxiom(owlClass, individual)
        manager!!.addAxiom(holonOntology, ax)
        return individual
    }

    /**
     * Create and add ObjectAxiom
     *
     * @param property
     * @param ind1
     * @param ind2
     * @param ontologyIRI
     */
    private fun addObjectAxiom(property: String, ind1: OWLNamedIndividual, ind2: OWLNamedIndividual?) {
        val objectProperty = dataFactory!!.getOWLObjectProperty(ontologyIRI.toString() + "#" + property)
        val objectAxiom = dataFactory!!.getOWLObjectPropertyAssertionAxiom(objectProperty, ind1, ind2)
        manager!!.addAxiom(holonOntology, objectAxiom)
    }

    /**
     * Create and add data property axiom
     *
     * @param property
     * @param value
     * @param ind
     * @param ontologyIRI
     */
    private fun addDataAxiom(property: String, value: String?, ind: OWLNamedIndividual, dataType: String) {
        var owlDatatype: OWLDatatype? = null
        owlDatatype = if (dataType == "string") {
            dataFactory!!.stringOWLDatatype
        } else {
            dataFactory!!.booleanOWLDatatype
        }
        val dataProperty = dataFactory!!.getOWLDataProperty(ontologyIRI.toString() + "#" + property)
        val literal = dataFactory!!.getOWLLiteral(value, owlDatatype)
        val holonIDAxiom = dataFactory!!.getOWLDataPropertyAssertionAxiom(dataProperty, ind, literal)
        manager!!.addAxiom(holonOntology, holonIDAxiom)
    }

    /**
     *
     * @param holonId
     * @throws FileNotFoundException
     * @throws OWLOntologyStorageException
     */
    @Throws(FileNotFoundException::class, OWLOntologyStorageException::class)
    fun saveOntologyToDesk(holonId: String) {
        val holonFile = File("holons/$holonId.owl")
        val out = FileOutputStream(holonFile)
        manager!!.saveOntology(holonOntology, out)
    }
}