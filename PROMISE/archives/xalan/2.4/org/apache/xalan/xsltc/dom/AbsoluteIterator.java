package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public final class AbsoluteIterator extends NodeIteratorBase {
    private NodeIterator _source;
	
    public AbsoluteIterator(NodeIterator source) {
	_source = source;
    }

    public int next() {
	return returnNode(_source.next());
    }

    public void setRestartable(boolean isRestartable) {
	_isRestartable = isRestartable;
	_source.setRestartable(isRestartable);
    }
	
    int _mask = -1;

    public NodeIterator setStartNode(int node) {
	if (_mask == -1) {
            _mask = node & 0xFF000000;
        }
	_startNode = _mask | DOM.ROOTNODE;
	if (_isRestartable) {
	    resetPosition();
	    return _source.setStartNode(_startNode = _mask | DOM.ROOTNODE);
	}
	return reset();
    }

    public NodeIterator cloneIterator() {
	try {
	    final AbsoluteIterator clone = (AbsoluteIterator)super.clone();
	    clone.setRestartable(false);
	    clone._source = _source.cloneIterator();
	    return clone.reset();
	}
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }

    public NodeIterator reset() {
	_source.reset();
	return resetPosition();
    }
    
    public void setMark() {
	_source.setMark();
    }

    public void gotoMark() {
	_source.gotoMark();
    }
}
