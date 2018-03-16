package org.apache.xerces.tree;

import org.xml.sax.AttributeList;


/**
 * This interface extends the SAX AttributeList interface to expose
 * information needed to support DOM Level 1 features used in document
 * editing, and detection of ID attributes which are declared for
 * an element.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public interface AttributeListEx extends AttributeList
{
    /**
     * Returns true if the attribute was specified in the document.
     * <em> This method only relates to document editing; there is no
     * difference in semantics between explicitly specifying values
     * of attributes in a DTD vs another part of the document. </em>
     *
     * @param i the index of the attribute in the list.
     */
    public boolean isSpecified (int i);

    /**
     * Returns the default value of the specified attribute, or null
     * if no default value is known.  Default values may be explicitly
     * specified in documents; in fact, for standalone documents, they
     * must be so specified.  If <em>isSpecified</em> is false, the
     * value returned by this method will be what <em>getValue</em>
     * returns.
     *
     * @param i the index of the attribute in the list.
     */
    public String getDefault (int i);

    /**
     * Returns the name of the ID attribute for the associated element,
     * if one was declared.  If such an ID value was provided, this
     * name can be inferred from methods in the base class; but if none
     * was provided, this will be the only way this name can be determined.
     */
    public String getIdAttributeName ();
}
