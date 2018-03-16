package org.apache.xerces.dom;

import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.DOMExceptionImpl;

/** DefaultNodeIterator implements a NodeIterator, which iterates a 
 *  DOM tree in the expected depth first way. 
 *
 *  <p>The whatToShow and filter functionality is implemented as expected.
 *  
 *  <p>This class also has method removeNode to enable iterator "fix-up" 
 *  on DOM remove. It is expected that the DOM implementation call removeNode
 *  right before the actual DOM transformation. If not called by the DOM,
 *  the client could call it before doing the removal.
 */
public class NodeIteratorImpl implements NodeIterator {
    
    
    /** The DocumentImpl which created this iterator, so it can be detached. */
    private DocumentImpl fDocument;
    /** The root. */
    private Node fRoot;
    /** The whatToShow mask. */
    private int fWhatToShow = NodeFilter.SHOW_ALL;
    /** The NodeFilter reference. */
    private NodeFilter fNodeFilter;
    /** If detach is called, the fDetach flag is true, otherwise flase. */
    private boolean fDetach = false;
    
    
    /** The last Node returned. */
    private Node fCurrentNode;
    
    /** The direction of the iterator on the fCurrentNode.
     *  <pre>
     *  nextNode()  ==      fForward = true;
     *  previousNode() ==   fForward = false;
     *  </pre>
     */
    private boolean fForward = true;
    
    /** When TRUE, the children of entites references are returned in the iterator. */
    private boolean fEntityReferenceExpansion;
    
    
    /** Public constructor */
    public NodeIteratorImpl( DocumentImpl document,
                             Node root, 
                             int whatToShow, 
                             NodeFilter nodeFilter,
                             boolean entityReferenceExpansion) {
        fDocument = document;
        fRoot = root;
        fCurrentNode = null;
        fWhatToShow = whatToShow;
        fNodeFilter = nodeFilter;
        fEntityReferenceExpansion = entityReferenceExpansion;
    }
    
    public Node getRoot() {
	return fRoot;
    }

    
    /** Return the whatToShow value */
    public int                getWhatToShow() {
        return fWhatToShow;
    }

    /** Return the filter */
    public NodeFilter         getFilter() {
        return fNodeFilter;
    }
    
    /** Return whether children entity references are included in the iterator. */
    public boolean            getExpandEntityReferences() {
        return fEntityReferenceExpansion;
    }
            
    /** Return the next Node in the Iterator. The node is the next node in 
     *  depth-first order which also passes the filter, and whatToShow. 
     *  If there is no next node which passes these criteria, then return null.
     */
    public Node               nextNode() {
        
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMExceptionImpl.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
        
        if (fRoot == null) return null;
        
        Node nextNode = fCurrentNode;
     
        accepted_loop:
        while (!accepted) {
            
            if (!fForward && nextNode!=null) {
                nextNode = fCurrentNode;
            } else { 
                if (!fEntityReferenceExpansion
                    && nextNode != null
                    && nextNode.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
                    nextNode = nextNode(nextNode, false);
                } else {
                    nextNode = nextNode(nextNode, true);
                }
            }
   
            
            if (nextNode == null) return null; 
            
            accepted = acceptNode(nextNode);
            if (accepted) {
                fCurrentNode = nextNode;
                return fCurrentNode;
            } else 
                continue accepted_loop;
            
        
        return null;
            
    }
    
    /** Return the previous Node in the Iterator. The node is the next node in 
     *  _backwards_ depth-first order which also passes the filter, and whatToShow. 
     */
    public Node               previousNode() {
        
    	if( fDetach) {
    		throw new DOMExceptionImpl(
    			DOMExceptionImpl.INVALID_STATE_ERR, 
			"DOM011 Invalid state");
        }
 
        if (fRoot == null || fCurrentNode == null) return null;
       
        Node previousNode = fCurrentNode;
        boolean accepted = false;
        
        accepted_loop:
        while (!accepted) {
            
            if (fForward && previousNode != null) {
                previousNode = fCurrentNode;
            } else { 
                previousNode = previousNode(previousNode);
            }
            
            fForward = false;
            
            if (previousNode == null) return null;
            
            accepted = acceptNode(previousNode);
            if (accepted) {
                fCurrentNode = previousNode;
                return fCurrentNode;
            } else 
                continue accepted_loop;
        }
        return null;
    }
                
    /** The node is accepted if it passes the whatToShow and the filter. */
    boolean acceptNode(Node node) {
                
        if (fNodeFilter == null) {            
            return ( fWhatToShow & (1 << node.getNodeType()-1)) != 0 ;
        } else {
            return ((fWhatToShow & (1 << node.getNodeType()-1)) != 0 ) 
                && fNodeFilter.acceptNode(node) == NodeFilter.FILTER_ACCEPT;
        }
    } 
    
    /** Return node, if matches or any parent if matches. */
    Node matchNodeOrParent(Node node) {
        for (Node n = node; n != fRoot; n = n.getParentNode()) {
            if (node == n) return n;
        }
        return null;
    }
    
    /** The method nextNode(Node, boolean) returns the next node 
     *  from the actual DOM tree.
     * 
     *  The boolean visitChildren determines whether to visit the children.
     *  The result is the nextNode.
     */
    Node nextNode(Node node, boolean visitChildren) {
            
        if (node == null) return fRoot;

        Node result;
        if (visitChildren) {
            if (node.hasChildNodes()) {
                result = node.getFirstChild();
                return result;
            }
        }
            
        result = node.getNextSibling();
        if (result != null) return result;
        
                
        Node parent = node.getParentNode();
        while (parent != null && parent != fRoot) {
            result = parent.getNextSibling();
            if (result != null) {
                return result;
            } else {
                parent = parent.getParentNode();
            }
                            
        
        return null;            
    }
    
    /** The method previousNode(Node) returns the previous node 
     *  from the actual DOM tree.
     */
    Node previousNode(Node node) {
        
        Node result;
        
        if (node == fRoot) return null;
        
        result = node.getPreviousSibling();
        if (result == null) {
            result = node.getParentNode();
            return result;
        }
        
        if (result.hasChildNodes()
            && !(!fEntityReferenceExpansion
                && result != null
                && result.getNodeType() == Node.ENTITY_REFERENCE_NODE)) 
       
        {
            while (result.hasChildNodes()) {
                result = result.getLastChild();
            }
        }          
            
        return result;
    }
    
    /** Fix-up the iterator on a remove. Called by DOM or otherwise,
     *  before an actual DOM remove.   
     */
    public void removeNode(Node node) {
        
        
        if (node == null) return;
        
        Node deleted = matchNodeOrParent(node);
        
        if (deleted == null) return;
        
        if (fForward) {
            fCurrentNode = previousNode(deleted);
        } else
        {
            Node next = nextNode(deleted, false);
            if (next!=null) {
                fCurrentNode = next;
            } else {
                fCurrentNode = previousNode(deleted);
                fForward = true;
            }
                
        }
        
    }
    
    public void               detach() {
        fDetach = true;
        fDocument.removeNodeIterator(this);
    }
    
}
