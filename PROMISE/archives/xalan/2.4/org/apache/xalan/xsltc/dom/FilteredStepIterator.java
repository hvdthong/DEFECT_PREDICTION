package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public final class FilteredStepIterator extends StepIterator {

    private Filter _filter;
	
    public FilteredStepIterator(NodeIterator source,
				NodeIterator iterator,
				Filter filter) {
	super(source, iterator);
	_filter = filter;
    }

    public NodeIterator cloneIterator() {
	try {
	    final FilteredStepIterator clone =
		(FilteredStepIterator)super.clone();
	    clone._source = _source.cloneIterator();
	    clone._iterator = _iterator.cloneIterator();
	    clone._filter = _filter;
	    clone.setRestartable(false);
	    return clone.reset();
	}
	catch (CloneNotSupportedException e) {
	    BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR,
				      e.toString());
	    return null;
	}
    }

    public int next() {
	int node;
	while ((node = super.next()) != END) {
	    if (_filter.test(node))
		return returnNode(node);
	}
	return(node);
    }

}
