package org.apache.xalan.templates;

import org.w3c.dom.*;

import org.xml.sax.*;

import org.apache.xpath.*;
import org.apache.xml.utils.QName;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

/**
 * <meta name="usage" content="advanced"/>
 * Implement xsl:template.
 * <pre>
 * <!ELEMENT xsl:template
 *  (#PCDATA
 *   %instructions;
 *   %result-elements;
 *   | xsl:param)
 * >
 *
 * <!ATTLIST xsl:template
 *   match %pattern; #IMPLIED
 *   name %qname; #IMPLIED
 *   priority %priority; #IMPLIED
 *   mode %qname; #IMPLIED
 *   %space-att;
 * >
 * </pre>
 */
public class ElemTemplate extends ElemTemplateElement
{
  /** The public identifier for the current document event.
   *  @serial          */
  private String m_publicId;

  /** The system identifier for the current document event.
   *  @serial          */
  private String m_systemId;

  /**
   * Return the public identifier for the current document event.
   * <p>This will be the public identifier
   * @return A string containing the public identifier, or
   *         null if none is available.
   * @see #getSystemId
   */
  public String getPublicId()
  {
    return m_publicId;
  }

  /**
   * Return the system identifier for the current document event.
   *
   * <p>If the system identifier is a URL, the parser must resolve it
   * fully before passing it to the application.</p>
   *
   * @return A string containing the system identifier, or null
   *         if none is available.
   * @see #getPublicId
   */
  public String getSystemId()
  {
    return m_systemId;
  }

  /**
   * Set the location information for this element.
   *
   * @param locator SourceLocator holding location information 
   */
  public void setLocaterInfo(SourceLocator locator)
  {

    m_publicId = locator.getPublicId();
    m_systemId = locator.getSystemId();

    super.setLocaterInfo(locator);
  }

  /**
   * The owning stylesheet.
   * (Should this only be put on the template element, to
   * conserve space?)
   * @serial
   */
  private Stylesheet m_stylesheet;

  /**
   * Get the stylesheet composed (resolves includes and
   * imports and has methods on it that return "composed" properties.
   * 
   * @return The stylesheet composed.
   */
  public StylesheetComposed getStylesheetComposed()
  {
    return m_stylesheet.getStylesheetComposed();
  }

  /**
   * Get the owning stylesheet.
   *
   * @return The owning stylesheet.
   */
  public Stylesheet getStylesheet()
  {
    return m_stylesheet;
  }

  /**
   * Set the owning stylesheet.
   *
   * @param sheet The owning stylesheet for this element
   */
  public void setStylesheet(Stylesheet sheet)
  {
    m_stylesheet = sheet;
  }

  /**
   * Get the root stylesheet.
   *
   * @return The root stylesheet for this element
   */
  public StylesheetRoot getStylesheetRoot()
  {
    return m_stylesheet.getStylesheetRoot();
  }

  /**
   * The match attribute is a Pattern that identifies the source
   * node or nodes to which the rule applies.
   * @serial
   */
  private XPath m_matchPattern = null;

  /**
   * Set the "match" attribute.
   * The match attribute is a Pattern that identifies the source
   * node or nodes to which the rule applies. The match attribute
   * is required unless the xsl:template element has a name
   * attribute (see [6 Named Templates]). It is an error for the
   * value of the match attribute to contain a VariableReference.
   *
   * @param v Value to set for the "match" attribute
   */
  public void setMatch(XPath v)
  {
    m_matchPattern = v;
  }

  /**
   * Get the "match" attribute.
   * The match attribute is a Pattern that identifies the source
   * node or nodes to which the rule applies. The match attribute
   * is required unless the xsl:template element has a name
   * attribute (see [6 Named Templates]). It is an error for the
   * value of the match attribute to contain a VariableReference.
   *
   * @return Value of the "match" attribute 
   */
  public XPath getMatch()
  {
    return m_matchPattern;
  }

  /**
   * An xsl:template element with a name attribute specifies a named template.
   * @serial
   */
  private QName m_name = null;

  /**
   * Set the "name" attribute.
   * An xsl:template element with a name attribute specifies a named template.
   * If an xsl:template element has a name attribute, it may, but need not,
   * also have a match attribute.
   *
   * @param v Value to set the "name" attribute
   */
  public void setName(QName v)
  {
    m_name = v;
  }

