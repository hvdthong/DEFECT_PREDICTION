package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;
import org.apache.xerces.dom.DOMExceptionImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.range.*;
import java.util.Vector;

/** The RangeImpl class implements the org.w3c.dom.range.Range interface.
 *  <p> Please see the API documentation for the interface classes  
 *  and use the interfaces in your client programs.
 */
public class RangeImpl  implements Range {
    
    

    static final int START = 0;
    static final int AFTER = 1;
    static final int BEFORE = -1;
    
    
    DocumentImpl fDocument;
    Node fStartContainer;
    Node fEndContainer;
    int fStartOffset;
    int fEndOffset;
    boolean fIsCollapsed;   
    boolean fDetach = false;
    Node fInsertNode = null;
    Node fDeleteNode = null;
    Node fSplitNode = null;
    
    
    /** The constructor. Clients must use DocumentRange.createRange(),
     *  because it registers the Range with the document, so it can 
     *  be fixed-up.
     */
    public RangeImpl(DocumentImpl document) {
        fDocument = document;
        fStartContainer = document;
        fEndContainer = document;
        fStartOffset = 0;
        fEndOffset = 0;
        fDetach = false;
    }
    
    public Node getStartContainer() {
        return fStartContainer;
    }
    
    public int getStartOffset() {
        return fStartOffset;
    }
    
    public Node getEndContainer() {
        return fEndContainer;
    }
    public int getEndOffset() {
        return fEndOffset;
    }
    
    public boolean getCollapsed() {
        return (fStartContainer == fEndContainer 
             && fStartOffset == fEndOffset);
    }
    
    public Node getCommonAncestorContainer(){
        Vector startV = new Vector();
        Node node;
        for (node=fStartContainer; node != null; 
             node=node.getParentNode()) 
        {
            startV.addElement(node);
        }
        Vector endV = new Vector();
        for (node=fEndContainer; node != null; 
             node=node.getParentNode()) 
        {
            endV.addElement(node);
        }
        int s = startV.size()-1;
        int e = endV.size()-1;
        Object result = null;
        while (s>=0 && e>=0) {
            if (startV.elementAt(s) == endV.elementAt(e)) {
                result = startV.elementAt(s);
            } else {
                break;
            }
            --s;
            --e;
        }
        return (Node)result; 
    }
    
    
    public void setStart(Node refNode, int offset)
                         throws RangeException, DOMException
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        if ( !isAncestorTypeValid(refNode)) {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        
        checkIndex(refNode, offset);
        
        fStartContainer = refNode;
        fStartOffset = offset;
    }
    
    public void setEnd(Node refNode, int offset)
                       throws RangeException, DOMException
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        if ( !isAncestorTypeValid(refNode)) {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        
        checkIndex(refNode, offset);
        
        fEndContainer = refNode;
        fEndOffset = offset;
    }
    public void setStartBefore(Node refNode) 
        throws RangeException 
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        if ( !isAncestorTypeValid(refNode)) {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        fStartContainer = refNode.getParentNode();
        int i = 0;
        for (Node n = refNode; n!=null; n = n.getPreviousSibling()) {
            i++;
        }
        fStartOffset = i-1;
    }
    public void setStartAfter(Node refNode)
        throws RangeException
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        if ( !isAncestorTypeValid(refNode)) {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        fStartContainer = refNode.getParentNode();
        int i = 0;
        for (Node n = refNode; n!=null; n = n.getPreviousSibling()) {
            i++;
        }
        fStartOffset = i;
    }
    public void setEndBefore(Node refNode)
        throws RangeException
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        if ( !isAncestorTypeValid(refNode)) {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        fEndContainer = refNode.getParentNode();
        int i = 0;
        for (Node n = refNode; n!=null; n = n.getPreviousSibling()) {
            i++;
        }
        fEndOffset = i-1;
    }
                                            
