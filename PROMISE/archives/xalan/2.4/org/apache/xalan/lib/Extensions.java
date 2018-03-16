package org.apache.xalan.lib;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeIterator;

import org.apache.xpath.NodeSet;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.DOMHelper;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.utils.XMLString;

import org.xml.sax.SAXNotSupportedException;

import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.xslt.EnvironmentCheck;

import javax.xml.parsers.*;

/**
 * <meta name="usage" content="general"/>
 * This class contains many of the Xalan-supplied extensions.
 * It is accessed by specifying a namespace URI as follows:
 * <pre>
 * </pre>
 */
public class Extensions
{

  /**
   * Constructor Extensions
   *
   */

  /**
   * This method is an extension that implements as a Xalan extension
   * the node-set function also found in xt and saxon.
   * If the argument is a Result Tree Fragment, then <code>nodeset</code>
   * returns a node-set consisting of a single root node as described in
   * section 11.1 of the XSLT 1.0 Recommendation.  If the argument is a
   * node-set, <code>nodeset</code> returns a node-set.  If the argument
   * is a string, number, or boolean, then <code>nodeset</code> returns
   * a node-set consisting of a single root node with a single text node
   * child that is the result of calling the XPath string() function on the
   * passed parameter.  If the argument is anything else, then a node-set
   * is returned consisting of a single root node with a single text node
   * child that is the result of calling the java <code>toString()</code>
   * method on the passed argument.
   * Most of the
   * actual work here is done in <code>MethodResolver</code> and
   * <code>XRTreeFrag</code>.
   * @param myProcessor Context passed by the extension processor
   * @param rtf Argument in the stylesheet to the nodeset extension function
   *
   * NEEDSDOC ($objectName$) @return
   */
  public static NodeSet nodeset(ExpressionContext myProcessor, Object rtf)
  {

    String textNodeValue;

    if (rtf instanceof NodeIterator)
    {
      return new NodeSet((NodeIterator) rtf);
    }
    else
    {
      if (rtf instanceof String)
      {
        textNodeValue = (String) rtf;
      }
      else if (rtf instanceof Boolean)
      {
        textNodeValue = new XBoolean(((Boolean) rtf).booleanValue()).str();
      }
      else if (rtf instanceof Double)
      {
        textNodeValue = new XNumber(((Double) rtf).doubleValue()).str();
      }
      else
      {
        textNodeValue = rtf.toString();
      }

      try
      {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document myDoc = db.newDocument();
        
        Text textNode = myDoc.createTextNode(textNodeValue);
        DocumentFragment docFrag = myDoc.createDocumentFragment();
  
        docFrag.appendChild(textNode);
  
        return new NodeSet(docFrag);
      }
      catch(ParserConfigurationException pce)
      {
        throw new org.apache.xml.utils.WrappedRuntimeException(pce);
      }
    }
  }

  /**
   * Returns the intersection of two node-sets.
   * 
   * @param ni1 NodeIterator for first node-set
   * @param ni2 NodeIterator for second node-set
   * @return a NodeSet containing the nodes in ni1 that are also
   * in ni2
   *
   * @throws javax.xml.transform.TransformerException
   */
  public static NodeSet intersection(NodeIterator ni1, NodeIterator ni2)
          throws javax.xml.transform.TransformerException
  {

    NodeSet ns1 = new NodeSet(ni1);
    NodeSet ns2 = new NodeSet(ni2);
    NodeSet inter = new NodeSet();

    inter.setShouldCacheNodes(true);

    for (int i = 0; i < ns1.getLength(); i++)
    {
      Node n = ns1.elementAt(i);

      if (ns2.contains(n))
        inter.addElement(n);
    }

    return inter;
  }

  /**
   * Returns the difference between two node-sets.
   * 
   * @param ni1 NodeIterator for first node-set
   * @param ni2 NodeIterator for second node-set
   * @return a NodeSet containing the nodes in ni1 that are not
   * in ni2
   *
   * @throws javax.xml.transform.TransformerException
   */
  public static NodeSet difference(NodeIterator ni1, NodeIterator ni2)
          throws javax.xml.transform.TransformerException
  {

    NodeSet ns1 = new NodeSet(ni1);
    NodeSet ns2 = new NodeSet(ni2);

    NodeSet diff = new NodeSet();

    diff.setShouldCacheNodes(true);

    for (int i = 0; i < ns1.getLength(); i++)
    {
      Node n = ns1.elementAt(i);

      if (!ns2.contains(n))
        diff.addElement(n);
    }

    return diff;
  }

