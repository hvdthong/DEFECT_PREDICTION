package org.apache.xerces.validators.common;

import org.apache.xerces.utils.QName;
import org.apache.xerces.validators.schema.EquivClassComparator;

/**
 * ContentModel is an interface that can be used by your own custom validators
 * to plug in various types of content models. It is used internally as well
 * for the same purposes.
 * <p>
 * Since there are a number of optimizations that can be used for simple or
 * special content models, this class provides the interface via which all of
 * the various content model types are managed. So the validation handler
 * class has a list of things derived from this class. It finds the one for
 * the desired element, then asks it to validate the element contents.
 * <p>
 * The validation interface from the scanner to the validation handle provides
 * a child count and an array of element name indices into the string pool.
 * So it is assumed that those same parameters will be passed to the content
 * model to be validated. Therefore the validateContent() method accepts
 * this standard view of the elements to be validated.
 *
 * @author  Dean Roddey, Eric Ye
 * @version $Id: XMLContentModel.java 315994 2000-08-10 02:57:44Z ericye $
 */
public interface XMLContentModel {

    /**
     * Check that the specified content is valid according to this
     * content model. This method can also be called to do 'what if' 
     * testing of content models just to see if they would be valid.
     * <p>
     * A value of -1 in the children array indicates a PCDATA node. All other 
     * indexes will be positive and represent child elements. The count can be
     * zero, since some elements have the EMPTY content model and that must be 
     * confirmed.
     *
     * @param children The children of this element.  Each integer is an index within
     *                 the <code>StringPool</code> of the child element name.  An index
     *                 of -1 is used to indicate an occurrence of non-whitespace character
     *                 data.
     * @param offset Offset into the array where the children starts.
     * @param length The number of entries in the <code>children</code> array.
     *
     * @return The value -1 if fully valid, else the 0 based index of the child
     *         that first failed. If the value returned is equal to the number
     *         of children, then the specified children are valid but additional
     *         content is required to reach a valid ending state.
     *
     * @exception Exception Thrown on error.
     */
    public int validateContent(QName children[], int offset, int length) throws Exception;

    /**
     * This method is different from "validateContent" in that it will try to use
     * the EquivClassComparator to match children against the content model.
     * <p>
     * A value of -1 in the children array indicates a PCDATA node. All other 
     * indexes will be positive and represent child elements. The count can be
     * zero, since some elements have the EMPTY content model and that must be 
     * confirmed.
     *
     * @param children The children of this element.  Each integer is an index within
     *                 the <code>StringPool</code> of the child element name.  An index
     *                 of -1 is used to indicate an occurrence of non-whitespace character
     *                 data.
     * @param offset Offset into the array where the children starts.
     * @param length The number of entries in the <code>children</code> array.
     *
     * @return The value -1 if fully valid, else the 0 based index of the child
     *         that first failed. If the value returned is equal to the number
     *         of children, then the specified children are valid but additional
     *         content is required to reach a valid ending state.
     *
     * @exception Exception Thrown on error.
     */
    public int validateContentSpecial(QName children[], int offset, int length) throws Exception;

    /**
     * The setter method to pass in the EquivCLassComparator.
     *
     * @param comparator a EquivClassComparator object.
     * @return 
     * @exception 
     */

    /**
     * Returns information about which elements can be placed at a particular point
     * in the passed element's content model.
     * <p>
     * Note that the incoming content model to test must be valid at least up to
     * the insertion point. If not, then -1 will be returned and the info object
     * will not have been filled in.
     * <p>
     * If, on return, the info.isValidEOC flag is set, then the 'insert after'
     * element is a valid end of content. In other words, nothing needs to be
     * inserted after it to make the parent element's content model valid.
     *
     * @param fullyValid Only return elements that can be inserted and still
     *                   maintain the validity of subsequent elements past the
     *                   insertion point (if any).  If the insertion point is at
     *                   the end, and this is true, then only elements that can
     *                   be legal final states will be returned.
     * @param info An object that contains the required input data for the method,
     *             and which will contain the output information if successful.
     *
     * @return The value -1 if fully valid, else the 0 based index of the child
     *         that first failed before the insertion point. If the value 
     *         returned is equal to the number of children, then the specified
     *         children are valid but additional content is required to reach a
     *         valid ending state.
     *
     * @see InsertableElementsInfo
     */
    public int whatCanGoHere(boolean fullyValid, 
                             InsertableElementsInfo info) throws Exception;

    public ContentLeafNameTypeVector getContentLeafNameTypeVector() ;