  /**
   * Get the "name" attribute.
   * An xsl:template element with a name attribute specifies a named template.
   * If an xsl:template element has a name attribute, it may, but need not,
   * also have a match attribute.
   *
   * @return Value of the "name" attribute
   */
  public QName getName()
  {
    return m_name;
  }

  /**
   * Modes allow an element to be processed multiple times,
   * each time producing a different result.
   * @serial
   */
  private QName m_mode;

  /**
   * Set the "mode" attribute.
   * Modes allow an element to be processed multiple times,
   * each time producing a different result.  If xsl:template
   * does not have a match attribute, it must not have a mode attribute.
   *
   * @param v Value to set the "mode" attribute
   */
  public void setMode(QName v)
  {
    m_mode = v;
  }

  /**
   * Get the "mode" attribute.
   * Modes allow an element to be processed multiple times,
   * each time producing a different result.  If xsl:template
   * does not have a match attribute, it must not have a mode attribute.
   *
   * @return Value of the "mode" attribute
   */
  public QName getMode()
  {
    return m_mode;
  }

  /**
   * The priority of a template rule is specified by the priority
   * attribute on the template rule.
   * @serial
   */
  private double m_priority = XPath.MATCH_SCORE_NONE;

  /**
   * Set the "priority" attribute.
   * The priority of a template rule is specified by the priority
   * attribute on the template rule. The value of this must be a
   * real number (positive or negative), matching the production
   * Number with an optional leading minus sign (-).
   *
   * @param v The value to set for the "priority" attribute
   */
  public void setPriority(double v)
  {
    m_priority = v;
  }

  /**
   * Get the "priority" attribute.
   * The priority of a template rule is specified by the priority
   * attribute on the template rule. The value of this must be a
   * real number (positive or negative), matching the production
   * Number with an optional leading minus sign (-).
   *
   * @return The value of the "priority" attribute
   */
  public double getPriority()
  {
    return m_priority;
  }

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for the element
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_TEMPLATE;
  }

  /**
   * Return the node name.
   *
   * @return The element's name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_TEMPLATE_STRING;
  }
  
  /**
   * The stack frame size for this template, which is equal to the maximum number 
   * of params and variables that can be declared in the template at one time.
   */
  public int m_frameSize;
  
  /**
   * The size of the portion of the stack frame that can hold parameter 
   * arguments.
   */
  int m_inArgsSize;
  
  /**
   * List of namespace/local-name pairs, DTM style, that are unique 
   * qname identifiers for the arguments.  The position of a given qname 
   * in the list is the argument ID, and thus the position in the stack
   * frame.
   */
  private int[] m_argsQNameIDs;
  
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
    java.util.Vector vnames = cstate.getVariableNames();
    if(null != m_matchPattern)
      m_matchPattern.fixupVariables(vnames, sroot.getComposeState().getGlobalsSize());
      
    cstate.resetStackFrameSize();
    m_inArgsSize = 0;
  }
  
  /**
   * This after the template's children have been composed.
   */
  public void endCompose(StylesheetRoot sroot) throws TransformerException
  {
    StylesheetRoot.ComposeState cstate = sroot.getComposeState();
    super.endCompose(sroot);
    m_frameSize = cstate.getFrameSize();
    
    cstate.resetStackFrameSize();
  }

  /**
   * Copy the template contents into the result tree.
   * The content of the xsl:template element is the template
   * that is instantiated when the template rule is applied.
   *
   * @param transformer non-null reference to the the current transform-time state.
   *
   * @throws TransformerException
   */
  public void execute(
          TransformerImpl transformer)
            throws TransformerException
  {
    XPathContext xctxt = transformer.getXPathContext();
    
    transformer.getStackGuard().checkForInfinateLoop();
    
    xctxt.pushRTFContext();

    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEvent(this);

      transformer.executeChildTemplates(this, true);

    if (TransformerImpl.S_DEBUG)
      transformer.getTraceManager().fireTraceEndEvent(this);

    xctxt.popRTFContext();  
    }

  /**
   * This function is called during recomposition to
   * control how this element is composed.
   * @param root The root stylesheet for this transformation.
   */
  public void recompose(StylesheetRoot root)
  {
    root.recomposeTemplates(this);
  }

}
