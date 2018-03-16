package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.DTMFilter;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

/**
 * Similar to a CurrentNodeListIterator except that the filter has a 
 * simpler interface (only needs the node, no position, last, etc.)  
 * It takes a source iterator and a Filter object and returns nodes 
 * from the source after filtering them by calling filter.test(node).
 */
public final class FilterIterator extends DTMAxisIteratorBase {

    /**
     * Reference to source iterator.
     */
    private DTMAxisIterator _source;

    /**
     * Reference to a filter object that to be applied to each node.
     */
    private final DTMFilter _filter;

    /**
     * A flag indicating if position is reversed.
     */
    private final boolean _isReverse;
	
    public FilterIterator(DTMAxisIterator source, DTMFilter filter) {
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

    public DTMAxisIterator cloneIterator() {

	try {
	    final FilterIterator clone = (FilterIterator) super.clone();
	    clone._source = _source.cloneIterator();
	    clone._isRestartable = false;
	    return clone.reset();
	}
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }
    
    public DTMAxisIterator reset() {
	_source.reset();
	return resetPosition();
    }
    
    public int next() {
	int node;
	while ((node = _source.next()) != END) {
	    if (_filter.acceptNode(node, DTMFilter.SHOW_ALL) == DTMIterator.FILTER_ACCEPT) {
		return returnNode(node);
	    }
	}
	return END;
    }

    public DTMAxisIterator setStartNode(int node) {
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
