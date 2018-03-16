package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;

import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;

/**
 * Implement xsl:message.
 * <pre>
 * <!ELEMENT xsl:message %template;>
 * <!ATTLIST xsl:message
 *   %space-att;
 *   terminate (yes|no) "no"
 * >
 * </pre>
 * @xsl.usage advanced
 */
public class ElemMessage extends ElemTemplateElement
{
    static final long serialVersionUID = 1530472462155060023L;

  /**
   * If the terminate attribute has the value yes, then the
   * XSLT transformer should terminate processing after sending
   * the message. The default value is no.
   * @serial
   */

  /**
   * Set the "terminate" attribute.
   * If the terminate attribute has the value yes, then the
   * XSLT transformer should terminate processing after sending
   * the message. The default value is no.
   *
   * @param v Value to set for "terminate" attribute. 
   */
  public void setTerminate(boolean v)
  {
    m_terminate = v;
  }

  /**
   * Get the "terminate" attribute.
   * If the terminate attribute has the value yes, then the
   * XSLT transformer should terminate processing after sending
   * the message. The default value is no.
   *
   * @return value of "terminate" attribute.
   */
  public boolean getTerminate()
  {
    return m_terminate;
  }

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for this element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_MESSAGE;
  }

  /**
   * Return the node name.
   *
   * @return name of the element 
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_MESSAGE_STRING;
  }

  /**
   * Send a message to diagnostics.
   * The xsl:message instruction sends a message in a way that
   * is dependent on the XSLT transformer. The content of the xsl:message
   * instruction is a template. The xsl:message is instantiated by
   * instantiating the content to create an XML fragment. This XML
   * fragment is the content of the message.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {

    if (transformer.getDebug())
      transformer.getTraceManager().fireTraceEvent(this);

    String data = transformer.transformToString(this);

    transformer.getMsgMgr().message(this, data, m_terminate);
    
    if(m_terminate)
    
    if (transformer.getDebug())
	  transformer.getTraceManager().fireTraceEndEvent(this); 
  }
}
