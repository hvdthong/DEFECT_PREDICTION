package org.apache.xalan.xsltc.dom;

/*
 * IMPORTANT NOTE - this interface will probably be replaced by
 * org.apache.xml.dtm.Axis (very similar)
 */

public interface Axis {

    public static final int ANCESTOR         =  0;
    public static final int ANCESTORORSELF   =  1;
    public static final int ATTRIBUTE        =  2;
    public static final int CHILD            =  3;
    public static final int DESCENDANT       =  4;
    public static final int DESCENDANTORSELF =  5;
    public static final int FOLLOWING        =  6;
    public static final int FOLLOWINGSIBLING =  7;
    public static final int NAMESPACEDECLS   =  8;
    public static final int NAMESPACE        =  9;
    public static final int PARENT           = 10;
    public static final int PRECEDING        = 11;
    public static final int PRECEDINGSIBLING = 12;
    public static final int SELF             = 13;
	
    public static final String[] names = {
	"ancestor",
	"ancestor-or-self",
	"attribute",
	"child",
	"descendant",
	"descendant-or-self",
	"following",
	"following-sibling",
	"namespace",
	"namespace-decls",
	"parent",
	"preceding",
	"preceding-sibling",
	"self"
    };

    public static final boolean[] isReverse = {
    };
}
