package org.apache.ivy.util.cli;

import java.util.HashMap;
import java.util.Map;

public class CommandLine {
    private Map/*<String, String[]>*/ optionValues = new HashMap();
    private String[] leftOverArgs;
    
    void addOptionValues(String option, String[] values) {
        optionValues.put(option, values);
    }
    
    void setLeftOverArgs(String[] args) {
        leftOverArgs = args;
    }

    public boolean hasOption(String option) {
        return optionValues.containsKey(option);
    }

    public String getOptionValue(String option) {
        String[] values = getOptionValues(option);
        return values == null || values.length == 0 ? null : values[0];
    }

    public String getOptionValue(String option, String defaultValue) {
        String value = getOptionValue(option);
        return value == null ? defaultValue : value;
    }

    public String[] getOptionValues(String option) {
        return (String[]) optionValues.get(option);
    }

    public String[] getLeftOverArgs() {
        return leftOverArgs;
    }

}
