package online.iamwz.holon

object CustomBlocks {
    /**
     * get pre-defined blocks
     * @param list: a list of keys
     */
    fun getPredefinedBlocks(list: List<String>): List<String> {
        val blocks = mutableListOf<String>()
        list.forEach { blocks.add(this.blocks[it]!!) }
        return blocks
    }

    /**
     * @param list: a list of keys
     */
    fun getPredefinedCodeGenerator(list: List<String>):List<String>{
        val codeGens = mutableListOf<String>()
        list.forEach { codeGens.add(this.codeGen[it]!!) }
        return codeGens
    }

    //pre-defined code generators
    private val codeGen = mapOf(
        Pair(
            "event_sensor", """
            
            Blockly.Python['event_sensor'] = function(block) { 
              var value_trigger_event = Blockly.Python.valueToCode(block, 'trigger_event', Blockly.Python.ORDER_ATOMIC);
              var statements_workflow_body = Blockly.Python.statementToCode(block, 'workflow_body');
              // TODO: Assemble Python into code variable.
              var code = ''
              var code = 'def '+ 'sensor_event_callback:\n' + statements_workflow_body;
              return code;
            };
            
        """.trimIndent()
        ),

        Pair(
            "", """
Blockly.Python['device_light'] = function(block) {
  // TODO: Assemble Python into code variable.
  var code = 'device["light"]';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.Python.ORDER_NONE];
};
        """.trimIndent()
        ),


        Pair(
            "device_motion_sensor", """
        Blockly.Python['device_motion_sensor'] = function(block) {
            // TODO: Assemble Python into code variable.
            var code = 'device["motion_sensor"]';
            // TODO: Change ORDER_NONE to the correct strength.
            return [code, Blockly.Python.ORDER_NONE];
        };
        """.trimIndent()
        ),

        Pair(
            "service_someone_passed", """
Blockly.Python['service_someone_passed'] = function(block) {
  var value_sensor = Blockly.Python.valueToCode(block, 'sensor', Blockly.Python.ORDER_ATOMIC);
  // TODO: Assemble Python into code variable.
  var code = value_sensor + '.detects_someone_passed';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.Python.ORDER_NONE];
};
        """.trimIndent()
        ),


        Pair(
            "service_switch", """
Blockly.Python['service_switch'] = function(block) {
  var dropdown_state = block.getFieldValue('state');
  var value_device = Blockly.Python.valueToCode(block, 'device', Blockly.Python.ORDER_ATOMIC);
  // TODO: Assemble Python into code variable.
  var code = value_device + '.switch('+ dropdown_state +')\n';
  return code;
};
        """.trimIndent()
        ),
//        Pair("","""
//
//        """.trimIndent()),


    )

    //pre-defined block definitions
    private val blocks = mapOf(
        Pair(
            "service_someone_passed", """
             {"type":"service_someone_passed","message0":"%1 detects someone passed","args0":[{"type":"input_value","name":"sensor","check":"Holon"}],"output":"Boolean","colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "device_motion_sensor", """
            {"type":"device_motion_sensor","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"motion_sensor"}],"output":"Holon","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "event_scene", """
            {"type":"event_scene","message0":"Scene %1 %2 %3","args0":[{"type":"field_input","name":"NAME","text":"name of this scene"},{"type":"input_dummy"},{"type":"input_statement","name":"workflow_body"}],"colour":135,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "event_sensor", """
            {"type":"event_sensor","message0": "Trigger When %1 do %2","args0":[{"type":"input_value","name":"trigger_event","check":"Boolean"},{"type":"input_statement","name":"workflow_body"}],"colour":135,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "event_time", """
            {"type":"event_time","message0":"When %1 : %2 : %3 %4 on every %5 %6 do %7","args0":[{"type":"field_number","name":"Hour","value":0,"min":0,"max":24,"precision":1},{"type":"field_number","name":"Minute","value":0,"min":0,"max":60,"precision":1},{"type":"field_number","name":"Second","value":0,"min":0,"max":60,"precision":1},{"type":"input_dummy"},{"type":"field_dropdown","name":"day_selector","options":[["day","DAY"],["weekday","WEEKDAY"],["weekend","WEEKEND"]]},{"type":"input_dummy"},{"type":"input_statement","name":"workflow_body"}],"colour":135,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "service_switch", """
            {"type":"service_switch","message0":"switch %1 %2","args0":[{"type":"field_dropdown","name":"state","options":[["on","OPTION_ON"],["off","OPTION_OFF"]]},{"type":"input_value","name":"device","check":"Holon"}],"previousStatement":null,"nextStatement":null,"colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "device_light", """
            {"type":"device_light","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"LivingRoom_Light"}],"output":"Holon","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "service_check_state", """
            {"type":"service_check_state","message0":"%1 is %2","args0":[{"type":"input_value","name":"NAME","check":"Holon"},{"type":"field_dropdown","name":"NAME","options":[["on","ON"],["off","OFF"]]}],"output":"Boolean","colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "service_open", """
            {"type":"service_open","message0":"open %1","args0":[{"type":"input_value","name":"NAME","check":"Holon"}],"previousStatement":null,"nextStatement":null,"colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "service_close", """
            {"type":"service_close","message0":"close %1","args0":[{"type":"input_value","name":"NAME","check":"Holon"}],"previousStatement":null,"nextStatement":null,"colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "service_get_value", """
            {"type":"service_get_value","message0":"get property: %1 from device: %2","args0":[{"type":"input_value","name":"property_select","check":"String"},{"type":"input_value","name":"HolonSelect","check":"Holon"}],"output":null,"colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "attribute_brightness", """
            {"type":"attribute_brightness","message0":"BRIGHTNESS","output":"String","colour":300,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "service_wait", """
            {"type":"service_wait","message0":"wait for %1 second","args0":[{"type":"field_number","name":"time_span","value":100,"min":0,"precision":0.1}],"previousStatement":null,"nextStatement":null,"colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "service_play_random_songs", """
            {"type":"service_play_random_songs","message0":"play_random_songs %1","args0":[{"type":"input_value","name":"NAME","check":"Holon"}],"previousStatement":null,"nextStatement":null,"colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "device_smart_speaker", """
            {"type":"device_smart_speaker","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"Smart Speaker "}],"output":"Holon","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "device_curtain", """
            {"type":"device_curtain","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"Curtain"}],"output":"Holon","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "service_set_percentage", """
            {"type":"service_set_percentage","message0":"set %1 of %2 to %3 %%","args0":[{"type":"input_value","name":"Attribute","check":"String"},{"type":"input_value","name":"Holon","check":"Holon"},{"type":"field_number","name":"percentage ","value":0,"min":0,"max":100,"precision":0.1}],"inputsInline":true,"previousStatement":null,"nextStatement":null,"colour":230,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "device_brightness_sensor", """
            {"type":"device_brightness_sensor","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"BrightnessSensor"}],"output":"Holon","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "device_lamp", """
            {"type":"device_lamp","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"Bedside Lamp"}],"output":"Holon","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "device_light1", """
{"type":"device_light1","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"Light 1"}],"output":"Holon","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "device_light2", """
{"type":"device_light2","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"Light 2"}],"output":"Holon","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),

        Pair(
            "device_light3", """
{"type":"device_light3","message0":"%1","args0":[{"type":"field_label_serializable","name":"NAME","text":"Light 3"}],"output":"Holon","colour":30,"tooltip":"","helpUrl":""}
        """.trimIndent()
        ),
    )
}