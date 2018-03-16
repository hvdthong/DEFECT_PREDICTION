package org.apache.xalan.transformer;

import java.util.Vector;

import javax.xml.transform.TransformerException;

import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.templates.KeyDeclaration;
import org.apache.xml.dtm.Axis;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPath;
import org.apache.xpath.axes.OneStepIteratorForward;

/**
 * <meta name="usage" content="internal"/>
 * This class implements an optimized iterator for 
 * "key()" patterns, matching each node to the 
 * match attribute in one or more xsl:key declarations.
 */
public class KeyIterator extends OneStepIteratorForward
{

  /** Key name.
   *  @serial           */
  private QName m_name;

  /**
   * Get the key name from a key declaration this iterator will process
   *
   *
   * @return Key name
   */
  public QName getName()
  {
    return m_name;
  }

  /** Vector of Key declarations in the stylesheet.
   *  @serial          */
  private Vector m_keyDeclarations;

  /**
   * Get the key declarations from the stylesheet 
   *
   *
   * @return Vector containing the key declarations from the stylesheet
   */
  public Vector getKeyDeclarations()
  {
    return m_keyDeclarations;
  }

  /**
    * Create a KeyIterator object.
    *
    * @param compiler A reference to the Compiler that contains the op map.
    * @param opPos The position within the op map, which contains the
    * location path expression for this itterator.
    *
    * @throws javax.xml.transform.TransformerException
    */
  KeyIterator(QName name, Vector keyDeclarations)
  {
    super(Axis.ALL);
    m_keyDeclarations = keyDeclarations;
    m_name = name;
  }

  /**
   *  Test whether a specified node is visible in the logical view of a
   * TreeWalker or NodeIterator. This function will be called by the
   * implementation of TreeWalker and NodeIterator; it is not intended to
   * be called directly from user code.
   * 
   * @param testnode  The node to check to see if it passes the filter or not.
   *
   * @return  a constant to determine whether the node is accepted,
   *   rejected, or skipped, as defined  above .
   */
  public short acceptNode(int testNode)
  {
    boolean foundKey = false;
    KeyIterator ki = (KeyIterator) m_lpi;
    org.apache.xpath.XPathContext xctxt = ki.getXPathContext();
    Vector keys = ki.getKeyDeclarations();

    QName name = ki.getName();
    try
    {
      int nDeclarations = keys.size();

      for (int i = 0; i < nDeclarations; i++)
      {
        KeyDeclaration kd = (KeyDeclaration) keys.elementAt(i);

        if (!kd.getName().equals(name))
          continue;

        foundKey = true;

        XPath matchExpr = kd.getMatch();
        double score = matchExpr.getMatchScore(xctxt, testNode);

        if (score == kd.getMatch().MATCH_SCORE_NONE)
          continue;

        return DTMIterator.FILTER_ACCEPT;

    }
    catch (TransformerException se)
    {

    }

    if (!foundKey)
      throw new RuntimeException(
        XSLMessages.createMessage(
          XSLTErrorResources.ER_NO_XSLKEY_DECLARATION,
          new Object[] { name.getLocalName()}));
          
    return DTMIterator.FILTER_REJECT;
  }

}
