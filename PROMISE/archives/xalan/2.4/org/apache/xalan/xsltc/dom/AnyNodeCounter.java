package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.NodeIterator;

public abstract class AnyNodeCounter extends NodeCounter {
    public AnyNodeCounter(Translet translet,
			  DOM document, NodeIterator iterator) {
	super(translet, document, iterator);
    }
	
    public NodeCounter setStartNode(int node) {
	_node = node;
	_nodeType = _document.getType(node);
	return this;
    }

    public String getCounter() {
	int result;
	if (_value != Integer.MIN_VALUE) {
	    result = _value;
	}
	else {
	    int next = _node;
	    result = 0;
	    while (next >= 0 && !matchesFrom(next)) {
		if (matchesCount(next)) {
		    ++result;	
		}
		--next;
	    }
	}
	return formatNumbers(result);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet,
						    DOM document,
						    NodeIterator iterator) {
	return new DefaultAnyNodeCounter(translet, document, iterator);
    }

    static class DefaultAnyNodeCounter extends AnyNodeCounter {
	public DefaultAnyNodeCounter(Translet translet,
				     DOM document, NodeIterator iterator) {
	    super(translet, document, iterator);
	}

	public String getCounter() {
	    int result;
	    if (_value != Integer.MIN_VALUE) {
		result = _value;
	    }
	    else {
		int next = _node;
		result = 0;
		final int ntype = _document.getType(_node); 
		while (next >= 0) {
		    if (ntype == _document.getType(next)) {
			result++;
		    }
		    next--;
		}
	    }
	    return formatNumbers(result);
	}
    }
}
