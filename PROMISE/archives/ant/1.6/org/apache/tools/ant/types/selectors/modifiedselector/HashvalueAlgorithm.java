package org.apache.tools.ant.types.selectors.modifiedselector;


import java.io.File;


/**
 * Computes a 'hashvalue' for the content of file using String.hashValue().
 * Use of this algorithm doesn't require any additional nested <param>s and
 * doesn't support any.
 *
 * @version 2003-09-13
 * @since  Ant 1.6
 */
public class HashvalueAlgorithm implements Algorithm {

    /**
     * This algorithm doesn't need any configuration.
     * Therefore it's always valid.
     * @return always true
     */
    public boolean isValid() {
        return true;
    }

    /**
     * Computes a 'hashvalue' for a file content.
     * It reads the content of a file, convert that to String and use the
     * String.hashCode() method.
     * @param file  The file for which the value should be computed
     * @return the hashvalue or <i>null</i> if the file couldn't be read
     */
    public String getValue(File file) {
        try {
            if (!file.canRead()) {
                return null;
            }
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            byte[] content = new byte[fis.available()];
            fis.read(content);
            fis.close();
            String s = new String(content);
            int hash = s.hashCode();
            return Integer.toString(hash);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Override Object.toString().
     * @return information about this comparator
     */
    public String toString() {
        return "HashvalueAlgorithm";
    }

}
