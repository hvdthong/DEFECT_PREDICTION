package org.apache.tools.ant.taskdefs.optional.depend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.tools.ant.util.depend.AbstractAnalyzer;

/**
 * An analyzer which uses the depend task's bytecode classes to analyze
 * dependencies
 *
 */
public class AntAnalyzer extends AbstractAnalyzer {
    /**
     * Default constructor
     */
    public AntAnalyzer() {
    }

    /**
     * Determine the dependencies of the configured root classes.
     *
     * @param files a vector to be populated with the files which contain
     *      the dependency classes
     * @param classes a vector to be populated with the names of the
     *      dependency classes.
     */
    protected void determineDependencies(Vector files, Vector classes) {
        Hashtable dependencies = new Hashtable();
        Hashtable containers = new Hashtable();
        Hashtable toAnalyze = new Hashtable();
        for (Enumeration e = getRootClasses(); e.hasMoreElements();) {
            String classname = (String) e.nextElement();
            toAnalyze.put(classname, classname);
        }

        int count = 0;
        int maxCount = isClosureRequired() ? MAX_LOOPS : 1;
        Hashtable analyzedDeps = null;
        while (toAnalyze.size() != 0 && count++ < maxCount) {
            analyzedDeps = new Hashtable();
            for (Enumeration e = toAnalyze.keys(); e.hasMoreElements();) {
                String classname = (String) e.nextElement();
                dependencies.put(classname, classname);
                try {
                    File container = getClassContainer(classname);
                    if (container == null) {
                        continue;
                    }
                    containers.put(container, container);

                    ZipFile zipFile = null;
                    InputStream inStream = null;
                    try {
                        if (container.getName().endsWith(".class")) {
                            inStream = new FileInputStream(container.getPath());
                        } else {
                            zipFile = new ZipFile(container.getPath());
                            String entryName
                                = classname.replace('.', '/') + ".class";
                            ZipEntry entry = new ZipEntry(entryName);
                            inStream
                                = zipFile.getInputStream(entry);
                        }
                        ClassFile classFile = new ClassFile();
                        classFile.read(inStream);
                        Vector dependencyList = classFile.getClassRefs();
                        Enumeration depEnum = dependencyList.elements();
                        while (depEnum.hasMoreElements()) {
                            String dependency = (String) depEnum.nextElement();
                            analyzedDeps.put(dependency, dependency);
                        }
                    } finally {
                        if (inStream != null) {
                            inStream.close();
                        }
                        if (zipFile != null) {
                            zipFile.close();
                        }
                    }
                } catch (IOException ioe) {
                }
            }

            toAnalyze.clear();

            Enumeration depsEnum = analyzedDeps.elements();
            while (depsEnum.hasMoreElements()) {
                String className = (String) depsEnum.nextElement();
                if (!dependencies.containsKey(className)) {
                    toAnalyze.put(className, className);
                }
            }
        }

        Enumeration depsEnum = analyzedDeps.elements();
        while (depsEnum.hasMoreElements()) {
            String className = (String) depsEnum.nextElement();
            dependencies.put(className, className);
        }

        files.removeAllElements();
        for (Enumeration e = containers.keys(); e.hasMoreElements();) {
            files.addElement((File) e.nextElement());
        }

        classes.removeAllElements();
        for (Enumeration e = dependencies.keys(); e.hasMoreElements();) {
            classes.addElement((String) e.nextElement());
        }
    }

    /**
     * Indicate if this analyzer can determine dependent files.
     *
     * @return true if the analyzer provides dependency file information.
     */
    protected boolean supportsFileDependencies() {
        return true;
    }

}

