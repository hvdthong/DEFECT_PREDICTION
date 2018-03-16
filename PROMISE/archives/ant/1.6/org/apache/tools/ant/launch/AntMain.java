package org.apache.tools.ant.launch;

import java.util.Properties;

/**
 * Interface used to bridge to the actual Main class without any
 * messy reflection
 *
 * @since Ant 1.6
 */
public interface AntMain {
    /**
     * Start Ant.
     *
     * @param args command line args
     * @param additionalUserProperties properties to set beyond those that
     *        may be specified on the args list
     * @param coreLoader - not used
     *
     * @since Ant 1.6
     */
    void startAnt(String[] args, Properties additionalUserProperties,
                  ClassLoader coreLoader);
}

