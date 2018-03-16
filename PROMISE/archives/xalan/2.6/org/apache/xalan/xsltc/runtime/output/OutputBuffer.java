package org.apache.xalan.xsltc.runtime.output;

/**
 * @author Santiago Pericas-Geertsen
 */
interface OutputBuffer {

    public String close();
    public OutputBuffer append(char ch);
    public OutputBuffer append(String s);
    public OutputBuffer append(char[] s, int from, int to);

}