    public void setEndAfter(Node refNode)
        throws RangeException
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        if ( !isAncestorTypeValid(refNode)) {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        fEndContainer = refNode.getParentNode();
        int i = 0;
        for (Node n = refNode; n!=null; n = n.getPreviousSibling()) {
            i++;
        }
        fEndOffset = i;
    }
    public void collapse(boolean toStart) {
        
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        
        if (toStart) {
            fEndContainer = fStartContainer;
            fEndOffset = fStartOffset;
        } else {
            fStartContainer = fEndContainer;
            fStartOffset = fEndOffset;
        }
    }
    
    public void selectNode(Node refNode)
        throws RangeException
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        if ( !isAncestorTypeValid(refNode)) {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        Node parent = refNode.getParentNode();
        {
            fStartContainer = parent;
            fEndContainer = parent;
            int i = 0;
            for (Node n = refNode; n!=null; n = n.getPreviousSibling()) {
                i++;
            }
            fStartOffset = i-1;
            fEndOffset = fStartOffset+1;
        }
    }
        
    public void selectNodeContents(Node refNode)
        throws RangeException
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        if ( !isAncestorTypeValid(refNode)) {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        fStartContainer = refNode;
        fEndContainer = refNode;
        Node first = refNode.getFirstChild();
        fStartOffset = 0;
        if (first == null) {
            fEndOffset = 0;
        } else {
            int i = 0;
            for (Node n = first; n!=null; n = n.getNextSibling()) {
                i++;
            }
            fEndOffset = i;
        }
        
    }

