package org.apache.tools.ant.types.resources;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A GZip compressed resource.
 *
 * <p>Wraps around another resource, delegates all quries to that
 * other resource but uncompresses/compresses streams on the fly.</p>
 *
 * @since Ant 1.7
 */
public class GZipResource extends CompressedResource {

    /** A no-arg constructor */
    public GZipResource() {
    }

    /**
     * Constructor with another resource to wrap.
     * @param other the resource to wrap.
     */
    public GZipResource(org.apache.tools.ant.types.ResourceCollection other) {
        super(other);
    }

    /**
     * Decompress on the fly using java.util.zip.GZIPInputStream.
     * @param in the stream to wrap.
     * @return the wrapped stream.
     * @throws IOException if there is a problem.
     */
    protected InputStream wrapStream(InputStream in) throws IOException {
        return new GZIPInputStream(in);
    }

    /**
     * Compress on the fly using java.util.zip.GZIPOutStream.
     * @param out the stream to wrap.
     * @return the wrapped stream.
     * @throws IOException if there is a problem.
     */
     protected OutputStream wrapStream(OutputStream out) throws IOException {
        return new GZIPOutputStream(out);
    }

    /**
     * Get the name of the compression method.
     * @return the string "GZip".
     */
    protected String getCompressionName() {
        return "GZip";
    }
}