  /**
   * Returns node-set containing distinct string values.
   * @param ni NodeIterator for node-set
   * @return a NodeSet with nodes from ni containing distinct string values.
   * In other words, if more than one node in ni contains the same string value,
   * only include the first such node found.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public static NodeSet distinct(ExpressionContext myContext, NodeIterator ni)
          throws javax.xml.transform.TransformerException
  {


    NodeSet dist = new NodeSet();
    dist.setShouldCacheNodes(true);

    Hashtable stringTable = new Hashtable();

    Node currNode = ni.nextNode();

    while (currNode != null)
    {
      String key = myContext.toString(currNode);

      if (!stringTable.containsKey(key))
      {
        stringTable.put(key, currNode);
        dist.addElement(currNode);
      }
      currNode = ni.nextNode();
    }

    return dist;
  }

  /**
   * Returns true if both node-sets contain the same set of nodes.
   * @param n1 NodeIterator for first node-set
   *
   * NEEDSDOC @param ni1
   * @param ni2 NodeIterator for second node-set
   * @return true if ni1 and ni2 contain exactly the same set of nodes.
   */
  public static boolean hasSameNodes(NodeIterator ni1, NodeIterator ni2)
  {

    NodeSet ns1 = new NodeSet(ni1);
    NodeSet ns2 = new NodeSet(ni2);

    if (ns1.getLength() != ns2.getLength())
      return false;

    for (int i = 0; i < ns1.getLength(); i++)
    {
      Node n = ns1.elementAt(i);

      if (!ns2.contains(n))
        return false;
    }

    return true;
  }

  /**
   * Returns the result of evaluating the argument as a string containing
   * an XPath expression.  Used where the XPath expression is not known until
   * run-time.  The expression is evaluated as if the run-time value of the
   * argument appeared in place of the evaluate function call at compile time.
   * @param myContext an <code>ExpressionContext</code> passed in by the
   *                  extension mechanism.  This must be an XPathContext.
   * @param xpathExtr The XPath expression to be evaluated.
   * NEEDSDOC @param xpathExpr
   * @return the XObject resulting from evaluating the XPath
   *
   * @throws Exception
   * @throws SAXNotSupportedException
   */
  public static XObject evaluate(
          ExpressionContext myContext, String xpathExpr)
            throws SAXNotSupportedException, Exception
  {

    if (myContext instanceof XPathContext.XPathExpressionContext)
    {
      try
      {
        XPathContext xctxt =
                    ((XPathContext.XPathExpressionContext) myContext).getXPathContext();
        XPath dynamicXPath = new XPath(xpathExpr, xctxt.getSAXLocator(),
                                       xctxt.getNamespaceContext(),
                                       XPath.SELECT);

        return dynamicXPath.execute(xctxt, myContext.getContextNode(),
                                    xctxt.getNamespaceContext());
      }
      catch (Exception e)
      {
        throw e;
      }
    }
    else
  }

