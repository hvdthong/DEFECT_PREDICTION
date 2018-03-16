package org.apache.tools.ant.taskdefs.optional.ejb;



import javax.xml.parsers.SAXParser;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


public interface EJBDeploymentTool {
    /**
     * Process a deployment descriptor, generating the necessary vendor specific
     * deployment files.
     *
     * @param descriptorFilename the name of the deployment descriptor
     * @param saxParser a SAX parser which can be used to parse the deployment descriptor. 
     */
    void processDescriptor(String descriptorFilename, SAXParser saxParser) 
        throws BuildException;
    
    /**
     * Called to validate that the tool parameters have been configured.
     *
     */
    void validateConfigured() throws BuildException;

    /**
     * Set the task which owns this tool
     */
    void setTask(Task task);
    
    /**
     * Configure this tool for use in the ejbjar task.
     */
    void configure(EjbJar.Config config);     
}
