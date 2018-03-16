package org.apache.xalan.transformer;

import org.w3c.dom.Node;
import org.apache.xml.dtm.DTM;

import org.xml.sax.*;

import org.apache.xml.dtm.ref.DTMTreeWalker;
import org.apache.xml.utils.MutableAttrListImpl;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xpath.DOMHelper;
import org.apache.xpath.XPathContext;

/**
 * <meta name="usage" content="internal"/>
 * Handle a walk of a tree, but screen out attributes for
 * the result tree.
 */
public class TreeWalker2Result extends DTMTreeWalker
{

  /** The transformer instance          */
  TransformerImpl m_transformer;

  /** The result tree handler          */
  ResultTreeHandler m_handler;

  /** Node where to start the tree walk           */
  int m_startNode;

  /**
   * Constructor.
   *
   * @param transformer Non-null transformer instance
   * @param handler The Result tree handler to use
   */
  public TreeWalker2Result(TransformerImpl transformer,
                           ResultTreeHandler handler)
  {

    super(handler, null);

    m_transformer = transformer;
    m_handler = handler;
  }

  /**
   * Perform a pre-order traversal non-recursive style.
   *
   * @param pos Start node for traversal
   *
   * @throws TransformerException
   */
  public void traverse(int pos) throws org.xml.sax.SAXException
  {
    m_dtm = m_transformer.getXPathContext().getDTM(pos);
    m_startNode = pos;

    super.traverse(pos);
  }
        
        /**
   * End processing of given node 
   *
   *
   * @param node Node we just finished processing
   *
   * @throws org.xml.sax.SAXException
   */
  protected void endNode(int node) throws org.xml.sax.SAXException
  {
    super.endNode(node);
    if(DTM.ELEMENT_NODE == m_dtm.getNodeType(node))
    {
      m_transformer.getXPathContext().popCurrentNode();
    }
  }

  /**
   * Start traversal of the tree at the given node
   *
   *
   * @param node Starting node for traversal
   *
   * @throws TransformerException
   */
  protected void startNode(int node) throws org.xml.sax.SAXException
  {

    XPathContext xcntxt = m_transformer.getXPathContext();
    try
    {
      
      if (DTM.ELEMENT_NODE == m_dtm.getNodeType(node))
      {
        xcntxt.pushCurrentNode(node);                   
                                        
        if(m_startNode != node)
        {
          super.startNode(node);
        }
        else
        {
          String elemName = m_dtm.getNodeName(node);
          String localName = m_dtm.getLocalName(node);
          String namespace = m_dtm.getNamespaceURI(node);
                                        
          m_handler.startElement(namespace, localName, elemName, null);

          boolean hasNSDecls = false;
          DTM dtm = m_dtm;
          for (int ns = dtm.getFirstNamespaceNode(node, true); 
               DTM.NULL != ns; ns = dtm.getNextNamespaceNode(node, ns, true))
          {
            m_handler.ensureNamespaceDeclDeclared(dtm, ns);
          }
                                                
          if(hasNSDecls)
          {
            m_handler.addNSDeclsToAttrs();
          }
                                                
          for (int attr = dtm.getFirstAttribute(node); 
               DTM.NULL != attr; attr = dtm.getNextAttribute(attr))
          {
            m_handler.addAttribute(attr);
          }
        }
                                
      }
      else
      {
        xcntxt.pushCurrentNode(node);
        super.startNode(node);
        xcntxt.popCurrentNode();
      }
    }
    catch(javax.xml.transform.TransformerException te)
    {
      throw new org.xml.sax.SAXException(te);
    }
  }
}
