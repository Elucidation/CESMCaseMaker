/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

/**
 *
 * @author Sam
 */
public class CaseTemplate {
    
    // Hardcoded template
    private String template = "create_newcase -case <CASENAME>"
            + " -res <RESOLUTION>"
            + " -compset <COMPSET>"
            + " -mach <MACHINE>";
    
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
                               String machine) {
        return template.replaceAll("<CASENAME>", casename
                      ).replaceAll("<RESOLUTION>", resolution
                      ).replaceAll("<COMPSET>", compset
                      ).replaceAll("<MACHINE>", machine);
    }
    
}
