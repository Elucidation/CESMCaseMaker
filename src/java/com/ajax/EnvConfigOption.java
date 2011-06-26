/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajax;

/**
 *
 * @author Sam
 */
public class EnvConfigOption {
    private String id;              // Will be in SQL DB eventually
    private String name;            // ex. RUN_TYPE, Proper name used by CESM xmlchange
    private String defaultValue;    // Initial value used
    private String readableName;    // Name shown as option in index.jsp UI
    private String description;     // Description eventually to show as tooltip
    //private ? limits/clamp/possible values // Needs to be implemented somehow

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReadableName() {
        return readableName;
    }

    public EnvConfigOption(String id, String name, String defaultValue, String readableName, String description) {
        this.id = id;
        this.name = name;
        this.defaultValue = defaultValue;
        this.readableName = readableName;
        this.description = description;
    }
}
