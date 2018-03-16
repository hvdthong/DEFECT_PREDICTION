package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public final class SortingIterator extends NodeIteratorBase {
    private final static int INIT_DATA_SIZE = 16;
    private NodeIterator _source;
    private NodeSortRecordFactory _factory;
    private NodeSortRecord[] _data;
    private int _free = 0;

    public SortingIterator(NodeIterator source, 
			   NodeSortRecordFactory factory) {
	_source = source;
	_factory = factory;
    }

    public int next() {
	return _current < _free ? _data[_current++].getNode() : END;
    }
	
    public NodeIterator setStartNode(int node) {
	try {
	    _source.setStartNode(_startNode = node);
	    _data = new NodeSortRecord[INIT_DATA_SIZE];
	    _free = 0;

	    while ((node = _source.next()) != END) {
		addRecord(_factory.makeNodeSortRecord(node,_free));
	    }
	    quicksort(0, _free - 1);

	    _current = 0;
	    return this;
	}
	catch (Exception e) {
	    return this;
	}
    }
	
    public int getPosition() {
	return _current == 0 ? 1 : _current;
    }

    public int getLast() {
	return _free;
    }

    public void setMark() {
	_source.setMark();
	_markedNode = _current;
    }

    public void gotoMark() {
	_source.gotoMark();
	_current = _markedNode;
    }
    
    /**
     * Clone a <code>SortingIterator</code> by cloning its source
     * iterator and then sharing the factory and the array of
     * <code>NodeSortRecords</code>.
     */
    public NodeIterator cloneIterator() {
	try {
	    final SortingIterator clone = (SortingIterator) super.clone();
	    clone._source = _source.cloneIterator();  
	    clone._free = _free;
	    clone._current = _current;
	    clone.setRestartable(false);
	    return clone.reset();
	}
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }

    private void addRecord(NodeSortRecord record) {
	if (_free == _data.length) {
	    NodeSortRecord[] newArray = new NodeSortRecord[_data.length * 2];
	    System.arraycopy(_data, 0, newArray, 0, _free);
	    _data = newArray;
	}
	_data[_free++] = record;
    }

    private void quicksort(int p, int r) {
	while (p < r) {
	    final int q = partition(p, r);
	    quicksort(p, q);
	    p = q + 1;
	}
    }
    
    private int partition(int p, int r) {
	final NodeSortRecord x = _data[(p + r) >>> 1];
	int i = p - 1;
	int j = r + 1;
	while (true) {
	    while (x.compareTo(_data[--j]) < 0);
	    while (x.compareTo(_data[++i]) > 0);
	    if (i < j) {
		final NodeSortRecord t = _data[i];
		_data[i] = _data[j];
		_data[j] = t;
	    }
	    else {
		return(j);
	    }
	}
    }
}
