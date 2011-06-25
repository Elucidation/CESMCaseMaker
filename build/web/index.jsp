<%-- 
    Document   : index
    Created on : Jun 25, 2011, 4:04:11 PM
    Author     : Sam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="web/javascript.js"></script>
        <title>AJAX Auto-Completion</title>
    </head>
    <body onload="init()">
        <h1>Auto-Completion using AJAX Maybe Not</h1>
        <p>This example shows how you can do real time auto-completion using Asynchronous
            JavaScript and XML (Ajax) interactions.</p>

        <p>In the form below enter a name. Possible names that will be completed are displayed
            below the form. For example, try typing in "Bach," "Mozart," or "Stravinsky,"
            then click on one of the selections to see composer details.</p>

        <form name="autofillform" action="autocomplete">
            <table border="0" cellpadding="5">
                <thead>
                    <tr>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Composer Name:</strong></td>
                        <td>
                            <input type="text"
                                   size="40"
                                   id="complete-field"
                                   onkeyup="doCompletion();"/>

                        </td>
                    </tr>
                    <tr>
                        <td id="auto-row" colspan="2">
                            <table id="complete-table" />
                        </td>
                    </tr>
                </tbody>
            </table>

        </form>
        <br/>
        <br/>
        <line />
        <br/>
        <textarea name="ErrorConsole" id="error-field" rows="10" cols="100" readonly="readonly" />
        
    </body>
</html>