  /**
   * Returns a NodeSet containing one text node for each token in the first argument.
   * Delimiters are specified in the second argument.
   * Tokens are determined by a call to <code>StringTokenizer</code>.
   * If the first argument is an empty string or contains only delimiters, the result
   * will be an empty NodeSet.
   * Contributed to XalanJ1 by <a href="mailto:benoit.cerrina@writeme.com">Benoit Cerrina</a>.
   * @param myContext an <code>ExpressionContext</code> passed in by the
   *                  extension mechanism.  This must be an XPathContext.
   * @param toTokenize The string to be split into text tokens.
   * @param delims The delimiters to use.
   * @return a NodeSet as described above.
   *
   */
  public static NodeSet tokenize(ExpressionContext myContext,
                                 String toTokenize, String delims)
  {

    Document lDoc;

    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      lDoc = db.newDocument();
    }
    catch(ParserConfigurationException pce)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(pce);
    }

    StringTokenizer lTokenizer = new StringTokenizer(toTokenize, delims);
    NodeSet resultSet = new NodeSet();

    while (lTokenizer.hasMoreTokens())
    {
      resultSet.addNode(lDoc.createTextNode(lTokenizer.nextToken()));
    }

    return resultSet;
  }

  /**
   * Returns a NodeSet containing one text node for each token in the first argument.
   * Delimiters are whitespace.  That is, the delimiters that are used are tab (&#x09),
   * linefeed (&#x0A), return (&#x0D), and space (&#x20).
   * Tokens are determined by a call to <code>StringTokenizer</code>.
   * If the first argument is an empty string or contains only delimiters, the result
   * will be an empty NodeSet.
   * Contributed to XalanJ1 by <a href="mailto:benoit.cerrina@writeme.com">Benoit Cerrina</a>.
   * @param myContext an <code>ExpressionContext</code> passed in by the
   *                  extension mechanism.  This must be an XPathContext.
   * @param toTokenize The string to be split into text tokens.
   * @return a NodeSet as described above.
   *
   */
  public static NodeSet tokenize(ExpressionContext myContext,
                                 String toTokenize)
  {
    return tokenize(myContext, toTokenize, " \t\n\r");
  }

  /**
   * Return a Node of basic debugging information from the 
   * EnvironmentCheck utility about the Java environment.
   *
   * <p>Simply calls the {@link org.apache.xalan.xslt.EnvironmentCheck}
   * utility to grab info about the Java environment and CLASSPATH, 
   * etc., and then returns the resulting Node.  Stylesheets can 
   * then maniuplate this data or simply xsl:copy-of the Node.  Note 
   * that we first attempt to load the more advanced 
   * org.apache.env.Which utility by reflection; only if that fails 
   * to we still use the internal version.  Which is available from 
   *
   * <p>We throw a WrappedRuntimeException in the unlikely case 
   * that reading information from the environment throws us an 
   * exception. (Is this really the best thing to do?)</p>
   *
   * @param myContext an <code>ExpressionContext</code> passed in by the
   *                  extension mechanism.  This must be an XPathContext.
   * @return a Node as described above.
   */
  public static Node checkEnvironment(ExpressionContext myContext)
  {

    Document factoryDocument;
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      factoryDocument = db.newDocument();
    }
    catch(ParserConfigurationException pce)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(pce);
    }

    Node resultNode = null;
    try
    {
      resultNode = checkEnvironmentUsingWhich(myContext, factoryDocument);

      if (null != resultNode)
        return resultNode;

      EnvironmentCheck envChecker = new EnvironmentCheck();
      Hashtable h = envChecker.getEnvironmentHash();
      resultNode = factoryDocument.createElement("checkEnvironmentExtension");
      envChecker.appendEnvironmentReport(resultNode, factoryDocument, h);
      envChecker = null;
    }
    catch(Exception e)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(e);
    }

    return resultNode;
  }

  /**
   * Private worker method to attempt to use org.apache.env.Which.
   *
   * @param myContext an <code>ExpressionContext</code> passed in by the
   *                  extension mechanism.  This must be an XPathContext.
   * @param factoryDocument providing createElement services, etc.
   * @return a Node with environment info; null if any error
   */
  private static Node checkEnvironmentUsingWhich(ExpressionContext myContext, 
        Document factoryDocument)
  {
    final String WHICH_CLASSNAME = "org.apache.env.Which";
    final String WHICH_METHODNAME = "which";
    final Class WHICH_METHOD_ARGS[] = { java.util.Hashtable.class,
                                        java.lang.String.class,
                                        java.lang.String.class };
    try
    {
      Class clazz = Class.forName(WHICH_CLASSNAME);
      if (null == clazz)
        return null;

      java.lang.reflect.Method method = clazz.getMethod(WHICH_METHODNAME, WHICH_METHOD_ARGS);
      Hashtable report = new Hashtable();

      Object[] methodArgs = { report, "XmlCommons;Xalan;Xerces;Crimson;Ant", "" };
      Object returnValue = method.invoke(null, methodArgs);

      Node resultNode = factoryDocument.createElement("checkEnvironmentExtension");
      org.apache.xml.utils.Hashtree2Node.appendHashToNode(report, "whichReport", 
            resultNode, factoryDocument);

      return resultNode;
    }
    catch (Throwable t)
    {
      return null;
    }
  }
}
