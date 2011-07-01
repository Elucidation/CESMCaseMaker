var isIE;
function init() {    
    caseField = document.getElementById("case-field");
    resField = document.getElementById("res-field");
    compsetField = document.getElementById("compset-field");
    machField = document.getElementById("machine-field");
    
    
    envOptionTable = document.getElementById("envOption-table");
    envConfigField = document.getElementById("envConfig-field");
    envBuildField = document.getElementById("envBuild-field");
    envRunField = document.getElementById("envRun-field");
    
    templateField = document.getElementById("template-field");
    templateField.textContent = "";
    
    doCompletion();
}

function doCompletion() {
    clearTable();
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
/* depending on id it will update a popup table with those environment options*/
function doAutoComplete(id) {
    var url = "fillTemplate?action=complete";
    if (id == "envConfig-field") {
        url += "&type=config&currentValue="+escape(envConfigField.value);
    } else if (id == "envBuild-field") {
        url += "&type=build&currentValue="+escape(envBuildField.value);
    } else if (id == "envRun-field") {
        url += "&type=run&currentValue="+escape(envRunField.value);
    }
    
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
    envOptionTable.style.display = 'none';
    if (envOptionTable.getElementsByTagName("tr").length > 0) {
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
    //templateField.textContent = "booop: " + xmlTable[0].childNodes.length;
    clearTable(); // Might remove this if it's a waste
    //templateField.textContent = "popup table length: " + xmlTable[0].childNodes.length + "\n";
    addPopUpRow("Name","Default Value","Description","ID");
    //addHeaderRow() To be implemented
    if (xmlTable[0].childNodes.length > 0) {
        // There are values, so lets make a table!
        for (loop = 0; loop < xmlTable[0].childNodes.length; loop++ ) {
            //templateField.textContent += "#"+loop+" of "+xmlTable[0].childNodes.length + " | ";
            var envOption = xmlTable[0].childNodes[loop];
            var id = envOption.getElementsByTagName("id")[0].childNodes[0].nodeValue;
            //var longname = escape(envOption.getElementsByTagName("name")[0].childNodes[0].nodeValue); // pretty crappy longname atm, skipping
            var defaultValue = envOption.getElementsByTagName("default")[0].childNodes[0];
            if (defaultValue == undefined) {
                defaultValue = "";
            } else {
                defaultValue = defaultValue.nodeValue;
            }
            //templateField.textContent += " " + defaultValue + "\n";
            var description = envOption.getElementsByTagName("description")[0].childNodes[0];
            if (description == undefined) {
                description = "";
            } else {
                description = description.nodeValue;
            }
            
            addPopUpRow(id, defaultValue, description, id); // not using longname, using id instead
        }
    }
}

function addPopUpRow(longname, defaultValue, description, id) {
    var row;
    var cellId;
    var cellDefault;
    var cellDescription;
    var linkElement;
    if (isIE) {
        envOptionTable.style.display = 'block';
        row = envOptionTable.insertRow(envOptionTable.rows.length);
        cellId = row.insertCell(0);
        cellDefault = row.insertCell(1);
        cellDescription = row.insertCell(2);
    } else {
        envOptionTable.style.display = 'table';
        row = document.createElement("tr");
        cellId = document.createElement("td");
        cellDefault = document.createElement("td");
        cellDescription = document.createElement("td");
        row.appendChild(cellId);
        row.appendChild(cellDefault);
        row.appendChild(cellDescription);
        envOptionTable.appendChild(row);
    }
    cellId.className = "popupBox";
    cellDefault.className = "popupBox";
    cellDescription.className = "popupBox";

    linkElement = document.createElement("a");
    linkElement.className = "popupItem";
    linkElement.setAttribute("href", "NotImplementedYet &id=" + id);
    linkElement.setAttribute("tabindex", "5");
    linkElement.appendChild(document.createTextNode(longname));
    cellId.appendChild(linkElement);
    cellDefault.appendChild(document.createTextNode(defaultValue))
    cellDescription.appendChild(document.createTextNode(description))
}

function updateTemplateField(caseElement) {
    templateField.textContent = caseElement.childNodes[0].nodeValue;
}
function addOptionFields(optionsEC) {
    //clearTable(); // Start fresh
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