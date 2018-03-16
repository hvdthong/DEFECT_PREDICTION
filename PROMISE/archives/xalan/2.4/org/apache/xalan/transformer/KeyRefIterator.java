package org.apache.xalan.transformer;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.NodeVector;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import java.util.Vector;
import org.apache.xml.utils.QName;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;

/**
 * <meta name="usage" content="internal"/>
 * This class filters nodes from a key iterator, according to 
 * whether or not the use value matches the ref value.  
 */
public class KeyRefIterator extends org.apache.xpath.axes.ChildTestIterator
{
  /**
   * Constructor KeyRefIterator
   *
   *
   * @param ref Key value to match
   * @param ki The main key iterator used to walk the source tree 
   */
  public KeyRefIterator(QName name, XMLString ref, Vector keyDecls, DTMIterator ki)
  {
    super(null);
    m_name = name;
    m_ref = ref;
    m_keyDeclarations = keyDecls;
    m_keysNodes = ki;
    setWhatToShow(org.apache.xml.dtm.DTMFilter.SHOW_ALL);
  }
  
  DTMIterator m_keysNodes;
  
  /**
   * Get the next node via getNextXXX.  Bottlenecked for derived class override.
   * @return The next node on the axis, or DTM.NULL.
   */
  protected int getNextNode()
  {                  
  	int next;   
    while(DTM.NULL != (next = m_keysNodes.nextNode()))
    {
    	if(DTMIterator.FILTER_ACCEPT == filterNode(next))
    		break;
    }
    m_lastFetched = next;
    
    return next;
  }


  /**
   *  Test whether a specified node is visible in the logical view of a
   * TreeWalker or NodeIterator. This function will be called by the
   * implementation of TreeWalker and NodeIterator; it is not intended to
   * be called directly from user code.
   * 
   * @param testnode  The node to check to see if it passes the filter or not.
   *
   * @return  a constant to determine whether the node is accepted,
   *   rejected, or skipped, as defined  above .
   */
  public short filterNode(int testNode)
  {
    boolean foundKey = false;
    Vector keys = m_keyDeclarations;

    QName name = m_name;
    KeyIterator ki = (KeyIterator)(((XNodeSet)m_keysNodes).getContainedIter());
    org.apache.xpath.XPathContext xctxt = ki.getXPathContext();
    
    if(null == xctxt)
    	assertion(false, "xctxt can not be null here!");

    try
    {
      XMLString lookupKey = m_ref;

      int nDeclarations = keys.size();

      for (int i = 0; i < nDeclarations; i++)
      {
        KeyDeclaration kd = (KeyDeclaration) keys.elementAt(i);

        if (!kd.getName().equals(name))
          continue;

        foundKey = true;

        XObject xuse = kd.getUse().execute(xctxt, testNode, ki.getPrefixResolver());

        if (xuse.getType() != xuse.CLASS_NODESET)
        {
          XMLString exprResult = xuse.xstr();

          if (lookupKey.equals(exprResult))
            return DTMIterator.FILTER_ACCEPT;
        }
        else
        {
          DTMIterator nl = ((XNodeSet)xuse).iterRaw();
          int useNode;
          
          while (DTM.NULL != (useNode = nl.nextNode()))
          {
            DTM dtm = getDTM(useNode);
            XMLString exprResult = dtm.getStringValue(useNode);
            if ((null != exprResult) && lookupKey.equals(exprResult))
              return DTMIterator.FILTER_ACCEPT;
          }
        }

    }
    catch (javax.xml.transform.TransformerException te)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(te);
    }

    if (!foundKey)
      throw new RuntimeException(
        XSLMessages.createMessage(
          XSLTErrorResources.ER_NO_XSLKEY_DECLARATION,
          new Object[] { name.getLocalName()}));
    return DTMIterator.FILTER_REJECT;
  }

  protected XMLString m_ref;
  protected QName m_name;

  /** Vector of Key declarations in the stylesheet.
   *  @serial          */
  protected Vector m_keyDeclarations;

}
