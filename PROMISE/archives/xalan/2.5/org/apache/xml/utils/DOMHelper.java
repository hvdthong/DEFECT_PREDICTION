package org.apache.xml.utils;

import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.dtm.ref.DTMNodeProxy;
import org.apache.xml.res.XMLErrorResources;
import org.apache.xml.res.XMLMessages;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * @deprecated Since the introduction of the DTM, this class will be removed.
 * This class provides a front-end to DOM implementations, providing
 * a number of utility functions that either aren't yet standardized
 * by the DOM spec or that are defined in optional DOM modules and
 * hence may not be present in all DOMs.
 */
public class DOMHelper
{

  /**
   * DOM Level 1 did not have a standard mechanism for creating a new
   * Document object. This function provides a DOM-implementation-independent
   * abstraction for that for that concept. It's typically used when 
   * outputting a new DOM as the result of an operation.
   * <p>
   * TODO: This isn't directly compatable with DOM Level 2. 
   * The Level 2 createDocument call also creates the root 
   * element, and thus requires that you know what that element will be
   * before creating the Document. We should think about whether we want
   * to change this code, and the callers, so we can use the DOM's own 
   * method. (It's also possible that DOM Level 3 may relax this
   * sequence, but you may give up some intelligence in the DOM by
   * doing so; the intent was that knowing the document type and root
   * element might let the DOM automatically switch to a specialized
   * subclass for particular kinds of documents.)
   *
   * @return The newly created DOM Document object, with no children, or
   * null if we can't find a DOM implementation that permits creating
   * new empty Documents.
   */
  public static Document createDocument()
  {

    try
    {

      DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();

      dfactory.setNamespaceAware(true);
      dfactory.setValidating(true);

      DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
      Document outNode = docBuilder.newDocument();

      return outNode;
    }
    catch (ParserConfigurationException pce)
    {
      throw new RuntimeException(
        XMLMessages.createXMLMessage(

    }
  }

  /**
   * <meta name="usage" content="advanced"/>
   * Tells, through the combination of the default-space attribute
   * on xsl:stylesheet, xsl:strip-space, xsl:preserve-space, and the
   * xml:space attribute, whether or not extra whitespace should be stripped
   * from the node.  Literal elements from template elements should
   * <em>not</em> be tested with this function.
   * @param textNode A text node from the source tree.
   * @return true if the text node should be stripped of extra whitespace.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public boolean shouldStripSourceNode(Node textNode)
          throws javax.xml.transform.TransformerException
  {

    return false;
  }

  /**
   * Supports the XPath function GenerateID by returning a unique
   * identifier string for any given DOM Node.
   * <p>
   * Warning: The base implementation uses the Node object's hashCode(),
   * which is NOT guaranteed to be unique. If that method hasn't been
   * overridden in this DOM ipmlementation, most Java implementions will
   * derive it from the object's address and should be OK... but if
   * your DOM uses a different definition of hashCode (eg hashing the
   * contents of the subtree), or if your DOM may have multiple objects
   * that represent a single Node in the data structure (eg via proxying),
   * you may need to find another way to assign a unique identifier.
   * <p>
   * Also, be aware that if nodes are destroyed and recreated, there is
   * an open issue regarding whether an ID may be reused. Currently
   * we're assuming that the input document is stable for the duration
   * of the XPath/XSLT operation, so this shouldn't arise in this context.
   * <p>
   * (DOM Level 3 is investigating providing a unique node "key", but
   * that won't help Level 1 and Level 2 implementations.)
   *
   * @param node whose identifier you want to obtain
   *
   * @return a string which should be different for every Node object.
   */
  public String getUniqueID(Node node)
  {
    return "N" + Integer.toHexString(node.hashCode()).toUpperCase();
  }

  /**
   * Figure out whether node2 should be considered as being later
   * in the document than node1, in Document Order as defined
   * by the XPath model. This may not agree with the ordering defined
   * by other XML applications.
   * <p>
   * There are some cases where ordering isn't defined, and neither are
   * the results of this function -- though we'll generally return true.
   * 
   * TODO: Make sure this does the right thing with attribute nodes!!!
   *
   * @param node1 DOM Node to perform position comparison on.
   * @param node2 DOM Node to perform position comparison on .
   * 
   * @return false if node2 comes before node1, otherwise return true.
   * You can think of this as 
   * <code>(node1.documentOrderPosition &lt;= node2.documentOrderPosition)</code>.
   */
  public static boolean isNodeAfter(Node node1, Node node2)
  {
    if (node1 == node2 || isNodeTheSame(node1, node2))
      return true;

    boolean isNodeAfter = true;
        
    Node parent1 = getParentOfNode(node1);
    Node parent2 = getParentOfNode(node2);          

    {
      if (null != parent1)
        isNodeAfter = isNodeAfterSibling(parent1, node1, node2);
      else
      {
                  
      }
    }
    else
    {

                

      while (parent1 != null)
      {
        nParents1++;

        parent1 = getParentOfNode(parent1);
      }

      while (parent2 != null)
      {
        nParents2++;

        parent2 = getParentOfNode(parent2);
      }

      Node startNode1 = node1, startNode2 = node2;

      if (nParents1 < nParents2)
      {
        int adjust = nParents2 - nParents1;

        for (int i = 0; i < adjust; i++)
        {
          startNode2 = getParentOfNode(startNode2);
        }
      }
      else if (nParents1 > nParents2)
      {
        int adjust = nParents1 - nParents2;

        for (int i = 0; i < adjust; i++)
        {
          startNode1 = getParentOfNode(startNode1);
        }
      }


      while (null != startNode1)
      {
        {
          {

            isNodeAfter = (nParents1 < nParents2) ? true : false;

          }
          else 
          {
            isNodeAfter = isNodeAfterSibling(startNode1, prevChild1,
                                             prevChild2);

          }

        prevChild1 = startNode1;
        startNode1 = getParentOfNode(startNode1);
        prevChild2 = startNode2;
        startNode2 = getParentOfNode(startNode2);
        
        
    /* -- please do not remove... very useful for diagnostics --
    System.out.println("node1 = "+node1.getNodeName()+"("+node1.getNodeType()+")"+
    ", node2 = "+node2.getNodeName()
    +"("+node2.getNodeType()+")"+
    ", isNodeAfter = "+isNodeAfter); */
    return isNodeAfter;

  /**
   * Use DTMNodeProxy to determine whether two nodes are the same.
   * 
   * @param node1 The first DOM node to compare.
   * @param node2 The second DOM node to compare.
   * @return true if the two nodes are the same.
   */
  public static boolean isNodeTheSame(Node node1, Node node2)
  {
    if (node1 instanceof DTMNodeProxy && node2 instanceof DTMNodeProxy)
      return ((DTMNodeProxy)node1).equals((DTMNodeProxy)node2);
    else
      return (node1 == node2);
  }

  /**
   * Figure out if child2 is after child1 in document order.
   * <p>
   * Warning: Some aspects of "document order" are not well defined.
   * For example, the order of attributes is considered
   * meaningless in XML, and the order reported by our model will
   * be consistant for a given invocation but may not 
   * match that of either the source file or the serialized output.
   * 
   * @param parent Must be the parent of both child1 and child2.
   * @param child1 Must be the child of parent and not equal to child2.
   * @param child2 Must be the child of parent and not equal to child1.
   * @return true if child 2 is after child1 in document order.
   */
  private static boolean isNodeAfterSibling(Node parent, Node child1,
                                            Node child2)
  {

    boolean isNodeAfterSibling = false;
    short child1type = child1.getNodeType();
    short child2type = child2.getNodeType();

    if ((Node.ATTRIBUTE_NODE != child1type)
            && (Node.ATTRIBUTE_NODE == child2type))
    {

      isNodeAfterSibling = false;
    }
    else if ((Node.ATTRIBUTE_NODE == child1type)
             && (Node.ATTRIBUTE_NODE != child2type))
    {

      isNodeAfterSibling = true;
    }
    else if (Node.ATTRIBUTE_NODE == child1type)
    {
      NamedNodeMap children = parent.getAttributes();
      int nNodes = children.getLength();
      boolean found1 = false, found2 = false;

      for (int i = 0; i < nNodes; i++)
      {
        Node child = children.item(i);

        if (child1 == child || isNodeTheSame(child1, child))
        {
          if (found2)
          {
            isNodeAfterSibling = false;

            break;
          }

          found1 = true;
        }
        else if (child2 == child || isNodeTheSame(child2, child))
        {
          if (found1)
          {
            isNodeAfterSibling = true;

            break;
          }

          found2 = true;
        }
      }
    }
    else
    {
      Node child = parent.getFirstChild();
      boolean found1 = false, found2 = false;

      while (null != child)
      {

        if (child1 == child || isNodeTheSame(child1, child))
        {
          if (found2)
          {
            isNodeAfterSibling = false;

            break;
          }

          found1 = true;
        }
        else if (child2 == child || isNodeTheSame(child2, child))
        {
          if (found1)
          {
            isNodeAfterSibling = true;

            break;
          }

          found2 = true;
        }

        child = child.getNextSibling();
      }
    }

    return isNodeAfterSibling;


  /**
   * <meta name="usage" content="internal"/>
   * Get the depth level of this node in the tree (equals 1 for
   * a parentless node).
   *
   * @param n Node to be examined.
   * @return the number of ancestors, plus one
   */
  public short getLevel(Node n)
  {

    short level = 1;

    while (null != (n = getParentOfNode(n)))
    {
      level++;
    }

    return level;
  }

  /**
   * Given an XML Namespace prefix and a context in which the prefix
   * is to be evaluated, return the Namespace Name this prefix was 
   * bound to. Note that DOM Level 3 is expected to provide a version of
   * this which deals with the DOM's "early binding" behavior.
   * 
   * Default handling:
   *
   * @param prefix String containing namespace prefix to be resolved, 
   * without the ':' which separates it from the localname when used 
   * in a Node Name. The empty sting signifies the default namespace
   * at this point in the document.
   * @param namespaceContext Element which provides context for resolution.
   * (We could extend this to work for other nodes by first seeking their
   * nearest Element ancestor.)
   *
   * @return a String containing the Namespace URI which this prefix
   * represents in the specified context.
   */
  public String getNamespaceForPrefix(String prefix, Element namespaceContext)
  {

    int type;
    Node parent = namespaceContext;
    String namespace = null;

    if (prefix.equals("xml"))
    {
    }
        else if(prefix.equals("xmlns"))
    {
    }
    else
    {
          String declname=(prefix=="")
                        ? "xmlns"
                        : "xmlns:"+prefix;
                                           
      while ((null != parent) && (null == namespace)
             && (((type = parent.getNodeType()) == Node.ELEMENT_NODE)
                 || (type == Node.ENTITY_REFERENCE_NODE)))
      {
        if (type == Node.ELEMENT_NODE)
        {
                        
                        
                        Attr attr=((Element)parent).getAttributeNode(declname);
                        if(attr!=null)
                        {
                namespace = attr.getNodeValue();
                break;
                        }
                }

        parent = getParentOfNode(parent);
      }
    }

    return namespace;
  }

  /**
   * An experiment for the moment.
   */
  Hashtable m_NSInfos = new Hashtable();

  /** Object to put into the m_NSInfos table that tells that a node has not been 
   *  processed, but has xmlns namespace decls.  */
  protected static final NSInfo m_NSInfoUnProcWithXMLNS = new NSInfo(false,
                                                            true);

  /** Object to put into the m_NSInfos table that tells that a node has not been 
   *  processed, but has no xmlns namespace decls.  */
  protected static final NSInfo m_NSInfoUnProcWithoutXMLNS = new NSInfo(false,
                                                               false);

  /** Object to put into the m_NSInfos table that tells that a node has not been 
   *  processed, and has no xmlns namespace decls, and has no ancestor decls.  */
  protected static final NSInfo m_NSInfoUnProcNoAncestorXMLNS =
    new NSInfo(false, false, NSInfo.ANCESTORNOXMLNS);

  /** Object to put into the m_NSInfos table that tells that a node has been 
   *  processed, and has xmlns namespace decls.  */
  protected static final NSInfo m_NSInfoNullWithXMLNS = new NSInfo(true,
                                                          true);

  /** Object to put into the m_NSInfos table that tells that a node has been 
   *  processed, and has no xmlns namespace decls.  */
  protected static final NSInfo m_NSInfoNullWithoutXMLNS = new NSInfo(true,
                                                             false);

  /** Object to put into the m_NSInfos table that tells that a node has been 
   *  processed, and has no xmlns namespace decls. and has no ancestor decls.  */
  protected static final NSInfo m_NSInfoNullNoAncestorXMLNS =
    new NSInfo(true, false, NSInfo.ANCESTORNOXMLNS);

  /** Vector of node (odd indexes) and NSInfos (even indexes) that tell if 
   *  the given node is a candidate for ancestor namespace processing.  */
  protected Vector m_candidateNoAncestorXMLNS = new Vector();

  /**
   * Returns the namespace of the given node. Differs from simply getting
   * the node's prefix and using getNamespaceForPrefix in that it attempts
   * to cache some of the data in NSINFO objects, to avoid repeated lookup.
   * TODO: Should we consider moving that logic into getNamespaceForPrefix?
   *
   * @param n Node to be examined.
   *
   * @return String containing the Namespace Name (uri) for this node.
   * Note that this is undefined for any nodes other than Elements and
   * Attributes.
   */
  public String getNamespaceOfNode(Node n)
  {

    String namespaceOfPrefix;
    boolean hasProcessedNS;
    NSInfo nsInfo;
    short ntype = n.getNodeType();

    if (Node.ATTRIBUTE_NODE != ntype)
    {

      nsInfo = (nsObj == null) ? null : (NSInfo) nsObj;
      hasProcessedNS = (nsInfo == null) ? false : nsInfo.m_hasProcessedNS;
    }
    else
    {
      hasProcessedNS = false;
      nsInfo = null;
    }

    if (hasProcessedNS)
    {
      namespaceOfPrefix = nsInfo.m_namespace;
    }
    else
    {
      namespaceOfPrefix = null;

      String nodeName = n.getNodeName();
      int indexOfNSSep = nodeName.indexOf(':');
      String prefix;

      if (Node.ATTRIBUTE_NODE == ntype)
      {
        if (indexOfNSSep > 0)
        {
          prefix = nodeName.substring(0, indexOfNSSep);
        }
        else
        {

          return namespaceOfPrefix;
        }
      }
      else
      {
        prefix = (indexOfNSSep >= 0)
                 ? nodeName.substring(0, indexOfNSSep) : "";
      }

      boolean ancestorsHaveXMLNS = false;
      boolean nHasXMLNS = false;

      if (prefix.equals("xml"))
      {
        namespaceOfPrefix = QName.S_XMLNAMESPACEURI;
      }
      else
      {
        int parentType;
        Node parent = n;

        while ((null != parent) && (null == namespaceOfPrefix))
        {
          if ((null != nsInfo)
                  && (nsInfo.m_ancestorHasXMLNSAttrs
                      == NSInfo.ANCESTORNOXMLNS))
          {
            break;
          }

          parentType = parent.getNodeType();

          if ((null == nsInfo) || nsInfo.m_hasXMLNSAttrs)
          {
            boolean elementHasXMLNS = false;

            if (parentType == Node.ELEMENT_NODE)
            {
              NamedNodeMap nnm = parent.getAttributes();

              for (int i = 0; i < nnm.getLength(); i++)
              {
                Node attr = nnm.item(i);
                String aname = attr.getNodeName();

                if (aname.charAt(0) == 'x')
                {
                  boolean isPrefix = aname.startsWith("xmlns:");

                  if (aname.equals("xmlns") || isPrefix)
                  {
                    if (n == parent)
                      nHasXMLNS = true;

                    elementHasXMLNS = true;
                    ancestorsHaveXMLNS = true;

                    String p = isPrefix ? aname.substring(6) : "";

                    if (p.equals(prefix))
                    {
                      namespaceOfPrefix = attr.getNodeValue();

                      break;
                    }
                  }
                }
              }
            }

            if ((Node.ATTRIBUTE_NODE != parentType) && (null == nsInfo)
                    && (n != parent))
            {
              nsInfo = elementHasXMLNS
                       ? m_NSInfoUnProcWithXMLNS : m_NSInfoUnProcWithoutXMLNS;

              m_NSInfos.put(parent, nsInfo);
            }
          }

          if (Node.ATTRIBUTE_NODE == parentType)
          {
            parent = getParentOfNode(parent);
          }
          else
          {
            m_candidateNoAncestorXMLNS.addElement(parent);
            m_candidateNoAncestorXMLNS.addElement(nsInfo);

            parent = parent.getParentNode();
          }

          if (null != parent)
          {

            nsInfo = (nsObj == null) ? null : (NSInfo) nsObj;
          }
        }

        int nCandidates = m_candidateNoAncestorXMLNS.size();

        if (nCandidates > 0)
        {
          if ((false == ancestorsHaveXMLNS) && (null == parent))
          {
            for (int i = 0; i < nCandidates; i += 2)
            {
              Object candidateInfo = m_candidateNoAncestorXMLNS.elementAt(i
                                       + 1);

              if (candidateInfo == m_NSInfoUnProcWithoutXMLNS)
              {
                m_NSInfos.put(m_candidateNoAncestorXMLNS.elementAt(i),
                              m_NSInfoUnProcNoAncestorXMLNS);
              }
              else if (candidateInfo == m_NSInfoNullWithoutXMLNS)
              {
                m_NSInfos.put(m_candidateNoAncestorXMLNS.elementAt(i),
                              m_NSInfoNullNoAncestorXMLNS);
              }
            }
          }

          m_candidateNoAncestorXMLNS.removeAllElements();
        }
      }

      if (Node.ATTRIBUTE_NODE != ntype)
      {
        if (null == namespaceOfPrefix)
        {
          if (ancestorsHaveXMLNS)
          {
            if (nHasXMLNS)
              m_NSInfos.put(n, m_NSInfoNullWithXMLNS);
            else
              m_NSInfos.put(n, m_NSInfoNullWithoutXMLNS);
          }
          else
          {
            m_NSInfos.put(n, m_NSInfoNullNoAncestorXMLNS);
          }
        }
        else
        {
          m_NSInfos.put(n, new NSInfo(namespaceOfPrefix, nHasXMLNS));
        }
      }
    }

    return namespaceOfPrefix;
  }

  /**
   * Returns the local name of the given node. If the node's name begins
   * with a namespace prefix, this is the part after the colon; otherwise
   * it's the full node name.
   *
   * @param n the node to be examined.
   *
   * @return String containing the Local Name
   */
  public String getLocalNameOfNode(Node n)
  {

    String qname = n.getNodeName();
    int index = qname.indexOf(':');

    return (index < 0) ? qname : qname.substring(index + 1);
  }

  /**
   * Returns the element name with the namespace prefix (if any) replaced
   * by the Namespace URI it was bound to. This is not a standard 
   * representation of a node name, but it allows convenient 
   * single-string comparison of the "universal" names of two nodes.
   *
   * @param elem Element to be examined.
   *
   * @return String in the form "namespaceURI:localname" if the node
   * belongs to a namespace, or simply "localname" if it doesn't.
   * @see #getExpandedAttributeName
   */
  public String getExpandedElementName(Element elem)
  {

    String namespace = getNamespaceOfNode(elem);

    return (null != namespace)
           ? namespace + ":" + getLocalNameOfNode(elem)
           : getLocalNameOfNode(elem);
  }

  /**
   * Returns the attribute name with the namespace prefix (if any) replaced
   * by the Namespace URI it was bound to. This is not a standard 
   * representation of a node name, but it allows convenient 
   * single-string comparison of the "universal" names of two nodes.
   *
   * @param attr Attr to be examined
   *
   * @return String in the form "namespaceURI:localname" if the node
   * belongs to a namespace, or simply "localname" if it doesn't.
   * @see #getExpandedElementName
   */
  public String getExpandedAttributeName(Attr attr)
  {

    String namespace = getNamespaceOfNode(attr);

    return (null != namespace)
           ? namespace + ":" + getLocalNameOfNode(attr)
           : getLocalNameOfNode(attr);
  }


  /**
   * Tell if the node is ignorable whitespace. Note that this can
   * be determined only in the context of a DTD or other Schema,
   * and that DOM Level 2 has nostandardized DOM API which can
   * return that information.
   * @deprecated
   *
   * @param node Node to be examined
   *
   * @return CURRENTLY HARDCODED TO FALSE, but should return true if
   * and only if the node is of type Text, contains only whitespace,
   * and does not appear as part of the #PCDATA content of an element.
   * (Note that determining this last may require allowing for 
   * Entity References.)
   */
  public boolean isIgnorableWhitespace(Text node)
  {


    return isIgnorable;
  }

  /**
   * Get the first unparented node in the ancestor chain.
   * @deprecated
   *
   * @param node Starting node, to specify which chain to chase
   *
   * @return the topmost ancestor.
   */
  public Node getRoot(Node node)
  {

    Node root = null;

    while (node != null)
    {
      root = node;
      node = getParentOfNode(node);
    }

    return root;
  }

  /**
   * Get the root node of the document tree, regardless of
   * whether or not the node passed in is a document node.
   * <p>
   * TODO: This doesn't handle DocumentFragments or "orphaned" subtrees
   * -- it's currently returning ownerDocument even when the tree is
   * not actually part of the main Document tree. We should either
   * rewrite the description to say that it finds the Document node,
   * or change the code to walk up the ancestor chain.

   *
   * @param n Node to be examined
   *
   * @return the Document node. Note that this is not the correct answer
   * if n was (or was a child of) a DocumentFragment or an orphaned node,
   * as can arise if the DOM has been edited rather than being generated
   * by a parser.
   */
  public Node getRootNode(Node n)
  {
    int nt = n.getNodeType();
    return ( (Node.DOCUMENT_NODE == nt) || (Node.DOCUMENT_FRAGMENT_NODE == nt) ) 
           ? n : n.getOwnerDocument();
  }

  /**
   * Test whether the given node is a namespace decl node. In DOM Level 2
   * this can be done in a namespace-aware manner, but in Level 1 DOMs
   * it has to be done by testing the node name.
   *
   * @param n Node to be examined.
   *
   * @return boolean -- true iff the node is an Attr whose name is 
   * "xmlns" or has the "xmlns:" prefix.
   */
  public boolean isNamespaceNode(Node n)
  {

    if (Node.ATTRIBUTE_NODE == n.getNodeType())
    {
      String attrName = n.getNodeName();

      return (attrName.startsWith("xmlns:") || attrName.equals("xmlns"));
    }

    return false;
  }

  /**
   * Obtain the XPath-model parent of a DOM node -- ownerElement for Attrs,
   * parent for other nodes. 
   * <p>
   * Background: The DOM believes that you must be your Parent's
   * Child, and thus Attrs don't have parents. XPath said that Attrs
   * do have their owning Element as their parent. This function
   * bridges the difference, either by using the DOM Level 2 ownerElement
   * function or by using a "silly and expensive function" in Level 1
   * DOMs.
   * <p>
   * (There's some discussion of future DOMs generalizing ownerElement 
   * into ownerNode and making it work on all types of nodes. This
   * still wouldn't help the users of Level 1 or Level 2 DOMs)
   * <p>
   *
   * @param node Node whose XPath parent we want to obtain
   *
   * @return the parent of the node, or the ownerElement if it's an
   * Attr node, or null if the node is an orphan.
   *
   * @throws RuntimeException if the Document has no root element.
   * This can't arise if the Document was created
   * via the DOM Level 2 factory methods, but is possible if other
   * mechanisms were used to obtain it
   */
  public static Node getParentOfNode(Node node) throws RuntimeException
  {
    Node parent;
    short nodeType = node.getNodeType();

    if (Node.ATTRIBUTE_NODE == nodeType)
    {
      Document doc = node.getOwnerDocument();
          /*
      TBD:
      if(null == doc)
      {
      }
      */

          DOMImplementation impl=doc.getImplementation();
          if(impl!=null && impl.hasFeature("Core","2.0"))
          {
                  parent=((Attr)node).getOwnerElement();
                  return parent;
          }


      Element rootElem = doc.getDocumentElement();

      if (null == rootElem)
      {
        throw new RuntimeException(
          XMLMessages.createXMLMessage(
            XMLErrorResources.ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      }

      parent = locateAttrParent(rootElem, node);

        }
    else
    {
      parent = node.getParentNode();

    }

    return parent;
  }

  /**
   * Given an ID, return the element. This can work only if the document
   * is interpreted in the context of a DTD or Schema, since otherwise
   * we don't know which attributes are or aren't IDs.
   * <p>
   * Note that DOM Level 1 had no ability to retrieve this information.
   * DOM Level 2 introduced it but does not promise that it will be
   * supported in all DOMs; those which can't support it will always
   * return null.
   * <p>
   * TODO: getElementByID is currently unimplemented. Support DOM Level 2?
   *
   * @param id The unique identifier to be searched for.
   * @param doc The document to search within.
   * @return CURRENTLY HARDCODED TO NULL, but it should be:
   * The node which has this unique identifier, or null if there
   * is no such node or this DOM can't reliably recognize it.
   */
  public Element getElementByID(String id, Document doc)
  {
    return null;
  }

  /**
   * The getUnparsedEntityURI function returns the URI of the unparsed
   * entity with the specified name in the same document as the context
   * node (see [3.3 Unparsed Entities]). It returns the empty string if
   * there is no such entity.
   * <p>
   * XML processors may choose to use the System Identifier (if one
   * is provided) to resolve the entity, rather than the URI in the
   * Public Identifier. The details are dependent on the processor, and
   * we would have to support some form of plug-in resolver to handle
   * this properly. Currently, we simply return the System Identifier if
   * present, and hope that it a usable URI or that our caller can
   * map it to one.
   * TODO: Resolve Public Identifiers... or consider changing function name.
   * <p>
   * If we find a relative URI 
   * reference, XML expects it to be resolved in terms of the base URI 
   * of the document. The DOM doesn't do that for us, and it isn't 
   * entirely clear whether that should be done here; currently that's
   * pushed up to a higher levelof our application. (Note that DOM Level 
   * 1 didn't store the document's base URI.)
   * TODO: Consider resolving Relative URIs.
   * <p>
   * (The DOM's statement that "An XML processor may choose to
   * completely expand entities before the structure model is passed
   * to the DOM" refers only to parsed entities, not unparsed, and hence
   * doesn't affect this function.)
   *
   * @param name A string containing the Entity Name of the unparsed
   * entity.
   * @param doc Document node for the document to be searched.
   *
   * @return String containing the URI of the Unparsed Entity, or an
   * empty string if no such entity exists.
   */
  public String getUnparsedEntityURI(String name, Document doc)
  {

    String url = "";
    DocumentType doctype = doc.getDoctype();

    if (null != doctype)
    {
      NamedNodeMap entities = doctype.getEntities();
      if(null == entities)
        return url;
      Entity entity = (Entity) entities.getNamedItem(name);
      if(null == entity)
        return url;
      
      String notationName = entity.getNotationName();

      {
        url = entity.getSystemId();

        if (null == url)
        {
          url = entity.getPublicId();
        }
        else
        {
        }        
      }
    }

    return url;
  }

  /**
   * Support for getParentOfNode; walks a DOM tree until it finds
   * the Element which owns the Attr. This is hugely expensive, and
   * if at all possible you should use the DOM Level 2 Attr.ownerElement()
   * method instead.
   *  <p>
   * The DOM Level 1 developers expected that folks would keep track
   * of the last Element they'd seen and could recover the info from
   * that source. Obviously that doesn't work very well if the only
   * information you've been presented with is the Attr. The DOM Level 2
   * getOwnerElement() method fixes that, but only for Level 2 and
   * later DOMs.
   *
   * @param elem Element whose subtree is to be searched for this Attr
   * @param attr Attr whose owner is to be located.
   *
   * @return the first Element whose attribute list includes the provided
   * attr. In modern DOMs, this will also be the only such Element. (Early
   * DOMs had some hope that Attrs might be sharable, but this idea has
   * been abandoned.)
   */
  private static Node locateAttrParent(Element elem, Node attr)
  {

    Node parent = null;

        Attr check=elem.getAttributeNode(attr.getNodeName());
        if(check==attr)
                parent = elem;

    if (null == parent)
    {
      for (Node node = elem.getFirstChild(); null != node;
              node = node.getNextSibling())
      {
        if (Node.ELEMENT_NODE == node.getNodeType())
        {
          parent = locateAttrParent((Element) node, attr);

          if (null != parent)
            break;
        }
      }
    }

    return parent;
  }

  /**
   * The factory object used for creating nodes
   * in the result tree.
   */
  protected Document m_DOMFactory = null;

  /**
   * Store the factory object required to create DOM nodes
   * in the result tree. In fact, that's just the result tree's
   * Document node...
   *
   * @param domFactory The DOM Document Node within whose context
   * the result tree will be built.
   */
  public void setDOMFactory(Document domFactory)
  {
    this.m_DOMFactory = domFactory;
  }

  /**
   * Retrieve the factory object required to create DOM nodes
   * in the result tree.
   *
   * @return The result tree's DOM Document Node.
   */
  public Document getDOMFactory()
  {

    if (null == this.m_DOMFactory)
    {
      this.m_DOMFactory = createDocument();
    }

    return this.m_DOMFactory;
  }

  /**
   * Get the textual contents of the node. See
   * getNodeData(Node,FastStringBuffer) for discussion of how
   * whitespace nodes are handled.
   *
   * @param node DOM Node to be examined
   * @return String containing a concatenation of all the 
   * textual content within that node. 
   * @see #getNodeData(Node,FastStringBuffer)
   * 
   */
  public static String getNodeData(Node node)
  {

    FastStringBuffer buf = StringBufferPool.get();
    String s;

    try
    {
      getNodeData(node, buf);

      s = (buf.length() > 0) ? buf.toString() : "";
    }
    finally
    {
      StringBufferPool.free(buf);
    }

    return s;
  }

  /**
   * Retrieve the text content of a DOM subtree, appending it into a
   * user-supplied FastStringBuffer object. Note that attributes are
   * not considered part of the content of an element.
   * <p>
   * There are open questions regarding whitespace stripping. 
   * Currently we make no special effort in that regard, since the standard
   * DOM doesn't yet provide DTD-based information to distinguish
   * whitespace-in-element-context from genuine #PCDATA. Note that we
   * should probably also consider xml:space if/when we address this.
   * DOM Level 3 may solve the problem for us.
   *
   * @param node Node whose subtree is to be walked, gathering the
   * contents of all Text or CDATASection nodes.
   * @param buf FastStringBuffer into which the contents of the text
   * nodes are to be concatenated.
   */
  public static void getNodeData(Node node, FastStringBuffer buf)
  {

    switch (node.getNodeType())
    {
    case Node.DOCUMENT_FRAGMENT_NODE :
    case Node.DOCUMENT_NODE :
    case Node.ELEMENT_NODE :
    {
      for (Node child = node.getFirstChild(); null != child;
              child = child.getNextSibling())
      {
        getNodeData(child, buf);
      }
    }
    break;
    case Node.TEXT_NODE :
    case Node.CDATA_SECTION_NODE :
      buf.append(node.getNodeValue());
      break;
    case Node.ATTRIBUTE_NODE :
      buf.append(node.getNodeValue());
      break;
    case Node.PROCESSING_INSTRUCTION_NODE :
      break;
    default :
      break;
    }
  }
}
