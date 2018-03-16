package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

/**
 * UnionIterator takes a set of NodeIterators and produces
 * a merged NodeSet in document order with duplicates removed
 * The individual iterators are supposed to generate nodes
 * in document order
 */
public final class UnionIterator extends NodeIteratorBase {
    /** wrapper for NodeIterators to support iterator
	comparison on the value of their next() method
    */
    final private DOM _dom;

    private final static class LookAheadIterator {
	public int node, markedNode;
	public final NodeIterator iterator;
		
	public LookAheadIterator(NodeIterator iterator) {
	    this.iterator = iterator;
	}
		
	public int step() {
	    node = iterator.next();
	    return node;
	}

	public void setMark() {
	    markedNode = node;
	    iterator.setMark();
	}

	public void gotoMark() {
	    node = markedNode;
	    iterator.gotoMark();
	}


    private static final int InitSize = 8;
  
    private int            _heapSize = 0;
    private int            _size = InitSize;
    private LookAheadIterator[] _heap = new LookAheadIterator[InitSize];
    private int            _free = 0;
  
    private int _returnedLast;

    public UnionIterator(DOM dom) {
	_dom = dom;
    }

    public NodeIterator cloneIterator() {
	final LookAheadIterator[] heapCopy = 
	    new LookAheadIterator[_heap.length];
	try {
	    final UnionIterator clone = (UnionIterator)super.clone();
	    System.arraycopy(_heap, 0, heapCopy, 0, _heap.length);
	    clone.setRestartable(false);
	    clone._heap = heapCopy;
	    return clone.reset();
	} 
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }
    
    public UnionIterator addIterator(NodeIterator iterator) {
	if (_free == _size) {
	    LookAheadIterator[] newArray = new LookAheadIterator[_size *= 2];
	    System.arraycopy(_heap, 0, newArray, 0, _free);
	    _heap = newArray;
	}
	_heapSize++;
	_heap[_free++] = new LookAheadIterator(iterator);
	return this;
    }
  
    public int next() {
	while (_heapSize > 0) {
	    final int smallest = _heap[0].node;
		if (_heapSize > 1) {
		    final LookAheadIterator temp = _heap[0];
		    _heap[0] = _heap[--_heapSize];
		    _heap[_heapSize] = temp;
		}
		else {
		    return END;
		}
	    }
	    }
	    else {
		heapify(0);
		return returnNode(_returnedLast = smallest);
	    }
	    heapify(0);
	}
	return END;
    }
  
    public NodeIterator setStartNode(int node) {
	if (_isRestartable) {
	    _startNode = node;
	    for (int i = 0; i < _free; i++) {
		_heap[i].iterator.setStartNode(node);
	    }
	    for (int i = (_heapSize = _free)/2; i >= 0; i--) {
		heapify(i);
	    }
	    _returnedLast = END;
	    return resetPosition();
	}
	return this;
    }
	
    private void heapify(int i) {
	for (int r, l, smallest;;) {
	    r = (i + 1) << 1; l = r - 1;
	    smallest = l < _heapSize 
		&& _dom.lessThan(_heap[l].node, _heap[i].node) ? l : i;
	    if (r < _heapSize && _dom.lessThan(_heap[r].node,
					       _heap[smallest].node)) {
		smallest = r;
	    }
	    if (smallest != i) {
		final LookAheadIterator temp = _heap[smallest];
		_heap[smallest] = _heap[i];
		_heap[i] = temp;
		i = smallest;
	    }
	    else
		break;
	}
    }

    public void setMark() {
	for (int i = 0; i < _free; i++) {
	    _heap[i].setMark();
	}
    }

    public void gotoMark() {
	for (int i = 0; i < _free; i++) {
	    _heap[i].gotoMark();
	}
    }

    public NodeIterator reset() {
	super.reset();
	for (int i = 0; i < _free; i++) {
	    _heap[i].iterator.reset();
	}
	return resetPosition();
    }

}
