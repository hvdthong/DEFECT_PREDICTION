package org.apache.xalan.xsltc.runtime.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Santiago Pericas-Geertsen
 */
class WriterOutputBuffer implements OutputBuffer {
    private static final int KB = 1024;
    private static int BUFFER_SIZE = 4 * KB;

    static {
	final String osName = System.getProperty("os.name");
	if (osName.equalsIgnoreCase("solaris")) {
	    BUFFER_SIZE = 32 * KB;
	}
    }

    private Writer _writer;

    /**
     * Initializes a WriterOutputBuffer by creating an instance of a 
     * BufferedWriter. The size of the buffer in this writer may have 
     * a significant impact on throughput. Solaris prefers a larger
     * buffer, while Linux works better with a smaller one.
     */
    public WriterOutputBuffer(Writer writer) {
	_writer = new BufferedWriter(writer, BUFFER_SIZE);
    }

    public String close() {
	try {
	    _writer.flush();
	}
	catch (IOException e) {
	    throw new RuntimeException(e.toString());
	}
	return "";
    }

    public OutputBuffer append(String s) {
	try {
	    _writer.write(s);
	}
	catch (IOException e) {
	    throw new RuntimeException(e.toString());
	}
	return this;
    }

    public OutputBuffer append(char[] s, int from, int to) {
	try {
	    _writer.write(s, from, to);
	}
	catch (IOException e) {
	    throw new RuntimeException(e.toString());
	}
	return this;
    }

    public OutputBuffer append(char ch) {
	try {
	    _writer.write(ch);
	}
	catch (IOException e) {
	    throw new RuntimeException(e.toString());
	}
	return this;
    }
}


