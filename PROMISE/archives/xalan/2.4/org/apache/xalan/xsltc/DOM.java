package org.apache.xalan.xsltc;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface DOM {
    public final static int  FIRST_TYPE             = 0;

    public final static int  NO_TYPE                = -1;
    public final static int  ROOT                   = 0;
    public final static int  TEXT                   = 1;
    public final static int  NAMESPACE              = 2;
    public final static int  ELEMENT                = 3;
    public final static int  ATTRIBUTE              = 4;
    public final static int  PROCESSING_INSTRUCTION = 5;
    public final static int  COMMENT                = 6;
	
    public final static int  NTYPES                 = 7;
    
    public final static int NULL     = 0;
    public final static int ROOTNODE = 1;

    public final static int RETURN_CURRENT = 0;
    public final static int RETURN_PARENT  = 1;
    
    /** returns singleton iterator containg the document root */
    public NodeIterator getIterator();
    public String getStringValue();
	
    public NodeIterator getChildren(final int node);
    public NodeIterator getTypedChildren(final int type);
    public NodeIterator getAxisIterator(final int axis);
    public NodeIterator getTypedAxisIterator(final int axis, final int type);
    public NodeIterator getNthDescendant(int node, int n, boolean includeself);
    public NodeIterator getNamespaceAxisIterator(final int axis, final int ns);
    public NodeIterator getNodeValueIterator(NodeIterator iter, int returnType,
					     String value, boolean op);
    public NodeIterator orderNodes(NodeIterator source, int node);
    public String getNodeName(final int node);
    public String getNamespaceName(final int node);
    public int getType(final int node);
    public int getNamespaceType(final int node);
    public int getParent(final int node);
    public int getAttributeNode(final int gType, final int element);
    public String getNodeValue(final int node);
    public void copy(final int node, TransletOutputHandler handler)
	throws TransletException;
    public void copy(NodeIterator nodes, TransletOutputHandler handler)
	throws TransletException;
    public String shallowCopy(final int node, TransletOutputHandler handler)
	throws TransletException;
    public boolean lessThan(final int node1, final int node2);
    public void characters(final int textNode, TransletOutputHandler handler)
	throws TransletException;
    public Node makeNode(int index);
    public Node makeNode(NodeIterator iter);
    public NodeList makeNodeList(int index);
    public NodeList makeNodeList(NodeIterator iter);
    public String getLanguage(int node);
    public int getSize();
    public String getDocumentURI(int node);
    public int getTypedPosition(int type, int node);
    public int getTypedLast(int type, int node);
    public void setFilter(StripFilter filter);
    public void setupMapping(String[] names, String[] namespaces);
    public boolean isElement(final int node);
    public boolean isAttribute(final int node);
    public String lookupNamespace(int node, String prefix)
	throws TransletException;
}
