package org.apache.xerces.tree;

import org.w3c.dom.Element;


/**
 * This interface defines a factory which can be used by documents
 * to provide namespace-aware element creation.
 *
 * <P> A variety of implementation techniques are possible for this
 * interface.  Among them are compiled-in mappings between element
 * names and classes, mappings defined in configuration files, ones
 * derived from the document as it is parsed, and using namespaces
 * to identify packages to be used with classes whose names correspond
 * to elements.
 *
 * <P> DOM implementations will have constraints on the implementation
 * classes returned by this factory, typically that they be subclasses
 * of their implementation base class.
 *
 * @see DocumentEx
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public interface ElementFactory
{
    /**
     * This is like the DOM <em>Document.createElement</em> method,
     * except that its return type is different.
     */
    public ElementEx createElementEx (String tag);

    /**
     * Returns an element which may be specialized to support application
     * specific behaviors as associated with the specified namespace.
     */
    public ElementEx createElementEx (String uri, String tag);
}
