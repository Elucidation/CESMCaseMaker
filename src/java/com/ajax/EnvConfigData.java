/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

/** Data stored in HashMap, pulled via getEnvConfigOptions()
 * format : String id, String name, String defaultValue, String readableName, String description
 * @author Sam
 */
public class EnvConfigData {

    private HashMap envConfigOptions = new HashMap();
    private HashMap envBuildOptions = new HashMap();
    private HashMap envRunOptions = new HashMap();
    
    //private Collections 
    private Set setConfig;
    private Set setBuild;
    private Set setRun;
    private Set setAll;


    // Getters
    public HashMap getEnvConfigOptions() {
        return envConfigOptions;
    }
    public HashMap getEnvBuildOptions() {
        return envBuildOptions;
    }

    public HashMap getEnvRunOptions() {
        return envRunOptions;
    }
    
    
    public String[] getKeys() {
        // Get keys of all the sets
        return (String[]) setAll.toArray(new String[0]);
    }

    public EnvConfigData() {
        // This entire file is just a placeholder till SQL DB is implemented.
        try {
            loadFromTabDelimitedFile("env_conf_tab_delimited");
        } catch (Exception e) {
            System.err.println("Couldn't load file for environment options");
            envConfigOptions.put("RUN_TYPE", new EnvConfigOption("1", "RUN_TYPE", "startup", "Run Type", "Run initialization type [startup,hybrid,branch]"));
            envConfigOptions.put("RUN_STARTDATE", new EnvConfigOption("2", "RUN_STARTDATE", "0001-01-01", "Start Date", "Run start date (yyyy-mm-dd). Only used for startup or hybrid runs Ignored for branch runs."));
            envConfigOptions.put("RUN_REFCASE", new EnvConfigOption("3", "RUN_REFCASE", "case.std", "Reference Case", "Reference case for hybrid or branch runs"));
            envConfigOptions.put("RUN_REFDATE", new EnvConfigOption("4", "RUN_REFDATE", "0001-01-01", "Reference Date", "Reference date for hybrid or branch runs (yyyy-mm-dd). Used to determine the component dataset that the model starts from. Ignored for startup runs"));
        }

        // Used to sort data, since it's not for some reason
        TreeMap sortedConfigMap = new TreeMap();
        sortedConfigMap.putAll(envConfigOptions);
        setConfig = sortedConfigMap.keySet();
        
        TreeMap sortedBuildMap = new TreeMap();
        sortedBuildMap.putAll(envBuildOptions);
        setBuild = sortedBuildMap.keySet();
        
        TreeMap sortedRunMap = new TreeMap();
        sortedRunMap.putAll(envRunOptions);
        setRun = sortedRunMap.keySet();
        
        setAll = setConfig;
        setAll.addAll(setBuild);
        setAll.addAll(setRun);
        // Values from env_conf.xml variables : http://www.cesm.ucar.edu/models/cesm1.0/cesm/cesm_doc/a4288.html
    }

    /** Skips first line
     * Format expected per line:
     * Name	Type	Default	(Description\Valid Values)
     * @param fileLocation
     * @throws FileNotFoundException 
     */
    private void loadFromTabDelimitedFile(String fileLocation) throws FileNotFoundException, IOException {
        fileLocation = getClass().getResource(fileLocation).getPath();
        BufferedReader readbuffer = new BufferedReader(new FileReader(fileLocation));
        String strRead;

        int i = 0;
        while ((strRead = readbuffer.readLine()) != null) {
            if (i == 0) {
                i++;
                continue;
            } // Skip first line
            String[] parts = strRead.split("\t");
            // parts[0] is ID : (called Name by CESM)
            // parts[2] is Default : Default value
            // parts[3] is Description : Description of parameter
            String niceName = parts[0].substring(0, 1) + parts[0].substring(1).replaceAll("_", " ").toLowerCase(); // Nice version
            envConfigOptions.put(parts[0], new EnvConfigOption(
                    Integer.toString(i), // Inner ID, not used
                    parts[0], // Name/ID
                    parts[2], // Default value
                    niceName, // Long name
                    parts[3] // Description
                    ));
            i++;
        }
    }

    String getKeysAsString(String delimiter) {
        // Returns keys array joined with delimiter
        StringBuilder out = new StringBuilder();
        String[] keys = getKeys();
        for (int i=0; i<keys.length;i++){
            out.append(keys[i]);
            out.append(delimiter);
        }
        return out.toString().trim();
    }
}
