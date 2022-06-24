/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online.iamwz.holon

import org.semanticweb.HermiT.Reasoner
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.InferenceType
import org.semanticweb.owlapi.reasoner.OWLReasoner
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream

/**
 * OntologyParser
 */
object OntologyParser {
    private var manager: OWLOntologyManager? = null
    private var holonOntology: OWLOntology? = null
    private var dataFactory: OWLDataFactory? = null
    private var reasoner: OWLReasoner? = null
    private val instancesClassesMap: ConcurrentHashMap<String, String> =
        ConcurrentHashMap<String, String>() //key = instance, value = class
    private var holon: Holon? = null
    private var ontologyIRI: IRI? = null

    /**
     * load an ontology from a file
     */
    @Throws(OWLOntologyCreationException::class)
    fun loadOntology(file: File?) {
        //loading the ontology
        manager = OWLManager.createOWLOntologyManager()
        holonOntology = manager!!.loadOntologyFromOntologyDocument(file)
    }

    /**
     * parse a OWL file into a Holon
     */
    @Throws(OWLOntologyCreationException::class)
    fun parse(): Holon? {
        holon = Holon()
        dataFactory = holonOntology!!.owlOntologyManager.owlDataFactory
        val rf: OWLReasonerFactory = Reasoner.ReasonerFactory()
        reasoner = rf.createReasoner(holonOntology)
        reasoner!!.precomputeInferences(InferenceType.CLASS_HIERARCHY)
        parseInstances()
        ontologyIRI = holonOntology!!.ontologyID.ontologyIRI.get()
        val holonOWLClass = dataFactory!!.getOWLClass(IRI.create(ontologyIRI.toString() ))
        var holonInstance: OWLNamedIndividual? = null
        val instances = reasoner!!.getInstances(holonOWLClass, false)
        val iterIndividual = instances.entities().iterator()
        if (iterIndividual.hasNext()) {
            holonInstance = iterIndividual.next()
        }
        val holonParameters = getHolonParameters(holonInstance)
        //System.out.println("holonParameters = " + holonParameters);
        val parameterIter: Iterator<OWLNamedIndividual> = holonParameters.iterator()
        while (parameterIter.hasNext()) {
            val ind = parameterIter.next()
            val type = classifyParameter(ind)
            when (type) {
                "data" -> assignValueToHolonParameter(ind.iri.shortForm, getDataPropertyValues(ind))
                "object" -> assignValueToHolonParameter(ind.iri.shortForm, getObjectPropertyValues(ind))
                else -> {
                    System.err.println("Unrecognised parameter type")
                    System.exit(0)
                }
            }
        }
        holon!!.services = getHolonFunctions(holonInstance)
        //        System.out.println("holon = " + holon.toString());
        return holon
    }

    /**
     * returns the literal of the individual
     *
     * @param individual
     * @return
     */
    fun getDataPropertyValues(individual: OWLNamedIndividual?): String? {
        val ax = holonOntology!!.dataPropertyAssertionAxioms(individual)
        val iter = ax.iterator()
        while (iter.hasNext()) {
            val axiom = iter.next()
            val literal = axiom.getObject()
            return literal.literal
        }
        return null
    }

    /**
     * returns the class of the linked property
     *
     * @param individual
     * @return
     */
    fun getObjectPropertyValues(individual: OWLNamedIndividual): String? {
        val ax = holonOntology!!.objectPropertyAssertionAxioms(individual)
        val iter = ax.iterator()
        if (iter.hasNext()) {
            val axiom = iter.next()
            val stream = axiom.individualsInSignature()
            val compIter = stream.iterator()
            while (compIter.hasNext()) {
                val comp = compIter.next()
                if (comp.iri.iriString != individual.iri.iriString) {
                    return comp.iri.shortForm
                    //                    if (instancesClassesMap.containsKey(comp.getIRI().getShortForm())) {                        
//                        return instancesClassesMap.get(comp.getIRI().getShortForm());
//                    }
                }
            }
        }
        return null
    }

    /**
     * Classifies the parameter to either data or object property
     *
     * @param individual
     * @return
     */
    fun classifyParameter(individual: OWLNamedIndividual?): String {
        val axData = holonOntology!!.dataPropertyAssertionAxioms(individual)
        val dataCount = axData.count()
        val axObject = holonOntology!!.objectPropertyAssertionAxioms(individual)
        val objectCount = axObject.count()
        return if (dataCount > 0 && objectCount == 0L) {
            "data"
        } else if (objectCount > 0 && dataCount == 0L) {
            "object"
        } else {
            ""
        }
    }

