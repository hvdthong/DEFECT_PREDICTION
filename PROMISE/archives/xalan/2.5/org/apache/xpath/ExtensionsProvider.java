package org.apache.xpath;

import java.util.Vector;

import org.apache.xpath.functions.FuncExtFunction;

/**
 * Interface that XPath objects can call to obtain access to an 
 * ExtensionsTable.
 * 
 */
public interface ExtensionsProvider
{
  /**
   * Is the extension function available?
   */
  
  public boolean functionAvailable(String ns, String funcName)
          throws javax.xml.transform.TransformerException;
  
  /**
   * Is the extension element available?
   */
  public boolean elementAvailable(String ns, String elemName)
          throws javax.xml.transform.TransformerException;
   
  /**
   * Execute the extension function.
   */
  public Object extFunction(String ns, String funcName, 
                            Vector argVec, Object methodKey)
            throws javax.xml.transform.TransformerException;

  /**
   * Execute the extension function.
   */
  public Object extFunction(FuncExtFunction extFunction, 
                            Vector argVec)
            throws javax.xml.transform.TransformerException;
}
