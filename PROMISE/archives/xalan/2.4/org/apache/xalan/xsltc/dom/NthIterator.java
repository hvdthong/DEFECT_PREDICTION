package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public final class NthIterator extends NodeIteratorBase {
    private NodeIterator _source;
    private final int _position;
    private boolean _ready;

    public NthIterator(NodeIterator source, int n) {
	_source = source;
	_position = n;
    }

    public void setRestartable(boolean isRestartable) {
	_isRestartable = isRestartable;
	_source.setRestartable(isRestartable);
    }
    
    public int next() {
	if (_ready && _position > 0) {
	    _ready = false;
	    final int pos = _position;
	    for (int n = pos - 1; n-- > 0;) {
		if (_source.next() == NodeIterator.END) {
		    return NodeIterator.END;
		}
	    }
	    return _source.next();
	}
	return NodeIterator.END;
    }
	
    public NodeIterator setStartNode(final int node) {
	if (_isRestartable) {
	    _source.setStartNode(node);
	    _ready = true;
	}
	return this;
    }
	
    public NodeIterator reset() {
	_source.reset();
	_ready = true;
	return this;
    }
    
    public int getLast() {
	return 1;
    }
    
    public int getPosition() {
	return 1;
    }
    
    public boolean isReverse() {
	return _source.isReverse();
    }
    
    public void setMark() {
	_source.setMark();
    }
    
    public void gotoMark() {
	_source.gotoMark();
    }

    public NodeIterator cloneIterator() {
	NodeIterator clone = _source.cloneIterator();
	NthIterator other = new NthIterator(clone, _position);
	other.setRestartable(false);
	return other.reset();
    }
}
