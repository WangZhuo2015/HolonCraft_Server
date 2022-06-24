//start function
//will be fired when user click the start button
//it will record the start time point
function start() {
    Blockly.mainWorkspace.addChangeListener(updateFunction);
    start_time = new Date()
    document.getElementById("timer").innerHTML = start_time
}

//finish function
//will be fired when user click the finish button
function stop() {
    end_time = new Date()
    // record the time consumption
    const time_take = (end_time - start_time) / 1000;
    document.getElementById("timer").innerHTML = "task takes "+ time_take +"s"
    let name = document.getElementById("name").value
    let task = "task" + (document.getElementById('task').selectedIndex + 1)
    //update code in monaco editor
    const xml_text = Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(Blockly.mainWorkspace));
    monaco.editor.getModels()[0].setValue(Blockly.Python.workspaceToCode(Blockly.mainWorkspace))
    const code = monaco.editor.getModels()[0].getValue()
    // upload result to the server
    uploadResult(name,task,xml_text,code,time_take)
}

// restore workspace with user XML file
function loadWorkspace(xmlText) {
    let workspace = Blockly.getMainWorkspace();
    workspace.clear();
    let dom = Blockly.Xml.textToDom(xmlText);
    Blockly.Xml.domToWorkspace(dom, workspace);
}

//host url
local_host = "http://127.0.0.1:80"
remote_host = "http://sfo.iamwz.online"

//load blocks for specific task
function requestForBlocks(task) {
    document.getElementById("timer").innerHTML = "Not started yet"
    if (typeof(monaco) !== "undefined"){
        //clear content of monaco editor
        monaco.editor.getModels()[0].setValue("print(\"Python Code for Task "+ task +" will be generated here\")")
    }
    //clear the workspace
    holon_workspace.clear()

    console.log("task:\n" + task)
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    // send result the callback function
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === 4) {
            // console.log(this.responseText);
            //load blocks to toolbox
            custom_blocks = JSON.parse(this.responseText)
            Blockly.defineBlocksWithJsonArray(custom_blocks)
        }
    });
    xhr.open("GET", remote_host+"/api/load-blocks?task=task" + task);
    xhr.send();
}

//upload result to server
//name: user name
//task: task number
//xml_text: original xml
//code: python code
//time_take: time consumption
function uploadResult(name,task,xml_text,code,time_take) {
    let result = task + "  takes: " + time_take + "\nXML:\n" + xml_text + "\n\nPython:\n" + code +"\n\n"
    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function() {
        if(this.readyState === 4) {
            console.log(this.responseText);
        }
    });

    xhr.open("POST", remote_host+"/api/result_report?task="+task+"&name="+name);
    xhr.setRequestHeader("Content-Type", "text/plain");

    xhr.send(result);
}