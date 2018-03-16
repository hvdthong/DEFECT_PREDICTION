package org.apache.xpath.axes;

import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.patterns.NodeTest;

/**
 * This class defines a simplified type of union iterator that only 
 * tests along the child axes.  If the conditions are right, it is 
 * much faster than using a UnionPathIterator.
 */
public class UnionChildIterator extends ChildTestIterator
{
  /**
   * Even though these may hold full LocPathIterators, this array does 
   * not have to be cloned, since only the node test and predicate 
   * portion are used, and these only need static information.  However, 
   * also note that index predicates can not be used!
   */
  private PredicatedNodeTest[] m_nodeTests = null;

  /**
   * Constructor for UnionChildIterator
   */
  public UnionChildIterator()
  {
    super(null);
  }

  /**
   * Add a node test to the union list.
   *
   * @param test reference to a NodeTest, which will be added 
   * directly to the list of node tests (in other words, it will 
   * not be cloned).  The parent of this test will be set to 
   * this object.
   */
  public void addNodeTest(PredicatedNodeTest test)
  {

    if (null == m_nodeTests)
    {
      m_nodeTests = new PredicatedNodeTest[1];
      m_nodeTests[0] = test;
    }
    else
    {
      PredicatedNodeTest[] tests = m_nodeTests;
      int len = m_nodeTests.length;

      m_nodeTests = new PredicatedNodeTest[len + 1];

      System.arraycopy(tests, 0, m_nodeTests, 0, len);

      m_nodeTests[len] = test;
    }
    test.exprSetParent(this);
  }

  /**
   * Test whether a specified node is visible in the logical view of a
   * TreeWalker or NodeIterator. This function will be called by the
   * implementation of TreeWalker and NodeIterator; it is not intended to
   * be called directly from user code.
   * @param n  The node to check to see if it passes the filter or not.
   * @return  a constant to determine whether the node is accepted,
   *   rejected, or skipped, as defined  above .
   */
  public short acceptNode(int n)
  {
    XPathContext xctxt = getXPathContext();
    try
    {
      xctxt.pushCurrentNode(n);
      for (int i = 0; i < m_nodeTests.length; i++)
      {
        PredicatedNodeTest pnt = m_nodeTests[i];
        XObject score = pnt.execute(xctxt, n);
        if (score != NodeTest.SCORE_NONE)
        {
          if (pnt.getPredicateCount() > 0)
          {
            if (pnt.executePredicates(n, xctxt))
              return DTMIterator.FILTER_ACCEPT;
          }
          else
            return DTMIterator.FILTER_ACCEPT;

        }
      }
    }
    catch (javax.xml.transform.TransformerException se)
    {

      throw new RuntimeException(se.getMessage());
    }
    finally
    {
      xctxt.popCurrentNode();
    }
    return DTMIterator.FILTER_SKIP;
  }

}
