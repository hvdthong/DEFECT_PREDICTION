package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public final class FilterIterator extends NodeIteratorBase {
    private NodeIterator _source;
    private final Filter _filter;
    private final boolean _isReverse;
	
    public FilterIterator(NodeIterator source, Filter filter) {
	_source = source;
	_filter = filter;
	_isReverse = source.isReverse();
    }

    public boolean isReverse() {
	return _isReverse;
    }

    public void setRestartable(boolean isRestartable) {
	_isRestartable = isRestartable;
	_source.setRestartable(isRestartable);
    }

    public NodeIterator cloneIterator() {
	try {
	    final FilterIterator clone = (FilterIterator)super.clone();
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
    
    public int next() {
	int node;
	while ((node = _source.next()) != END) {
	    if (_filter.test(node)) {
		return returnNode(node);
	    }
	}
	return END;
    }

    public NodeIterator setStartNode(int node) {
	if (_isRestartable) {
	    _source.setStartNode(_startNode = node); 
	    return resetPosition();
	}
	return this;
    }

    public void setMark() {
	_source.setMark();
    }

    public void gotoMark() {
	_source.gotoMark();
    }

}