    public short compareBoundaryPoints(short how, Range sourceRange)
        throws DOMException
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
    	}
       
        Node endPointA;
        Node endPointB;
        int offsetA;
        int offsetB;
        
        if (how == START_TO_START) {
            endPointA = sourceRange.getStartContainer();
            endPointB = fStartContainer;
            offsetA = sourceRange.getStartOffset();
            offsetB = fStartOffset;
        } else 
        if (how == START_TO_END) {
            endPointA = sourceRange.getStartContainer();
            endPointB = fEndContainer;
            offsetA = sourceRange.getStartOffset();
            offsetB = fEndOffset;
        } else 
        if (how == END_TO_START) {
            endPointA = sourceRange.getEndContainer();
            endPointB = fStartContainer;
            offsetA = sourceRange.getEndOffset();
            offsetB = fStartOffset;
        } else {
            endPointA = sourceRange.getEndContainer();
            endPointB = fEndContainer;
            offsetA = sourceRange.getEndOffset();
            offsetB = fEndOffset;
        }
        
        if (endPointA == endPointB) {
            if (offsetA < offsetB) return -1;
            if (offsetA == offsetB) return 0;
            return 1;
        }
        for (Node node=endPointA.getFirstChild(); node != null; node=node.getNextSibling()) {
            if (isAncestorOf(node, endPointB)) {
                int index = indexOf(node, endPointA);
                if (offsetA <  index) return -1;
                if (offsetA == index) return 0;
                return 1;
            }
        }
        for (Node node=endPointB.getFirstChild(); node != null; node=node.getNextSibling()) {
            if (isAncestorOf(node, endPointA)) {
                int index = indexOf(node, endPointB);
                if (offsetB <  index) return -1;
                if (offsetB == index) return 0;
                return 1;
            }
        }
        Node ancestor = getCommonAncestorContainer();
        Node current = ancestor;
                
        do {
            if (current == endPointA) return -1;
            if (current == endPointB) return 1;
        }
        while (current!=null && current!=ancestor); 
        
        return -2;
    }
    
    public void deleteContents()
        throws DOMException
    {
        Node current = fStartContainer;
        Node parent = null;
        Node next;
        boolean deleteCurrent = false;
        Node root = getCommonAncestorContainer();
        
        if (fStartContainer == fEndContainer) {
                return; 
            } 
            if (fStartContainer.getNodeType() == Node.TEXT_NODE) {
                String value = fStartContainer.getNodeValue();
                int realStart = fStartOffset;
                int realEnd = fEndOffset;
                if (fStartOffset > value.length()) realStart = value.length()-1;
                if (fEndOffset > value.length()) realEnd = value.length()-1;
                
                deleteData((CharacterData)fStartContainer, realStart, realEnd-realStart);
                
                } else {
                current = fStartContainer.getFirstChild();
                int i = 0;
                for(i = 0; i < fStartOffset && current != null; i++) {
                    current=current.getNextSibling();
                }
                for(i = 0; i < fEndOffset-fStartOffset && current != null; i++) {
                    Node newCurrent=current.getNextSibling();
                    removeChild(fStartContainer, current);
                    current = newCurrent;
                }
            }
            collapse(true);
            return;
        }
        
        Node partialNode = null;
        int partialInt = START;
        
        Node startRoot = null;
        if (current.getNodeType() == Node.TEXT_NODE) {
            deleteData((CharacterData)current, fStartOffset, 
                current.getNodeValue().length()-fStartOffset);
        } else {
            current = current.getFirstChild();
            for (int i = 0 ; i < fStartOffset && current != null; i++){
                current = current.getNextSibling();
            }
            if (current==null) {
                current = fStartContainer;
            } else 
            if (current != fStartContainer)
            {
                deleteCurrent = true;
            }
        }
        
        
        while (current != root && current != null) {
            
            parent = current.getParentNode();
            if (parent == root) {
                if (startRoot == null)
                    startRoot = current;
            } else {
                if (partialNode==null) {
                    partialNode = parent;
                    partialInt = AFTER;
                }
            }

            
            if (parent != root) {
                next = current.getNextSibling();
                Node nextnext;
                while (next != null) {
                    nextnext = next.getNextSibling();
                    removeChild(parent, next);
                    next = nextnext;
                }
            }
            
            if (deleteCurrent) {
                removeChild(parent, current);
                deleteCurrent = false;
            }
            current = parent;
        }
        
        Node endRoot = null;
        current = fEndContainer;
        if (current.getNodeType() == Node.TEXT_NODE) {
            deleteData((CharacterData)current, 0, fEndOffset); 
        } else {
    
                current = fEndContainer;
            }
            else {
                current = current.getFirstChild();
                for(int i = 1; i < fEndOffset && current != null; i++) {
                    current=current.getNextSibling();
                }
                    current = fEndContainer.getLastChild();
                } else 
                if (current != fStartContainer) {
                    deleteCurrent = true;
                }
                
            }
        }
        
        while (current != root && current != null) {
            
            parent = current.getParentNode();
            if (parent == root) {
                if (endRoot == null)
                    endRoot = current;
            } else {
                if (partialNode==null) {
                    partialNode = parent;
                    partialInt = BEFORE;
                }
            }
       
            if (parent != root && parent != null) {
                next = current.getPreviousSibling();
                Node nextnext;
                while (next != null) {
                    nextnext = next.getPreviousSibling();
                    removeChild(parent, next);
                    next = nextnext;
                }
            }
            
            if (deleteCurrent) {
                removeChild(parent, current);
                deleteCurrent = false;
            }
            current = parent;
        }
        
        current = endRoot.getPreviousSibling();
        Node prev = null;
        while (current != null && current != startRoot ) {
            prev = current.getPreviousSibling();
            parent = current.getParentNode();
            if (parent != null) {
                removeChild(parent, current);
            }
            current = prev;
        }
        
        if (partialNode == null) {
            collapse(true);
        } else 
        if (partialInt == AFTER) {
            setStartAfter(partialNode);
            setEndAfter(partialNode);
        }
        else if (partialInt == BEFORE) {
            setStartBefore(partialNode);
            setEndBefore(partialNode);
        }        
    }
        
    public DocumentFragment extractContents()
        throws DOMException
    {
        return traverseContents(EXTRACT_CONTENTS);
    }
        
    public DocumentFragment cloneContents()
        throws DOMException
    {
        return traverseContents(CLONE_CONTENTS);
    }
    
    public void insertNode(Node newNode)
        throws DOMException, RangeException
    {
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
    	}
        int type = newNode.getNodeType();
        if (type == Node.ATTRIBUTE_NODE
            || type == Node.ENTITY_NODE
            || type == Node.NOTATION_NODE
            || type == Node.DOCUMENT_NODE
            || type == Node.DOCUMENT_FRAGMENT_NODE)
        {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        Node cloneCurrent;
        Node current;
        boolean MULTIPLE_MODE = false;
        if (fStartContainer.getNodeType() == Node.TEXT_NODE) {
                cloneCurrent = fStartContainer.cloneNode(false);
                ((TextImpl)cloneCurrent).setNodeValueInternal(
                    (cloneCurrent.getNodeValue()).substring(fStartOffset));
                ((TextImpl)fStartContainer).setNodeValueInternal(
                    (fStartContainer.getNodeValue()).substring(0,fStartOffset));
                Node next = fStartContainer.getNextSibling();
                if (next != null) {
                    Node parent = fStartContainer.getParentNode();
                    if (parent !=  null) {
                        parent.insertBefore(newNode, next);
                        parent.insertBefore(cloneCurrent, next);
                    }
                } else {
                    Node parent = fStartContainer.getParentNode();
                    if (parent != null) {
                        parent.appendChild(newNode);
                        parent.appendChild(cloneCurrent);
                    }
                }
                signalSplitData(fStartContainer, cloneCurrent, fStartOffset);
                
                String value = fStartContainer.getNodeValue();
                String newValue = newNode.getNodeValue();
                insertData( (CharacterData)fStartContainer, fStartOffset, newValue);
            }
            current = fStartContainer.getFirstChild();
            int i = 0;
            for(i = 0; i < fStartOffset && current != null; i++) {
                current=current.getNextSibling();
            }
            if (current != null) {
                fStartContainer.insertBefore(newNode, current);
            } else {
                fStartContainer.appendChild(newNode);
            }
        }                
        
    }
    
    public void surroundContents(Node newParent)
        throws DOMException, RangeException
    {
        if (newParent==null) return;
        
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
    	}
        int type = newParent.getNodeType();
        if (type == Node.ATTRIBUTE_NODE
            || type == Node.ENTITY_NODE
            || type == Node.NOTATION_NODE
            || type == Node.DOCUMENT_TYPE_NODE
            || type == Node.DOCUMENT_NODE
            || type == Node.DOCUMENT_FRAGMENT_NODE)
        {
    		throw new RangeExceptionImpl(
    			RangeException.INVALID_NODE_TYPE_ERR, 
			"DOM012 Invalid node type");
        }
        
        Node root = getCommonAncestorContainer();
        
        Node realStart = fStartContainer;
        Node realEnd = fEndContainer;
        if (fStartContainer.getNodeType() == Node.TEXT_NODE) {
            realStart = fStartContainer.getParentNode();
        }
        if (fEndContainer.getNodeType() == Node.TEXT_NODE) {
            realEnd = fEndContainer.getParentNode();
        }
            
        if (realStart != realEnd) {
           	throw new RangeExceptionImpl(
    		RangeException.BAD_BOUNDARYPOINTS_ERR, 
    		"DOM013 Bad boundary points");
        }

    	DocumentFragment frag = extractContents();
    	insertNode(newParent);
    	newParent.appendChild(frag);
    	selectNode(newParent);
    }
        
    public Range cloneRange(){
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
    	}
        
        Range range = fDocument.createRange();
        range.setStart(fStartContainer, fStartOffset);
        range.setEnd(fEndContainer, fEndOffset);
        return range;
    }
    
    public String toString(){
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMException.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
    	}
    	
    	Node node = fStartContainer;
    	StringBuffer sb = new StringBuffer();
    	if (fStartContainer.getNodeType() == Node.TEXT_NODE
    	 || fStartContainer.getNodeType() == Node.CDATA_SECTION_NODE
    	) {
    	    if (fStartContainer == fEndContainer) {
    	        sb.append(fStartContainer.getNodeValue().substring(fStartOffset, fEndOffset));
    	        return sb.toString();
    	    } else {
    	        sb.append(fStartContainer.getNodeValue().substring(fStartOffset));
    	    }
    	}
    	while (node != fEndContainer) {
    	    node = nextNode(node, true);
    	    if (node == null) break;
    	    if (node.getNodeType() == Node.TEXT_NODE
    	    ||  node.getNodeType() == Node.CDATA_SECTION_NODE
    	    ) {
    	        sb.append(node.getNodeValue());
    	    }
    	}
    	if (fEndContainer.getNodeType() == Node.TEXT_NODE
    	 || fEndContainer.getNodeType() == Node.CDATA_SECTION_NODE) {
    	    sb.append(fEndContainer.getNodeValue().substring(0,fEndOffset));
    	}
    	return sb.toString();
    }
    
    public void detach() {
        fDetach = true;
        fDocument.removeRange(this);
    }
    
    
    /** Signal other Ranges to update their start/end 
     *  containers/offsets. The data has already been split
     *  into the two Nodes.
     */
    void signalSplitData(Node node, Node newNode, int offset) {
        fSplitNode = node;
        fDocument.splitData(node, newNode, offset);
        fSplitNode = null;
    }
    
    /** Fix up this Range if another Range has split a Text Node
     *  into 2 Nodes.
     */
    void receiveSplitData(Node node, Node newNode, int offset) {
        if (node == null || newNode == null) return;
        if (fSplitNode == node) return;
        
        if (node == fStartContainer 
        && fStartContainer.getNodeType() == Node.TEXT_NODE) {
            if (fStartOffset > offset) {
                fStartOffset = fStartOffset - offset;
                fStartContainer = newNode;
            }
        }
        if (node == fEndContainer 
        && fEndContainer.getNodeType() == Node.TEXT_NODE) {
            if (fEndOffset > offset) {
                fEndOffset = fEndOffset-offset;
                fEndContainer = newNode;
            }
        }
        
    }
   
    /** This function inserts text into a Node and invokes
     *  a method to fix-up all other Ranges.
     */
    void deleteData(CharacterData node, int offset, int count) {
        fDeleteNode = node;
        node.deleteData( offset,  count);
        fDeleteNode = null;
    }
    
    
    /** This function is called from DOM.
     *  The  text has already beeen inserted.
     *  Fix-up any offsets.
     */
    void receiveDeletedText(Node node, int offset, int count) {
        if (node == null) return;
        if (fDeleteNode == node) return;
        if (node == fStartContainer 
        && fStartContainer.getNodeType() == Node.TEXT_NODE) {
            if (fStartOffset > offset+count) {
                fStartOffset = offset+(fStartOffset-(offset+count));
            } else 
            if (fStartOffset > offset) {
                fStartOffset = offset;
            }  
        }
        if (node == fEndContainer 
        && fEndContainer.getNodeType() == Node.TEXT_NODE) {
            if (fEndOffset > offset+count) {
                fEndOffset = offset+(fEndOffset-(offset+count));
            } else 
            if (fEndOffset > offset) {
                fEndOffset = offset;
            }  
        }
        
    }
   
    /** This function inserts text into a Node and invokes
     *  a method to fix-up all other Ranges.
     */
    void insertData(CharacterData node, int index, String insert) {
        fInsertNode = node;
        node.insertData( index,  insert);
        fInsertNode = null;
    }
    
    
    /** This function is called from DOM.
     *  The  text has already beeen inserted.
     *  Fix-up any offsets.
     */
    void receiveInsertedText(Node node, int index, int len) {
        if (node == null) return;
        if (fInsertNode == node) return;
        if (node == fStartContainer 
        && fStartContainer.getNodeType() == Node.TEXT_NODE) {
            if (index < fStartOffset) {
                fStartOffset = fStartOffset+len;
            }
        }
        if (node == fEndContainer 
        && fEndContainer.getNodeType() == Node.TEXT_NODE) {
            if (index < fEndOffset) {
                fEndOffset = fEndOffset+len;
            }
        }
        
    }
   
    /** This function is called from DOM.
     *  The  text has already beeen replaced.
     *  Fix-up any offsets.
     */
    void receiveReplacedText(Node node) {
        if (node == null) return;
        if (node == fStartContainer 
        && fStartContainer.getNodeType() == Node.TEXT_NODE) {
            fStartOffset = 0;
        }
        if (node == fEndContainer 
        && fEndContainer.getNodeType() == Node.TEXT_NODE) {
            fEndOffset = 0;
        }
        
    }
    
    /** This function is called from the DOM.
     *  This node has already been inserted into the DOM.
     *  Fix-up any offsets.
     */
    public void insertedNodeFromDOM(Node node) {
        if (node == null) return;
        if (fInsertNode == node) return;
        
        Node parent = node.getParentNode();
        
        if (parent == fStartContainer) {
            int index = indexOf(node, fStartContainer);
            if (index < fStartOffset) {
                fStartOffset++;
            }
        }
        
        if (parent == fEndContainer) {
            int index = indexOf(node, fEndContainer);
            if (index < fEndOffset) {
                fEndOffset++;
            }
        }
        
    }
    
    /** This function is called within Range 
     *  instead of Node.removeChild,
     *  so that the range can remember that it is actively
     *  removing this child.
     */
     
    Node fRemoveChild = null;
    Node removeChild(Node parent, Node child) {
        fRemoveChild = child;
        Node n = parent.removeChild(child);
        fRemoveChild = null;
        return n;
    }
    
    /** This function must be called by the DOM _BEFORE_
     *  a node is deleted, because at that time it is
     *  connected in the DOM tree, which we depend on.
     */
    void removeNode(Node node) {
        if (node == null) return;
        if (fRemoveChild == node) return;
        
        Node parent = node.getParentNode();
        
        if (parent == fStartContainer) {
            int index = indexOf(node, fStartContainer);
            if (index <= fStartOffset) {
                fStartOffset--;
            }
        }
        
        if (parent == fEndContainer) {
            int index = indexOf(node, fEndContainer);
            if (index < fEndOffset) {
                fEndOffset--;
            }
        }
   
        if (parent != fStartContainer 
        &&  parent != fEndContainer) {
            if (isAncestorOf(node, fStartContainer)) {
                fStartContainer = parent;
                fStartOffset = indexOf( node, parent)-1;
            }   
            if (isAncestorOf(node, fEndContainer)) {
                fEndContainer = parent;
                fEndOffset = indexOf( node, parent)-1;
            }
        } 
        
    }
        
    
    static final int EXTRACT_CONTENTS = 1;
    static final int CLONE_CONTENTS = 2;
    
    /** This is the master traversal function which is used by 
     *  both extractContents and cloneContents().
     */
    DocumentFragment traverseContents(int traversalType)
        throws DOMException
    {
        if (fStartContainer == null || fEndContainer == null) {
        }
        
        DocumentFragment frag = fDocument.createDocumentFragment();
        
        
        Node current = fStartContainer;
        Node cloneCurrent = null;
        Node cloneParent = null;
        Node partialNode = null;
        int partialInt = START;
        
        Vector d = new Vector();
        
        if (fStartContainer == fEndContainer) {
            } 
            if (fStartContainer.getNodeType() == Node.TEXT_NODE) {
                cloneCurrent = fStartContainer.cloneNode(false);
                cloneCurrent.setNodeValue(
                (cloneCurrent.getNodeValue()).substring(fStartOffset, fEndOffset));
                if (traversalType == EXTRACT_CONTENTS) {
                    deleteData((CharacterData)current, fStartOffset, fEndOffset-fStartOffset);
                }
                frag.appendChild(cloneCurrent);
            } else {
                current = current.getFirstChild();
                int i = 0;
                for(i = 0; i < fStartOffset && current != null; i++) {
                    current=current.getNextSibling();
                }
                int n = fEndOffset-fStartOffset;
                for(i = 0; i < n && current != null ;i++) {
                    Node newCurrent=current.getNextSibling();
                
                    if (traversalType == CLONE_CONTENTS) {
                        cloneCurrent = current.cloneNode(true);
                        frag.appendChild(cloneCurrent);
                    } else
                    if (traversalType == EXTRACT_CONTENTS) {
                        frag.appendChild(current);
                    }
                    current = newCurrent;
                }
            }
            return frag;
        }
        
   
        Node root = getCommonAncestorContainer();
        Node parent = null;
        
        current = fStartContainer;
        
        if (current.getNodeType() == Node.TEXT_NODE) {
            cloneCurrent = current.cloneNode(false);
            cloneCurrent.setNodeValue(
                (cloneCurrent.getNodeValue()).substring(fStartOffset));
            if (traversalType == EXTRACT_CONTENTS) {
                deleteData((CharacterData)current, fStartOffset, 
                    current.getNodeValue().length()-fStartOffset);
            }
        } else {
            current = current.getFirstChild();
            for(int i = 0; i < fStartOffset && current != null; i++) {
                current=current.getNextSibling();
            }
                current = fStartContainer;
            }
        
            if (traversalType == CLONE_CONTENTS) {
                cloneCurrent = current.cloneNode(true);
            } else
            if (traversalType == EXTRACT_CONTENTS ) {
                cloneCurrent = current;
            }
        }
        
        Node startRoot = null;
        parent = null;
        
        while (current != root) {
            parent = current.getParentNode();

            if (parent == root) {
                cloneParent = frag;
                startRoot = current;
            } else {
                if (parent == null) System.out.println("parent==null: current="+current);
                cloneParent = parent.cloneNode(false);
                if (partialNode==null && parent != root) {
                    partialNode = parent;
                    partialInt = AFTER;
                }
                
            }
            
            Node next = null;
            
            current = current.getNextSibling();
            
            cloneParent.appendChild(cloneCurrent);
                     
            while (current != null) {
                next = current.getNextSibling();
                if (current != null && parent != root) {
                    if (traversalType == CLONE_CONTENTS) {
                        cloneCurrent = current.cloneNode(true);
                        cloneParent.appendChild(cloneCurrent);
                    } else
                    if (traversalType == EXTRACT_CONTENTS) {
                        cloneParent.appendChild(current);
                    }
                }
                current = next;
            }
            
            current = parent;
            cloneCurrent = cloneParent;
        }
        
        Node endRoot = null;
        current = fEndContainer;
        
        if (current.getNodeType() == Node.TEXT_NODE) {
            cloneCurrent = current.cloneNode(false);
            cloneCurrent.setNodeValue(
                (cloneCurrent.getNodeValue()).substring(0,fEndOffset));
            if (traversalType == EXTRACT_CONTENTS) {
                deleteData((CharacterData)current, 0, fEndOffset); 
            } 
        } else {
                current = fEndContainer;
            }
            else {
                current = current.getFirstChild();
                for(int i = 1; i < fEndOffset && current != null; i++) {
                    current=current.getNextSibling();
                }
                    current = fEndContainer.getLastChild();
                }
            }
            if (traversalType == CLONE_CONTENTS) {
                cloneCurrent = current.cloneNode(true);
            } else
            if (traversalType == EXTRACT_CONTENTS ) {
                cloneCurrent = current;
            }
        }
                
        while (current != root && current != null) {
            parent = current.getParentNode();
            if (parent == root) {
                cloneParent = frag;
                endRoot = current;
            } else {
                cloneParent = parent.cloneNode(false);
                if (partialNode==null && parent != root) {
                    partialNode = parent;
                    partialInt = BEFORE;
                }
            }
            
            Node holdCurrent = current;
                
            current = parent.getFirstChild();
            
            cloneParent.appendChild(cloneCurrent);
          
            Node next = null;
            while (current != holdCurrent && current != null) {
                next = current.getNextSibling();
                if (current != null && parent != root) {
                    if (traversalType == CLONE_CONTENTS) {
                        cloneCurrent = current.cloneNode(true);
                        cloneParent.appendChild(cloneCurrent);
                    } else
                    if (traversalType == EXTRACT_CONTENTS) {
                        cloneParent.appendChild(current);
                    }
                }
                current = next;
            }
            
            current = parent;
            cloneCurrent = cloneParent;
        
        }
        
        d.removeAllElements();
        
        Node clonedPrevious = frag.getLastChild();
        current = endRoot.getPreviousSibling();
        Node prev = null;
        while (current != startRoot && current != null) {
            prev = current.getPreviousSibling();
            
            if (traversalType == CLONE_CONTENTS) {
                cloneCurrent = current.cloneNode(true);
            } else
            if (traversalType == EXTRACT_CONTENTS) {
                cloneCurrent = current;
            } 
                        
            frag.insertBefore(cloneCurrent, clonedPrevious);
            
            current = prev;
            clonedPrevious = cloneCurrent;
        }

        if (traversalType == EXTRACT_CONTENTS ) {
            if (partialNode == null) {
                collapse(true);
            } else 
            if (partialInt == AFTER) {
                setStartAfter(partialNode);
                setEndAfter(partialNode);
            }
            else if (partialInt == BEFORE) {
                setStartBefore(partialNode);
                setEndBefore(partialNode);
            }          
        }
        
        return frag;
            
    }
    
    void checkIndex(Node refNode, int offset) throws DOMException
    {
        if (offset < 0) {
    		throw new DOMExceptionImpl(
    			DOMException.INDEX_SIZE_ERR, 
			"DOM004 Index out of bounds");
    	}

        int type = refNode.getNodeType();
        
        if((type == Node.TEXT_NODE
        || type == Node.CDATA_SECTION_NODE
        || type == Node.COMMENT_NODE
        || type == Node.PROCESSING_INSTRUCTION_NODE)
        && offset > refNode.getNodeValue().length()){
    		throw new DOMExceptionImpl(
    			DOMException.INDEX_SIZE_ERR, 
			"DOM004 Index out of bounds");
        }
        
        Node child = refNode.getFirstChild();
        int i = 1;
        for (; child != null; i++) {
            child = child.getNextSibling();
        }
        if (i > offset) {
    		throw new DOMExceptionImpl(
    			DOMException.INDEX_SIZE_ERR, 
			"DOM004 Index out of bounds");
        }
            
    }
                                        
    boolean isAncestorTypeValid(Node node) {
        for (Node n = node; n!=null; n = n.getParentNode()) {
            int type = n.getNodeType();
            if (type == Node.ATTRIBUTE_NODE
             || type == Node.ENTITY_NODE
             || type == Node.NOTATION_NODE
             || type == Node.DOCUMENT_TYPE_NODE)
             return false;
        }
        return true;
    }
    
    Node nextNode(Node node, boolean visitChildren) {
            
        if (node == null) return null;

        Node result;
        if (visitChildren) {
            result = node.getFirstChild();
            if (result != null) {
                return result;
            }
        }
            
        result = node.getNextSibling();
        if (result != null) {
            return result;
        }
        
                
        Node parent = node.getParentNode();
        while (parent != null
               && parent != fDocument
                ) {
            result = parent.getNextSibling();
            if (result != null) {
                return result;
            } else {
                parent = parent.getParentNode();
            }
                            
        
        return null;            
    }
    
    /** is a an ancestor of b ? */
    boolean isAncestorOf(Node a, Node b) {
        for (Node node=b; node != null; node=node.getParentNode()) {
            if (node == a) return true;
        }
        return false;
    }

    /** what is the index of the child in the parent */
    int indexOf(Node child, Node parent) {
        Node node;
        int i = 0;
        if (child.getParentNode() != parent) return -1;
        for(node = child; node!= null; node=node.getPreviousSibling()) {
            i++;
        }
        return i;
    }

}
