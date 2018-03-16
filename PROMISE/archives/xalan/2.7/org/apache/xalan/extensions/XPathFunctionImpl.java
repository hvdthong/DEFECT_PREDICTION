package org.apache.xalan.extensions;

import java.util.List;
import java.util.Vector;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

/**
 * A sample implementation of XPathFunction, with support for
 * EXSLT extension functions and Java extension functions.
 */
public class XPathFunctionImpl implements XPathFunction
{
    private ExtensionHandler m_handler;
    private String m_funcName;
    
    /**
     * Construct an instance of XPathFunctionImpl from the
     * ExtensionHandler and function name.
     */
    public XPathFunctionImpl(ExtensionHandler handler, String funcName)
    {
        m_handler = handler;
        m_funcName = funcName;
    }
        
    /**
     * @see javax.xml.xpath.XPathFunction#evaluate(java.util.List)
     */
    public Object evaluate(List args)
        throws XPathFunctionException
    {
        Vector  argsVec = listToVector(args);
        
        try {
            return m_handler.callFunction(m_funcName, argsVec, null, null);
        }
        catch (TransformerException e)
        {
            throw new XPathFunctionException(e);
        }
    }
    
    /**
     * Convert a java.util.List to a java.util.Vector. 
     * No conversion is done if the List is already a Vector.
     */
    private static Vector listToVector(List args)
    {
        if (args == null)
            return null;
        else if (args instanceof Vector)
            return (Vector)args;
        else
        {
            Vector result = new Vector();
            result.addAll(args);
            return result;
        }        
    }
}
