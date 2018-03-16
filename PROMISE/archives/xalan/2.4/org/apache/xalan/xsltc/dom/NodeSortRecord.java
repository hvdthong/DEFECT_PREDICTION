package org.apache.xalan.xsltc.dom;

import java.util.Vector;
import java.text.Collator;
import java.text.CollationKey;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;

/**
 * Base class for sort records containing application specific sort keys 
 */
public abstract class NodeSortRecord {
    public static int COMPARE_STRING     = 0;
    public static int COMPARE_NUMERIC    = 1;

    public static int COMPARE_ASCENDING  = 0;
    public static int COMPARE_DESCENDING = 1;

    protected static Collator _collator = Collator.getInstance();

    protected int   _levels = 1;
    protected int[] _compareType;
    protected int[] _sortOrder;

    private AbstractTranslet _translet = null;

    private DOM    _dom = null;


    /**
     * This constructor is run by a call to Class.forName() in the
     * makeNodeSortRecord method in the NodeSortRecordFactory class. Since we
     * cannot pass any parameters to the constructor in that case we just set
     * the default values here and wait for new values through initialize().
     */ 
    public NodeSortRecord(int node) {
	_node = node;
    }

    public NodeSortRecord() {
        this(0);
    }

    /**
     * This method allows the caller to set the values that could not be passed
     * to the default constructor.
     */
    public final void initialize(int node, int last, DOM dom,
				 AbstractTranslet translet,
				 int[] order, int[] type) {
	_dom = dom;
	_node = node;
	_last = last;
	_translet = translet;
	_scanned = 0;

	_levels = order.length;
	_sortOrder = order;
	_compareType = type;

	_values = new Object[_levels];
    }

    /**
     * Returns the node for this sort object
     */
    public final int getNode() {
	return _node;
    }

    /**
     *
     */
    public final int compareDocOrder(NodeSortRecord other) {
	return _node - other._node;
    }

    /**
     * Get the string or numeric value of a specific level key for this sort
     * element. The value is extracted from the DOM if it is not already in
     * our sort key vector.
     */
    private final CollationKey stringValue(int level) {
	if (_scanned <= level) {
	    final String str = extractValueFromDOM(_dom, _node, level,
						   _translet, _last);
	    final CollationKey key = _collator.getCollationKey(str);
	    _values[_scanned++] = key;
	    return(key);
	}
	return((CollationKey)_values[level]);
    }
    
    private final Double numericValue(int level) {
	if (_scanned <= level) {
	    final String str = extractValueFromDOM(_dom, _node, level,
						   _translet, _last);
	    Double num;
	    try {
		num = new Double(str);
	    }
	    catch (NumberFormatException e) {
		num = new Double(Double.NEGATIVE_INFINITY);
	    }
	    _values[_scanned++] = num;
	    return(num);
	}
	return((Double)_values[level]);
    }

    /**
     * Compare this sort element to another. The first level is checked first,
     * and we proceed to the next level only if the first level keys are
     * identical (and so the key values may not even be extracted from the DOM)
     *
     * !!!!MUST OPTIMISE - THIS IS REALLY, REALLY SLOW!!!!
     */
    public int compareTo(NodeSortRecord other) {
	int cmp, level;
	for (level = 0; level < _levels; level++) {
	    if (_compareType[level] == COMPARE_NUMERIC) {
		final Double our = numericValue(level);
		final Double their = other.numericValue(level);
		cmp = our.compareTo(their);
	    }
	    else {
		final CollationKey our = stringValue(level);
		final CollationKey their = other.stringValue(level);
		cmp = our.compareTo(their);
	    }
	    
	    if (cmp != 0) {
		return _sortOrder[level] == COMPARE_DESCENDING ? 0 - cmp : cmp;
	    }
	}
	return(_node - other._node);
    }

    /**
     * Returns the Collator used for text comparisons in this object.
     * May be overridden by inheriting classes
     */
    public Collator getCollator() {
	return _collator;
    }

    /**
     * Extract the sort value for a level of this key.
     */
    public abstract String extractValueFromDOM(DOM dom, int current, int level,
					       AbstractTranslet translet,
					       int last);

}
