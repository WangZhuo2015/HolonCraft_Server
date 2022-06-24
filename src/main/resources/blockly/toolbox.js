/**
 * Construct the blocks required by the flyout for the events category.
 * @param {!Blockly.Workspace} workspace The workspace this flyout is for.
 * @return {T[]} Array of XML block elements.
 */

// filter designed kind of blocks
function getEvents(){
    return custom_blocks.filter(it=>it.type.startsWith('event'));
}

function getActuators(){
    return custom_blocks.filter(it=>it.type.startsWith('actuator'));
}

function getServices(){
    return custom_blocks.filter(it=>it.type.startsWith('service'));
}
function getSensors(){
    return custom_blocks.filter(it=>it.type.startsWith('sensor'));
}

// Callback functions for custom block sections
var eventsFlyoutCallback = function(workspace) {
  var xmlList = [];
  var blocks = getEvents();
    for (var i = 0; i < blocks.length; i++) {
      var blockText = '<block type="'+blocks[i].type+'"></block>';
//      var blockText = '<block type="text"><field name="TEXT"></field></block>';
      var block = Blockly.Xml.textToDom(blockText);
      xmlList.push(block);
    }
  return xmlList;
};

var servicesFlyoutCallback = function(workspace) {
  var xmlList = [];
  var blocks = getServices()
    for (var i = 0; i < blocks.length; i++) {
      var blockText = '<block type="'+blocks[i].type+'"></block>';
      var block = Blockly.Xml.textToDom(blockText);
      xmlList.push(block);
    }
  return xmlList;
};

var sensorsFlyoutCallback = function(workspace) {
  var xmlList = [];
  var blocks = getSensors()
//  console.log("================================================")
//  console.log(JSON.stringify(custom_blocks))
//  console.log(JSON.stringify(blocks))
    for (var i = 0; i < blocks.length; i++) {
      var blockText = '<block type="'+blocks[i].type+'"></block>';
      var block = Blockly.Xml.textToDom(blockText);
      xmlList.push(block);
    }
  return xmlList;
};

var actuatorsFlyoutCallback = function(workspace) {
  var xmlList = [];
  var blocks = getActuators()
    for (var i = 0; i < blocks.length; i++) {
    var blockText = '<block type="'+blocks[i].type+'"></block>';
      var block = Blockly.Xml.textToDom(blockText);
      xmlList.push(block);
    }
  return xmlList;
};

//register callback functions
holon_workspace.registerToolboxCategoryCallback(
  'SERVICE', servicesFlyoutCallback);
holon_workspace.registerToolboxCategoryCallback(
  'EVENT', eventsFlyoutCallback);
holon_workspace.registerToolboxCategoryCallback(
  'ACTUATORS', actuatorsFlyoutCallback);
holon_workspace.registerToolboxCategoryCallback(
  'SENSORS', sensorsFlyoutCallback);



// event listener for blockly events
function updateFunction(event) {
  if (event.type === Blockly.Events.BLOCK_MOVE || event.type === Blockly.Events.BLOCK_CHANGE){
    // var xml = Blockly.Xml.workspaceToDom(Blockly.mainWorkspace);
    // var xml_text = Blockly.Xml.domToText(xml);
    let code = Blockly.Python.workspaceToCode(Blockly.mainWorkspace);
    monaco.editor.getModels()[0].setValue(code)
    // JSInterface.saveBlocks(xml_text,code)
  }
}

//register event listener
holon_workspace.addChangeListener(updateFunction);



//holon_workspace.registerToolboxCategoryCallback(
//  'SERVICE', servicesFlyoutCallback);
//holon_workspace.registerToolboxCategoryCallback(
//  'EVENT', eventsFlyoutCallback);
//holon_workspace.registerToolboxCategoryCallback(
//  'ATTRIBUTE', attributesFlyoutCallback);
//holon_workspace.registerToolboxCategoryCallback(
//  'DEVICE', devicesFlyoutCallback);
