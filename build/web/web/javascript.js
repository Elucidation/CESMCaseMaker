/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var req;
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
    doLoadOptions(); // Set up available options initially
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
    req = initRequest();
    req.open("GET",url,true);
    req.onreadystatechange = callback;
    req.send(null);
}

function doLoadOptions() {
    var url = "autocomplete?action=loadOptionsEnvConf";
    req = initRequest();
    req.open("GET",url,true);
    req.onreadystatechange = callback;
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
    req = initRequest();
    req.open("GET",url,true);
    req.onreadystatechange = callback;
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
function callback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            parseMessages(req.responseXML);
        }
    }
}

function addOption(readableName, name_id, defaultValue) {
    // readableName is name shown for option
    // name_id is the id used for reference to the text input field
    // default value is the value shown initially in the text input field
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
    
    namecell.appendChild(document.createTextNode(readableName));
    
    formElement = document.createElement("input");
    formElement.setAttribute("type","text");
    formElement.setAttribute("id",name_id+"-field");
    formElement.setAttribute("value",defaultValue);
    formElement.setAttribute("onkeyup","doEnvConfAdd();");
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

function appendComposer(firstName,lastName,composerId) {

    var row;
    var cell;
    var linkElement;

    if (isIE) {
        completeTable.style.display = 'block';
        row = completeTable.insertRow(completeTable.rows.length);
        cell = row.insertCell(0);
    } else {
        completeTable.style.display = 'table';
        row = document.createElement("tr");
        cell = document.createElement("td");
        row.appendChild(cell);
        completeTable.appendChild(row);
    }

    cell.className = "popupCell";

    linkElement = document.createElement("a");
    linkElement.className = "popupItem";
    linkElement.setAttribute("href", "autocomplete?action=lookup&id=" + composerId);
    linkElement.appendChild(document.createTextNode(firstName + " " + lastName));
    cell.appendChild(linkElement);
}

function getElementY(element){

    var targetTop = 0;

    if (element.offsetParent) {
        while (element.offsetParent) {
            targetTop += element.offsetTop;
            element = element.offsetParent;
        }
    } else if (element.y) {
        targetTop += element.y;
    }
    return targetTop;
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
    } else if (responseXML.getElementsByTagName("create_newcase").length != 0){
        templateField.textContent = responseXML.getElementsByTagName("create_newcase")[0].childNodes[0].nodeValue;
    } else if (responseXML.getElementsByTagName("env_conf").length != 0){
        var optionsEC = responseXML.getElementsByTagName("env_conf")[0]; // Environment Config Options
        clearTable(); // Start fresh
        if (optionsEC.childNodes.length > 0) {
            for (loop = 0; loop < optionsEC.childNodes.length; loop++) {
                var option = optionsEC.childNodes[loop];
                // Format:
                /*<envConfigOption>
                 *  <id></id>
                 *  <name></name>
                 *  <defaultValue></defaultValue>
                 *  <readableName></readableName>
                 *  <description></description>
                 *</envConfigOption>
                 */
                //var id = option.getElementsByTagName("id")[0].childNodes[0].nodeValue;
                var name = option.getElementsByTagName("name")[0].childNodes[0].nodeValue;
                var defaultValue = option.getElementsByTagName("defaultValue")[0].childNodes[0].nodeValue;
                var readableName = option.getElementsByTagName("readableName")[0].childNodes[0].nodeValue;
                //var description = option.getElementsByTagName("description")[0].childNodes[0].nodeValue;
                
                addOption(readableName + ":", name, defaultValue);
            }
        }
    } else {
        templateField.textContent = "--Bad Response--\n";
        return false;
    }
    
}

/*
        var composers = responseXML.getElementsByTagName("composers")[0];
        if (composers.childNodes.length > 0) {
            completeTable.setAttribute("bordercolor", "black");
            completeTable.setAttribute("border", "1");
            templateField.textContent += composers.childNodes.length +" Composers...\n";

            for (loop = 0; loop < composers.childNodes.length; loop++) {
                var composer = composers.childNodes[loop];
                var firstName = composer.getElementsByTagName("firstName")[0];
                var lastName = composer.getElementsByTagName("lastName")[0];
                var composerId = composer.getElementsByTagName("id")[0];
                appendComposer(firstName.childNodes[0].nodeValue,
                    lastName.childNodes[0].nodeValue,
                    composerId.childNodes[0].nodeValue);
            }
        }*/