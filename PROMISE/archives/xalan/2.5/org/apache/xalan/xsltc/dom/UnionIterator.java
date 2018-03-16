package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

/**
 * UnionIterator takes a set of NodeIterators and produces
 * a merged NodeSet in document order with duplicates removed
 * The individual iterators are supposed to generate nodes
 * in document order
 */
public final class UnionIterator extends DTMAxisIteratorBase {
    /** wrapper for NodeIterators to support iterator
	comparison on the value of their next() method
    */
    final private DOM _dom;

    private final static class LookAheadIterator {
	public int node, markedNode;
	public DTMAxisIterator iterator;
		
	public LookAheadIterator(DTMAxisIterator iterator) {
	    this.iterator = iterator;
	}
		
	public int step() {
	    node = iterator.next();
	    return node;
	}

	public LookAheadIterator cloneIterator() {
	    final LookAheadIterator clone = 
		 new LookAheadIterator(iterator.cloneIterator());
	    clone.node = node;
	    clone.markedNode = node;
	    return clone;
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


    public DTMAxisIterator cloneIterator() {
	_isRestartable = false;
	final LookAheadIterator[] heapCopy = 
	    new LookAheadIterator[_heap.length];
	try {
	    final UnionIterator clone = (UnionIterator)super.clone();
            for (int i = 0; i < _free; i++) {
                heapCopy[i] = _heap[i].cloneIterator();
            }
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
    
    public UnionIterator addIterator(DTMAxisIterator iterator) {
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
  
    public DTMAxisIterator setStartNode(int node) {
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

    public DTMAxisIterator reset() {
	for (int i = 0; i < _free; i++) {
	    _heap[i].iterator.reset();
	    _heap[i].step();
	}
	for (int i = (_heapSize = _free)/2; i >= 0; i--) {
	    heapify(i);
	}
	_returnedLast = END;
	return resetPosition();
    }

}
