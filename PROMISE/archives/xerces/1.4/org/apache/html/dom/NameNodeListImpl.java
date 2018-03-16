package org.apache.html.dom;

import org.w3c.dom.*;
import java.util.Vector;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.DeepNodeListImpl;

/**
 * This class implements the DOM's NodeList behavior for
 * HTMLDocuemnt.getElementsByName().
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 * @see DeepNodeListImpl
 */
public class NameNodeListImpl 
    extends DeepNodeListImpl
    implements NodeList {
    
    
    /** Constructor. */
    public NameNodeListImpl(NodeImpl rootNode, String tagName) {
	super( rootNode, tagName );
    }  
    

    /** 
     * Iterative tree-walker. When you have a Parent link, there's often no
     * need to resort to recursion. NOTE THAT only Element nodes are matched
     * since we're specifically supporting getElementsByTagName().
     */
    protected Node nextMatchingElementAfter(Node current) {
        
        Node next;
        while (current != null) {
            if (current.hasChildNodes()) {
                current = (current.getFirstChild());
            }
            
            else if (current != rootNode && null != (next = current.getNextSibling())) {
                current = next;
            }
            
            else {
                next = null;
                     current = current.getParentNode()) {
                    
                    next = current.getNextSibling();
                    if (next != null)
                        break;
                }
                current = next;
            }
            
            if (current != rootNode && current != null
                && current.getNodeType() ==  Node.ELEMENT_NODE  ) {
                String name = ((ElementImpl) current).getAttribute( "name" );
                if ( name.equals("*") || name.equals(tagName))
                    return current;
            }
            
        }
        
        return null;
        
    
