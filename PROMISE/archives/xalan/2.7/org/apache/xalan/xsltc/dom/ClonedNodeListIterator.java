package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;

/**
 * A ClonedNodeListIterator is returned by the cloneIterator() method
 * of a CachedNodeListIterator. Its next() method retrieves the nodes from
 * the cache of the CachedNodeListIterator.
 */
public final class ClonedNodeListIterator extends DTMAxisIteratorBase {

    /**
     * Source for this iterator.
     */
    private CachedNodeListIterator _source;
    private int _index = 0;

    public ClonedNodeListIterator(CachedNodeListIterator source) {
	_source = source;
    }

    public void setRestartable(boolean isRestartable) {
    }

    public DTMAxisIterator setStartNode(int node) {
	return this;
    }

    public int next() {
        return _source.getNode(_index++);
    }
    
    public int getPosition() {
    	return _index == 0 ? 1 : _index;
    }

    public int getNodeByPosition(int pos) {
    	return _source.getNode(pos);
    }
    
    public DTMAxisIterator cloneIterator() {
	return _source.cloneIterator();
    }

    public DTMAxisIterator reset() {
    	_index = 0;
    	return this;
    }
    
    public void setMark() {
	_source.setMark();
    }

    public void gotoMark() {
	_source.gotoMark();
    }
}
