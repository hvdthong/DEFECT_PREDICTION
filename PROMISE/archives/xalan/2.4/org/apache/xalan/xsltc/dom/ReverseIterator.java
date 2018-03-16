package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.TransletException;

public final class ReverseIterator extends NodeIteratorBase {

    private final static int INIT_DATA_SIZE = 16;
    private final NodeIterator _source;
    private int[] _data = null;
    private int _last = 0;
    private int _current = 0;
    private int _start = -1;

    public ReverseIterator(NodeIterator source) {
	_source = source;
    }

    public int next() {
	return _current > 0 ? _data[--_current] : END;
    }
	
    public NodeIterator setStartNode(int node) {
	if ((_data == null) || (node != _start)) {
	    _start = node;
	    _source.setStartNode(node);
	    _data = new int[INIT_DATA_SIZE];
	    _last = 0;
	    while ((node = _source.next()) != END) {
		if (_last == _data.length) {
		    int[] newArray = new int[_data.length * 2];
		    System.arraycopy(_data, 0, newArray, 0, _last);
		    _data = newArray;
		}
		_data[_last++] = node;
	    }
	}
	_startNode = _current = _last;
	return this;
    }

    public NodeIterator reset() {
	_current = _startNode;
	return this;
    }

    public int getPosition() {
	return (_last - _current);
    }

    public int getLast() {
	return _last;
    }

    public void setMark() {
	_source.setMark();
	_markedNode = _current;
    }

    public void gotoMark() {
	_source.gotoMark();
	_current = _markedNode;
    }

}
