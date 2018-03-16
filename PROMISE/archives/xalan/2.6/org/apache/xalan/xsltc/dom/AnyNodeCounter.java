package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public abstract class AnyNodeCounter extends NodeCounter {
    public AnyNodeCounter(Translet translet,
			  DOM document, DTMAxisIterator iterator) {
	super(translet, document, iterator);
    }
	
    public NodeCounter setStartNode(int node) {
	_node = node;
	_nodeType = _document.getExpandedTypeID(node);
	return this;
    }

    public String getCounter() {
	int result;
	if (_value != Integer.MIN_VALUE) {
	    result = _value;
	}
	else {
	    int next = _node; 
            final int root = _document.getDocument();
	    result = 0;
	    while (next >= root && !matchesFrom(next)) {
		if (matchesCount(next)) {
		    ++result;	
		}
		next--;
		/*
		if (next == root) {
		    break;
                }
		else {
		    --next;		
                }
                */
	    }
	}
	return formatNumbers(result);
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet,
						    DOM document,
						    DTMAxisIterator iterator) {
	return new DefaultAnyNodeCounter(translet, document, iterator);
    }

    static class DefaultAnyNodeCounter extends AnyNodeCounter {
	public DefaultAnyNodeCounter(Translet translet,
				     DOM document, DTMAxisIterator iterator) {
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
		final int ntype = _document.getExpandedTypeID(_node);
                final int root = _document.getDocument();
		while (next >= 0) {
		    if (ntype == _document.getExpandedTypeID(next)) {
			result++;
		    }
		    if (next == root) {
		        break;
                    }
		    else {
		        --next;
                    }
		}
	    }
	    return formatNumbers(result);
	}
    }
}
