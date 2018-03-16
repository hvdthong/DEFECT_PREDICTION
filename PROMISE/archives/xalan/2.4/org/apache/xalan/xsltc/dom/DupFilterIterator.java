package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.TransletException;

public final class DupFilterIterator extends NodeIteratorBase {

    private final static int INIT_DATA_SIZE = 16;

    private int _current = 0;

    /**
     * Creates a new duplicate filter iterator based on an existing iterator.
     * This iterator should be used with union expressions and other complex
     * iterator combinations (like 'get me the parents of all child node in
     * the dom' sort of thing). The iterator is also used to cache node-sets
     * returned by id() and key() iterators.
     * @param source The iterator this iterator will get its nodes from
     */
    public DupFilterIterator(NodeIterator source) {
	_source = source;

	if (source instanceof KeyIndex) setStartNode(DOM.ROOTNODE);
    }

    /**
     * Returns the next node in this iterator - excludes duplicates.
     * @return The next node in this iterator
     */
    public int next() {
	return _current < _last ? _data[_current++] : END;
    }

    /**
     * Set the start node for this iterator
     * @param node The start node
     * @return A reference to this node iterator
     */
    public NodeIterator setStartNode(int node) {


	if ((_source instanceof KeyIndex) && (_data != null)) return this;

	if ((_data == null) || (node != _startNode)) {

	    _startNode = node;
	    _last = 0;
	    _source.setStartNode(node);
	    _data = new int[INIT_DATA_SIZE];

	    while ((node = _source.next()) != END) {
		if (_last == _data.length) {
		    int[] newArray = new int[_data.length * 2];
		    System.arraycopy(_data, 0, newArray, 0, _last);
		    _data = newArray;
		}

		for (i=0; i<_last; i++) {
		    if (_data[i] == node) {
			break;
		    }
		    else if (_data[i] > node) {
			for (j = _last++; j>i; j--)
			    _data[j] = _data[j-1];
			_data[i] = node;
			break;
		    }
		}
		if (i == _last) _data[_last++] = node;
	    }
	}

	return this;
    }

    /**
     * Returns the current position of the iterator. The position is within the
     * node set covered by this iterator, not within the DOM.
     */
    public int getPosition() {
	return (_current);
    }

    /**
     * Returns the position of the last node in this iterator. The integer
     * returned is equivalent to the number of nodes in this iterator.
     */
    public int getLast() {
	return _last;
    }

    /**
     * Saves the position of this iterator - see gotoMark()
     */
    public void setMark() {
	_source.setMark();
	_markedNode = _current;
    }

    /**
     * Restores the position of this iterator - see setMark()
     */
    public void gotoMark() {
	_source.gotoMark();
	_current = _markedNode;
    }

    public NodeIterator reset() {
	_current = 0;
	return(this);
    }

}
