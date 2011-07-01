var isIE;

function init() {    
    caseField = document.getElementById("case-field");
    resField = document.getElementById("res-field");
    compsetField = document.getElementById("compset-field");
    machField = document.getElementById("machine-field");
    
    
    envOptionTable = document.getElementById("envOption-table");
    
    templateField = document.getElementById("template-field");
    templateField.textContent = "";
    
    doCompletion();
}

function doCompletion() {
    var url = "fillTemplate?action=fill"+
    "&casename="+escape(caseField.value)+
    "&resolution="+escape(resField.value)+
    "&compset="+escape(compsetField.value)+
    "&machine="+escape(machField.value);
    
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
        return new XMLHttpRequest(); // Not IE then, Firefox or something
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
    else {
        // Do nothing
        return null;
    }
}

function callback(req) {
    if (req.readyState == 4) { // 4 = complete
        if (req.status == 200) { 
            parseMessages(req.responseXML);
        }
    }
}

//    readableName is name shown for option
//    name_id is the id used for reference to the text input field
//    value is the value shown initially in the text input field
//    default value is the value shown when mouse hovers over the readable Name
//    description is put in title, showing up when mouse hovers over field
function addOption(readableName, name_id, value, defaultValue, description) {
    var row;
    
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
}

function clearTable() {
    if (envOptionTable.getElementsByTagName("tr").length > 0) {
        envOptionTable.style.display = 'none';
        for (loop = envOptionTable.childNodes.length - 1; loop >= 0 ; loop--) {
            envOptionTable.removeChild(envOptionTable.childNodes[loop]);
        }
    }
}

function parseMessages(responseXML) {
    if (responseXML == null) {
        templateField.textContent += "--Null Response--\n";
    } else if (responseXML.getElementsByTagName("wrapper").length != 0) {
        if (responseXML.getElementsByTagName("tableXML").length != 0) {
            // popup box stuff
            doPopupTable(responseXML.getElementsByTagName("tableXML"));
        } else { // Normal template update
            updateTemplateField(responseXML.getElementsByTagName("template")[0]); 
        }
    } else {
        templateField.textContent = "--Bad Response--\n";
    }
}

function doPopupTable(xmlTable) {
    if (xmlTable.childNodes.length > 0) {
        envOptionTable.setAttribute("bordercolor", "blue");
        envOptionTable.setAttribute("border", "4");
    }
}

function updateTemplateField(caseElement) {
    templateField.textContent = caseElement.childNodes[0].nodeValue;
}
function addOptionFields(optionsEC) {
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
            var name = option.getElementsByTagName("name")[0].childNodes[0].nodeValue;
            var value = option.getElementsByTagName("value")[0].childNodes[0].nodeValue;
            var defaultValue = option.getElementsByTagName("defaultValue")[0].childNodes[0].nodeValue;
            var readableName = option.getElementsByTagName("readableName")[0].childNodes[0].nodeValue;
            var description = option.getElementsByTagName("description")[0].childNodes[0].nodeValue;
                
            addOption(readableName + ":", name, value, defaultValue, description);
        }
    }
}