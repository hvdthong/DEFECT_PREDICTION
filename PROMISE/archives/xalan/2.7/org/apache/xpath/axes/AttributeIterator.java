package org.apache.xpath.axes;

import org.apache.xml.dtm.DTM;
import org.apache.xpath.compiler.Compiler;

/**
 * This class implements an optimized iterator for
 * attribute axes patterns.
 * @see org.apache.xpath.axes#ChildTestIterator
 * @xsl.usage advanced
 */
public class AttributeIterator extends ChildTestIterator
{
    static final long serialVersionUID = -8417986700712229686L;

  /**
   * Create a AttributeIterator object.
   *
   * @param compiler A reference to the Compiler that contains the op map.
   * @param opPos The position within the op map, which contains the
   * location path expression for this itterator.
   *
   * @throws javax.xml.transform.TransformerException
   */
  AttributeIterator(Compiler compiler, int opPos, int analysis)
          throws javax.xml.transform.TransformerException
  {
    super(compiler, opPos, analysis);
  }
    
  /**
   * Get the next node via getFirstAttribute && getNextAttribute.
   */
  protected int getNextNode()
  {
    m_lastFetched = (DTM.NULL == m_lastFetched)
                     ? m_cdtm.getFirstAttribute(m_context)
                     : m_cdtm.getNextAttribute(m_lastFetched);
    return m_lastFetched;
  }
  
  /**
   * Returns the axis being iterated, if it is known.
   * 
   * @return Axis.CHILD, etc., or -1 if the axis is not known or is of multiple 
   * types.
   */
  public int getAxis()
  {
    return org.apache.xml.dtm.Axis.ATTRIBUTE;
  }



}
