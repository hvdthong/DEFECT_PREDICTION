package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public final class CurrentNodeListIterator extends NodeIteratorBase {

    private boolean _docOrder;
    private NodeIterator _source;
    private final CurrentNodeListFilter _filter;
    private IntegerArray _nodes = new IntegerArray();
	
    private int _last = -1;		
	
    private final int _currentNode;
    private AbstractTranslet _translet;

    public CurrentNodeListIterator(NodeIterator source, 
				   CurrentNodeListFilter filter,
				   int currentNode,
				   AbstractTranslet translet) 
    {
	this(source, !source.isReverse(), filter, currentNode, translet);
    }

    public CurrentNodeListIterator(NodeIterator source, boolean docOrder,
				   CurrentNodeListFilter filter,
				   int currentNode,
				   AbstractTranslet translet) 
    {
	_source = source;
	_filter = filter;
	_translet = translet;
	_docOrder = docOrder;
	_currentNode = currentNode;
    }

    public void setRestartable(boolean isRestartable) {
	_isRestartable = isRestartable;
	_source.setRestartable(isRestartable);
    }

    public boolean isReverse() {
	return !_docOrder;
    }

    public NodeIterator cloneIterator() {
	try {
	    final CurrentNodeListIterator clone =
		(CurrentNodeListIterator)super.clone();
	    clone._nodes = (IntegerArray)_nodes.clone();
	    clone.setRestartable(false);
	    return clone.reset();
	}
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }
    
    public NodeIterator reset() {
	_current = 0;
	return resetPosition();
    }

    public int next() {
	final int last = _nodes.cardinality();
	final int currentNode = _currentNode;
	final AbstractTranslet translet = _translet;

	for (int index = _current; index < last; ) {
	    if (_filter.test(node, index, last, currentNode, translet, this)) {
		_current = index;
		return returnNode(node);
	    }
	}
	return END;
    }

    public NodeIterator setStartNode(int node) {
	NodeIterator retval = this;
	
	if (_isRestartable) {
	    _source.setStartNode(_startNode = node);

	    _nodes.clear();
	    while ((node = _source.next()) != END) {
		_nodes.add(node);
	    }
	    _current = 0;
	    retval = resetPosition();
	}
	return retval;
    }
	
    public int getLast() {
	if (_last == -1) {
	    _last = computePositionOfLast();
	}
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

    private int computePositionOfLast() {
        final int last = _nodes.cardinality();
        final int currNode = _currentNode;
	final AbstractTranslet translet = _translet;

	int lastPosition = 0;
	for (int index = _current; index < last; ) {
            if (_filter.test(nodeIndex, index, last, currNode, translet, this)) {
                lastPosition++;
            }
        }
	return lastPosition;
    }
}

