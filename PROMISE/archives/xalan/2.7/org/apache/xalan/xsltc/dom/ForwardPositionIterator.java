package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

/**
 * This iterator is a wrapper that always returns the position of
 * a node in document order. It is needed for the case where 
 * a call to position() occurs in the context of an XSLT element
 * such as xsl:for-each, xsl:apply-templates, etc. 
 *
 * The getPosition() methods in DTMAxisIterators defined
 * in DTMDefaultBaseIterators always return the position
 * in document order, which is backwards for XPath in the
 * case of the ancestor, ancestor-or-self, previous and
 * previous-sibling.
 *
 * XSLTC implements position() with the
 * BasisLibrary.positionF() method, and uses the
 * DTMAxisIterator.isReverse() method to determine
 * whether the result of getPosition() should be
 * interpreted as being equal to position().
 * But when the expression appears in apply-templates of
 * for-each, the position() function operates in document
 * order.
 *
 * The only effect of the ForwardPositionIterator is to force
 * the result of isReverse() to false, so that
 * BasisLibrary.positionF() calculates position() in a way
 * that's consistent with the context in which the
 * iterator is being used."
 *
 * (Apparently the correction of isReverse() occurs
 * implicitly, by inheritance. This class also appears
 * to maintain its own position counter, which seems
 * redundant.)
 *
 * @deprecated This class exists only for backwards compatibility with old
 *             translets.  New code should not reference it.
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
