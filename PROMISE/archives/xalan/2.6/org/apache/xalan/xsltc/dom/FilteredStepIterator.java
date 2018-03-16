package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;

/**
 * Extends a StepIterator by adding the ability to filter nodes. It 
 * uses filters similar to those of a FilterIterator.
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
public final class FilteredStepIterator extends StepIterator {

    private Filter _filter;

    public FilteredStepIterator(DTMAxisIterator source,
				DTMAxisIterator iterator,
				Filter filter) {
	super(source, iterator);
	_filter = filter;
    }

    public int next() {
	int node;
	while ((node = super.next()) != END) {
	    if (_filter.test(node)) {
		return returnNode(node);
	    }
	}
	return node;
    }

}
