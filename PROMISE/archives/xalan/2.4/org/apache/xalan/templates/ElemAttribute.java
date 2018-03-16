package org.apache.xalan.templates;

import org.apache.xml.dtm.DTM;

import javax.xml.transform.TransformerException;

import org.apache.xml.utils.QName;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.transformer.ResultTreeHandler;
import org.apache.xpath.XPathContext;

import org.xml.sax.SAXException;

/**
 * <meta name="usage" content="advanced"/>
 * Implement xsl:attribute.
 * <pre>
 * &amp;!ELEMENT xsl:attribute %char-template;>
 * &amp;!ATTLIST xsl:attribute
 *   name %avt; #REQUIRED
 *   namespace %avt; #IMPLIED
 *   %space-att;
 * &amp;
 * </pre>
 */
public class ElemAttribute extends ElemElement
{

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for this element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_ATTRIBUTE;
  }

  /**
   * Return the node name.
   *
   * @return The element name 
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_ATTRIBUTE_STRING;
  }

  /**
   * Create an attribute in the result tree.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {
    ResultTreeHandler rhandler = transformer.getResultTreeHandler();

    if (!rhandler.isElementPending())
    {
      if (TransformerImpl.S_DEBUG)
        transformer.getTraceManager().fireTraceEvent(this);

      XPathContext xctxt = transformer.getXPathContext();
      int sourceNode = xctxt.getCurrentNode();
      String attrName = m_name_avt.evaluate(xctxt, sourceNode, this);
      transformer.getMsgMgr().warn(this,
                                   XSLTErrorResources.WG_ILLEGAL_ATTRIBUTE,
                                   new Object[]{ attrName });

      if (TransformerImpl.S_DEBUG)
        transformer.getTraceManager().fireTraceEndEvent(this);
      return;

    }
    
    super.execute(transformer);
    
  }
  
  /**
   * Resolve the namespace into a prefix.  At this level, if no prefix exists, 
   * then return a manufactured prefix.
   *
   * @param rhandler The current result tree handler.
   * @param prefix The probable prefix if already known.
   * @param nodeNamespace  The namespace, which should not be null.
   *
   * @return The prefix to be used.
   */
  protected String resolvePrefix(ResultTreeHandler rhandler,
                                 String prefix, String nodeNamespace)
    throws TransformerException
  {

    if (null != prefix && (prefix.length() == 0 || prefix.equals("xmlns")))
    {
      prefix = rhandler.getPrefix(nodeNamespace);

      if (null == prefix || prefix.length() == 0 || prefix.equals("xmlns"))
      {
        if(nodeNamespace.length() > 0)
        {
          prefix = rhandler.getNewUniqueNSPrefix();
        }
        else
          prefix = "";
      }
    }
    return prefix;
  }
  
  /**
   * Validate that the node name is good.
   * 
   * @param nodeName Name of the node being constructed, which may be null.
   * 
   * @return true if the node name is valid, false otherwise.
   */
   protected boolean validateNodeName(String nodeName)
   {
      if(null == nodeName)
        return false;
      if(nodeName.equals("xmlns"))
        return false;
      return super.validateNodeName(nodeName);
   }
  
  /**
   * Construct a node in the result tree.  This method is overloaded by 
   * xsl:attribute. At this class level, this method creates an element.
   *
   * @param nodeName The name of the node, which may be null.
   * @param prefix The prefix for the namespace, which may be null.
   * @param nodeNamespace The namespace of the node, which may be null.
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  void constructNode(
          String nodeName, String prefix, String nodeNamespace, TransformerImpl transformer)
            throws TransformerException
  {

    if(null != nodeName && nodeName.length() > 0)
    {
      ResultTreeHandler rhandler = transformer.getResultTreeHandler();
      if(prefix != null && prefix.length() > 0)
      {
        try
        {
          rhandler.startPrefixMapping(prefix, nodeNamespace, false);
        }
        catch(SAXException se)
        {
          throw new TransformerException(se);
        }
      }
      String val = transformer.transformToString(this);
      String localName = QName.getLocalPart(nodeName);
      rhandler.addAttribute(nodeNamespace, localName, nodeName, "CDATA", val);
    }
  }


  /**
   * Add a child to the child list.
   * <!ELEMENT xsl:attribute %char-template;>
   * <!ATTLIST xsl:attribute
   *   name %avt; #REQUIRED
   *   namespace %avt; #IMPLIED
   *   %space-att;
   * >
   *
   * @param newChild Child to append to the list of this node's children
   *
   * @return The node we just appended to the children list 
   *
   * @throws DOMException
   */
  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {

    int type = ((ElemTemplateElement) newChild).getXSLToken();

    switch (type)
    {

    case Constants.ELEMNAME_TEXTLITERALRESULT :
    case Constants.ELEMNAME_APPLY_TEMPLATES :
    case Constants.ELEMNAME_APPLY_IMPORTS :
    case Constants.ELEMNAME_CALLTEMPLATE :
    case Constants.ELEMNAME_FOREACH :
    case Constants.ELEMNAME_VALUEOF :
    case Constants.ELEMNAME_COPY_OF :
    case Constants.ELEMNAME_NUMBER :
    case Constants.ELEMNAME_CHOOSE :
    case Constants.ELEMNAME_IF :
    case Constants.ELEMNAME_TEXT :
    case Constants.ELEMNAME_COPY :
    case Constants.ELEMNAME_VARIABLE :
    case Constants.ELEMNAME_MESSAGE :

      break;
    default :
      error(XSLTErrorResources.ER_CANNOT_ADD,
            new Object[]{ newChild.getNodeName(),

    }

    return super.appendChild(newChild);
  }
	/**
	 * @see ElemElement#setName(AVT)
	 */
	public void setName(AVT v) {
        if (v.isSimple())
        {
            if (v.getSimpleString().equals("xmlns"))
            {
                throw new IllegalArgumentException();
            }
        }
		super.setName(v);
	}

}
