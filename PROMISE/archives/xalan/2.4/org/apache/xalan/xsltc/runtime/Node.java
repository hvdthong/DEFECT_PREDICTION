package org.apache.xalan.xsltc.runtime;

/**
 * This class is used as "wrapper" for dom nodes. Wrappers are needed when
 * a node is passed as a parameter to a template.
 */
public class Node {
    public int node;
    public int type;
	
    public Node(int n, int t) {
	node = n;
	type = t;
    }
}
