/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 *
 * @author Sam
 */
public class CaseTemplate {

    private String template;
    private String templateFileLocation = "template";

    public CaseTemplate() {
        try {
            templateFileLocation = getClass().getResource(templateFileLocation).getPath();
            template = readFile(templateFileLocation);

        } catch (Exception ex) {
            System.err.println("Error: Couldn't read/find template file " + templateFileLocation + "\n> " + ex.getMessage());

            // Hardcoded template
            template = "create_newcase -case <CASENAME>"
                    + " -res <RESOLUTION>"
                    + " -compset <COMPSET>"
                    + " -mach <MACHINE> \n\n" + ex.getMessage();
        }
    }

    /**
     * Populated template
     * @param casename
     * @param resolution
     * @param compset
     * @param machine
     * @return template populated w/ parameters
     */
    public String fillTemplate(String casename,
            String resolution,
            String compset,
            String machine,
            String runType,
            String startDate) {

        String[] fillers = {"<CASEROOT>","<CASENAME>", "<RESOLUTION>", "<COMPSET>", "<MACHINE>"};
        String caseRoot = "scripts/<CASENAME>/";
        String[] replacements = {caseRoot,casename, resolution, compset, machine};
        String populatedTemplate = template;
        
        // ENV_CONF.XML Variables
        String envConf = "";
        
        if (!runType.isEmpty() && !runType.equalsIgnoreCase("startup")) {
            // If runType is branched or hybrid add in xmlchange (it's defaulted to startup already)
            envConf += caseRoot+xmlChange(caseRoot+"env_conf.xml", "RUN_TYPE", runType); // hardcoded RUN_TYPE, must be changed eventually
        }
        if (startDate != null && !startDate.equalsIgnoreCase("0001-01-01")) {
            // If startDate exists (runtype=startup/hybrid) & not default
            envConf += caseRoot+xmlChange(caseRoot+"env_conf.xml", "RUN_STARTDATE", startDate); // hardcoded RUN_STARTDATE, must be changed eventually
        }
        
        if (!envConf.isEmpty()) {
            envConf = "# Edits to file 'env_conf.xml'"+ "\n" + envConf;
        }

        populatedTemplate = populatedTemplate.replaceAll("<ENV CONF>", envConf);
        
        
        // Main create case & directory replaces, must run after all XML configs
        for (int i = 0; i < fillers.length; i++) {
            if (!replacements[i].isEmpty()) {
                populatedTemplate = populatedTemplate.replaceAll(fillers[i], replacements[i].trim()); // Added trim
            }
        }
        
        // Have to do this or XML parser goes wonky
        populatedTemplate = populatedTemplate.replaceAll("<", "").replaceAll(">", "");

        return populatedTemplate;
    }
    /**
     * Syntax for invoking CESM's xmlchange script
     * @param file The xml file to be edited.
     * @param id The xml variable name to be changed.
     * @param value The intended value of the variable associated with the -id argument.
     * @return String for invoking xmlchange w/ passed parameters
     */
    public static String xmlChange(String file, String id, String value) {
        return "xmlchange -file " + file
                + " -id " + id
                + " -val " + value + "\n";
    }

    /** Read a File to a string
     * Modified from http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
     *
     * @param path
     * @return
     * @throws IOException
     */
    private static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }
}
