package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.util.Hashtable;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class WeblogicTOPLinkDeploymentTool extends WeblogicDeploymentTool {

    private static final String TL_DTD_LOC

    private String toplinkDescriptor;
    private String toplinkDTD;

    /**
     * Setter used to store the name of the toplink descriptor.
     * @param inString the string to use as the descriptor name.
     */
    public void setToplinkdescriptor(String inString) {
        this.toplinkDescriptor = inString;
    }

    /**
     * Setter used to store the location of the toplink DTD file.
     * This is expected to be an URL (file or otherwise). If running
     * this on NT using a file URL, the safest thing would be to not use a
     * drive spec in the URL and make sure the file resides on the drive that
     * ANT is running from.  This will keep the setting in the build XML
     * platform independent.
     *
     * @param inString the string to use as the DTD location.
     */
    public void setToplinkdtd(String inString) {
        this.toplinkDTD = inString;
    }

    protected DescriptorHandler getDescriptorHandler(File srcDir) {
        DescriptorHandler handler = super.getDescriptorHandler(srcDir);
        if (toplinkDTD != null) {
        } else {
        }
        return handler;
    }

    /**
     * Add any vendor specific files which should be included in the
     * EJB Jar.
     */
    protected void addVendorFiles(Hashtable ejbFiles, String ddPrefix) {
        super.addVendorFiles(ejbFiles, ddPrefix);



        File toplinkDD = new File(getConfig().descriptorDir, ddPrefix + toplinkDescriptor);

        if (toplinkDD.exists()) {
            ejbFiles.put(META_DIR + toplinkDescriptor,
                         toplinkDD);
        } else {
            log("Unable to locate toplink deployment descriptor. "
                + "It was expected to be in "
                + toplinkDD.getPath(), Project.MSG_WARN);
        }
    }

    /**
     * Called to validate that the tool parameters have been configured.
     *
     */
    public void validateConfigured() throws BuildException {
        super.validateConfigured();
        if (toplinkDescriptor == null) {
            throw new BuildException("The toplinkdescriptor attribute must "
                + "be specified");
        }
    }
}
