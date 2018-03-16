package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public final class MatchingIterator extends NodeIteratorBase {
    private NodeIterator _source;
    private final int    _match;
    private int          _matchPos, _matchLast = -1;
	
    public MatchingIterator(int match, NodeIterator source) {
	_source = source;
	_match = match;
    }

    public void setRestartable(boolean isRestartable) {
	_isRestartable = isRestartable;
	_source.setRestartable(isRestartable);
    }

    public NodeIterator cloneIterator() {
	try {
	    final MatchingIterator clone = (MatchingIterator)super.clone();
	    clone._source = _source.cloneIterator();
	    clone.setRestartable(false);
	    return clone;
	}
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }
    
    public NodeIterator setStartNode(int node) {
	if (_isRestartable) {
	    _source.setStartNode(node);

	    _matchPos = 1;
	    _matchLast = -1;
	    while ( ((node = _source.next()) != END) && (node != _match) )
		_matchPos++;
	}
	return this;
    }

    public NodeIterator reset() {
	_source.reset();
	return this;
    }
    
    public int next() {
	return _source.next();
    }
	
    public int getLast() {
	if (_matchLast == -1)
	    _matchLast = _source.getLast();
	return _matchLast;
    }

    public int getPosition() {
	return _matchPos;
    }

    public void setMark() {
	_source.setMark();
    }

    public void gotoMark() {
	_source.gotoMark();
    }
}
