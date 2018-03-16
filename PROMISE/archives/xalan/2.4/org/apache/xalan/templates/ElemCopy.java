package org.apache.xalan.templates;

import org.apache.xml.dtm.DTM;

import org.xml.sax.*;

import org.apache.xpath.*;

import java.util.*;

import org.apache.xml.utils.QName;
import org.apache.xalan.trace.*;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.transformer.ResultTreeHandler;
import org.apache.xalan.transformer.ClonerToResultTree;

import javax.xml.transform.TransformerException;

/**
 * <meta name="usage" content="advanced"/>
 * Implement xsl:copy.
 * <pre>
 * <!ELEMENT xsl:copy %template;>
 * <!ATTLIST xsl:copy
 *   %space-att;
 *   use-attribute-sets %qnames; #IMPLIED
 * >
 * </pre>
 */
public class ElemCopy extends ElemUse
{

  /**
   * Get an int constant identifying the type of element.
   * @see org.apache.xalan.templates.Constants
   *
   * @return The token ID for this element 
   */
  public int getXSLToken()
  {
    return Constants.ELEMNAME_COPY;
  }

  /**
   * Return the node name.
   *
   * @return This element's name
   */
  public String getNodeName()
  {
    return Constants.ELEMNAME_COPY_STRING;
  }

  /**
   * The xsl:copy element provides an easy way of copying the current node.
   * Executing this function creates a copy of the current node into the
   * result tree.
   * <p>The namespace nodes of the current node are automatically
   * copied as well, but the attributes and children of the node are not
   * automatically copied. The content of the xsl:copy element is a
   * template for the attributes and children of the created node;
   * the content is instantiated only for nodes of types that can have
   * attributes or children (i.e. root nodes and element nodes).</p>
   * <p>The root node is treated specially because the root node of the
   * result tree is created implicitly. When the current node is the
   * root node, xsl:copy will not create a root node, but will just use
   * the content template.</p>
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
      
    try
    {
      int sourceNode = xctxt.getCurrentNode();
      xctxt.pushCurrentNode(sourceNode);
      DTM dtm = xctxt.getDTM(sourceNode);
      short nodeType = dtm.getNodeType(sourceNode);

      if ((DTM.DOCUMENT_NODE != nodeType) && (DTM.DOCUMENT_FRAGMENT_NODE != nodeType))
      {
        ResultTreeHandler rthandler = transformer.getResultTreeHandler();

        if (TransformerImpl.S_DEBUG)
          transformer.getTraceManager().fireTraceEvent(this);
            
        ClonerToResultTree.cloneToResultTree(sourceNode, nodeType, dtm, 
                                             rthandler, false);

        if (DTM.ELEMENT_NODE == nodeType)
        {
          super.execute(transformer);
          rthandler.processNSDecls(sourceNode, nodeType, dtm);
          transformer.executeChildTemplates(this, true);
          
          String ns = dtm.getNamespaceURI(sourceNode);
          String localName = dtm.getLocalName(sourceNode);
          transformer.getResultTreeHandler().endElement(ns, localName,
                                                        dtm.getNodeName(sourceNode));
        }
        if (TransformerImpl.S_DEBUG)
		  transformer.getTraceManager().fireTraceEndEvent(this);         
      }
      else
      {
        if (TransformerImpl.S_DEBUG)
          transformer.getTraceManager().fireTraceEvent(this);

        super.execute(transformer);
        transformer.executeChildTemplates(this, true);

        if (TransformerImpl.S_DEBUG)
          transformer.getTraceManager().fireTraceEndEvent(this);
      }
    }
    catch(org.xml.sax.SAXException se)
    {
      throw new TransformerException(se);
    }
    finally
    {
      xctxt.popCurrentNode();
    }
  }
}
