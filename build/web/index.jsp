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

        <p>Hover over words & fields to get more information about them, 
            or get direct links to CESM documentation.</p> 
        <p>For Environment options a pop-up table will appear that reduces as you type in letters.
            For now you must click an option to add it to the template, you can add as many as you want.</p>
        <p>Defaults are provided, and as you fill out the options below the template will auto-update on the side.</p>
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
                            <table border="0" 
                                   cellpadding="0" 
                                   id="options-table"
                                   width="350px">
                                <tbody>
                                    <tr> <!-- Row 1 -->
                                        <td><strong>Case Name:</strong></td>
                                        <td>
                                            <input type="text"
                                                   size="20"
                                                   id="case-field"
                                                   value="example"
                                                   onkeyup="doCompletion();"
                                                   tabindex="20"
                                                   title="Chosen by User, used to name case directory as /scripts/CASENAME folder of CESM"/>

                                        </td>
                                    </tr>
                                    <tr> <!-- Row 2 -->
                                        <td>
                                            <strong><a href="http://www.cesm.ucar.edu/models/cesm1.0/cesm/cesm_doc/a3668.html">Resolution</a>: </strong>
                                        </td>
                                        <td>
                                            <input type="text"
                                                   size="20"
                                                   value="f19_g16"
                                                   id="res-field"
                                                   onkeyup="doCompletion();"
                                                   tabindex="20"
                                                   title="Shortname for supported Resolutions, A Resolution is a combination of 4 grids, Atmosphere, Land, Ice & Ocean"/>

                                        </td>
                                    </tr>
                                    <tr> <!-- Row 3 -->
                                        <td><strong><a href="http://www.cesm.ucar.edu/models/cesm1.0/cesm/cesm_doc/a3134.html">Component Set</a>:</strong></td>
                                        <td>
                                            <input type="text"
                                                   size="20"
                                                   value="B2000_CN"
                                                   id="compset-field"
                                                   onkeyup="doCompletion();"
                                                   tabindex="20"
                                                   title="Shortname for supported Component Sets, A Component Set consists of 5 partial models, Atmosphere, Land, Ocean, Land-Ice & Sea-Ice"/>

                                        </td>
                                    </tr>
                                    <tr> <!-- Row 4 -->
                                        <td><strong><a href="http://www.cesm.ucar.edu/models/cesm1.0/cesm/cesm_doc/a3940.html">Machine</a>:</strong></td>
                                        <td>
                                            <input type="text"
                                                   size="20"
                                                   value="bluefire"
                                                   id="machine-field"
                                                   onkeyup="doCompletion();"
                                                   tabindex="20"
                                                   title="Name for supported Machines, A Machine describes the hardware protocols used to run CESM (# processors, directory information etc.)"/>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <strong><a href="http://www.cesm.ucar.edu/models/cesm1.0/cesm/cesm_doc/a4323.html">Environment Config XML Options</a></strong>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <input type="text"
                                                   size="20"
                                                   value=""
                                                   id="envConfig-field"
                                                   onkeyup="doAutoComplete(this.id);"
                                                   tabindex="20"
                                                   title="Insert Environment Configuration Option"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <table id="envConfig-table"></table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <strong><a href="http://www.cesm.ucar.edu/models/cesm1.0/cesm/cesm_doc/a4856.html">Environment Build XML Options</a></strong>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <input type="text"
                                                   size="20"
                                                   value=""
                                                   id="envBuild-field"
                                                   onkeyup="doAutoComplete(this.id);"
                                                   tabindex="20"
                                                   title="Insert Environment Build Option"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <table id="envBuild-table"></table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <strong><a href="http://www.cesm.ucar.edu/models/cesm1.0/cesm/cesm_doc/a5005.html">Environment Run XML Options</a></strong>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <input type="text"
                                                   size="20"
                                                   value=""
                                                   id="envRun-field"
                                                   onkeyup="doAutoComplete(this.id);"
                                                   tabindex="20"
                                                   title="Insert Environment Run Option"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <table id="envRun-table"></table>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>

                            <!-- OPTIONS END -->
                        </td>
                        <td>
                            <textarea id="template-field" rows="40" cols="80" tabindex="30"></textarea>
                        </td>
                    </tr>
                </tbody>
            </table>

        </form>
        <h5>Special notes:</h5>
    <smallNote>
        <ul>
            <li>Environment Option Fields are automatically generated, as are the 
                xmlchange additions to the template, all loaded from separate 
                data files (comma delimited format). All </li>
            <li>Run Type effects 3 other configuration options, depending on it's value:
                <ul>
                    <li><i>Startup</i> : Reference Case & Date are not used</li>
                    <li><i>Branched</i> : Start Date is not used</li>
                    <li><i>Hybrid</i> : Any can be used</li>
                </ul>
                These constraints can be described in either direction, and 
                currently resides in a <a href="https://github.com/Elucidation/CESMCaseMaker/blob/6bd201b51d51a1d238c178b6423e43703ed5dfd1/src/java/com/ajax/AutoCompleteServlet.java#L223">separate doCheck() function</a> that does a 
                boolean check to see if constraint is satisfied, this will make
                it easier to connect an inference engine to the system.
            </li>
            <li>Start Date needs to be ignored when not needed, these sort of 
                relationships can be mapped accurately using an 
                <a href="http://en.wikipedia.org/wiki/Web_Ontology_Language">OWL ontology</a> 
                (or for just deduction & speed, an sql database), 
                as  hardcoding it (current format) will be difficult to update, 
                not to mention quickly complicate the code past usability.</li>
            <li>Changes to the env_conf.xml file must be done after creation 
                of a new case but before calling the CESM configure script, 
                this order of operations is automatically taken care of 
                by the template.</li>
            <li>There are many more options in just the <a href="http://www.cesm.ucar.edu/models/cesm1.0/cesm/cesm_doc/a4288.html">env_conf.xml</a> file, however
                there are many other files, and options, many with special 
                circumstances and dependencies on other options. These
                relationships can be mapped using a knowledge base such that a
                computer can <a href="http://en.wikipedia.org/wiki/Semantic_reasoner">infer relationships</a> and do the drudge work instead.</li>
        </ul>
    </smallNote>


    <p>The eventual goal is to present a visual interface 
        to encapsulate creating, configuring, and running CESM simulations.
    </p>
    <b> Todo </b>
    <ul>
        <li>Ergonomics of Popup tables (Enter to select, ability to remove fields, etc.)</li>
        <li>Autocomplete for Component Sets and Resolutions</li>
        <li>AJAX autocomplete via SQL Database</li>
        <li>OWL ontological reasoning to reduce options instead of hardcoding </li>
        <li>Separate page for porting to local machine</li>
    </ul>
    <p>Source code freely available at <a href="https://github.com/Elucidation/CESMCaseMaker">https://github.com/Elucidation/CESMCaseMaker</a> 
        or go to <a href="">current version</a></p>
    <!--<p>Diagram of CESM Data Flow during Configuration</p>-->

    <!-- Environmental XML Options table pop-up stuff lives peacefully here -->

    <table id="envOption-table"
           class="popupBox">
        <tr>
            <td>
                Bleeep
            </td>
        </tr>
        <tr>
            <td>
                Bleeep
            </td>
        </tr>
        <tr>
            <td>
                Bleeep
            </td>
        </tr>
        <tr>
            <td>
                Bleeep
            </td>
        </tr>
        <doop id="dimSwitch">bloop</doop>
    </table>
</body>
</html>
