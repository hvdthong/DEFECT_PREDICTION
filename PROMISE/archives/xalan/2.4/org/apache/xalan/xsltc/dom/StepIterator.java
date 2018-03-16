package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public class StepIterator extends NodeIteratorBase {

    protected NodeIterator _source;
    protected NodeIterator _iterator;
    private int _pos = -1;

    public StepIterator(NodeIterator source, NodeIterator iterator) {
	_source = source;
	_iterator = iterator;
    }

    public void setRestartable(boolean isRestartable) {
	_isRestartable = isRestartable;
	_source.setRestartable(isRestartable);
    }

    public NodeIterator cloneIterator() {
	try {
	    final StepIterator clone = (StepIterator)super.clone();
	    clone._source = _source.cloneIterator();
	    clone._iterator = _iterator.cloneIterator();
	    clone.setRestartable(false);
	    return clone.reset();
	}
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }
    
    public NodeIterator setStartNode(int node) {
	if (_isRestartable) {
	    _source.setStartNode(_startNode = node);

	    _iterator.setStartNode(_includeSelf ? _startNode : _source.next());
	    return resetPosition();
	}
	return this;
    }

    public NodeIterator reset() {
	_source.reset();
	_iterator.setStartNode(_includeSelf ? _startNode : _source.next());
	return resetPosition();
    }
    
    public int next() {
	for (int node;;) {
	    if ((node = _iterator.next()) != END) {
		return returnNode(node);
	    }
	    else if ((node = _source.next()) == END) {
		return END;
	    }
	    else {
		_iterator.setStartNode(node);
	    }
	}
    }

    public void setMark() {
	_source.setMark();
	_iterator.setMark();
	_pos = _position;
    }

    public void gotoMark() {
	_source.gotoMark();
	_iterator.gotoMark();
	_position = _pos;
    }
}
