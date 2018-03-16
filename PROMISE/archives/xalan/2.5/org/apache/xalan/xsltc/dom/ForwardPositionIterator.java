package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

/**
 * This iterator is a wrapper that always returns the position of
 * a node in document order. It is needed for the case where 
 * a call to position() occurs in the context of an XSLT element
 * such as xsl:for-each, xsl:apply-templates, etc. 
 */
public final class ForwardPositionIterator extends DTMAxisIteratorBase {

    private DTMAxisIterator _source;

    public ForwardPositionIterator(DTMAxisIterator source) {
	_source = source;
    }

    public DTMAxisIterator cloneIterator() {
	try {
	    final ForwardPositionIterator clone = 
		(ForwardPositionIterator) super.clone();
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

    public int next() {
	return returnNode(_source.next());
    }
	
    public DTMAxisIterator setStartNode(int node) {
	_source.setStartNode(node);
	return this;
    }

    public DTMAxisIterator reset() {
	_source.reset();
	return resetPosition();
    }

    public void setMark() {
	_source.setMark();
    }

    public void gotoMark() {
	_source.gotoMark();
    }
}
