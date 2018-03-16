package org.apache.tools.ant.taskdefs.optional.junit;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Serves as a wrapper the implementations of JUnitResultFormatter,
 * for example as a nested <formatter> element in <junit>.
 *
 * @author <a href="mailto:stefan.bodewig@megabit.net">Stefan Bodewig</a> 
 */
public class FormatterElement {

    private String classname;
    private String extension;
    private OutputStream out = System.out;
    private File outFile;
    private boolean useFile = true;

    public void setType(TypeAttribute type) {
        if ("xml".equals(type.getValue())) {
            setClassname("org.apache.tools.ant.taskdefs.optional.junit.XMLJUnitResultFormatter");
            setExtension(".xml");
            setClassname("org.apache.tools.ant.taskdefs.optional.junit.PlainJUnitResultFormatter");
            setExtension(".txt");
        }
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClassname() {
        return classname;
    }

    public void setExtension(String ext) {
        this.extension = ext;
    }

    public String getExtension() {
        return extension;
    }

    void setOutfile(File out) {
        this.outFile = out;
    }

    public void setOutput(OutputStream out) {
        this.out = out;
    }

    public void setUseFile(boolean useFile) {
        this.useFile = useFile;
    }

    boolean getUseFile() {
        return useFile;
    }

    JUnitResultFormatter createFormatter() throws BuildException {
        if (classname == null) {
            throw new BuildException("you must specify type or classname");
        }
        
        Class f = null;
        try {
            f = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new BuildException(e);
        }

        Object o = null;
        try {
            o = f.newInstance();
        } catch (InstantiationException e) {
            throw new BuildException(e);
        } catch (IllegalAccessException e) {
            throw new BuildException(e);
        }

        if (!(o instanceof JUnitResultFormatter)) {
            throw new BuildException(classname+" is not a JUnitResultFormatter");
        }

        JUnitResultFormatter r = (JUnitResultFormatter) o;

        if (useFile && outFile != null) {
            try {
                out = new FileOutputStream(outFile);
            } catch (java.io.IOException e) {
                throw new BuildException(e);
            }
        }
        r.setOutput(out);
        return r;
    }

    /**
     * Enumerated attribute with the values "plain" and "xml".
     */
    public static class TypeAttribute extends EnumeratedAttribute {
        public String[] getValues() {
            return new String[] {"plain", "xml"};
        }
    }
}
