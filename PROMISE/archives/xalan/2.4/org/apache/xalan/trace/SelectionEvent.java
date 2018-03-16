package org.apache.xalan.trace;

import org.w3c.dom.*;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPath;
import org.apache.xpath.objects.XObject;

/**
 * <meta name="usage" content="advanced"/>
 * Event triggered by selection of a node in the style stree.
 */
public class SelectionEvent implements java.util.EventListener
{

  /**
   * The node in the style tree where the event occurs.
   */
  public final ElemTemplateElement m_styleNode;

  /**
   * The XSLT processor instance.
   */
  public final TransformerImpl m_processor;

  /**
   * The current context node.
   */
  public final Node m_sourceNode;

  /**
   * The attribute name from which the selection is made.
   */
  public final String m_attributeName;

  /**
   * The XPath that executed the selection.
   */
  public final XPath m_xpath;

  /**
   * The result of the selection.
   */
  public final XObject m_selection;

  /**
   * Create an event originating at the given node of the style tree.
   * 
   * @param processor The XSLT TransformerFactory.
   * @param sourceNode The current context node.
   * @param mode The current mode.
   * @param styleNode node in the style tree reference for the event.
   * Should not be null.  That is not enforced.
   * @param attributeName The attribute name from which the selection is made.
   * @param xpath The XPath that executed the selection.
   * @param selection The result of the selection.
   */
  public SelectionEvent(TransformerImpl processor, Node sourceNode,
                        ElemTemplateElement styleNode, String attributeName,
                        XPath xpath, XObject selection)
  {

    this.m_processor = processor;
    this.m_sourceNode = sourceNode;
    this.m_styleNode = styleNode;
    this.m_attributeName = attributeName;
    this.m_xpath = xpath;
    this.m_selection = selection;
  }
}
