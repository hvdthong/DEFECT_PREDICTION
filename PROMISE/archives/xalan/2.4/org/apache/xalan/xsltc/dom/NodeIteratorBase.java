package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public abstract class NodeIteratorBase implements NodeIterator {
    private int _last = -1;
    protected int _position = 0;

    protected int _markedNode;
    protected int _startNode = NodeIterator.END;
    protected boolean _includeSelf = false;
    protected boolean _isRestartable = true;

    public void setRestartable(boolean isRestartable) {
	_isRestartable = isRestartable;
    }

    public NodeIterator reset() {
	final boolean temp = _isRestartable;
	_isRestartable = true;
	setStartNode(_includeSelf ? _startNode + 1 : _startNode);
	_isRestartable = temp;
	return this;
    }

    public NodeIterator includeSelf() {
	_includeSelf = true;
	return this;
    }

    public int getLast() {
	if (_last == -1) {
	    final int temp = _position;
	    setMark();
	    reset();
	    do {
		_last++;
	    } while (next() != END);
	    gotoMark();
	    _position = temp;
	}
	return _last;
    }

    public int getPosition() {
	return _position == 0 ? 1 : _position;
    }

    public boolean isReverse() {
	return false;
    }
    
    public NodeIterator cloneIterator() {
	try {
	    final NodeIteratorBase clone = (NodeIteratorBase)super.clone();
	    clone._isRestartable = false;
	    return clone.reset();
	}
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }
    
    protected final int returnNode(final int node) {
	_position++;
	return node;
    }
    
    protected final NodeIterator resetPosition() {
	_position = 0;
	return this;
    }

}
