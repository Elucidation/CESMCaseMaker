/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


//var req;
var isIE;

var completeField;
var completeTable;
var autoRow;

function init() {
    // Called every time page is loaded supposedly (boady onload in index.jsp)
    //completeField = document.getElementById("complete-field");
    
    caseField = document.getElementById("case-field");
    resField = document.getElementById("res-field");
    compsetField = document.getElementById("compset-field");
    machField = document.getElementById("machine-field");
    
    
    envConfigTable = document.getElementById("envConfig-table");
    
    //completeTable = document.getElementById("complete-table");
    //autoRow = document.getElementById("auto-row");
    //completeTable.style.top = getElementY(autoRow) + "px";
    templateField = document.getElementById("template-field");
    templateField.textContent = "";
    
    doCompletion(); // So it's not empty on startup, uses whatever values in fields'
    doLoadOptions("full"); // Set up available options initially, clear everything
// Async causes fun and interesting things to happen.
// I got it, 'req' get's overwritten because they're both done in series!
// Fixed issue by removing global req and passing callback w/ local req
}

function doCompletion() {
    var url = "autocomplete?action=fill"+
    "&name="+escape(caseField.value)+
    "&res="+escape(resField.value)+
    "&compset="+escape(compsetField.value)+
    "&mach="+escape(machField.value);
    
    //templateField.textContent = "doCompletion: " + url + "\n\n"; // not important
    
    // This kind of hard-coded logic needs to be moved to java, and further to SQL
    // it will get way too complicated at this rate.
    /*
    if (escape(runTypeField.value) == "Branched") {
        startdateRowField.hidden = true;
    } else {
        startdateRowField.hidden = false;
        url += "&startDate="+escape(startDateField.value);
    }*/
    var req = initRequest();
    req.open("GET",url,true);
    req.onreadystatechange = function(){
        callback(req)
    };
    req.send(null);
}

function doLoadOptions(level) {
    // level can be 'full' or null
    var url = "autocomplete?action=loadOptionsEnvConf&level=";
    if (level) {
        url += level;
    }
    var req = initRequest();
    req.open("GET",url,true);
    req.onreadystatechange = function(){
        callback(req)
    };
    req.send(null);
}

function doEnvConfAdd() {
    var url = "autocomplete?action=fillEnvConf";
    
    if (envConfigTable.getElementsByTagName("tr").length > 0) {
        for (loop = 0; loop < envConfigTable.childNodes.length; loop++) {
            var field = envConfigTable.childNodes[loop];
            url += "&"+ field.childNodes[1].childNodes[0].getAttribute("id")
            +"=" + escape(field.childNodes[1].childNodes[0].value);
        }
    }
    
    //templateField.textContent = "doCompletion: " + url + "\n\n"; // not important
    
    // This kind of hard-coded logic needs to be moved to java, and further to SQL
    // it will get way too complicated at this rate.
    /*// env.conf variables
    runTypeField = document.getElementById("runType-field");
    startDateField = document.getElementById("startDate-field");
    
    optionsTable = document.getElementById("options-table");
    startdateRowField = document.getElementById("startdateRow");
     *
     *if (escape(runTypeField.value) == "Branched") {
        startdateRowField.hidden = true;
    } else {
        startdateRowField.hidden = false;
    }*/
    var req = initRequest();
    req.open("GET",url,true);
    req.onreadystatechange = function(){
        callback(req)
    };
    req.send(null);
}

