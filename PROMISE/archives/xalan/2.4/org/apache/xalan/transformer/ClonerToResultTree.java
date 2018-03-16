package org.apache.xalan.transformer;

import org.apache.xalan.templates.Stylesheet;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.utils.XMLString;

import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

import org.apache.xpath.XPathContext;
import org.apache.xalan.res.XSLTErrorResources;

/**
 * <meta name="usage" content="internal"/>
 * Class used to clone a node, possibly including its children to 
 * a result tree.
 */
public class ClonerToResultTree
{

  /** Result tree handler for the cloned tree           */
  private ResultTreeHandler m_rth;

  /** Transformer instance to use for cloning          */
  private TransformerImpl m_transformer;

  /**
   * Constructor ClonerToResultTree
   *
   *
   * @param transformer non-null transformer instance to use for the cloning
   * @param rth non-null result tree handler for the cloned tree
   */
  public ClonerToResultTree(TransformerImpl transformer,
                            ResultTreeHandler rth)
  {
    m_rth = rth;
    m_transformer = transformer;
  }

  
  /**
   * Clone an element with or without children.
   * TODO: Fix or figure out node clone failure!
   * the error condition is severe enough to halt processing.
   *
   * @param node The node to clone
   * @param shouldCloneAttributes Flag indicating whether to 
   * clone children attributes
   * 
   * @throws TransformerException
   */
  public static void cloneToResultTree(int node, int nodeType, DTM dtm, 
                                             ResultTreeHandler rth,
                                             boolean shouldCloneAttributes)
    throws TransformerException
  {

    try
    {
      switch (nodeType)
      {
      case DTM.TEXT_NODE :
        dtm.dispatchCharactersEvents(node, rth, false);
        break;
      case DTM.DOCUMENT_FRAGMENT_NODE :
      case DTM.DOCUMENT_NODE :
        break;
      case DTM.ELEMENT_NODE :
        {
          String ns = dtm.getNamespaceURI(node);
          if (ns==null) ns="";
          String localName = dtm.getLocalName(node);
          rth.startElement(ns, localName, dtm.getNodeNameX(node), null);
          
          if (shouldCloneAttributes)
          {
            rth.addAttributes(node);
            rth.processNSDecls(node, nodeType, dtm);
          }
        }
        break;
      case DTM.CDATA_SECTION_NODE :
        rth.startCDATA();          
        dtm.dispatchCharactersEvents(node, rth, false);
        rth.endCDATA();
        break;
      case DTM.ATTRIBUTE_NODE :
        rth.addAttribute(node);
        break;
			case DTM.NAMESPACE_NODE:
  			rth.processNSDecls(node,DTM.NAMESPACE_NODE,dtm);
				break;
      case DTM.COMMENT_NODE :
        XMLString xstr = dtm.getStringValue (node);
        xstr.dispatchAsComment(rth);
        break;
      case DTM.ENTITY_REFERENCE_NODE :
        rth.entityReference(dtm.getNodeNameX(node));
        break;
      case DTM.PROCESSING_INSTRUCTION_NODE :
        {
          rth.processingInstruction(dtm.getNodeNameX(node), 
                                      dtm.getNodeValue(node));
        }
        break;
      default :
        throw new  TransformerException(
                         "Can't clone node: "+dtm.getNodeName(node));
      }
    }
    catch(org.xml.sax.SAXException se)
    {
      throw new TransformerException(se);
    }
}
