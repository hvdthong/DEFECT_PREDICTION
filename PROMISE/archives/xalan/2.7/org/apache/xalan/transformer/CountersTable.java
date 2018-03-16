package org.apache.xalan.transformer;

import java.util.Hashtable;
import java.util.Vector;

import javax.xml.transform.TransformerException;

import org.apache.xalan.templates.ElemNumber;
import org.apache.xml.dtm.DTM;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;

/**
 * This is a table of counters, keyed by ElemNumber objects, each
 * of which has a list of Counter objects.  This really isn't a true
 * table, it is more like a list of lists (there must be a technical
 * term for that...).
 * @xsl.usage internal
 */
public class CountersTable extends Hashtable
{
    static final long serialVersionUID = 2159100770924179875L;

  /**
   * Construct a CountersTable.
   */
  public CountersTable(){}

  /**
   * Get the list of counters that corresponds to
   * the given ElemNumber object.
   *
   * @param numberElem the given xsl:number element.
   *
   * @return the list of counters that corresponds to
   * the given ElemNumber object.
   */
  Vector getCounters(ElemNumber numberElem)
  {

    Vector counters = (Vector) this.get(numberElem);

    return (null == counters) ? putElemNumber(numberElem) : counters;
  }

  /**
   * Put a counter into the table and create an empty
   * vector as it's value.
   *
   * @param numberElem the given xsl:number element.
   *
   * @return an empty vector to be used to store counts
   * for this number element.
   */
  Vector putElemNumber(ElemNumber numberElem)
  {

    Vector counters = new Vector();

    this.put(numberElem, counters);

    return counters;
  }

  /**
   * Place to collect new counters.
   */
  transient private NodeSetDTM m_newFound;

  /**
   * Add a list of counted nodes that were built in backwards document
   * order, or a list of counted nodes that are in forwards document
   * order.
   *
   * @param flist Vector of nodes built in forwards document order
   * @param blist Vector of nodes built in backwards document order
   */
  void appendBtoFList(NodeSetDTM flist, NodeSetDTM blist)
  {

    int n = blist.size();

    for (int i = (n - 1); i >= 0; i--)
    {
      flist.addElement(blist.item(i));
    }
  }


  /** Number of counters created so far          */
  transient int m_countersMade = 0;

  /**
   * Count forward until the given node is found, or until
   * we have looked to the given amount.
   *
   * @param support The XPath context to use  
   * @param numberElem The given xsl:number element.
   * @param node The node to count.
   * 
   * @return The node count, or 0 if not found.
   *
   * @throws TransformerException
   */
  public int countNode(XPathContext support, ElemNumber numberElem, int node)
          throws TransformerException
  {

    int count = 0;
    Vector counters = getCounters(numberElem);
    int nCounters = counters.size();

    int target = numberElem.getTargetNode(support, node);

    if (DTM.NULL != target)
    {
      for (int i = 0; i < nCounters; i++)
      {
        Counter counter = (Counter) counters.elementAt(i);

        count = counter.getPreviouslyCounted(support, target);

        if (count > 0)
          return count;
      }

      count = 0;
      if (m_newFound == null)
        m_newFound = new NodeSetDTM(support.getDTMManager());

      for (; DTM.NULL != target;
              target = numberElem.getPreviousNode(support, target))
      {

        if (0 != count)
        {
          for (int i = 0; i < nCounters; i++)
          {
            Counter counter = (Counter) counters.elementAt(i);
            int cacheLen = counter.m_countNodes.size();

            if ((cacheLen > 0)
                    && (counter.m_countNodes.elementAt(cacheLen
                                                      - 1) == target))
            {
              count += (cacheLen + counter.m_countNodesStartCount);

              if (cacheLen > 0)
                appendBtoFList(counter.m_countNodes, m_newFound);

              m_newFound.removeAllElements();

              return count;
            }
          }
        }

        m_newFound.addElement(target);

        count++;
      }

      Counter counter = new Counter(numberElem, new NodeSetDTM(support.getDTMManager()));


      appendBtoFList(counter.m_countNodes, m_newFound);
      m_newFound.removeAllElements();
      counters.addElement(counter);
    }

    return count;
  }
}
