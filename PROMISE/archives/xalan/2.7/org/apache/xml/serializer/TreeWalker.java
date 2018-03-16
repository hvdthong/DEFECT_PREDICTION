package org.apache.xml.serializer;

import java.io.File;

import org.apache.xml.serializer.utils.AttList;
import org.apache.xml.serializer.utils.DOM2Helper;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.LocatorImpl;

/**
 * This class does a pre-order walk of the DOM tree, calling a ContentHandler
 * interface as it goes.
 * 
 * This class is a copy of the one in org.apache.xml.utils. 
 * It exists to cut the serializers dependancy on that package.
 *  
 * @xsl.usage internal
 */

public final class TreeWalker
{

  /** Local reference to a ContentHandler          */
  final private ContentHandler m_contentHandler;
  /** 
   * If m_contentHandler is a SerializationHandler, then this is 
   * a reference to the same object. 
   */
  final private SerializationHandler m_Serializer;


  /** DomHelper for this TreeWalker          */
  final protected DOM2Helper m_dh;
        
  /** Locator object for this TreeWalker          */
  final private LocatorImpl m_locator = new LocatorImpl();

  /**
   * Get the ContentHandler used for the tree walk.
   *
   * @return the ContentHandler used for the tree walk
   */
  public ContentHandler getContentHandler()
  {
    return m_contentHandler;
  }
  
  public TreeWalker(ContentHandler ch) {
      this(ch,null);
  }
  /**
   * Constructor.
   * @param   contentHandler The implemention of the
   * contentHandler operation (toXMLString, digest, ...)
   */
  public TreeWalker(ContentHandler contentHandler, String systemId)
  {
      m_contentHandler = contentHandler;
      if (m_contentHandler instanceof SerializationHandler) {
          m_Serializer = (SerializationHandler) m_contentHandler;
      }
      else
          m_Serializer = null;
          
      m_contentHandler.setDocumentLocator(m_locator);
      if (systemId != null)
          m_locator.setSystemId(systemId);
      else {
          try {
            m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
           }
           }
      }
          
