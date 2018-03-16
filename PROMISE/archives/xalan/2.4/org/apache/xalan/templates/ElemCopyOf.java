package org.apache.xalan.templates;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMTreeWalker;

import org.xml.sax.*;

import org.apache.xpath.*;
import org.apache.xpath.objects.XObject;
import org.apache.xalan.trace.SelectionEvent;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xml.utils.QName;
import org.apache.xalan.transformer.TreeWalker2Result;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.transformer.ResultTreeHandler;

import javax.xml.transform.TransformerException;

/**
 * <meta name="usage" content="advanced"/>
 * Implement xsl:copy-of.
 * <pre>
 * <!ELEMENT xsl:copy-of EMPTY>
 * <!ATTLIST xsl:copy-of select %expr; #REQUIRED>
 * </pre>
 */
public class ElemCopyOf extends ElemTemplateElement
{

  /**
   * The required select attribute contains an expression.
   * @serial
   */
  public XPath m_selectExpression = null;

  /**
   * Set the "select" attribute.
   * The required select attribute contains an expression.
   *
   * @param expr Expression for select attribute 
   */
  public void setSelect(XPath expr)
  {
    m_selectExpression = expr;
  }

  /**
   * Get the "select" attribute.
   * The required select attribute contains an expression.
   *
   * @return Expression for select attribute 
   */
  public XPath getSelect()
  {
    return m_selectExpression;
  }
  
  /**
   * This function is called after everything else has been
   * recomposed, and allows the template to set remaining
   * values that may be based on some other property that
   * depends on recomposition.
   */
  public void compose(StylesheetRoot sroot) throws TransformerException
  {
    super.compose(sroot);
    
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    m_selectExpression.fixupVariables(cstate.getVariableNames(), cstate.getGlobalsSize());
  }

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for this element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_COPY_OF;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_COPY_OF_STRING;
  }

  /**
   * The xsl:copy-of element can be used to insert a result tree
   * fragment into the result tree, without first converting it to
   * a string as xsl:value-of does (see [7.6.1 Generating Text with
   * xsl:value-of]).
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {
    if (TransformerImpl.S_DEBUG)
    	transformer.getTraceManager().fireTraceEvent(this);

    try
    {
      XPathContext xctxt = transformer.getXPathContext();
      int sourceNode = xctxt.getCurrentNode();
      XObject value = m_selectExpression.execute(xctxt, sourceNode, this);

      if (TransformerImpl.S_DEBUG)
        transformer.getTraceManager().fireSelectedEvent(sourceNode, this,
                                                        "select", m_selectExpression, value);

      ResultTreeHandler handler = transformer.getResultTreeHandler();

      if (null != value)
                        {
        int type = value.getType();
        String s;

        switch (type)
        {
        case XObject.CLASS_BOOLEAN :
        case XObject.CLASS_NUMBER :
        case XObject.CLASS_STRING :
          s = value.str();

          handler.characters(s.toCharArray(), 0, s.length());
          break;
        case XObject.CLASS_NODESET :

          DTMIterator nl = value.iter();

          DTMTreeWalker tw = new TreeWalker2Result(transformer, handler);
          int pos;

          while (DTM.NULL != (pos = nl.nextNode()))
          {
            DTM dtm = xctxt.getDTMManager().getDTM(pos);
            short t = dtm.getNodeType(pos);

            if (t == DTM.DOCUMENT_NODE)
            {
              for (int child = dtm.getFirstChild(pos); child != DTM.NULL;
                   child = dtm.getNextSibling(child))
              {
                tw.traverse(child);
              }
            }
            else if (t == DTM.ATTRIBUTE_NODE)
            {
              handler.addAttribute(pos);
            }
            else
            {
              tw.traverse(pos);
            }
          }
          break;
        case XObject.CLASS_RTREEFRAG :
          handler.outputResultTreeFragment(value,
                                           transformer.getXPathContext());
          break;
        default :
          
          s = value.str();

          handler.characters(s.toCharArray(), 0, s.length());
          break;
        }
      }
                        

    }
    catch(org.xml.sax.SAXException se)
    {
      throw new TransformerException(se);
    }
    finally
    {
      if (TransformerImpl.S_DEBUG)
        transformer.getTraceManager().fireTraceEndEvent(this);
    }

  }

  /**
   * Add a child to the child list.
   *
   * @param newChild Child to add to this node's child list
   *
   * @return Child just added to child list
   */
  public ElemTemplateElement appendChild(ElemTemplateElement newChild)
  {

    error(XSLTErrorResources.ER_CANNOT_ADD,
          new Object[]{ newChild.getNodeName(),

    return null;
  }
  
  /**
   * Call the children visitors.
   * @param visitor The visitor whose appropriate method will be called.
   */
  protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs)
  {
  	if(callAttrs)
  		m_selectExpression.getExpression().callVisitors(m_selectExpression, visitor);
    super.callChildVisitors(visitor, callAttrs);
  }

}
