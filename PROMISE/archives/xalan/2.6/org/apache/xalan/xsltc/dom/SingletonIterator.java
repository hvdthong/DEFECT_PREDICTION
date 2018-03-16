package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public class SingletonIterator extends DTMAxisIteratorBase {
    private int _node;
    private final boolean _isConstant;

    public SingletonIterator() {
	this(Integer.MIN_VALUE, false);
    }

    public SingletonIterator(int node) {
	this(node, false);
    }

    public SingletonIterator(int node, boolean constant) {
	_node = _startNode = node;
	_isConstant = constant;
    }
    
    /**
     * Override the value of <tt>_node</tt> only when this
     * object was constructed using the empty constructor.
     */
    public DTMAxisIterator setStartNode(int node) {
	if (_isConstant) {
	    _node = _startNode;
	    return resetPosition();
	}
	else if (_isRestartable) {
	    if (_node <= 0)
		_node = _startNode = node;
	    return resetPosition();
	}
	return this;
    }
	
    public DTMAxisIterator reset() {
	if (_isConstant) {
	    _node = _startNode;
	    return resetPosition();
	}
	else {
	    final boolean temp = _isRestartable;
	    _isRestartable = true;
	    setStartNode(_startNode);
	    _isRestartable = temp;
	}
	return this;
    }
    
    public int next() {
	final int result = _node;
	_node = DTMAxisIterator.END;
	return returnNode(result);
    }

    public void setMark() {
	_markedNode = _node;
    }

    public void gotoMark() {
	_node = _markedNode;
    }
}
