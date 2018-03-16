package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.Axis;


/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public abstract class SingleNodeCounter extends NodeCounter {
    static private final int[] EmptyArray = new int[] { };
    DTMAxisIterator _countSiblings = null;

    public SingleNodeCounter(Translet translet,
			     DOM document,
			     DTMAxisIterator iterator) {
	super(translet, document, iterator);
    }

    public NodeCounter setStartNode(int node) {
	_node = node;
	_nodeType = _document.getExpandedTypeID(node);
    _countSiblings = _document.getAxisIterator(Axis.PRECEDINGSIBLING);
	return this;
    }

    public String getCounter() {
	int result;
	if (_value != Integer.MIN_VALUE) {
                if (_value == 0) return "0";
                else if (Double.isNaN(_value)) return "NaN";
                else if (_value < 0 && Double.isInfinite(_value)) return "-Infinity";
                else if (Double.isInfinite(_value)) return "Infinity";
                else result = (int) _value;
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
						    DTMAxisIterator iterator) {
	return new DefaultSingleNodeCounter(translet, document, iterator);
    }

    static class DefaultSingleNodeCounter extends SingleNodeCounter {
	public DefaultSingleNodeCounter(Translet translet,
					DOM document, DTMAxisIterator iterator) {
	    super(translet, document, iterator);
	}

	public NodeCounter setStartNode(int node) {
	    _node = node;
	    _nodeType = _document.getExpandedTypeID(node);
	    _countSiblings =
        _document.getTypedAxisIterator(Axis.PRECEDINGSIBLING,
					       _document.getExpandedTypeID(node));
	    return this;
	}

	public String getCounter() {
	    int result;
	    if (_value != Integer.MIN_VALUE) {
                if (_value == 0) return "0";
                else if (Double.isNaN(_value)) return "NaN";
                else if (_value < 0 && Double.isInfinite(_value)) return "-Infinity";
                else if (Double.isInfinite(_value)) return "Infinity";
                else result = (int) _value;
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

