package org.apache.tools.ant.taskdefs.optional.depend;

import java.util.zip.*;
import java.io.*;

/**
 * A class file iterator which iterates through the contents of a Java jar file.
 * 
 * @author Conor MacNeill
 */
public class JarFileIterator implements ClassFileIterator {
    private ZipInputStream jarStream;

    public JarFileIterator(InputStream stream) throws IOException {
        super();

        jarStream = new ZipInputStream(stream);
    }

    private byte[] getEntryBytes(InputStream stream) throws IOException {
        byte[]                buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        int                   n;

        while ((n = stream.read(buffer, 0, buffer.length)) != -1) {
            baos.write(buffer, 0, n);
        } 

        return baos.toByteArray();
    } 

    public ClassFile getNextClassFile() {
        ZipEntry         jarEntry;
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

