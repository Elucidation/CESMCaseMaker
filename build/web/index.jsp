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
        <link rel="stylesheet" type="text/css" href="stylesheet.css"/>
        <script type="text/javascript" src="web/javascript.js"></script>
        <title>CESM Case Maker</title>
    </head>
    <body onload="init();">
        <h1>CESM Case Maker <code>create_newcase</code></h1>

        <p>CESM stands for the <a href="http://www.cesm.ucar.edu/">Community Earth System Model</a>
            which is a global climate model used by the <a href="http://ncar.ucar.edu/">National Center for Atmospheric Research</a> for running simulations of the earth's climate.</p>

        <p>The goal of CESMCaseMaker is to present a visual interface to creating new CESM simulations.</p>
        <p>As you fill out the form below the template will auto-update on the side.</p>
        <h6>You can copy/download the script to run from your CESM home directory.</h6>

        <form name="autofillform" > <!--action="autocomplete" removed this since no action -->
            <table border="0" cellpadding="0">
                <thead>
                    <tr>
                        <th>Options</th>
                        <th>Template</th>                        
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <!-- OPTIONS -->
                            <table border="0" cellpadding="0">
                                <tbody>
                                    <tr> <!-- Row 1 -->
                                        <td><strong>Case Name:</strong></td>
                                        <td>
                                            <input type="text"
                                                   size="20"
                                                   id="case-field"
                                                   onkeyup="doCompletion();"/>

                                        </td>
                                    </tr>
                                    <tr> <!-- Row 2 -->
                                        <td><strong>Resolution: </strong></td>
                                        <td>
                                            <input type="text"
                                                   size="20"
                                                   id="res-field"
                                                   onkeyup="doCompletion();"/>

                                        </td>
                                    </tr>
                                    <tr> <!-- Row 3 -->
                                        <td><strong>Component Set:</strong></td>
                                        <td>
                                            <input type="text"
                                                   size="20"
                                                   id="compset-field"
                                                   onkeyup="doCompletion();"/>

                                        </td>
                                    </tr>
                                    <tr> <!-- Row 4 -->
                                        <td><strong>Machine:</strong></td>
                                        <td>
                                            <input type="text"
                                                   size="20"
                                                   id="machine-field"
                                                   onkeyup="doCompletion();"/>

                                        </td>
                                    </tr>
                                </tbody>
                            </table>

                            <!-- OPTIONS END -->
                        </td>
                        <td>
                            <textarea name="ErrorConsole" id="error-field" rows="10" cols="60"></textarea>
                        </td>
                    </tr> <!--
                    <tr>
                        <td id="auto-row" colspan="2" >
                            <table id="complete-table" class="popupBox" />
                        </td>
                    </tr> -->
                </tbody>
            </table>

        </form>
    </body>
</html>
