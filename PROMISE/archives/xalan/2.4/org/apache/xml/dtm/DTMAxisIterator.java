package org.apache.xml.dtm;

/**
 * This class iterates over a single XPath Axis, and returns node handles.
 */
public interface DTMAxisIterator extends Cloneable
{

  /** Specifies the end of the iteration, and is the same as DTM.NULL.  */
  public static final int END = DTM.NULL;

  /**
   * Get the next node in the iteration.
   *
   * @return The next node handle in the iteration, or END.
   */
  public int next();

  /**
   * Resets the iterator to the last start node.
   *
   * @return A DTMAxisIterator, which may or may not be the same as this 
   *         iterator.
   */
  public DTMAxisIterator reset();

  /**
   * @return the number of nodes in this iterator.  This may be an expensive 
   * operation when called the first time.
   */
  public int getLast();

  /**
   * @return The position of the current node in the set, as defined by XPath.
   */
  public int getPosition();

  /**
   * Remembers the current node for the next call to gotoMark().
   */
  public void setMark();

  /**
   * Restores the current node remembered by setMark().
   */
  public void gotoMark();

  /**
   * Set start to END should 'close' the iterator,
   * i.e. subsequent call to next() should return END.
   *
   * @param node Sets the root of the iteration.
   *
   * @return A DTMAxisIterator set to the start of the iteration.
   */
  public DTMAxisIterator setStartNode(int node);

  /**
   * Get start to END should 'close' the iterator,
   * i.e. subsequent call to next() should return END.
   *
   * @return The root node of the iteration.
   */
  public int getStartNode();

  /**
   * @return true if this iterator has a reversed axis, else false.
   */
  public boolean isReverse();

  /**
   * @return a deep copy of this iterator. The clone should not be reset 
   * from its current position.
   */
  public DTMAxisIterator cloneIterator();
}
