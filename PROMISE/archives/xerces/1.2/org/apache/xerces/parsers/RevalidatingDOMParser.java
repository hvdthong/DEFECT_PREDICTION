package org.apache.xerces.parsers;

import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.framework.XMLValidator;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.utils.XMLMessages;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is a validating DOM parser which can also
 * "revalidate" a DOM subtree after the document has been parsed.
 *
 * @version
 * @see org.apache.xerces.parsers.DOMParser
 */
public class RevalidatingDOMParser 
    extends DOMParser {



    /** Set to true to debug validate() method. */
    private static final boolean DEBUG_VALIDATE = false;


    /**
     * Given a node which is a DOM tree (or subtree), validate the tree 
     * against the current DTD.  If the tree is valid, return null.  If
     * the subtree is invalid return the highest (closest to the root) 
     * and left most node which causes the tree to be invalid.
     *
     * @param node  The node representing the root of the DOM subtree to be
     *              revalidated.
     *
     * @return <i>null</i> if the subtree is valid or the first (highest and 
     *         leftmost) node where the tree is invalid.
     */
    public final Node validate(Node node) {

        if (node.getNodeType() != Node.ELEMENT_NODE) {
            throw new IllegalArgumentException("Can't revalidate a non element");
        }

        return recursiveValidate(node, fValidator, fStringPool);



    /** 
     * Recursive validation. This method returns the first node that is
     * in error, or <i>null</i> if the content is valid.
     *
     * @param node              The node to validate.
     * @param validator         The validation handler.
     * @param stringPool        The string pool
     */
    private final Node recursiveValidate(Node node,
                                         XMLValidator validator,
                                         StringPool stringPool) {

        if (DEBUG_VALIDATE) {
            print(node, "");
        }

        int  children[] = new int[countChildren(node)];
        Node nodes[]    = new Node[children.length];
        int  count      = expandChildren(node, children, nodes, 0, stringPool);

        int result = -1;
        int parentNameIndex = stringPool.addSymbol(node.getNodeName());
        int parentIndex = -1;
        try {
            parentIndex = fStringPool.getDeclaration(parentNameIndex);
            result = validator.checkContent(parentIndex, count, children);
        } 
        catch (Exception e) {
            if (DEBUG_VALIDATE) {
                e.printStackTrace();
            }
        }

        Node errorNode = null;
        if (result != -1) {
            errorNode = nodes[result];

            String cs  = validator.getContentSpecAsString(parentIndex);
            int majorCode = result != count ? XMLMessages.MSG_CONTENT_INVALID : XMLMessages.MSG_CONTENT_INCOMPLETE;
            try {
                Object[] args = { node.getNodeName(), cs };
                fErrorReporter.reportError(fErrorReporter.getLocator(),
                                           XMLMessages.XML_DOMAIN,
                                           majorCode,
                                           0,
                                           args,
                                           XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
            }
            catch (Exception e) {
            }
        }

        else {
            for (int i = 0; i < count; i++) {
                if (children[i] != -1) {
                    Node child = nodes[i];
                    if (child != null) {
                        child = recursiveValidate(child, 
                                                  validator, 
                                                  stringPool);
                        if (child != null) {
                            errorNode = child;
                            break;
                        }
                    }
                }
            }
        }

        children = null;
        nodes    = null;

        return errorNode;
    
    }

    /**
     * Expands the children of the specified node into the given
     * integer array (internal indices) and Node array. 
     * <p>
     * This method returns the number of children that were actually
     * expanded since text nodes may be ignorable whitespace.
     *
     * @param node      The node for which to expand the children.
     * @param children  The destination array of the internal indices
     *                  representing the children of the specified node.
     *                  A value of -1 indicates text.
     * @param nodes     The destination array of node objects representing
     *                  the children.
     * @param count     The number of children. Since this method is
     *                  called recursively in the case of entity
     *                  references, this is the offset of where to begin
     *                  adding children. The first call to this method
     *                  passes 0 as the initial count.
     * @param stringPool The string pool where element name symbols are
     *                  stored.
     */
    private final int expandChildren(Node node, 
                                     int children[], Node nodes[], int count,
                                     StringPool stringPool) {

        if (node.hasChildNodes()) {

            NodeList childList  = node.getChildNodes();
            int      childCount = childList.getLength();
            for (int i = 0; i < childCount; i++) {
                Node child = childList.item(i);
                int  type  = child.getNodeType();

                if (type == Node.ELEMENT_NODE) {
                    children[count] = stringPool.addSymbol(child.getNodeName());
                    nodes[count]    = child;
                    count++;
                } 

                else if (type == Node.TEXT_NODE) {
                    if (!(child instanceof TextImpl) || 
                        !((TextImpl)child).isIgnorableWhitespace()) { 
                        children[count] = -1;
                        nodes[count]    = child;
                        count++;
                    }
                }

                else if (type == Node.ENTITY_REFERENCE_NODE) {
                    count = expandChildren(child, 
                                           children, nodes, count, 
                                           stringPool);
                }



        return count;


    /**
     * Returns the <i>true</i> number of children under the specified
     * node. When entity references occur as direct children, a
     * recursive call to this method is made in order to count those
     * nodes as children of the original node specified.
     *
     * @param node  The node whose children are to be counted.
     */
    private final int countChildren(Node node) {

        int count = 0;
        if (node.hasChildNodes()) {

            NodeList children = node.getChildNodes();
            count += children.getLength();

            for (int i = count - 1; i >= 0; i--) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
                    count += countChildren(child) - 1;
                }
            }
        }

        return count;



    /** Debugging print method. */
    private static void print(Node node, String indent) {

        if (DEBUG_VALIDATE) {
            if (node == null) {
                System.out.println(indent+"!!! node == null");
                return;
            }
            System.out.println(indent+
                               "node: type="+type(node.getNodeType())+
                               ", name="+node.getNodeName()+
                               ", value=\""+normalize(node.getNodeValue())+'"');
            if (node.hasChildNodes()) {
                System.out.println(indent+'{');
                NodeList children = node.getChildNodes();
                int count = children.getLength();
                for (int i = 0; i < count; i++) {
                    print(children.item(i), indent+"  ");
                }
                System.out.println(indent+'}');
            }
        }


    /** Debugging normalizing text. */
    private static String normalize(String s) {

        if (DEBUG_VALIDATE) {
            if (s == null) {
                return "[null]";
            }
            StringBuffer str = new StringBuffer();
            int len = s.length();
            for (int i = 0; i < len; i++) {
                if (i > 20) {
                    str.append("...");
                    break;
                }
                char ch = s.charAt(i);
                switch (ch) {
                    case '\r': str.append("[\\r]"); break;
                    case '\n': str.append("[\\n]"); break;
                    default: str.append(ch);
                }
            }
            return str.toString();
        }

        return null;


    /** Debugging node type name. */
    private static String type(int type) {

        if (DEBUG_VALIDATE) {
            switch (type) {
                case Node.ATTRIBUTE_NODE: return "ATTR";
                case Node.ELEMENT_NODE: return "ELEMENT";
                case Node.TEXT_NODE: return "TEXT";
                case Node.ENTITY_REFERENCE_NODE: return "ENTITY_REF";
            }
            return "???";
        }

        return null;


