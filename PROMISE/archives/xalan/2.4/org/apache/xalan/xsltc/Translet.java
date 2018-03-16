package org.apache.xalan.xsltc;

public interface Translet {

    public void transform(DOM document, TransletOutputHandler handler)
	throws TransletException;
    public void transform(DOM document, TransletOutputHandler[] handlers)
	throws TransletException;
    public void transform(DOM document, NodeIterator iterator,
			  TransletOutputHandler handler)
	throws TransletException;

    public Object addParameter(String name, Object value);

    public void buildKeys(DOM document, NodeIterator iterator,
			  TransletOutputHandler handler, int root)
	throws TransletException;
    public void addAuxiliaryClass(Class auxClass);
    public Class getAuxiliaryClass(String className);
    public String[] getNamesArray();
    public String[] getNamespaceArray();
}
