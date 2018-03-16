package org.apache.xpath.functions;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.SubContextList;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

/**
 * Execute the Position() function.
 * @xsl.usage advanced
 */
public class FuncPosition extends Function
{
    static final long serialVersionUID = -9092846348197271582L;
  private boolean m_isTopLevel;
  
  /**
   * Figure out if we're executing a toplevel expression.
   * If so, we can't be inside of a predicate. 
   */
  public void postCompileStep(Compiler compiler)
  {
    m_isTopLevel = compiler.getLocationPathDepth() == -1;
  }

  /**
   * Get the position in the current context node list.
   *
   * @param xctxt Runtime XPath context.
   *
   * @return The current position of the itteration in the context node list, 
   *         or -1 if there is no active context node list.
   */
  public int getPositionInContextNodeList(XPathContext xctxt)
  {

    SubContextList iter = m_isTopLevel ? null : xctxt.getSubContextList();

    if (null != iter)
    {
      int prox = iter.getProximityPosition(xctxt);
 
      return prox;
    }

    DTMIterator cnl = xctxt.getContextNodeList();

    if (null != cnl)
    {
      int n = cnl.getCurrentNode();
      if(n == DTM.NULL)
      {
        if(cnl.getCurrentPos() == 0)
          return 0;
          
        try 
        { 
          cnl = cnl.cloneWithReset(); 
        }
        catch(CloneNotSupportedException cnse)
        {
          throw new org.apache.xml.utils.WrappedRuntimeException(cnse);
        }
        int currentNode = xctxt.getContextNode();
        while(DTM.NULL != (n = cnl.nextNode()))
        {
          if(n == currentNode)
            break;
        }
      }
      return cnl.getCurrentPos();
    }

    return -1;
  }

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
    double pos = (double) getPositionInContextNodeList(xctxt);
    
    return new XNumber(pos);
  }
  
  /**
   * No arguments to process, so this does nothing.
   */
  public void fixupVariables(java.util.Vector vars, int globalsSize)
  {
  }
}