                if (m_contentHandler != null)
                        m_contentHandler.setDocumentLocator(m_locator);
                try {
                  m_locator.setSystemId(System.getProperty("user.dir") + File.separator + "dummy.xsl");
                } 
                  
    }
    m_dh = new DOM2Helper();
  }

  /**
   * Perform a pre-order traversal non-recursive style.  
   *
   * Note that TreeWalker assumes that the subtree is intended to represent 
   * a complete (though not necessarily well-formed) document and, during a 
   * traversal, startDocument and endDocument will always be issued to the 
   * SAX listener.
   *  
   * @param pos Node in the tree where to start traversal
   *
   * @throws TransformerException
   */
  public void traverse(Node pos) throws org.xml.sax.SAXException
  {

    this.m_contentHandler.startDocument();

    Node top = pos;

    while (null != pos)
    {
      startNode(pos);

      Node nextNode = pos.getFirstChild();

      while (null == nextNode)
      {
        endNode(pos);

        if (top.equals(pos))
          break;

        nextNode = pos.getNextSibling();

        if (null == nextNode)
        {
          pos = pos.getParentNode();

          if ((null == pos) || (top.equals(pos)))
          {
            if (null != pos)
              endNode(pos);

            nextNode = null;

            break;
          }
        }
      }

      pos = nextNode;
    }
    this.m_contentHandler.endDocument();
  }

  /**
   * Perform a pre-order traversal non-recursive style.

   * Note that TreeWalker assumes that the subtree is intended to represent 
   * a complete (though not necessarily well-formed) document and, during a 
   * traversal, startDocument and endDocument will always be issued to the 
   * SAX listener.
   *
   * @param pos Node in the tree where to start traversal
   * @param top Node in the tree where to end traversal
   *
   * @throws TransformerException
   */
  public void traverse(Node pos, Node top) throws org.xml.sax.SAXException
  {

    this.m_contentHandler.startDocument();
    
    while (null != pos)
    {
      startNode(pos);

      Node nextNode = pos.getFirstChild();

      while (null == nextNode)
      {
        endNode(pos);

        if ((null != top) && top.equals(pos))
          break;

        nextNode = pos.getNextSibling();

        if (null == nextNode)
        {
          pos = pos.getParentNode();

          if ((null == pos) || ((null != top) && top.equals(pos)))
          {
            nextNode = null;

            break;
          }
        }
      }

      pos = nextNode;
    }
    this.m_contentHandler.endDocument();
  }

  /** Flag indicating whether following text to be processed is raw text          */
  boolean nextIsRaw = false;
  
  /**
   * Optimized dispatch of characters.
   */
  private final void dispatachChars(Node node)
     throws org.xml.sax.SAXException
  {
    if(m_Serializer != null)
    {
      this.m_Serializer.characters(node);
    }
    else
    {
      String data = ((Text) node).getData();
      this.m_contentHandler.characters(data.toCharArray(), 0, data.length());
    }
  }

  /**
   * Start processing given node
   *
   *
   * @param node Node to process
   *
   * @throws org.xml.sax.SAXException
   */
  protected void startNode(Node node) throws org.xml.sax.SAXException
  {

                
                if (node instanceof Locator)
                {
                        Locator loc = (Locator)node;
                        m_locator.setColumnNumber(loc.getColumnNumber());
                        m_locator.setLineNumber(loc.getLineNumber());
                        m_locator.setPublicId(loc.getPublicId());
                        m_locator.setSystemId(loc.getSystemId());
                }
                else
                {
                        m_locator.setColumnNumber(0);
      m_locator.setLineNumber(0);
                }

    switch (node.getNodeType())
    {
    case Node.COMMENT_NODE :
    {
      String data = ((Comment) node).getData();

      if (m_contentHandler instanceof LexicalHandler)
      {
        LexicalHandler lh = ((LexicalHandler) this.m_contentHandler);

        lh.comment(data.toCharArray(), 0, data.length());
      }
    }
    break;
    case Node.DOCUMENT_FRAGMENT_NODE :

      break;
    case Node.DOCUMENT_NODE :
    
      break;
    case Node.ELEMENT_NODE :
      Element elem_node = (Element) node;
      {
          String uri = elem_node.getNamespaceURI();
          if (uri != null) {
              String prefix = elem_node.getPrefix();
              if (prefix==null)
                prefix="";
              this.m_contentHandler.startPrefixMapping(prefix,uri);              
          }
      }
      NamedNodeMap atts = elem_node.getAttributes();
      int nAttrs = atts.getLength();

      
      for (int i = 0; i < nAttrs; i++)
      {
        final Node attr = atts.item(i);
        final String attrName = attr.getNodeName();
        final int colon = attrName.indexOf(':');
        final String prefix;

        if (attrName.equals("xmlns") || attrName.startsWith("xmlns:"))
        {
          if (colon < 0)
            prefix = "";
          else
            prefix = attrName.substring(colon + 1);

          this.m_contentHandler.startPrefixMapping(prefix,
                                                   attr.getNodeValue());
        }
        else if (colon > 0) {
            prefix = attrName.substring(0,colon);
            String uri = attr.getNamespaceURI();
            if (uri != null)
                this.m_contentHandler.startPrefixMapping(prefix,uri);
        }        
      }

      String ns = m_dh.getNamespaceOfNode(node);
      if(null == ns)
        ns = "";
      this.m_contentHandler.startElement(ns,
                                         m_dh.getLocalNameOfNode(node),
                                         node.getNodeName(),
                                         new AttList(atts, m_dh));
      break;
    case Node.PROCESSING_INSTRUCTION_NODE :
    {
      ProcessingInstruction pi = (ProcessingInstruction) node;
      String name = pi.getNodeName();

      if (name.equals("xslt-next-is-raw"))
      {
        nextIsRaw = true;
      }
      else
      {
        this.m_contentHandler.processingInstruction(pi.getNodeName(),
                                                    pi.getData());
      }
    }
    break;
    case Node.CDATA_SECTION_NODE :
    {
      boolean isLexH = (m_contentHandler instanceof LexicalHandler);
      LexicalHandler lh = isLexH
                          ? ((LexicalHandler) this.m_contentHandler) : null;

      if (isLexH)
      {
        lh.startCDATA();
      }
      
      dispatachChars(node);

      {
        if (isLexH)
        {
          lh.endCDATA();
        }
      }
    }
    break;
    case Node.TEXT_NODE :
    {

      if (nextIsRaw)
      {
        nextIsRaw = false;

        m_contentHandler.processingInstruction(javax.xml.transform.Result.PI_DISABLE_OUTPUT_ESCAPING, "");
        dispatachChars(node);
        m_contentHandler.processingInstruction(javax.xml.transform.Result.PI_ENABLE_OUTPUT_ESCAPING, "");
      }
      else
      {
        dispatachChars(node);
      }
    }
    break;
    case Node.ENTITY_REFERENCE_NODE :
    {
      EntityReference eref = (EntityReference) node;

      if (m_contentHandler instanceof LexicalHandler)
      {
        ((LexicalHandler) this.m_contentHandler).startEntity(
          eref.getNodeName());
      }
      else
      {

      }
    }
    break;
    default :
    }
  }

  /**
   * End processing of given node 
   *
   *
   * @param node Node we just finished processing
   *
   * @throws org.xml.sax.SAXException
   */
  protected void endNode(Node node) throws org.xml.sax.SAXException
  {

    switch (node.getNodeType())
    {
    case Node.DOCUMENT_NODE :
      break;
      
    case Node.ELEMENT_NODE :
      String ns = m_dh.getNamespaceOfNode(node);
      if(null == ns)
        ns = "";
      this.m_contentHandler.endElement(ns,
                                         m_dh.getLocalNameOfNode(node),
                                         node.getNodeName());

      if (m_Serializer == null) {
      Element elem_node = (Element) node;    
      NamedNodeMap atts = elem_node.getAttributes();
      int nAttrs = atts.getLength();

      for (int i = (nAttrs-1); 0 <= i; i--)
      {
        final Node attr = atts.item(i);
        final String attrName = attr.getNodeName();
        final int colon = attrName.indexOf(':');
        final String prefix;

        if (attrName.equals("xmlns") || attrName.startsWith("xmlns:"))
        {
          if (colon < 0)
            prefix = "";
          else
            prefix = attrName.substring(colon + 1);

          this.m_contentHandler.endPrefixMapping(prefix);
        }
        else if (colon > 0) {
            prefix = attrName.substring(0, colon);
            this.m_contentHandler.endPrefixMapping(prefix);
        }
      }
      {
          String uri = elem_node.getNamespaceURI();
          if (uri != null) {
              String prefix = elem_node.getPrefix();
              if (prefix==null)
                prefix="";
              this.m_contentHandler.endPrefixMapping(prefix);              
          }
      }
      }
      break;
    case Node.CDATA_SECTION_NODE :
      break;
    case Node.ENTITY_REFERENCE_NODE :
    {
      EntityReference eref = (EntityReference) node;

      if (m_contentHandler instanceof LexicalHandler)
      {
        LexicalHandler lh = ((LexicalHandler) this.m_contentHandler);

        lh.endEntity(eref.getNodeName());
      }
    }
    break;
    default :
    }
  }

