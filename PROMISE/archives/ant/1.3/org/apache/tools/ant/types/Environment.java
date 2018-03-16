package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import java.util.Vector;

/**
 * Wrapper for environment variables.
 *
 * @author <a href="mailto:stefan.bodewig@megabit.net">Stefan Bodewig</a>
 */
public class Environment {

    protected Vector variables;

    public static class Variable {
        private String key, value;

        public Variable() {
            super();
        }

        public void setKey(String key) {
            this.key = key;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public void setPath(Path path) {
            this.value = path.toString();
        }

        public void setFile(java.io.File file) {
            this.value = file.getAbsolutePath();
        }

        public String getContent() throws BuildException {
            if (key == null || value == null) {
                throw new BuildException("key and value must be specified for environment variables.");
            }
            StringBuffer sb = new StringBuffer(key.trim());
            sb.append("=").append(value.trim());
            return sb.toString();
        }
    }

    public Environment() {
        variables = new Vector();
    }

    public void addVariable(Variable var) {
        variables.addElement(var);
    }

    public String[] getVariables() throws BuildException {
        if (variables.size() == 0) {
            return null;
        }
        String[] result = new String[variables.size()];
        for (int i=0; i<result.length; i++) {
            result[i] = ((Variable) variables.elementAt(i)).getContent();
        }
        return result;
    }
}
