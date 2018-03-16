package org.apache.xalan.xsltc.runtime;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public interface Operators {
    public static final int EQ = 0;
    public static final int NE = 1;
    public static final int GT = 2;
    public static final int LT = 3;
    public static final int GE = 4;
    public static final int LE = 5;
	
    public static final String[] names = {
	"=", "!=", ">", "<", ">=", "<="
    };
}
