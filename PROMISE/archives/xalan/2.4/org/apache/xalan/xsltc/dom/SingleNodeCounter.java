package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.NodeIterator;

public abstract class SingleNodeCounter extends NodeCounter {
    static private final int[] EmptyArray = new int[] { };
    NodeIterator _countSiblings = null;

    public SingleNodeCounter(Translet translet,
			     DOM document,
			     NodeIterator iterator) {
	super(translet, document, iterator);
    }

    public NodeCounter setStartNode(int node) {
	_node = node;
	_nodeType = _document.getType(node);
	_countSiblings = _document.getAxisIterator(PRECEDINGSIBLING);
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
	    if (!matchesCount(next)) {
		while ((next = _document.getParent(next)) > END) {
		    if (matchesCount(next)) {
		    }
		    if (matchesFrom(next)) {
			next = END;
		    }
		}
	    }

	    if (next != END) {
		_countSiblings.setStartNode(next);
		do {
		    if (matchesCount(next)) result++;
		} while ((next = _countSiblings.next()) != END);
	    }
	    else {
		return formatNumbers(EmptyArray);
	    }
	}
	return formatNumbers(result);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet,
						    DOM document,
						    NodeIterator iterator) {
	return new DefaultSingleNodeCounter(translet, document, iterator);
    }

    static class DefaultSingleNodeCounter extends SingleNodeCounter {
	public DefaultSingleNodeCounter(Translet translet,
					DOM document, NodeIterator iterator) {
	    super(translet, document, iterator);
	}

	public NodeCounter setStartNode(int node) {
	    _node = node;
	    _nodeType = _document.getType(node);
	    _countSiblings =
		_document.getTypedAxisIterator(PRECEDINGSIBLING,
					       _document.getType(node));
	    return this;
	}

	public String getCounter() {
	    int result;
	    if (_value != Integer.MIN_VALUE) {
		result = _value;
	    }
	    else {
		int next;
		result = 1;
		_countSiblings.setStartNode(_node);
		while ((next = _countSiblings.next()) != END) {
		    result++;
		}
	    }
	    return formatNumbers(result);
	}
    }
}

