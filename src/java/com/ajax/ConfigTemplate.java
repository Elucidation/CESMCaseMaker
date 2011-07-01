/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Sam
 */
class ConfigTemplate {

    private static String template;
    private static final String templateFileLocation = "template";
    private static final EnvConfigData envConfigTable = new EnvConfigData(); // keys are UPPERCASE
    // The are the placeholders, case sensitive
    private static final String[] validPlaceholders = ("casename resolution compset machine " // the basic create_newcase.sh parameters
            //+ "RUN_TYPE RUN_STARTDATE RUN_REFCASE RUN_REFDATE" // Come env_conf.xml Environment Variables
            + envConfigTable.getKeysAsString(" ")
            + "").split(" ");

    public static String[] getValidPlaceholders() {
        return validPlaceholders;
    }
    private String[] placeholders;
    private String[] replacements;
    // Used to see if passed placeholder is an environment xml variable
    private static final String[] ENV_LOCATION_PLACEHOLDERS = {"ENVCONF", "ENVBUILD", "ENVRUN"}; // The 3 places in cesm configuration where editing environment files goes

    public ConfigTemplate() {
        try {
            template = readFile(getClass().getResource(templateFileLocation).getPath());
        } catch (Exception ex) {
            System.err.println("Error: Couldn't read/find template file " + templateFileLocation + "\n> " + ex.getMessage());
            template = "Error finding/loading template file: " + ex.getMessage();
        }
    }

    void passReplacements(String[] names, String[] values) {
        placeholders = names;
        setPlaceholdersToUppercase();
        replacements = values;
        keepSafe();
    }

    void setPlaceholdersToUppercase() {
        for (int i = 0; i < placeholders.length; i++) {
            placeholders[i] = placeholders[i].toUpperCase();
        } // Uppercase all placeholders
    }

    void keepSafe() {
        if (placeholders == null) {
            placeholders = new String[0];
            replacements = new String[0];
        }
    }

    String get() {
        String outTemplate = template;
        keepSafe();
        for (int i = 0; i < placeholders.length; i++) {
            //System.err.println(placeholders[i]);
            // if it's an env_config.xml change, do special replace to keep location
            if (envConfigTable.getEnvConfigOptions().containsKey(placeholders[i].toUpperCase())) {
                // Only Env Config works atm but any env xml can be implemented
                outTemplate = outTemplate.replaceFirst(ENV_LOCATION_PLACEHOLDERS[0],
                        ENV_LOCATION_PLACEHOLDERS[0] + "\n"
                        + xmlChange("env_conf.xml", placeholders[i].toUpperCase(), replacements[i]) // add xmlchange
                        );
            } else if (envConfigTable.getEnvBuildOptions().containsKey(placeholders[i].toUpperCase())) {
                // Only Env Config works atm but any env xml can be implemented
                outTemplate = outTemplate.replaceFirst(ENV_LOCATION_PLACEHOLDERS[1],
                        ENV_LOCATION_PLACEHOLDERS[1] + "\n"
                        + xmlChange("env_build.xml", placeholders[i].toUpperCase(), replacements[i]) // add xmlchange
                        );
            } else if (envConfigTable.getEnvRunOptions().containsKey(placeholders[i].toUpperCase())) {
                // Only Env Config works atm but any env xml can be implemented
                outTemplate = outTemplate.replaceFirst(ENV_LOCATION_PLACEHOLDERS[2],
                        ENV_LOCATION_PLACEHOLDERS[2] + "\n"
                        + xmlChange("env_run.xml", placeholders[i].toUpperCase(), replacements[i]) // add xmlchange
                        );
            } else {
                outTemplate = outTemplate.replaceAll(placeholders[i].toUpperCase(), replacements[i]);
            }
        }


        // Now remove all env_config location placeholders
        for (String environmentLocationPlaceholder : ENV_LOCATION_PLACEHOLDERS) {
            outTemplate = outTemplate.replaceAll(environmentLocationPlaceholder + "\n", "");
            outTemplate = outTemplate.replaceAll(environmentLocationPlaceholder, "");
        }

        return outTemplate;
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
                + " -val " + value;
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

    /**Returns XML table of options in following format:
     * <option>
     *  <id>
     *  <name>
     *  <default>
     *  <description>
     *
     * return xml table of autocomplete entries for requested table
     * @param currentValue
     * @param option config/build/run
     * @return 
     */
    String getReducedXMLList(String currentValue, String option) {
        String out = "";
        HashMap options;
        if (option.contentEquals("config")) {
            options = envConfigTable.getEnvConfigOptions();
        } else if (option.contentEquals("build")) {
            options = envConfigTable.getEnvBuildOptions();
        } else if (option.contentEquals("run")) {
            options = envConfigTable.getEnvRunOptions();
        } else {
            // Silly wank, return nothing
            return null;
        }
        Set keys = options.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String optionId = (String) it.next();
            if (currentValue.isEmpty() || optionId.startsWith(currentValue.toUpperCase())) {
                EnvConfigOption envOption = (EnvConfigOption) options.get(optionId); // EnvConfig needs to be name Changed to just envOption *
                out += "<option>";
                out += "<id>" + envOption.getName() + "</id>";
                out += "<name>" + envOption.getReadableName() + "</name>";
                out += "<default>" + envOption.getDefaultValue() + "</default>";
                out += "<description>" + envOption.getDescription() + "</description>";
                out += "</option>";
            }
        }
        return out;
    }
}
