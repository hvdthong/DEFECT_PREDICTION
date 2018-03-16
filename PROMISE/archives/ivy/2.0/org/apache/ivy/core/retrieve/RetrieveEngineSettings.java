package org.apache.ivy.core.retrieve;

import org.apache.ivy.core.settings.IvyVariableContainer;
import org.apache.ivy.plugins.parser.ParserSettings;

public interface RetrieveEngineSettings extends ParserSettings {

    boolean isCheckUpToDate();

    IvyVariableContainer getVariables();
    
    String[] getIgnorableFilenames();

}