function initRequest() {
    if (window.XMLHttpRequest) {
        if (navigator.userAgent.indexOf('MSIE') != -1) {
            isIE = true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}
/*
 * readyState Value	Object Status Definition
 * 0	uninitialized
 * 1	loading
 * 2	loaded
 * 3	interactive
 * 4	complete
 */
function callback(req) {
    if (req.readyState == 4) {
        if (req.status == 200) {
            //templateField.textContent += " wup ";
            parseMessages(req.responseXML);
        }
    }
}

function addOption(readableName, name_id, value, defaultValue, description) {
    // readableName is name shown for option
    // name_id is the id used for reference to the text input field
    // value is the value shown initially in the text input field
    // default value is the value shown when mouse hovers over the readable Name
    // description is put in title, showing up when mouse hovers over field
    var row;
    var cell;
    //var value = "test";

    if (isIE) {
        envConfigTable.style.display = 'block';
        row = envConfigTable.insertRow(envconfigTable.rows.length);
        namecell = row.insertCell(0);
        valuecell = row.insertCell(1);
    } else {
        envConfigTable.style.display = 'table';
        row = document.createElement("tr");
        namecell = document.createElement("td");
        valuecell = document.createElement("td");
        row.appendChild(namecell);
        row.appendChild(valuecell);
        envConfigTable.appendChild(row);
    }
    //var nameElement = document.createTextNode(readableName);
    var nameElement = document.createElement("normalText");
    nameElement.setAttribute("title", "xml option: "+ name_id +", default: "+defaultValue);
    nameElement.textContent = readableName;
    namecell.appendChild(nameElement);
    
    var formElement = document.createElement("input");
    formElement.setAttribute("type","text");
    formElement.setAttribute("id",name_id+"-field");
    formElement.setAttribute("value",value);
    formElement.setAttribute("onkeyup","doEnvConfAdd();");
    formElement.setAttribute("title",description);
    valuecell.appendChild(formElement);
        
            
            
/*<tr> <!-- Row 5 -->
                                        <td>Type of run:</td>
                                        <td>
                                            <select name="runType" 
                                                    id="runType-field"
                                                    onchange="doEnvConfAdd();">
                                                <option>Startup</option>
                                                <option>Branched</option>
                                                <option>Hybrid</option>
                                            </select>

                                        </td>
                                    </tr>

                                    <tr id="startdateRow"> <!-- Row 6 -->
                                        <td>Start date:</td>
                                        <td>
                                            <input type="text" 
                                                   name="start date" 
                                                   id="startDate-field"
                                                   value="0001-01-01"
                                                   onkeyup="doEnvConfAdd();"/>

                                        </td>
                                    </tr> */
}

function clearTable() {
    if (envConfigTable.getElementsByTagName("tr").length > 0) {
        envConfigTable.style.display = 'none';
        for (loop = envConfigTable.childNodes.length - 1; loop >= 0 ; loop--) {
            envConfigTable.removeChild(envConfigTable.childNodes[loop]);
        }
    }
}

function parseMessages(responseXML) {
    if (responseXML == null) {
        templateField.textContent += "--Null Response--\n";
        return false;
    } else if (responseXML.getElementsByTagName("wrap").length != 0) {
        // wrapper containing both create_newcase & env_conf, so do both
        updateTemplateField(responseXML.getElementsByTagName("create_newcase")[0]);
        
        // Only if there are changes (# of fields)
        // Not a very accurate check, if two changes cancel each other no change will show up!
        if (envConfigTable.getElementsByTagName("tr").length != responseXML.getElementsByTagName("env_conf")[0].childNodes.length) {
            addOptionFields(responseXML.getElementsByTagName("env_conf")[0]);
        }
        //templateField.textContent = "Hmmmmmm....\n";
    } else if (responseXML.getElementsByTagName("create_newcase").length != 0){
        updateTemplateField(responseXML.getElementsByTagName("create_newcase")[0]);
    } else if (responseXML.getElementsByTagName("env_conf").length != 0){
        addOptionFields(responseXML.getElementsByTagName("env_conf")[0]);
    //templateField.textContent += "derp";
    } else {
        templateField.textContent = "--Bad Response--\n";
        return false;
    }
    
}
function updateTemplateField(caseElement) {
    templateField.textContent = caseElement.childNodes[0].nodeValue;
}
function addOptionFields(optionsEC) {
    //var optionsEC = ; // Environment Config Options
    clearTable(); // Start fresh
    if (optionsEC.childNodes.length > 0) {
        for (loop = 0; loop < optionsEC.childNodes.length; loop++) {
            var option = optionsEC.childNodes[loop];
            // Format:
            /*<envConfigOption>
                 *  <id></id>
                 *  <name></name>
                 *  <value></value>
                 *  <defaultValue></defaultValue>
                 *  <readableName></readableName>
                 *  <description></description>
                 *</envConfigOption>
                 */
            //var id = option.getElementsByTagName("id")[0].childNodes[0].nodeValue;
            var name = option.getElementsByTagName("name")[0].childNodes[0].nodeValue;
            var value = option.getElementsByTagName("value")[0].childNodes[0].nodeValue;
            var defaultValue = option.getElementsByTagName("defaultValue")[0].childNodes[0].nodeValue;
            var readableName = option.getElementsByTagName("readableName")[0].childNodes[0].nodeValue;
            var description = option.getElementsByTagName("description")[0].childNodes[0].nodeValue;
                
            addOption(readableName + ":", name, value, defaultValue, description);
        }
    }
}