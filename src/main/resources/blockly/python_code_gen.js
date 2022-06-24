//Code generators for task 1
Blockly.Python['event_sensor'] = function (block) {
    var code = ''
    if (Blockly.Python.STATEMENT_PREFIX) {
        // Automatic prefix insertion is switched off for this block.  Add manually.
        code += Blockly.Python.injectId(Blockly.Python.STATEMENT_PREFIX, block);
    }
    conditionCode = Blockly.Python.valueToCode(block, 'trigger_event',
        Blockly.Python.ORDER_NONE) || 'False';
    branchCode = Blockly.Python.statementToCode(block, 'workflow_body') ||
        Blockly.Python.PASS;
    if (Blockly.Python.STATEMENT_SUFFIX) {
        branchCode = Blockly.Python.prefixLines(
            Blockly.Python.injectId(Blockly.Python.STATEMENT_SUFFIX, block),
            Blockly.Python.INDENT) + branchCode;
    }
    // code = 'set_sensor_event_callback(' + conditionCode + '):\n' + "def:\n" + branchCode;
    code = "def sensor_event_callback:\n" + branchCode + '\n' + 'set_sensor_event_callback(' + conditionCode + ',' + 'sensor_event_callback)\n';
    return code;
};
Blockly.Python['actuator_Living_Room_Light'] = function (block) {
    // TODO: Assemble Python into code variable.
    var code = 'actuator["Living_Room_Light"]';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};
Blockly.Python['sensor_motion_sensor'] = function (block) {
    // TODO: Assemble Python into code variable.
    var code = 'sensor["motion_sensor"]';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};
Blockly.Python['service_someone_passed'] = function (block) {
    var value_sensor = Blockly.Python.valueToCode(block, 'sensor', Blockly.Python.ORDER_NONE);
    // TODO: Assemble Python into code variable.
    var code = value_sensor + '.detects_someone_passed';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};
Blockly.Python['service_switch'] = function (block) {
    var dropdown_state = block.getFieldValue('dropdown');
    var value_device = Blockly.Python.valueToCode(block, 'device', Blockly.Python.ORDER_NONE);
    // TODO: Assemble Python into code variable.
    var option = dropdown_state == "OPTION_ON" ? "True" : "False"
    var code = value_device + '.switch_on(' + option + ')\n';
    return code;
};

//Code Gen Task 2
//Task 2 Code Gen
//Task 2
//Task 2 Code Gen
Blockly.Python['event_time'] = function (block) {
    var number_hour = block.getFieldValue('Hour');
    var number_minute = block.getFieldValue('Minute');
    var number_second = block.getFieldValue('Second');
    var dropdown_day_selector = block.getFieldValue('day_selector');
    // TODO: Assemble Python into code variable.
    var code = '';
    var time_str = '"' + number_hour + ':' + number_minute + ':' + number_second + '"'
    var time_format = '"' + "HH:mm:ss" + '"'
    var day = ''
    if (dropdown_day_selector === "DAY") {
        day = "Time_Trigger.EVERYDAY"
    } else if (dropdown_day_selector === "WEEKDAY") {
        day = "Time_Trigger.WEEKDAY"
    } else {
        day = "Time_Trigger.WEEKEND"
    }

    if (Blockly.Python.STATEMENT_PREFIX) {
        // Automatic prefix insertion is switched off for this block.  Add manually.
        code += Blockly.Python.injectId(Blockly.Python.STATEMENT_PREFIX, block);
    }
    branchCode = Blockly.Python.statementToCode(block, 'workflow_body') ||
        Blockly.Python.PASS;

    if (Blockly.Python.STATEMENT_SUFFIX) {
        branchCode = Blockly.Python.prefixLines(
            Blockly.Python.injectId(Blockly.Python.STATEMENT_SUFFIX, block),
            Blockly.Python.INDENT) + branchCode;
    }

    return code += 'def ' + 'time_event_callback(' + time_str + ',' + time_format + ',' + day + '):\n' + branchCode;
    ;
};


Blockly.Python['actuator_Curtain'] = function (block) {
    // TODO: Assemble Python into code variable.
    var code = 'actuator["curtain"]';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};

