package org.apache.tools.ant;

import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.reflect.*;

/**
 * This is the Ant command line front end to end. This front end
 * works out where ant is installed and loads the ant libraries before
 * starting Ant proper.
 *
 * @author <a href="mailto:conor@apache.org">Conor MacNeill</a>
 */ 
public class Launcher {
    static private File determineAntHome11() {
        String classpath = System.getProperty("java.class.path");
        StringTokenizer tokenizer = new StringTokenizer(classpath, System.getProperty("path.separator"));
        while (tokenizer.hasMoreTokens()) {
            String path = tokenizer.nextToken();
            if (path.endsWith("ant.jar")) {
                File antJarFile = new File(path);
                File libDirectory = new File(antJarFile.getParent());
                File antHome = new File(libDirectory.getParent());
                return antHome;
            }
        }
        return null;
    }

    static private File determineAntHome(ClassLoader systemClassLoader) {
        try {
            String className = Launcher.class.getName().replace('.', '/') + ".class";
            URL classResource = systemClassLoader.getResource(className);
            String fileComponent = classResource.getFile();
            if (classResource.getProtocol().equals("file")) {
                int classFileIndex = fileComponent.lastIndexOf(className);
                if (classFileIndex != -1) {
                    fileComponent = fileComponent.substring(0, classFileIndex);
                }
                File classFilesDir = new File(fileComponent);
                File buildDir = new File(classFilesDir.getParent());
                File devAntHome = new File(buildDir.getParent());
                return devAntHome;
            }
            else if (classResource.getProtocol().equals("jar")) {
                int classSeparatorIndex = fileComponent.lastIndexOf("!");
                if (classSeparatorIndex != -1) {
                    fileComponent = fileComponent.substring(0, classSeparatorIndex);
                }
                URL antJarURL = new URL(fileComponent);
                File antJarFile = new File(antJarURL.getFile());
                File libDirectory = new File(antJarFile.getParent());
                File antHome = new File(libDirectory.getParent());
                return antHome;
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    static private void addDirJars(AntClassLoader classLoader, File jarDir) {
        String[] fileList = jarDir.list(new FilenameFilter() {
                                            public boolean accept(File dir, String name) {
                                                return name.endsWith(".jar");
                                            }
                                        });

        if (fileList != null) {                                                
            for (int i = 0; i < fileList.length; ++i) {
                File jarFile = new File(jarDir, fileList[i]);                                        
                classLoader.addPathElement(jarFile.getAbsolutePath());
            }
        }
    }
    
    static private void addToolsJar(AntClassLoader antLoader) {
        String javaHome = System.getProperty("java.home");
        if (javaHome.endsWith("jre")) {
            javaHome = javaHome.substring(0, javaHome.length() - 4);
        }
        System.out.println("Java home is " + javaHome);
        File toolsJar = new File(javaHome, "lib/tools.jar");
        if (!toolsJar.exists()) {
            System.out.println("Unable to find tools.jar at " + toolsJar.getPath());
        }
        else {
            antLoader.addPathElement(toolsJar.getAbsolutePath());
        }
    }
    

    static public void main(String[] args) {
        File antHome = null;
        ClassLoader systemClassLoader = Launcher.class.getClassLoader();
        if (systemClassLoader == null) {
            antHome = determineAntHome11();
        }
        else {
            antHome = determineAntHome(systemClassLoader);
        }
        if (antHome == null) {
            System.err.println("Unable to determine ANT_HOME");
            System.exit(1);
        }
    
        System.out.println("ANT_HOME is " + antHome);

        AntClassLoader antLoader = new AntClassLoader(systemClassLoader, false);

        addToolsJar(antLoader);        
        
        File libDir = new File(antHome, "lib");
        addDirJars(antLoader, libDir);
        
        File optionalDir = new File(antHome, "lib/optional");
        addDirJars(antLoader, optionalDir);

        Properties launchProperties = new Properties();
        launchProperties.put("ant.home", antHome.getAbsolutePath());        
        
        try {
            Class mainClass = antLoader.loadClass("org.apache.tools.ant.Main");
            antLoader.initializeClass(mainClass);
            
            final Class[] param = {Class.forName("[Ljava.lang.String;"),
                                   Properties.class, ClassLoader.class};
            final Method startMethod = mainClass.getMethod("start", param);
            final Object[] argument = {args, launchProperties, systemClassLoader};
            startMethod.invoke(null, argument);
        }
        catch (Exception e) {
            System.out.println("Exception running Ant: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

