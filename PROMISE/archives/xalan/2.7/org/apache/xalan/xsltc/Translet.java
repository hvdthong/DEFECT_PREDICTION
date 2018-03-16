package org.apache.xalan.xsltc;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public interface Translet {

    public void transform(DOM document, SerializationHandler handler)
	throws TransletException;
    public void transform(DOM document, SerializationHandler[] handlers)
	throws TransletException;
    public void transform(DOM document, DTMAxisIterator iterator,
			  SerializationHandler handler)
	throws TransletException;

    public Object addParameter(String name, Object value);

    public void buildKeys(DOM document, DTMAxisIterator iterator,
			  SerializationHandler handler, int root)
	throws TransletException;
    public void addAuxiliaryClass(Class auxClass);
    public Class getAuxiliaryClass(String className);
    public String[] getNamesArray();
    public String[] getUrisArray();
    public int[]    getTypesArray();
    public String[] getNamespaceArray();
}
