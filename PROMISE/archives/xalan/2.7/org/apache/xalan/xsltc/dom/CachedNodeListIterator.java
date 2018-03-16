package org.apache.xalan.xsltc.dom;

import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMAxisIteratorBase;
import org.apache.xalan.xsltc.util.IntegerArray;

/**
 * CachedNodeListIterator is used for select expressions in a 
 * variable or parameter. This iterator caches all nodes in an 
 * IntegerArray. Its cloneIterator() method is overridden to 
 * return an object of ClonedNodeListIterator.
 */
public final class CachedNodeListIterator extends DTMAxisIteratorBase {

    /**
     * Source for this iterator.
     */
    private DTMAxisIterator _source;
    private IntegerArray _nodes = new IntegerArray();
    private int _numCachedNodes = 0;
    private int _index = 0;
    private boolean _isEnded = false;

    public CachedNodeListIterator(DTMAxisIterator source) {
	_source = source;
    }

    public void setRestartable(boolean isRestartable) {
    }

    public DTMAxisIterator setStartNode(int node) {
	if (_isRestartable) {
	    _startNode = node;
	    _source.setStartNode(node);
	    resetPosition();
	    
	    _isRestartable = false;
	}
	return this;
    }

    public int next() {
        return getNode(_index++);
    }
    
    public int getPosition() {
    	return _index == 0 ? 1 : _index;
    }
    
    public int getNodeByPosition(int pos) {
    	return getNode(pos);
    }
        
    public int getNode(int index) {
        if (index < _numCachedNodes) {
            return _nodes.at(index);
        }
        else if (!_isEnded){
            int node = _source.next();
            if (node != END) {
            	_nodes.add(node);
            	_numCachedNodes++;
            }
            else {
            	_isEnded = true;
            }
            return node;
        }
        else
            return END;
    }

    public DTMAxisIterator cloneIterator() {
	ClonedNodeListIterator clone = new ClonedNodeListIterator(this);
	return clone;
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
