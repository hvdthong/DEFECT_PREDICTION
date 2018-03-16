package org.apache.camel.converter;

import org.apache.camel.Converter;
import org.apache.camel.util.CollectionStringBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URL;

/**
 * Some core java.io based <a
 * 
 * @version $Revision: 582840 $
 */
@Converter
public class IOConverter {
    private static final transient Log LOG = LogFactory.getLog(IOConverter.class);

    /**
     * Utility classes should not have a public constructor.
     */
    private IOConverter() {        
    }

    @Converter
    public static InputStream toInputStream(URL url) throws IOException {
        return url.openStream();
    }

    @Converter
    public static InputStream toInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    @Converter
    public static BufferedReader toReader(File file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }

    @Converter
    public static File toFile(String name) throws FileNotFoundException {
        return new File(name);
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
    public static String toString(File file) throws IOException {
        return toString(toReader(file));
    }

    @Converter
    public static String toString(URL url) throws IOException {
        return toString(toInputStream(url));
    }

    @Converter
    public static String toString(Reader reader) throws IOException {
        if (reader instanceof BufferedReader) {
            return toString((BufferedReader)reader);
        } else {
            return toString(new BufferedReader(reader));
        }
    }

    @Converter
    public static String toString(BufferedReader reader) throws IOException {
        if (reader == null) {
            return null;
        }
        try {
            CollectionStringBuffer builder = new CollectionStringBuffer("\n");
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    return builder.toString();
                }
                builder.append(line);
            }
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                LOG.warn("Failed to close stream: " + e, e);
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

    @Converter
    public static ObjectOutput toObjectOutput(OutputStream stream) throws IOException {
        if (stream instanceof ObjectOutput) {
            return (ObjectOutput) stream;
        }
        else {
            return new ObjectOutputStream(stream);
        }
    }

    @Converter
    public static ObjectInput toObjectInput(InputStream stream) throws IOException {
        if (stream instanceof ObjectInput) {
            return (ObjectInput) stream;
        }
        else {
            return new ObjectInputStream(stream);
        }
    }
}
