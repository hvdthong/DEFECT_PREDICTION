package org.apache.tools.ant.taskdefs.optional.depend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A class file iterator which iterates through the contents of a Java jar
 * file.
 *
 * @author Conor MacNeill
 */
public class JarFileIterator implements ClassFileIterator {
    /** The jar stream from the jar file being iterated over*/
    private ZipInputStream jarStream;

    /**
     * Construct a iterartor over a jar stream
     *
     * @param stream the basic input stream from which the Jar is recived
     * @exception IOException if the jar stream connot be created
     */
    public JarFileIterator(InputStream stream) throws IOException {
        super();

        jarStream = new ZipInputStream(stream);
    }

    /**
     * Read a stream into an array of bytes
     *
     * @param stream the stream from which the bytes are read
     * @return the stream's content as a byte array
     * @exception IOException if the stream cannot be read
     */
    private byte[] getEntryBytes(InputStream stream) throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        int n;

        while ((n = stream.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, n);
        }

        return baos.toByteArray();
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

