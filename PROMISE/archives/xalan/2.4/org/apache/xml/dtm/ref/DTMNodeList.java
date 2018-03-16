package org.apache.xml.dtm.ref;
import org.apache.xml.dtm.*;
import org.w3c.dom.Node;
import org.w3c.dom.DOMException;

/**
 * <code>DTMNodeList</code> gives us an implementation of the DOM's
 * NodeList interface wrapped around a DTM Iterator. The author
 * considers this something of an abominations, since NodeList was not
 * intended to be a general purpose "list of nodes" API and is
 * generally considered by the DOM WG to have be a mistake... but I'm
 * told that some of the XPath/XSLT folks say they must have this
 * solution.
 *
 * Please note that this is not necessarily equivlaent to a DOM
 * NodeList operating over the same document. In particular:
 * <ul>
 *
 * <li>If there are several Text nodes in logical succession (ie,
 * across CDATASection and EntityReference boundaries), we will return
 * only the first; the caller is responsible for stepping through
 * them.
 * (%REVIEW% Provide a convenience routine here to assist, pending
 * proposed DOM Level 3 getAdjacentText() operation?) </li>
 *
 * <li>Since the whole XPath/XSLT architecture assumes that the source
 * document is not altered while we're working with it, we do not
 * promise to implement the DOM NodeList's "live view" response to
 * document mutation. </li>
 *
 * </ul>
 *
 * <p>State: In progress!!</p>
 * */
public class DTMNodeList implements org.w3c.dom.NodeList
{
  private DTMIterator dtm_iter;
  private boolean valid=true;
  private int m_firstChild;
  private DTM m_parentDTM;


  /** Public constructor: Wrap a DTMNodeList around an existing
   * and preconfigured DTMIterator
   *
   * WARNING: THIS HAS THE SIDE EFFECT OF ISSUING setShouldCacheNodes(true)
   * AGAINST THE DTMIterator.
   * */
  public DTMNodeList(DTMIterator dtmIterator)
    {
      int pos = dtmIterator.getCurrentPos();
      try
      {
        dtm_iter=(DTMIterator)dtmIterator.cloneWithReset();
      }
      catch(CloneNotSupportedException cnse) {}
      dtm_iter.setShouldCacheNodes(true);
      dtm_iter.runTo(-1);
      dtm_iter.setCurrentPos(pos);
    }

  /** Public constructor: Create a NodeList to support
   * DTMNodeProxy.getChildren().
   *
   * Unfortunately AxisIterators and DTMIterators don't share an API,
   * so I can't use the existing Axis.CHILD iterator. Rather than
   * create Yet Another Class, let's set up a special case of this
   * one.
   *
   * @param parentDTM The DTM containing this node
   * @param parentHandle DTM node-handle integer
   * */
  public DTMNodeList(DTM parentDTM,int parentHandle)
  {
    dtm_iter=null;
    m_parentDTM=parentDTM;
    m_firstChild=parentDTM.getFirstChild(parentHandle);
  }

  /** Access the wrapped DTMIterator. I'm not sure whether anyone will
   * need this or not, but let's write it and think about it.
   * */
  DTMIterator getDTMIterator()
    {
      return dtm_iter;
    }
  


    /**
     * Returns the <code>index</code>th item in the collection. If 
     * <code>index</code> is greater than or equal to the number of nodes in 
     * the list, this returns <code>null</code>.
     * @param indexIndex into the collection.
     * @return The node at the <code>index</code>th position in the 
     *   <code>NodeList</code>, or <code>null</code> if that is not a valid 
     *   index.
     */
    public Node item(int index)
    {
      if(dtm_iter!=null)
      {
        int handle=dtm_iter.item(index);
        return dtm_iter.getDTM(handle).getNode(handle);
      }
      else
      {
        int handle=m_firstChild;
        while(--index>=0 && handle!=DTM.NULL)
          handle=m_parentDTM.getNextSibling(handle);
        return m_parentDTM.getNode(handle);
      }
    }

    /**
     * The number of nodes in the list. The range of valid child node indices 
     * is 0 to <code>length-1</code> inclusive. 
     */
    public int getLength()
    {
      if(dtm_iter!=null)
      {
        return dtm_iter.getLength();
      }
      else
      {
        int count=0;
        for(int handle=m_firstChild;
            handle!=DTM.NULL;
            handle=m_parentDTM.getNextSibling(handle))
          ++count;
        return count;
      }
    }
}
