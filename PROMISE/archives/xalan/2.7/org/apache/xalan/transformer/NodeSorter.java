package org.apache.xalan.transformer;

import java.text.CollationKey;
import java.util.Vector;

import javax.xml.transform.TransformerException;

import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;

/**
 * This class can sort vectors of DOM nodes according to a select pattern.
 * @xsl.usage internal
 */
public class NodeSorter
{

  /** Current XPath context           */
  XPathContext m_execContext;

  /** Vector of NodeSortKeys          */


  /**
   * Construct a NodeSorter, passing in the XSL TransformerFactory
   * so it can know how to get the node data according to
   * the proper whitespace rules.
   *
   * @param p Xpath context to use
   */
  public NodeSorter(XPathContext p)
  {
    m_execContext = p;
  }

  /**
   * Given a vector of nodes, sort each node according to
   * the criteria in the keys.
   * @param v an vector of Nodes.
   * @param keys a vector of NodeSortKeys.
   * @param support XPath context to use
   *
   * @throws javax.xml.transform.TransformerException
   */
  public void sort(DTMIterator v, Vector keys, XPathContext support)
          throws javax.xml.transform.TransformerException
  {

    m_keys = keys;

    int n = v.getLength();

    
    Vector nodes = new Vector();

    for (int i = 0; i < n; i++)
    {
      NodeCompareElem elem = new NodeCompareElem(v.item(i));

      nodes.addElement(elem);
    }

    Vector scratchVector = new Vector();

    mergesort(nodes, scratchVector, 0, n - 1, support);

    for (int i = 0; i < n; i++)
    {
      v.setItem(((NodeCompareElem) nodes.elementAt(i)).m_node, i);
    }
    v.setCurrentPos(0);

  }

