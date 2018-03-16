package org.apache.camel.converter;

import org.apache.camel.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Some core java.io based
 *
 * @version $Revision: 544115 $
 */
@Converter
public class IOConverter {
    private static final transient Log log = LogFactory.getLog(IOConverter.class);

    @Converter
    public static InputStream toInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    @Converter
    public static BufferedReader toReader(File file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }

    @Converter
    public static OutputStream toOutputStream(File file) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    @Converter
    public static BufferedWriter toWriter(File file) throws IOException {
        return new BufferedWriter(new FileWriter(file));
    }

    @Converter
    public static Reader toReader(InputStream in) throws FileNotFoundException {
        return new InputStreamReader(in);
    }

    @Converter
    public static Writer toWriter(OutputStream out) throws FileNotFoundException {
        return new OutputStreamWriter(out);
    }


    @Converter
    public static StringReader toReader(String text) {
        return new StringReader(text);
    }

    @Converter
    public static InputStream toInputStream(String text) {
        return toInputStream(text.getBytes());
    }

    @Converter
    public static byte[] toByteArray(String text) {
        return text.getBytes();
    }

    @Converter
    public static String toString(byte[] data) {
        return new String(data);
    }

    @Converter
    public static String toString(Reader reader) throws IOException {
        if (reader instanceof BufferedReader) {
            return toString((BufferedReader) reader);
        }
        else {
            return toString(new BufferedReader(reader));
        }
    }

    @Converter
    public static String toString(BufferedReader reader) throws IOException {
        if (reader == null) {
            return null;
        }
        try {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    return builder.toString();
                }
                if (first) {
                    first = false;
                }
                else {
                    builder.append("\n");
                }
                builder.append(line);
            }
        }
        finally {
            try {
                reader.close();
            }
            catch (IOException e) {
                log.warn("Failed to close stream: "+ e, e);
            }
        }
    }
    
    @Converter
    public static String toString(InputStream in) throws IOException {
        return toString(toReader(in));
    }

    @Converter
    public static InputStream toInputStream(byte[] data) {
        return new ByteArrayInputStream(data);
    }
}
