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
    //completeField = document.getElementById("complete-field");
    
    caseField = document.getElementById("case-field");
    resField = document.getElementById("res-field");
    compsetField = document.getElementById("compset-field");
    machField = document.getElementById("machine-field");
    
    
    //completeTable = document.getElementById("complete-table");
    //autoRow = document.getElementById("auto-row");
    //completeTable.style.top = getElementY(autoRow) + "px";
    errField = document.getElementById("error-field"); // Sam addition
    errField.textContent = "Start";
}

function doCompletion() {
    var url = "autocomplete?action=fill"+
        "&name="+escape(caseField.value)+
        "&res="+escape(resField.value)+
        "&compset="+escape(compsetField.value)+
        "&mach="+escape(machField.value);
    
    errField.textContent = "doCompletion: " + url + "\n\n"; // not important
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
    
    //clearTable();
    if (req.readyState == 4) {
        if (req.status == 200) {
            errField.textContent += "callback: "+req.toString()+"\n";
            parseMessages(req.responseXML);
        }
    }
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
    if (completeTable.getElementsByTagName("tr").length > 0) {
        completeTable.style.display = 'none';
        for (loop = completeTable.childNodes.length -1; loop >= 0 ; loop--) {
            completeTable.removeChild(completeTable.childNodes[loop]);
        }
    }
}

function parseMessages(responseXML) {

    // no matches returned
    if (responseXML == null) {
        errField.textContent += "Not Parsing... \n";
        return false;
    } else {
        errField.textContent += "Parsing... \n";
        var createNewCaseScript = responseXML.getElementsByTagName("create_newcase")[0].childNodes[0].nodeValue;
        errField.textContent += "\n-----\n" + createNewCaseScript + "\n-----\n";
        /*
        var composers = responseXML.getElementsByTagName("composers")[0];
        if (composers.childNodes.length > 0) {
            completeTable.setAttribute("bordercolor", "black");
            completeTable.setAttribute("border", "1");
            errField.textContent += composers.childNodes.length +" Composers...\n";

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
    }
}