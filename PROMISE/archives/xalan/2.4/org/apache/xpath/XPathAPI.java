package org.apache.xpath;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.NodeList;

import org.apache.xpath.XPathContext;
import org.apache.xpath.XPath;
import org.apache.xpath.compiler.XPathParser;
import org.apache.xpath.XPathContext;
import org.apache.xml.utils.PrefixResolverDefault;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.objects.XObject;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xml.dtm.ref.DTMManagerDefault;

/**
 * The methods in this class are convenience methods into the
 * low-level XPath API.
 * These functions tend to be a little slow, since a number of objects must be
 * created for each evaluation.  A faster way is to precompile the
 * XPaths using the low-level API, and then just use the XPaths
 * over and over.
 *
 * NOTE: In particular, each call to this method will create a new
 * XPathContext, a new DTMManager... and thus a new DTM. That's very
 * safe, since it guarantees that you're always processing against a
 * fully up-to-date view of your document. But it's also portentially
 * very expensive, since you're rebuilding the DTM every time. You should
 * consider using an instance of CachedXPathAPI rather than these static
 * methods.
 *
 * */
public class XPathAPI
{

  /**
   * Use an XPath string to select a single node. XPath namespace
   * prefixes are resolved from the context node, which may not
   * be what you want (see the next method).
   *
   * @param contextNode The node to start searching from.
   * @param str A valid XPath string.
   * @return The first node found that matches the XPath, or null.
   *
   * @throws TransformerException
   */
  public static Node selectSingleNode(Node contextNode, String str)
          throws TransformerException
  {
    return selectSingleNode(contextNode, str, contextNode);
  }

  /**
   * Use an XPath string to select a single node.
   * XPath namespace prefixes are resolved from the namespaceNode.
   *
   * @param contextNode The node to start searching from.
   * @param str A valid XPath string.
   * @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   * @return The first node found that matches the XPath, or null.
   *
   * @throws TransformerException
   */
  public static Node selectSingleNode(
          Node contextNode, String str, Node namespaceNode)
            throws TransformerException
  {

    NodeIterator nl = selectNodeIterator(contextNode, str, namespaceNode);

    return nl.nextNode();
  }

  /**
   *  Use an XPath string to select a nodelist.
   *  XPath namespace prefixes are resolved from the contextNode.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @return A NodeIterator, should never be null.
   *
   * @throws TransformerException
   */
  public static NodeIterator selectNodeIterator(Node contextNode, String str)
          throws TransformerException
  {
    return selectNodeIterator(contextNode, str, contextNode);
  }

  /**
   *  Use an XPath string to select a nodelist.
   *  XPath namespace prefixes are resolved from the namespaceNode.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   *  @return A NodeIterator, should never be null.
   *
   * @throws TransformerException
   */
  public static NodeIterator selectNodeIterator(
          Node contextNode, String str, Node namespaceNode)
            throws TransformerException
  {

    XObject list = eval(contextNode, str, namespaceNode);

    return list.nodeset();
  }

  /**
   *  Use an XPath string to select a nodelist.
   *  XPath namespace prefixes are resolved from the contextNode.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @return A NodeIterator, should never be null.
   *
   * @throws TransformerException
   */
  public static NodeList selectNodeList(Node contextNode, String str)
          throws TransformerException
  {
    return selectNodeList(contextNode, str, contextNode);
  }

  /**
   *  Use an XPath string to select a nodelist.
   *  XPath namespace prefixes are resolved from the namespaceNode.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   *  @return A NodeIterator, should never be null.
   *
   * @throws TransformerException
   */
  public static NodeList selectNodeList(
          Node contextNode, String str, Node namespaceNode)
            throws TransformerException
  {

    XObject list = eval(contextNode, str, namespaceNode);

    return list.nodelist();
  }

  /**
   *  Evaluate XPath string to an XObject.  Using this method,
   *  XPath namespace prefixes will be resolved from the namespaceNode.
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   *  @return An XObject, which can be used to obtain a string, number, nodelist, etc, should never be null.
   *  @see org.apache.xpath.objects.XObject
   *  @see org.apache.xpath.objects.XNull
   *  @see org.apache.xpath.objects.XBoolean
   *  @see org.apache.xpath.objects.XNumber
   *  @see org.apache.xpath.objects.XString
   *  @see org.apache.xpath.objects.XRTreeFrag
   *
   * @throws TransformerException
   */
  public static XObject eval(Node contextNode, String str)
          throws TransformerException
  {
    return eval(contextNode, str, contextNode);
  }

  /**
   *  Evaluate XPath string to an XObject.
   *  XPath namespace prefixes are resolved from the namespaceNode.
   *  The implementation of this is a little slow, since it creates
   *  a number of objects each time it is called.  This could be optimized
   *  to keep the same objects around, but then thread-safety issues would arise.
   *
   *  @param contextNode The node to start searching from.
   *  @param str A valid XPath string.
   *  @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   *  @return An XObject, which can be used to obtain a string, number, nodelist, etc, should never be null.
   *  @see org.apache.xpath.objects.XObject
   *  @see org.apache.xpath.objects.XNull
   *  @see org.apache.xpath.objects.XBoolean
   *  @see org.apache.xpath.objects.XNumber
   *  @see org.apache.xpath.objects.XString
   *  @see org.apache.xpath.objects.XRTreeFrag
   *
   * @throws TransformerException
   */
  public static XObject eval(Node contextNode, String str, Node namespaceNode)
          throws TransformerException
  {

    XPathContext xpathSupport = new XPathContext();

    PrefixResolverDefault prefixResolver = new PrefixResolverDefault(
      (namespaceNode.getNodeType() == Node.DOCUMENT_NODE)
      ? ((Document) namespaceNode).getDocumentElement() : namespaceNode);

    XPath xpath = new XPath(str, null, prefixResolver, XPath.SELECT, null);

    int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);

    return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
  }

  /**
   *   Evaluate XPath string to an XObject.
   *   XPath namespace prefixes are resolved from the namespaceNode.
   *   The implementation of this is a little slow, since it creates
   *   a number of objects each time it is called.  This could be optimized
   *   to keep the same objects around, but then thread-safety issues would arise.
   *
   *   @param contextNode The node to start searching from.
   *   @param str A valid XPath string.
   *   @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
   *   @param prefixResolver Will be called if the parser encounters namespace
   *                         prefixes, to resolve the prefixes to URLs.
   *   @return An XObject, which can be used to obtain a string, number, nodelist, etc, should never be null.
   *   @see org.apache.xpath.objects.XObject
   *   @see org.apache.xpath.objects.XNull
   *   @see org.apache.xpath.objects.XBoolean
   *   @see org.apache.xpath.objects.XNumber
   *   @see org.apache.xpath.objects.XString
   *   @see org.apache.xpath.objects.XRTreeFrag
   *
   * @throws TransformerException
   */
  public static XObject eval(
          Node contextNode, String str, PrefixResolver prefixResolver)
            throws TransformerException
  {

    XPath xpath = new XPath(str, null, prefixResolver, XPath.SELECT, null);

    XPathContext xpathSupport = new XPathContext();
    int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);

    return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
  }
}