    /**
     * returns the Holon Parameters
     *
     * @param individual
     * @return
     */
    fun getHolonParameters(individual: OWLNamedIndividual?): ArrayList<OWLNamedIndividual> {
        val parameters = ArrayList<OWLNamedIndividual>()
        val axObject = holonOntology!!.objectPropertyAssertionAxioms(individual)
        val iterObject = axObject.iterator()
        while (iterObject.hasNext()) {
            val axiomObject = iterObject.next()
            if (axiomObject.containsEntityInSignature(dataFactory!!.getOWLObjectProperty(ontologyIRI.toString() + "#hasParameter"))) {
                val stream: Stream<*> = axiomObject.individualsInSignature()
                val compIter: Iterator<OWLNamedIndividual> = stream.iterator() as Iterator<OWLNamedIndividual>
                while (compIter.hasNext()) {
                    val comp = compIter.next()
                    if (comp.iri.iriString != individual!!.iri.iriString) {
                        parameters.add(comp)
                    }
                }
            }
        }
        return parameters
    }

    /**
     * returns the Holon functions
     *
     * @param individual
     * @return
     */
    fun getHolonFunctions(individual: OWLNamedIndividual?): ArrayList<Service?> {
        val functions = ArrayList<Service?>()
        val axObject = holonOntology!!.objectPropertyAssertionAxioms(individual)
        val iterObject = axObject.iterator()
        while (iterObject.hasNext()) {
            val axiomObject = iterObject.next()
            if (axiomObject.containsEntityInSignature(dataFactory!!.getOWLObjectProperty(ontologyIRI.toString() + "#providesService"))) {
                val stream: Stream<*> = axiomObject.individualsInSignature()
                val compIter: Iterator<OWLNamedIndividual> = stream.iterator() as Iterator<OWLNamedIndividual>
                while (compIter.hasNext()) {
                    val comp = compIter.next()
                    if (comp.iri.iriString != individual!!.iri.iriString) {
//                        System.out.println("service " + comp);
                        val ax = holonOntology!!.dataPropertyAssertionAxioms(comp)
                        val iter = ax.iterator()
                        val ser = Service()
                        while (iter.hasNext()) {
                            val axiom = iter.next()
                            val dataProp = axiom.dataPropertiesInSignature()
                            val dataPropIter = dataProp.iterator()
                            if (dataPropIter.hasNext()) {
                                val prop = dataPropIter.next()
                                assignValueToFunctionParameter(prop.iri.shortForm, axiom.getObject().literal, ser)
                            }
                        }
                        functions.add(ser)
                    }
                }
            }
        }
        return functions
    }

    /**
     * print a stream
     *
     * @param stream
     */
    private fun printStream(stream: Stream<*>) {
        val iter = stream.iterator()
        while (iter.hasNext()) {
            val o = iter.next()!!
            println(o)
        }
    }

    /**
     * Finds all the instances in the ontology and stores them with the their
     * classes in a map key = instances (in a short form), value = class (in a
     * short form)
     */
    private fun parseInstances() {
        val classes = holonOntology!!.classesInSignature()
        val iter = classes.iterator()
        while (iter.hasNext()) {
            val clazz = iter.next()
            val instances = reasoner!!.getInstances(clazz, false)
            val iterIndividual = instances.entities().iterator()
            while (iterIndividual.hasNext()) {
                val i = iterIndividual.next()
                instancesClassesMap.put(i.iri.shortForm, clazz.iri.shortForm)
                //                System.out.println(i.getIRI().getShortForm() +" : "+ clazz.getIRI());
            }
        }
    }

    /**
     * Assign extracted values to the holon object
     *
     * @param shortForm
     * @param value
     */
    private fun assignValueToHolonParameter(shortForm: String, value: String?) {
        val parameter = shortForm.split("-").toTypedArray()[1]
        when (parameter) {
            "id" -> holon?.name = (value)
            "ip" -> holon?.address = (value)
            "mobility" -> holon?.mobility = (value)
            "reliability" -> holon?.reliability = (value)
            "temperature" -> holon?.temperature = (value)
            else -> {
            }
        }
    }

    /**
     *
     * @param shortForm
     * @param value
     */
    private fun assignValueToFunctionParameter(shortForm: String, value: String, service: Service) {
        when (shortForm) {
            "name" -> service.name = (value)
            "url" -> service.url = (value)
            "parameters" -> {
//                val para = value.split(",").toTypedArray()
//                val arr = ArrayList<String>()
//                arr.addAll(Arrays.asList<String>(*para))
                service.parameters = (value)
            }
            "cost" -> service.cost = (value)
            else -> {
            }
        }
    }
}