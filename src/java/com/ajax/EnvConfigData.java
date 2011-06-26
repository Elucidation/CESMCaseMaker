/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

import java.util.HashMap;

/**
 *
 * @author Sam
 */
public class EnvConfigData {
    
    private HashMap envConfigOptions = new HashMap();

    public HashMap getEnvConfigOptions() {
        return envConfigOptions;
    }

    public EnvConfigData() {
        // This entire file is just a placeholder till SQL DB is implemented.
        //EnvConfigOption(String id, String name, String defaultValue, String readableName, String description)
        envConfigOptions.put("1", new EnvConfigOption("1", "RUN_TYPE", "startup", "Run Type","Run initialization type [startup,hybrid,branch]"));
        envConfigOptions.put("2", new EnvConfigOption("2", "RUN_STARTDATE", "0001-01-01", "Start Date","Run start date (yyyy-mm-dd). Only used for startup or hybrid runs Ignored for branch runs."));
        envConfigOptions.put("3", new EnvConfigOption("3", "RUN_REFCASE", "case.std", "Reference Case","Reference case for hybrid or branch runs"));
        envConfigOptions.put("3", new EnvConfigOption("3", "RUN_REFDATE", "0001-01-01", "Reference Date","Reference date for hybrid or branch runs (yyyy-mm-dd). Used to determine the component dataset that the model starts from. Ignored for startup runs"));
        
        // Values from env_conf.xml variables : http://www.cesm.ucar.edu/models/cesm1.0/cesm/cesm_doc/a4288.html
    }
}
