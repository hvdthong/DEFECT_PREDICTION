package org.apache.xalan.xsltc;

public interface NodeIterator extends Cloneable {
    public static final int END = DOM.NULL;

    /** 
     * Callers should not call next() after it returns END.
     */
    public int next();

    /**
     * Resets the iterator to the last start node.
     */
    public NodeIterator reset();

    /**
     * Returns the number of elements in this iterator.
     */
    public int getLast();

    /**
     * Returns the position of the current node in the set.
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
     */
    public NodeIterator setStartNode(int node);

    /**
     * True if this iterator has a reversed axis.
     */
    public boolean isReverse();

    /**
     * Returns a deep copy of this iterator.
     */
    public NodeIterator cloneIterator();

    /**
     * Prevents or allows iterator restarts.
     */
    public void setRestartable(boolean isRestartable);

}
