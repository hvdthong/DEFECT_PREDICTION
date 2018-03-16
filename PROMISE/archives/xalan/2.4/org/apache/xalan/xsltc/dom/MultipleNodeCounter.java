package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.util.IntegerArray;

public abstract class MultipleNodeCounter extends NodeCounter {
    private NodeIterator _precSiblings = null;

    public MultipleNodeCounter(Translet translet,
			       DOM document, NodeIterator iterator) {
	super(translet, document, iterator);
    }
	
    public NodeCounter setStartNode(int node) {
	_node = node;
	_nodeType = _document.getType(node);
	_precSiblings = _document.getAxisIterator(PRECEDINGSIBLING);
	return this;
    }

    public String getCounter() {
	if (_value != Integer.MIN_VALUE) {
	    return formatNumbers(_value);
	}

	IntegerArray ancestors = new IntegerArray();

	int next = _node;
	while ((next = _document.getParent(next)) > END && 
	       !matchesFrom(next)) {
	    ancestors.add(next);
	}

	final int nAncestors = ancestors.cardinality();
	final int[] counters = new int[nAncestors]; 
	for (int i = 0; i < nAncestors; i++) {
	    counters[i] = Integer.MIN_VALUE;
	}

	for (int j = 0, i = nAncestors - 1; i >= 0 ; i--, j++) {
	    final int counter = counters[j];
	    final int ancestor = ancestors.at(i);

	    if (matchesCount(ancestor)) {
		_precSiblings.setStartNode(ancestor);
		while ((next = _precSiblings.next()) != END) {
		    if (matchesCount(next)) {
			counters[j] = (counters[j] == Integer.MIN_VALUE) ? 1 
			    : counters[j] + 1;		
		    }
		}
		counters[j] = counters[j] == Integer.MIN_VALUE
		    ? 1 
		    : counters[j] + 1;	
	    }
	}
	return formatNumbers(counters);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet,
						    DOM document,
						    NodeIterator iterator) {
	return new DefaultMultipleNodeCounter(translet, document, iterator);
    }

    static class DefaultMultipleNodeCounter extends MultipleNodeCounter {
	public DefaultMultipleNodeCounter(Translet translet,
					  DOM document,
					  NodeIterator iterator) {
	    super(translet, document, iterator);
	}
    }
}
