package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.*;
import java.util.*;

import javax.ejb.deployment.EntityDescriptor;
import javax.ejb.deployment.DeploymentDescriptor;


/**
 * A helper class which performs the actual work of the ejbc task.
 *
 * This class is run with a classpath which includes the weblogic tools and the home and remote
 * interface class files referenced in the deployment descriptors being processed.
 *
 * @author <a href="mailto:conor@cortexebusiness.com.au">Conor MacNeill</a>, Cortex ebusiness Pty Limited
 */
public class EjbcHelper {
    /**
     * The root directory of the tree containing the serialised deployment desciptors. 
     */
    private File descriptorDirectory;
    
    /**
     * The directory where generated files are placed.
     */
    private File generatedFilesDirectory;
    
    /**
     * The name of the manifest file generated for the EJB jar.
     */
    private File manifestFile;
    
    /**
     * The classpath to be used in the weblogic ejbc calls. It must contain the weblogic
     * classes <b>and</b> the implementation classes of the home and remote interfaces.
     */
    private String classpath;
    
    /**
     * The source directory for the home and remote interfaces. This is used to determine if
     * the generated deployment classes are out of date.
     */
    private File sourceDirectory;
   
    /**
     * The names of the serialised deployment descriptors
     */
    String[] descriptors;

    private boolean keepGenerated;

    /**
     * Command line interface for the ejbc helper task.
     */    
    public static void main(String[] args) throws Exception {
        EjbcHelper helper = new EjbcHelper(args);
        helper.process();
    }
    
    /**
     * Initialise the EjbcHelper by reading the command arguments.
     */    
    private EjbcHelper(String[] args) {
        int index = 0;
        descriptorDirectory = new File(args[index++]);
        generatedFilesDirectory = new File(args[index++]);
        sourceDirectory = new File(args[index++]);
        manifestFile = new File(args[index++]);
        keepGenerated = Boolean.valueOf(args[index++]).booleanValue();
        
        descriptors = new String[args.length - index];
        for (int i = 0; index < args.length; ++i) {
            descriptors[i] = args[index++];
        }
    }
    
    private String[] getCommandLine(boolean debug, File descriptorFile) {
        Vector v = new Vector();
        if (!debug) {
            v.add("-noexit");
        }
        if (keepGenerated) {
            v.add("-keepgenerated");        
        }
        v.add("-d");
        v.add(generatedFilesDirectory.getPath());
        v.add(descriptorFile.getPath());
    
        String[] args = new String[v.size()];
        v.copyInto(args);
        return args;
    }

    /**
     * Determine if the weblogic EJB support classes need to be regenerated
     * for a given deployment descriptor.
     *
     * This process attempts to determine if the support classes need to be
     * rebuilt. It does this by examining only some of the support classes 
     * which are typically generated. If the ejbc task is interrupted generating
     * the support classes for a bean, all of the support classes should be removed
     * to force regeneration of the support classes.
     *
     * @param descriptorFile the serialised deployment descriptor
     *
     * @return true if the support classes need to be regenerated.
     *
     * @throws IOException if the descriptor file cannot be closed.
     */
    private boolean isRegenRequired(File descriptorFile) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(descriptorFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            DeploymentDescriptor dd = (DeploymentDescriptor)ois.readObject();
            fis.close();
            
            String homeInterfacePath = dd.getHomeInterfaceClassName().replace('.', '/') + ".java";
            String remoteInterfacePath = dd.getRemoteInterfaceClassName().replace('.', '/') + ".java";
            String primaryKeyClassPath = null;
            if (dd instanceof EntityDescriptor) {
                primaryKeyClassPath = ((EntityDescriptor)dd).getPrimaryKeyClassName().replace('.', '/') + ".java";;
            }
        
            File homeInterfaceSource = new File(sourceDirectory, homeInterfacePath);
            File remoteInterfaceSource = new File(sourceDirectory, remoteInterfacePath);
            File primaryKeyClassSource = null;
            if (primaryKeyClassPath != null) {
                primaryKeyClassSource = new File(sourceDirectory, remoteInterfacePath);
            }
            
            String beanClassBase = dd.getEnterpriseBeanClassName().replace('.', '/');
            File ejbImplentationClass 
                = new File(generatedFilesDirectory, beanClassBase + "EOImpl.class");
            File homeImplementationClass 
                = new File(generatedFilesDirectory, beanClassBase + "HomeImpl.class");
            File beanStubClass 
                = new File(generatedFilesDirectory, beanClassBase + "EOImpl_WLStub.class");
                
            if (!ejbImplentationClass.exists() || !homeImplementationClass.exists() ||
                    !beanStubClass.exists()) {
                return true;
            }
                
            long classModificationTime = ejbImplentationClass.lastModified();
            if (homeImplementationClass.lastModified() < classModificationTime) {
                classModificationTime = homeImplementationClass.lastModified();
            }
            if (beanStubClass.lastModified() < classModificationTime) {
                classModificationTime = beanStubClass.lastModified();
            }
            
            if (descriptorFile.lastModified() > classModificationTime ||
                    homeInterfaceSource.lastModified() > classModificationTime ||
                    remoteInterfaceSource.lastModified() > classModificationTime) {
                return true;
            }
            
            if (primaryKeyClassSource != null && 
                    primaryKeyClassSource.lastModified() > classModificationTime) {
                return true;
            }
        }
        catch (Throwable descriptorLoadException) {
            System.out.println("Exception occurred reading " + descriptorFile.getName() + " - continuing");
            return true;
        }
        finally {
            if (fis != null) {
                fis.close();
            }
        }
        
        return false;
    }
    
    /**
     * Process the descriptors in turn generating support classes for each and a manifest
     * file for all of the beans.
     */            
    private void process() throws Exception {
        String manifest = "Manifest-Version: 1.0\n\n";
        for (int i = 0; i < descriptors.length; ++i) {
            String descriptorName = descriptors[i];
            File descriptorFile = new File(descriptorDirectory, descriptorName);
            
            if (isRegenRequired(descriptorFile)) {
                System.out.println("Running ejbc for " + descriptorFile.getName());
                regenerateSupportClasses(descriptorFile);
            }
            else {
                System.out.println(descriptorFile.getName() + " is up to date");
            }
            manifest += "Name: " + descriptorName.replace('\\', '/') + "\nEnterprise-Bean: True\n\n";
        }
        
        FileWriter fw = new FileWriter(manifestFile);
        PrintWriter pw = new PrintWriter(fw);
        pw.print(manifest);
        fw.flush();
        fw.close();
    }
    
    /**
     * Perform the weblogic.ejbc call to regenerate the support classes.
     *
     * Note that this method relies on an undocumented -noexit option to the 
     * ejbc tool to stop the ejbc tool exiting the VM altogether.
     */
    private void regenerateSupportClasses(File descriptorFile) throws Exception {

        
        String[] args = getCommandLine(false,descriptorFile);
        
        try {
            weblogic.ejbc.main(args);
        }
        catch (Exception e) {
            String[] newArgs = getCommandLine(true, descriptorFile);
            weblogic.ejbc.main(newArgs);
        }
    }
}
