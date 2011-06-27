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

        <p>As you fill out the options below the template will auto-update on the side.</p>
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
                                                   title="Name for supported Machines, A Machine describes the hardware protocols used to run CESM (# processors, directory information etc.)"/>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td colSpan="2">
                                            <table id="envConfig-table"
                                                   border="0" 
                                                   cellpadding="0" 
                                                   id="options-table"
                                                   width="350px"></table>
                                            <!--<thead><tr><td colspan="2"><strong>Environment Configuration XML Options</strong></td></tr></thead> -->
                                        </td>
                                    </tr>

                                </tbody>
                            </table>

                            <!-- OPTIONS END -->
                        </td>
                        <td>
                            <textarea id="template-field" rows="20" cols="80"></textarea>
                        </td>
                    </tr>
                </tbody>
            </table>

        </form>
        <h5>Special notes:</h5>
    <smallNote>
        <ul>
            <li>Environment Configuration Option Fields are automatically generated, 
                as are the xmlchange additions to the template, all loaded from a table (HashMap atm)</li>
            <li>Start Date is only used when the run type is a startup or 
                hybrid, since a branched run has it's start date as the end 
                of the branch.</li>
            <li>Disabled - Start Date disappears when not needed, as does the template
                effect, these sort of relationships can be mapped accurately 
                using an <a href="http://en.wikipedia.org/wiki/Web_Ontology_Language">OWL ontology</a> 
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
        <li>AJAX autocomplete via SQL Database</li>
        <li>OWL ontological reasoning to reduce options instead of hardcoding </li>
        <li>Tooltip option information</li>
        <li>CESM environment XML file editing - proof of concept added</li>
        <li>Separate page for porting to local machine</li>
    </ul>
    <p>Source code freely available at <a href="https://github.com/Elucidation/CESMCaseMaker">https://github.com/Elucidation/CESMCaseMaker</a></p>
    <!--<p>Diagram of CESM Data Flow during Configuration</p>-->

</body>
</html>
