package org.apache.xerces.dom;

import org.w3c.dom.Node;

/** 
 * An interface for deferred node object. 
 *
 * @version
 */
public interface DeferredNode extends Node {


    /** Returns the node index. */
    public int getNodeIndex();