Blockly.Python['sensor_brightness_sensor'] = function (block) {
    // TODO: Assemble Python into code variable.
    var code = 'sensor["brightness_sensor"]';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};

Blockly.Python['actuator_Lamp'] = function (block) {
    // TODO: Assemble Python into code variable.
    var code = 'actuator["lamp"]';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};

Blockly.Python['actuator_Smart_Speaker'] = function (block) {
    // TODO: Assemble Python into code variable.
    var code = 'actuator["Smart Speaker"]';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};

Blockly.Python['service_open'] = function (block) {
    var value_name = Blockly.Python.valueToCode(block, 'device', Blockly.Python.ORDER_NONE);
    // TODO: Assemble Python into code variable.
    var code = value_name + '.open()\n';
    return code;
};

Blockly.Python['service_close'] = function (block) {
    var value_name = Blockly.Python.valueToCode(block, 'device', Blockly.Python.ORDER_NONE);
    // TODO: Assemble Python into code variable.
    var code = value_name + '.close()\n';
    return code;
};

Blockly.Python['service_set_brightness'] = function (block) {
    var value_holon = Blockly.Python.valueToCode(block, 'device', Blockly.Python.ORDER_NONE);
    var number_brightness_percentage_ = block.getFieldValue('percentage');
    // TODO: Assemble Python into code variable.
    var code = value_holon + '.set_percentage(' + number_brightness_percentage_ + ')\n';
    return code;
};

Blockly.Python['service_wait'] = function (block) {
    var number_time_span = block.getFieldValue('time_span');
    // TODO: Assemble Python into code variable.
    var code = 'time.sleep(' + number_time_span + ')\n';
    return code;
};

Blockly.Python['service_play_random_songs'] = function (block) {
    var value_name = Blockly.Python.valueToCode(block, 'device', Blockly.Python.ORDER_NONE);
    // TODO: Assemble Python into code variable.
    var code = value_name + '.play_randomSongs()\n';
    return code;
};

Blockly.Python['service_get_brightness'] = function (block) {
    var value_device = Blockly.Python.valueToCode(block, 'device', Blockly.Python.ORDER_NONE);
    // TODO: Assemble Python into code variable.
    var code = value_device + '.' + "brightness";
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};

//Task 3
Blockly.Python['event_scene'] = function (block) {
    var text_name = block.getFieldValue('NAME');
    var statements_workflow_body = Blockly.Python.statementToCode(block, 'workflow_body');
    var code = ''
    if (Blockly.Python.STATEMENT_PREFIX) {
        // Automatic prefix insertion is switched off for this block.  Add manually.
        code += Blockly.Python.injectId(Blockly.Python.STATEMENT_PREFIX, block);
    }
    branchCode = Blockly.Python.statementToCode(block, 'workflow_body') ||
        Blockly.Python.PASS;
    if (Blockly.Python.STATEMENT_SUFFIX) {
        branchCode = Blockly.Python.prefixLines(
            Blockly.Python.injectId(Blockly.Python.STATEMENT_SUFFIX, block),
            Blockly.Python.INDENT) + branchCode;
    }
    code += 'def ' + text_name + 'scene_event_callback():\n' + branchCode;
    return code;
};

Blockly.Python['service_check_state'] = function (block) {
    const value_name = Blockly.Python.valueToCode(block, 'device', Blockly.Python.ORDER_NONE);
    const dropdown_name = block.getFieldValue('dropdown');
    var code = ''
    // TODO: Assemble Python into code variable.
    if (dropdown_name === "OFF") {
        code += 'not '
    }
    code += value_name + '.' + 'is_on()\n'
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};

Blockly.Python['actuator_Light1'] = function (block) {
    // TODO: Assemble Python into code variable.
    var code = 'actuator["light1"]';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};

Blockly.Python['actuator_Light2'] = function (block) {
    // TODO: Assemble Python into code variable.
    var code = 'actuator["light2"]';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};

Blockly.Python['actuator_Light3'] = function (block) {
    // TODO: Assemble Python into code variable.
    var code = 'actuator["light3"]';
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.Python.ORDER_NONE];
};
