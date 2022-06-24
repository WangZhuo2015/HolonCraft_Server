package online.iamwz

import online.iamwz.holon.*

/**
 * This class can generate Holon objects for 3 tasks
 */
object TaskGenerator{
    fun generateTask1(): String {
        val motionSensor = Holon()
        val peoplePassed = Event()
        peoplePassed.name = "someone passed"
        peoplePassed.message = "%1 detects someone passed"
        initHolonDevice(
            motionSensor,
            "motion_sensor",
            type = HolonType.SENSOR,
            service = ArrayList(),
            events = arrayListOf(peoplePassed)
        )


        val light1 = Holon()
        val switchService = Service()
        initService(switchService,
            "switch",
            "switch %1 %2",
            parameters = "dropdown(on/off),device")
        initHolonDevice(
            light1,
            "Living Room Light",
            type = HolonType.ACTUATOR,
            service = arrayListOf(switchService),
            events = ArrayList()
        )
        OntologyCreator.creatOntology(light1)

        val lightBlocks = light1.toBlocklyJSON(light1)
        val motionSensorBlocks = motionSensor.toBlocklyJSON(motionSensor)
        val eventBlocks = mutableListOf<String>()
        eventBlocks.addAll(CustomBlocks.getPredefinedBlocks(listOf("event_sensor")))
        eventBlocks.addAll(lightBlocks)
        eventBlocks.addAll(motionSensorBlocks)
        return "[${eventBlocks.reduce { x, y -> "$x,$y" }}]"
    }


    fun generateTask2(): String {
        val brightnessSensor = Holon()
        val getBrightnessService = Service()
        initService(
            getBrightnessService, "get brightness", "get brightness from %1",
            parameters = "device", type = ServiceType.VALUE, returnType = "Number"
        )
        initHolonDevice(
            brightnessSensor,
            "brightness_sensor",
            type = HolonType.SENSOR,
            service = arrayListOf(getBrightnessService),
            events = arrayListOf()
        )

        val curtain = Holon()
        val openService = Service()
        initService(openService, "open", "open %1", parameters = "device")
        val closeService = Service()
        initService(closeService, "close", "close %1", parameters = "device")
        curtain.services = arrayListOf(openService, closeService)
        initHolonDevice(
            curtain,
            "Curtain",
            type = HolonType.ACTUATOR,
            service = arrayListOf(openService, closeService),
            events = arrayListOf()
        )

        val speaker = Holon()
        val playRandomSongService = Service()
        initService(playRandomSongService, "play_random_songs", "play_random_songs %1", parameters = "device")
        initHolonDevice(
            speaker,
            "Smart Speaker",
            type = HolonType.ACTUATOR,
            service = arrayListOf(playRandomSongService),
            events = ArrayList()
        )

        val lamp = Holon()
        val setBrightnessService = Service()
        initService(
            setBrightnessService,
            "set brightness",
            "set brightness of %1 to %2 %%",
            parameters = "device,percentage"
        )
        initHolonDevice(
            lamp,
            "Lamp",
            type = HolonType.ACTUATOR,
            service = arrayListOf(setBrightnessService),
            events = ArrayList()
        )

        val list = listOf<List<String>>(
            brightnessSensor.toBlocklyJSON(brightnessSensor),
            curtain.toBlocklyJSON(curtain),
            speaker.toBlocklyJSON(speaker),
            lamp.toBlocklyJSON(lamp)
        )
        val taskBlocks = mutableListOf<String>()
        taskBlocks.addAll(CustomBlocks.getPredefinedBlocks(listOf("event_time", "service_wait")))
        list.forEach { taskBlocks.addAll(it) }
        return "[${taskBlocks.reduce { x, y -> "$x,$y" }}]"
    }

    fun generateTask3():String{
        val light1 = Holon()
        val light2 = Holon()
        val light3 = Holon()
        val switchService = Service()
        initService(switchService, "switch", "switch %1 %2", parameters = "dropdown(on/off),device")
        val checkState = Service()
        initService(
            checkState,
            "check_state",
            "%1 is %2",
            parameters = "device,dropdown(on/off)",
            type = ServiceType.VALUE,
            returnType = "Boolean"
        )
        initHolonDevice(
            light1,
            "Light1",
            type = HolonType.ACTUATOR,
            service = arrayListOf(switchService, checkState),
            events = ArrayList(),
            modelType = "Light"
        )
        initHolonDevice(
            light2,
            "Light2",
            type = HolonType.ACTUATOR,
            service = ArrayList(),
            events = ArrayList(),
            modelType = "Light"
        )
        initHolonDevice(
            light3,
            "Light3",
            type = HolonType.ACTUATOR,
            service = ArrayList(),
            events = ArrayList(),
            modelType = "Light"
        )

        val list = listOf<List<String>>(
            light1.toBlocklyJSON(light1),
            light2.toBlocklyJSON(light2),
            light3.toBlocklyJSON(light3),
        )
        val taskBlocks = mutableListOf<String>()
        taskBlocks.addAll(CustomBlocks.getPredefinedBlocks(listOf("event_scene", "service_wait")))
        list.forEach { taskBlocks.addAll(it) }
        return "[${taskBlocks.reduce { x, y -> "$x,$y" }}]"
    }
}