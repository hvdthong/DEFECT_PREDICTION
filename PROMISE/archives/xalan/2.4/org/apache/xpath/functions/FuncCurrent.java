package org.apache.xpath.functions;

import org.apache.xpath.res.XPATHErrorResources;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;

import java.util.Vector;

import org.apache.xpath.XPathContext;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.axes.PredicatedNodeTest;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.ContextNodeList;
import org.apache.xpath.axes.SubContextList;

/**
 * <meta name="usage" content="advanced"/>
 * Execute the current() function.
 */
public class FuncCurrent extends Function
{

  /**
   * Execute the function.  The function must return
   * a valid object.
   * @param xctxt The current execution context.
   * @return A valid XObject.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException
  {
    Object subContextList = xctxt.getSubContextList();
    int currentNode;

    if (null != subContextList && subContextList instanceof PredicatedNodeTest)
    {
      LocPathIterator lpi = xctxt.getCurrentNodeList();

      currentNode = lpi.getCurrentContextNode();  
        
    }
    else if(xctxt.getIteratorRoot() != DTM.NULL)
    {
      currentNode = xctxt.getIteratorRoot();
    }
    else
    {
      DTMIterator cnl = xctxt.getContextNodeList();

      if (null != cnl)
      {
        currentNode = cnl.getCurrentNode();
      }
      else
        currentNode = DTM.NULL;
    }

    return new XNodeSet(currentNode, xctxt.getDTMManager());
  }
  
  /**
   * No arguments to process, so this does nothing.
   */
  public void fixupVariables(java.util.Vector vars, int globalsSize)
  {
  }

}
