package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

/**
 * A step iterator is used to evaluate expressions like "BOOK/TITLE". 
 * A better name for this iterator would have been ParentIterator since 
 * both "BOOK" and "TITLE" are steps in XPath lingo. Step iterators are 
 * constructed from two other iterators which we are going to refer to 
 * as "outer" and "inner". Every node from the outer iterator (the one 
 * for BOOK in our example) is used to initialize the inner iterator. 
 * After this initialization, every node from the inner iterator is 
 * returned (in essence, implementing a "nested loop").
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Erwin Bolwidt <ejb@klomp.org>
 * @author Morten Jorgensen
 */
public class StepIterator extends DTMAxisIteratorBase {

    /**
     * A reference to the "outer" iterator.
     */
    protected DTMAxisIterator _source;

    /**
     * A reference to the "inner" iterator.
     */
    protected DTMAxisIterator _iterator;

    /**
     * Temp variable to store a marked position.
     */
    private int _pos = -1;

    public StepIterator(DTMAxisIterator source, DTMAxisIterator iterator) {
	_source = source;
	_iterator = iterator;
    }


    public void setRestartable(boolean isRestartable) {
	_isRestartable = isRestartable;
	_source.setRestartable(isRestartable);
    }

    public DTMAxisIterator cloneIterator() {
	_isRestartable = false;
	try {
	    final StepIterator clone = (StepIterator) super.clone();
	    clone._source = _source.cloneIterator();
	    clone._iterator = _iterator.cloneIterator();
	    clone._isRestartable = false;
	    return clone.reset();
	}
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }
    
    public DTMAxisIterator setStartNode(int node) {
	if (_isRestartable) {
	    _source.setStartNode(_startNode = node);

	    _iterator.setStartNode(_includeSelf ? _startNode : _source.next());
	    return resetPosition();
	}
	return this;
    }

    public DTMAxisIterator reset() {
	_source.reset();
	_iterator.setStartNode(_includeSelf ? _startNode : _source.next());
	return resetPosition();
    }
    
    public int next() {
	for (int node;;) {
	    if ((node = _iterator.next()) != END) {
		return returnNode(node);
	    }
	    else if ((node = _source.next()) == END) {
		return END;
	    }
	    else {
		_iterator.setStartNode(node);
	    }
	}
    }

    public void setMark() {
	_source.setMark();
	_iterator.setMark();
    }

    public void gotoMark() {
	_source.gotoMark();
	_iterator.gotoMark();
    }
}
