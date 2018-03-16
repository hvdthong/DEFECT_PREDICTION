package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.*;

import javax.xml.parsers.SAXParser;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.*;

public interface EJBDeploymentTool {
    /**
     * Process a deployment descriptor, generating the necessary vendor specific
     * deployment files.
     *
     * @param descriptorFilename the name of the deployment descriptor
     * @param saxParser a SAX parser which can be used to parse the deployment descriptor. 
     */
    public void processDescriptor(String descriptorFilename, SAXParser saxParser) 
        throws BuildException;
    
    /**
     * Called to validate that the tool parameters have been configured.
     *
     */
    public void validateConfigured() throws BuildException;

    /**
     * Set the task which owns this tool
     */
    public void setTask(Task task);
    
    /**
     * Configure this tool for use in the ejbjar task.
     */
    public void configure(EjbJar.Config config);     
}