  /**
   * Return the results of a compare of two nodes.
   * TODO: Optimize compare -- cache the getStringExpr results, key by m_selectPat + hash of node.
   *
   * @param n1 First node to use in compare
   * @param n2 Second node to use in compare
   * @param kIndex Index of NodeSortKey to use for sort
   * @param support XPath context to use
   *
   * @return The results of the compare of the two nodes.
   *
   * @throws TransformerException
   */
  int compare(
          NodeCompareElem n1, NodeCompareElem n2, int kIndex, XPathContext support)
            throws TransformerException
  {

    int result = 0;
    NodeSortKey k = (NodeSortKey) m_keys.elementAt(kIndex);

    if (k.m_treatAsNumbers)
    {
      double n1Num, n2Num;

      if (kIndex == 0)
      {
        n1Num = ((Double) n1.m_key1Value).doubleValue();
        n2Num = ((Double) n2.m_key1Value).doubleValue();
      }
      else if (kIndex == 1)
      {
        n1Num = ((Double) n1.m_key2Value).doubleValue();
        n2Num = ((Double) n2.m_key2Value).doubleValue();
      }

      /* Leave this in case we decide to use an array later
      if (kIndex < maxkey)
      {
      double n1Num = (double)n1.m_keyValue[kIndex];
      double n2Num = (double)n2.m_keyValue[kIndex];
      }*/
      else
      {

        XObject r1 = k.m_selectPat.execute(m_execContext, n1.m_node,
                                           k.m_namespaceContext);
        XObject r2 = k.m_selectPat.execute(m_execContext, n2.m_node,
                                           k.m_namespaceContext);
        n1Num = r1.num();

        n2Num = r2.num();
      }

      if ((n1Num == n2Num) && ((kIndex + 1) < m_keys.size()))
      {
        result = compare(n1, n2, kIndex + 1, support);
      }
      else
      {
        double diff;
        if (Double.isNaN(n1Num))
        {
          if (Double.isNaN(n2Num))
            diff = 0.0;
          else
            diff = -1;
        }
        else if (Double.isNaN(n2Num))
           diff = 1;
        else
          diff = n1Num - n2Num;

        result = (int) ((diff < 0.0)
                        ? (k.m_descending ? 1 : -1)
                        : (diff > 0.0) ? (k.m_descending ? -1 : 1) : 0);
      }
    else
    {
      CollationKey n1String, n2String;

      if (kIndex == 0)
      {
        n1String = (CollationKey) n1.m_key1Value;
        n2String = (CollationKey) n2.m_key1Value;
      }
      else if (kIndex == 1)
      {
        n1String = (CollationKey) n1.m_key2Value;
        n2String = (CollationKey) n2.m_key2Value;
      }

      /* Leave this in case we decide to use an array later
      if (kIndex < maxkey)
      {
        String n1String = (String)n1.m_keyValue[kIndex];
        String n2String = (String)n2.m_keyValue[kIndex];
      }*/
      else
      {

        XObject r1 = k.m_selectPat.execute(m_execContext, n1.m_node,
                                           k.m_namespaceContext);
        XObject r2 = k.m_selectPat.execute(m_execContext, n2.m_node,
                                           k.m_namespaceContext);

        n1String = k.m_col.getCollationKey(r1.str());
        n2String = k.m_col.getCollationKey(r2.str());
      }

      result = n1String.compareTo(n2String);

      if (k.m_caseOrderUpper)
      {
        String tempN1 = n1String.getSourceString().toLowerCase();
        String tempN2 = n2String.getSourceString().toLowerCase();

        if (tempN1.equals(tempN2))
        {

          result = result == 0 ? 0 : -result;
        }
      }

      if (k.m_descending)
      {
        result = -result;
      }

    if (0 == result)
    {
      if ((kIndex + 1) < m_keys.size())
      {
        result = compare(n1, n2, kIndex + 1, support);
      }
    }

    if (0 == result)
    {

      result = dtm.isNodeAfter(n1.m_node, n2.m_node) ? -1 : 1;

    }

    return result;
  }

  /**
   * This implements a standard Mergesort, as described in
   * Robert Sedgewick's Algorithms book.  This is a better
   * sort for our purpose than the Quicksort because it
   * maintains the original document order of the input if
   * the order isn't changed by the sort.
   *
   * @param a First vector of nodes to compare
   * @param b Second vector of  nodes to compare 
   * @param l Left boundary of  partition
   * @param r Right boundary of  partition
   * @param support XPath context to use
   *
   * @throws TransformerException
   */
  void mergesort(Vector a, Vector b, int l, int r, XPathContext support)
          throws TransformerException
  {

    if ((r - l) > 0)
    {
      int m = (r + l) / 2;

      mergesort(a, b, l, m, support);
      mergesort(a, b, m + 1, r, support);

      int i, j, k;

      for (i = m; i >= l; i--)
      {

        if (i >= b.size())
          b.insertElementAt(a.elementAt(i), i);
        else
          b.setElementAt(a.elementAt(i), i);
      }

      i = l;

      for (j = (m + 1); j <= r; j++)
      {

        if (r + m + 1 - j >= b.size())
          b.insertElementAt(a.elementAt(j), r + m + 1 - j);
        else
          b.setElementAt(a.elementAt(j), r + m + 1 - j);
      }

      j = r;

      int compVal;

      for (k = l; k <= r; k++)
      {

        if (i == j)
          compVal = -1;
        else
          compVal = compare((NodeCompareElem) b.elementAt(i),
                            (NodeCompareElem) b.elementAt(j), 0, support);

        if (compVal < 0)
        {

          a.setElementAt(b.elementAt(i), k);

          i++;
        }
        else if (compVal > 0)
        {

          a.setElementAt(b.elementAt(j), k);

          j--;
        }
      }
    }
  }

  /**
   * This is a generic version of C.A.R Hoare's Quick Sort
   * algorithm.  This will handle arrays that are already
   * sorted, and arrays with duplicate keys.<BR>
   *
   * If you think of a one dimensional array as going from
   * the lowest index on the left to the highest index on the right
   * then the parameters to this function are lowest index or
   * left and highest index or right.  The first time you call
   * this function it will be with the parameters 0, a.length - 1.
   *
   * @param v       a vector of integers 
   * @param lo0     left boundary of array partition
   * @param hi0     right boundary of array partition
   *
   */

  /*  private void QuickSort2(Vector v, int lo0, int hi0, XPathContext support)
      throws javax.xml.transform.TransformerException,
             java.net.MalformedURLException,
             java.io.FileNotFoundException,
             java.io.IOException
    {
      int lo = lo0;
      int hi = hi0;

      if ( hi0 > lo0)
      {
        Node midNode = (Node)v.elementAt( ( lo0 + hi0 ) / 2 );

        while( lo <= hi )
        {
          while( (lo < hi0) && (compare((Node)v.elementAt(lo), midNode, 0, support) < 0) )
          {
            ++lo;

          while( (hi > lo0) && (compare((Node)v.elementAt(hi), midNode, 0, support) > 0) )
          {
            --hi;
          }

          if( lo <= hi )
          {
            swap(v, lo, hi);
            ++lo;
            --hi;
          }
        }

        if( lo0 < hi )
        {
          QuickSort2( v, lo0, hi, support );
        }

        if( lo < hi0 )
        {
          QuickSort2( v, lo, hi0, support );
        }
      }


  /**
   * This class holds the value(s) from executing the given
   * node against the sort key(s). 
   * @xsl.usage internal
   */
  class NodeCompareElem
  {

    /** Current node          */
    int m_node;

    /** This maxkey value was chosen arbitrarily. We are assuming that the    
    */
    int maxkey = 2;


    /** Value from first sort key           */
    Object m_key1Value;

    /** Value from second sort key            */
    Object m_key2Value;

    /**
     * Constructor NodeCompareElem
     *
     *
     * @param node Current node
     *
     * @throws javax.xml.transform.TransformerException
     */
    NodeCompareElem(int node) throws javax.xml.transform.TransformerException
    {
      m_node = node;

      if (!m_keys.isEmpty())
      {
        NodeSortKey k1 = (NodeSortKey) m_keys.elementAt(0);
        XObject r = k1.m_selectPat.execute(m_execContext, node,
                                           k1.m_namespaceContext);

        double d;

        if (k1.m_treatAsNumbers)
        {
          d = r.num();

          m_key1Value = new Double(d);
        }
        else
        {
          m_key1Value = k1.m_col.getCollationKey(r.str());
        }

        if (r.getType() == XObject.CLASS_NODESET)
        {
          DTMIterator ni = ((XNodeSet)r).iterRaw();
          int current = ni.getCurrentNode();
          if(DTM.NULL == current)
            current = ni.nextNode();


        }

        if (m_keys.size() > 1)
        {
          NodeSortKey k2 = (NodeSortKey) m_keys.elementAt(1);

          XObject r2 = k2.m_selectPat.execute(m_execContext, node,
                                              k2.m_namespaceContext);

          if (k2.m_treatAsNumbers) {
            d = r2.num();
            m_key2Value = new Double(d);
          } else {
            m_key2Value = k2.m_col.getCollationKey(r2.str());
          }
        }

        /* Leave this in case we decide to use an array later
        while (kIndex <= m_keys.size() && kIndex < maxkey)
        {
          NodeSortKey k = (NodeSortKey)m_keys.elementAt(kIndex);
          XObject r = k.m_selectPat.execute(m_execContext, node, k.m_namespaceContext);
          if(k.m_treatAsNumbers)
            m_KeyValue[kIndex] = r.num();
          else
            m_KeyValue[kIndex] = r.str();
        } */
    }
}
