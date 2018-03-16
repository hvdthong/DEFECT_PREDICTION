package org.apache.tools.ant.taskdefs.optional.depend;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A class file iterator which iterates through the contents of a Java jar
 * file.
 *
 */
public class JarFileIterator implements ClassFileIterator {
    /** The jar stream from the jar file being iterated over*/
    private ZipInputStream jarStream;

    /**
     * Construct an iterator over a jar stream
     *
     * @param stream the basic input stream from which the Jar is received
     * @exception IOException if the jar stream cannot be created
     */
    public JarFileIterator(InputStream stream) throws IOException {
        super();

        jarStream = new ZipInputStream(stream);
    }

    /**
     * Get the next ClassFile object from the jar
     *
     * @return a ClassFile object describing the class from the jar
     */
    public ClassFile getNextClassFile() {
        ZipEntry jarEntry;
        ClassFile nextElement = null;

        try {
            jarEntry = jarStream.getNextEntry();

            while (nextElement == null && jarEntry != null) {
                String entryName = jarEntry.getName();

                if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {

                    ClassFile javaClass = new ClassFile();

                    javaClass.read(jarStream);

                    nextElement = javaClass;
                } else {

                    jarEntry = jarStream.getNextEntry();
                }
            }
        } catch (IOException e) {
            String message = e.getMessage();
            String text = e.getClass().getName();

            if (message != null) {
                text += ": " + message;
            }

            throw new RuntimeException("Problem reading JAR file: " + text);
        }

        return nextElement;
    }

}

