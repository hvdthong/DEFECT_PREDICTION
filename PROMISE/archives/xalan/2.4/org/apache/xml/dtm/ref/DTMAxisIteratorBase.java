package org.apache.xml.dtm.ref;

import org.apache.xml.dtm.*;

/**
 * This class serves as a default base for implementations of DTMAxisIterators.
 */
public abstract class DTMAxisIteratorBase implements DTMAxisIterator
{

  /** The position of the last node within the iteration, as defined by XPath.
   * Note that this is _not_ the node's handle within the DTM. Also, don't
   * confuse it with the current (most recently returned) position.
   */
  private int _last = -1;

  /** The position of the current node within the iteration, as defined by XPath.
   * Note that this is _not_ the node's handle within the DTM!
   */
  private int _position = 0;

  /** The position of the marked node within the iteration;
   * a saved itaration state that we may want to come back to.
   * Note that only one mark is maintained; there is no stack.
   */
  protected int _markedNode;

  /** The handle to the start, or root, of the iteration.
   * Set this to END to construct an empty iterator.
   */
  protected int _startNode = DTMAxisIterator.END;

  /** True if the start node should be considered part of the iteration.
   * False will cause it to be skipped.
   */
  protected boolean _includeSelf = false;

  /** True if this iteration can be restarted. False otherwise (eg, if
   * we are iterating over a stream that can not be re-scanned, or if
   * the iterator was produced by cloning another iterator.)
   */
  protected boolean _isRestartable = true;
  
  /**
   * Get start to END should 'close' the iterator,
   * i.e. subsequent call to next() should return END.
   *
   * @return The root node of the iteration.
   */
  public int getStartNode()
  {
    return _startNode;
  }

  /**
   * @return A DTMAxisIterator which has been reset to the start node,
   * which may or may not be the same as this iterator.
   * */
  public DTMAxisIterator reset()
  {

    final boolean temp = _isRestartable;

    _isRestartable = true;

    setStartNode(_startNode);

    _isRestartable = temp;

    return this;
  }

  /**
   * Set the flag to include the start node in the iteration. 
   *
   *
   * @return This default method returns just returns this DTMAxisIterator,
   * after setting the flag.
   * (Returning "this" permits C++-style chaining of
   * method calls into a single expression.)
   */
  public DTMAxisIterator includeSelf()
  {

    _includeSelf = true;

    return this;
  }

  /** Returns the position of the last node within the iteration, as
   * defined by XPath.  In a forward iterator, I believe this equals the number of nodes which this
   * iterator will yield. In a reverse iterator, I believe it should return
   * 1 (since the "last" is the first produced.)
   *
   * This may be an expensive operation when called the first time, since
   * it may have to iterate through a large part of the document to produce
   * its answer.
   *
   * @return The number of nodes in this iterator (forward) or 1 (reverse).
   */
  public int getLast()
  {

    {

      setMark();

      do
      {
        _last++;
      }
      while (next() != END);

      _position = temp;
    }

    return _last;
  }

  /**
   * @return The position of the current node within the set, as defined by
   * XPath. Note that this is one-based, not zero-based.
   */
  public int getPosition()
  {
    return _position == 0 ? 1 : _position;
  }

  /**
   * @return true if this iterator has a reversed axis, else false
   */
  public boolean isReverse()
  {
    return false;
  }

  /**
   * Returns a deep copy of this iterator. Cloned iterators may not be
   * restartable. The iterator being cloned may or may not become
   * non-restartable as a side effect of this operation.
   *
   * @return a deep copy of this iterator.
   */
  public DTMAxisIterator cloneIterator()
  {

    try
    {
      final DTMAxisIteratorBase clone = (DTMAxisIteratorBase) super.clone();


      return clone;
    }
    catch (CloneNotSupportedException e)
    {
      throw new org.apache.xml.utils.WrappedRuntimeException(e);
    }
  }

  /**
   * Do any final cleanup that is required before returning the node that was
   * passed in, and then return it. The intended use is
   * <br />
   * <code>return returnNode(node);</code>
   *
   * %REVIEW% If we're calling it purely for side effects, should we really
   * be bothering with a return value? Something like
   * <br />
   * <code> accept(node); return node; </code>
   * <br />
   * would probably optimize just about as well and avoid questions
   * about whether what's returned could ever be different from what's
   * passed in.
   *
   * @param node Node handle which iteration is about to yield.
   *
   * @return The node handle passed in.  */
  protected final int returnNode(final int node)
  {
    _position++;

    return node;
  }

  /**
   * Reset the position to zero. NOTE that this does not change the iteration
   * state, only the position number associated with that state.
   *
   * %REVIEW% Document when this would be used?
   *
   * @return This instance.
   */
  protected final DTMAxisIterator resetPosition()
  {

    _position = 0;

    return this;
  }
  
  /**
   * Returns true if all the nodes in the iteration well be returned in document 
   * order.
   * 
   * @return true as a default.
   */
  public boolean isDocOrdered()
  {
    return true;
  }
  
  /**
   * Returns the axis being iterated, if it is known.
   * 
   * @return Axis.CHILD, etc., or -1 if the axis is not known or is of multiple 
   * types.
   */
  public int getAxis()
  {
    return -1;
  }

}
