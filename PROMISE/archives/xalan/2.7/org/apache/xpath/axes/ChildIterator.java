package org.apache.xpath.axes;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;

/**
 * This class implements an optimized iterator for
 * "node()" patterns, that is, any children of the
 * context node.
 * @see org.apache.xpath.axes.LocPathIterator
 * @xsl.usage advanced
 */
public class ChildIterator extends LocPathIterator
{
    static final long serialVersionUID = -6935428015142993583L;

  /**
   * Create a ChildIterator object.
   *
   * @param compiler A reference to the Compiler that contains the op map.
   * @param opPos The position within the op map, which contains the
   * location path expression for this itterator.
   * @param analysis Analysis bits of the entire pattern.
   *
   * @throws javax.xml.transform.TransformerException
   */
  ChildIterator(Compiler compiler, int opPos, int analysis)
          throws javax.xml.transform.TransformerException
  {
    super(compiler, opPos, analysis, false);

    initNodeTest(DTMFilter.SHOW_ALL);
  }
  
  /**
   * Return the first node out of the nodeset, if this expression is 
   * a nodeset expression.  This is the default implementation for 
   * nodesets.
   * <p>WARNING: Do not mutate this class from this function!</p>
   * @param xctxt The XPath runtime context.
   * @return the first node out of the nodeset, or DTM.NULL.
   */
  public int asNode(XPathContext xctxt)
    throws javax.xml.transform.TransformerException
  {
    int current = xctxt.getCurrentNode();
    
    DTM dtm = xctxt.getDTM(current);
    
    return dtm.getFirstChild(current);
  }

  /**
   *  Returns the next node in the set and advances the position of the
   * iterator in the set. After a NodeIterator is created, the first call
   * to nextNode() returns the first node in the set.
   *
   * @return  The next <code>Node</code> in the set being iterated over, or
   *   <code>null</code> if there are no more members in that set.
   */
  public int nextNode()
  {
  	if(m_foundLast)
  		return DTM.NULL;

    int next;

    m_lastFetched = next = (DTM.NULL == m_lastFetched)
                           ? m_cdtm.getFirstChild(m_context)
                           : m_cdtm.getNextSibling(m_lastFetched);

    if (DTM.NULL != next)
    {
      m_pos++;
      return next;
    }
    else
    {
      m_foundLast = true;

      return DTM.NULL;
    }
  }
  
  /**
   * Returns the axis being iterated, if it is known.
   * 
   * @return Axis.CHILD, etc., or -1 if the axis is not known or is of multiple 
   * types.
   */
  public int getAxis()
  {
    return org.apache.xml.dtm.Axis.CHILD;
  }


}
