package org.apache.xalan.extensions;

import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;

/**
 * A sample implementation of NamespaceContext, with support for 
 * EXSLT extension functions and Java extension functions.
 */
public class ExtensionNamespaceContext implements NamespaceContext
{
    public static final String EXSLT_PREFIX = "exslt";
    public static final String EXSLT_MATH_PREFIX = "math";
    public static final String EXSLT_SET_PREFIX = "set";
    public static final String EXSLT_STRING_PREFIX = "str";
    public static final String EXSLT_DATETIME_PREFIX = "datetime";
    public static final String EXSLT_DYNAMIC_PREFIX = "dyn";
    public static final String JAVA_EXT_PREFIX = "java";
    
    /**
     * Return the namespace uri for a given prefix
     */
    public String getNamespaceURI(String prefix)
    {
        if (prefix == null)
            throw new IllegalArgumentException(
                XSLMessages.createMessage(
                    XSLTErrorResources.ER_NAMESPACE_CONTEXT_NULL_PREFIX, null));
        
        if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX))
            return XMLConstants.NULL_NS_URI;
        else if (prefix.equals(XMLConstants.XML_NS_PREFIX))
            return XMLConstants.XML_NS_URI;
        else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE))
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        else if (prefix.equals(EXSLT_PREFIX))
            return EXSLT_URI;
        else if (prefix.equals(EXSLT_MATH_PREFIX))
            return EXSLT_MATH_URI;
        else if (prefix.equals(EXSLT_SET_PREFIX))
            return EXSLT_SET_URI;
        else if (prefix.equals(EXSLT_STRING_PREFIX))
            return EXSLT_STRING_URI;
        else if (prefix.equals(EXSLT_DATETIME_PREFIX))
            return EXSLT_DATETIME_URI;
        else if (prefix.equals(EXSLT_DYNAMIC_PREFIX))
            return EXSLT_DYNAMIC_URI;        
        else if (prefix.equals(JAVA_EXT_PREFIX))
            return JAVA_EXT_URI;
        else
            return XMLConstants.NULL_NS_URI;        
    }
    
    /**
     * Return the prefix for a given namespace uri.
     */
    public String getPrefix(String namespace)
    {
        if (namespace == null)
            throw new IllegalArgumentException(
                XSLMessages.createMessage(
                    XSLTErrorResources.ER_NAMESPACE_CONTEXT_NULL_NAMESPACE, null));
        
        if (namespace.equals(XMLConstants.XML_NS_URI))
            return XMLConstants.XML_NS_PREFIX;
        else if (namespace.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI))
            return XMLConstants.XMLNS_ATTRIBUTE;
        else if (namespace.equals(EXSLT_URI))
            return EXSLT_PREFIX;
        else if (namespace.equals(EXSLT_MATH_URI))
            return EXSLT_MATH_PREFIX;
        else if (namespace.equals(EXSLT_SET_URI))
            return EXSLT_SET_PREFIX;
        else if (namespace.equals(EXSLT_STRING_URI))
            return EXSLT_STRING_PREFIX;
        else if (namespace.equals(EXSLT_DATETIME_URI))
            return EXSLT_DATETIME_PREFIX;
        else if (namespace.equals(EXSLT_DYNAMIC_URI))
            return EXSLT_DYNAMIC_PREFIX;
        else if (namespace.equals(JAVA_EXT_URI))
            return JAVA_EXT_PREFIX;
        else
            return null;        
    }
    
    public Iterator getPrefixes(String namespace)
    {
    	final String result = getPrefix(namespace);
    	
        return new Iterator () {
        	
        	private boolean isFirstIteration = (result != null);
        	
        	public boolean hasNext() {
        		return isFirstIteration;
        	}
        	
        	public Object next() {
        		if (isFirstIteration) {
        			isFirstIteration = false;
        			return result;
        		}
        		else
        			return null;
        	}
        	
        	public void remove() {
        		throw new UnsupportedOperationException();
        	}
        };
    }
}
