package org.apache.xerces.validators.common;

import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.utils.ImplementationMessages;
import org.apache.xerces.utils.QName;

import org.apache.xerces.validators.schema.EquivClassComparator;

/**
 * SimpleContentModel is a derivative of the abstract content model base
 * class that handles a small set of simple content models that are just
 * way overkill to give the DFA treatment.
 * <p>
 * This class handles the following scenarios:
 * <ul>
 * <li> a
 * <li> a?
 * <li> a*
 * <li> a+
 * <li> a,b
 * <li> a|b
 * </ul>
 * <p>
 * These all involve a unary operation with one element type, or a binary
 * operation with two elements. These are very simple and can be checked
 * in a simple way without a DFA and without the overhead of setting up a
 * DFA for such a simple check.
 *
 * @version $Id: SimpleContentModel.java 315994 2000-08-10 02:57:44Z ericye $
 */
public class SimpleContentModel 
    implements XMLContentModel {


    /**
     * The element decl pool indices of the first (and optional second)
     * child node. The operation code tells us whether the second child
     * is used or not.
     */
    private QName fFirstChild = new QName();

    /**
     * The element decl pool indices of the first (and optional second)
     * child node. The operation code tells us whether the second child
     * is used or not.
     */
    private QName fSecondChild = new QName();

    /**
     * The operation that this object represents. Since this class only
     * does simple contents, there is only ever a single operation
     * involved (i.e. the children of the operation are always one or
     * two leafs.) This is one of the XMLDTDParams.CONTENTSPECNODE_XXX values.
     */
    private int fOp;

    /** Boolean to allow DTDs to validate even with namespace support. */
    private boolean fDTD;

    /* this is the EquivClassComparator object */
    private EquivClassComparator comparator = null;
    

    /**
     * Constructs a simple content model.
     *
     * @param firstChildIndex The first child index
     * @parma secondChildIndex The second child index.
     * @param cmOp The content model operator.
     *
     * @see XMLContentSpec
     */
    public SimpleContentModel(QName firstChild, QName secondChild, int cmOp) {
        this(firstChild, secondChild, cmOp, false);
    }

    /**
     * Constructs a simple content model.
     *
     * @param firstChildIndex The first child index
     * @parma secondChildIndex The second child index.
     * @param cmOp The content model operator.
     *
     * @see XMLContentSpec
     */
    public SimpleContentModel(QName firstChild, QName secondChild, 
                              int cmOp, boolean dtd) {
        fFirstChild.setValues(firstChild);
        if (secondChild != null) {
            fSecondChild.setValues(secondChild);
        }
        else {
            fSecondChild.clear();
        }
        fOp = cmOp;
        fDTD = dtd;
    }


    
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
    public int validateContent(QName children[], int offset, int length) throws Exception {

        switch(fOp)
        {
            case XMLContentSpec.CONTENTSPECNODE_LEAF :
                if (length == 0)
                    return 0;

                if (fDTD) {
                    if (children[offset].rawname != fFirstChild.rawname) {
                        return 0;
                    }
                }
                else {
                    if (children[offset].uri != fFirstChild.uri || 
                        children[offset].localpart != fFirstChild.localpart)
                        return 0;
                }

                if (length > 1)
                    return 1;
                break;

            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE :
                if (length == 1) {
                    if (fDTD) {
                        if (children[offset].rawname != fFirstChild.rawname) {
                            return 0;
                        }
                    }
                    else {
                        if (children[offset].uri != fFirstChild.uri || 
                         children[offset].localpart != fFirstChild.localpart)
                        return 0;
                    }
                }

                if (length > 1)
                    return 1;
                break;

            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE :
                if (length > 0)
                {
                    if (fDTD) {
                        for (int index = 0; index < length; index++) {
                            if (children[offset + index].rawname != fFirstChild.rawname) {
                                return index;
                            }
                        }
                    }
                    else {
                        for (int index = 0; index < length; index++)
                        {
                            if (children[offset + index].uri != fFirstChild.uri || 
                                children[offset + index].localpart != fFirstChild.localpart)
                                return index;
                        }
                    }
                }
                break;

            case XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE :
                if (length == 0)
                    return 0;

                if (fDTD) {
                    for (int index = 0; index < length; index++) {
                        if (children[offset + index].rawname != fFirstChild.rawname) {
                            return index;
                        }
                    }
                }
                else {
                    for (int index = 0; index < length; index++)
                    {
                        if (children[offset + index].uri != fFirstChild.uri || 
                            children[offset + index].localpart != fFirstChild.localpart)
                            return index;
                    }
                }
                break;

            case XMLContentSpec.CONTENTSPECNODE_CHOICE :
                if (length == 0)
                    return 0;

                if (fDTD) {
                    if ((children[offset].rawname != fFirstChild.rawname) &&
                        (children[offset].rawname != fSecondChild.rawname)) {
                        return 0;
                    }
                }
                else {
                    if ((children[offset].uri != fFirstChild.uri || children[offset].localpart != fFirstChild.localpart) &&
                        (children[offset].uri != fSecondChild.uri || children[offset].localpart != fSecondChild.localpart))
                        return 0;
                }

                if (length > 1)
                    return 1;
                break;

            case XMLContentSpec.CONTENTSPECNODE_SEQ :
                if (length == 2) {
                    if (fDTD) {
                        if (children[offset].rawname != fFirstChild.rawname) {
                            return 0;
                        }
                        if (children[offset + 1].rawname != fSecondChild.rawname) {
                            return 1;
                        }
                    }
                    else {
                        if (children[offset].uri != fFirstChild.uri || children[offset].localpart != fFirstChild.localpart)
                            return 0;

                        if (children[offset + 1].uri != fSecondChild.uri || children[offset + 1].localpart != fSecondChild.localpart)
                            return 1;
                    }
                }
                else {
                    if (length > 2) {
                        return 2;
                    }

                    return length;
                }

                break;

            default :
                throw new CMException(ImplementationMessages.VAL_CST);
        }

        return -1;
    }
    
    public int validateContentSpecial(QName children[], int offset, int length) throws Exception{

        if (comparator==null) {
            return validateContent(children,offset, length);
        }
        switch(fOp)
        {
            case XMLContentSpec.CONTENTSPECNODE_LEAF :
                if (length == 0)
                    return 0;

                if (children[offset].uri != fFirstChild.uri || 
                    children[offset].localpart != fFirstChild.localpart)
                    if (!comparator.isEquivalentTo(children[offset], fFirstChild)) 
                        return 0;

                if (length > 1)
                    return 1;
                break;

            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE :
                if (length == 1 && 
                    (children[offset].uri != fFirstChild.uri || 
                     children[offset].localpart != fFirstChild.localpart))
                    if (!comparator.isEquivalentTo(children[offset], fFirstChild)) 
                        return 0;

                if (length > 1)
                    return 1;
                break;

            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE :
                if (length > 0)
                {
                    for (int index = 0; index < length; index++)
                    {
                        if (children[offset + index].uri != fFirstChild.uri || 
                            children[offset + index].localpart != fFirstChild.localpart)
                            if (!comparator.isEquivalentTo(children[offset+index], fFirstChild)) 
                                return index;
                    }
                }
                break;

            case XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE :
                if (length == 0)
                    return 0;

                for (int index = 0; index < length; index++)
                {
                    if (children[offset + index].uri != fFirstChild.uri || 
                        children[offset + index].localpart != fFirstChild.localpart)
                        if (!comparator.isEquivalentTo(children[offset+index], fFirstChild)) 
                            return index;
                }
                break;

            case XMLContentSpec.CONTENTSPECNODE_CHOICE :
                if (length == 0)
                    return 0;

                if ((children[offset].uri != fFirstChild.uri || children[offset].localpart != fFirstChild.localpart) &&
                    (children[offset].uri != fSecondChild.uri || children[offset].localpart != fSecondChild.localpart))
                    if (   !comparator.isEquivalentTo(children[offset], fFirstChild) 
                        && !comparator.isEquivalentTo(children[offset], fSecondChild) ) 
                        return 0;

                if (length > 1)
                    return 1;
                break;

            case XMLContentSpec.CONTENTSPECNODE_SEQ :
                if (length == 2) {
                    if (children[offset].uri != fFirstChild.uri || children[offset].localpart != fFirstChild.localpart)
                        if (!comparator.isEquivalentTo(children[offset], fFirstChild)) 
                            return 0;

                    if (children[offset + 1].uri != fSecondChild.uri || children[offset + 1].localpart != fSecondChild.localpart)
                        if (!comparator.isEquivalentTo(children[offset+1], fSecondChild)) 
                            return 1;
                }
                else {
                    if (length > 2) {
                        return 2;
                    }

                    return length;
                }

                break;

            default :
                throw new CMException(ImplementationMessages.VAL_CST);
        }

        return -1;
    }

    public void setEquivClassComparator(EquivClassComparator comparator) {
        this.comparator = comparator;
    }

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
    public int whatCanGoHere(boolean fullyValid, InsertableElementsInfo info) 
        throws Exception {

        for (int index = info.insertAt; index < info.childCount; index++) {
            info.curChildren[index].setValues(info.curChildren[index+1]);
        }
        info.childCount--;
        
        final int failedIndex = validateContent(info.curChildren, 0, info.childCount);
        if ((failedIndex != -1) && (failedIndex < info.insertAt))
            return failedIndex;

        info.canHoldPCData = false;

        if ((fOp == XMLContentSpec.CONTENTSPECNODE_LEAF)
        ||  (fOp == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE)
        ||  (fOp == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE)
        ||  (fOp == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE))
        {
            info.resultsCount = 1;
        }
         else if ((fOp == XMLContentSpec.CONTENTSPECNODE_CHOICE)
              ||  (fOp == XMLContentSpec.CONTENTSPECNODE_SEQ))
        {
            info.resultsCount = 2;
        }
         else
        {
            throw new CMException(ImplementationMessages.VAL_CST);
        }

        if ((info.results == null) || (info.results.length < info.resultsCount))
            info.results = new boolean[info.resultsCount];

        if ((info.possibleChildren == null)
        ||  (info.possibleChildren.length < info.resultsCount))
        {
            info.possibleChildren = new QName[info.resultsCount];
            for (int i = 0; i < info.possibleChildren.length; i++) {
                info.possibleChildren[i] = new QName();
            }
        }

        info.possibleChildren[0].setValues(fFirstChild);
        info.results[0] = false;
        if (info.resultsCount == 2)
        {
            info.possibleChildren[1].setValues(fSecondChild);
            info.results[1] = false;
        }

        info.isValidEOC = false;

        switch(fOp)
        {
            case XMLContentSpec.CONTENTSPECNODE_LEAF :
            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE :
                if (info.childCount == 0)
                {
                    info.results[0] = true;
                }
                 else if (info.childCount > 0)
                {
                    if (!fullyValid && (info.insertAt == 0))
                        info.results[0] = true;
                }

                if (fOp == XMLContentSpec.CONTENTSPECNODE_LEAF)
                {
                    if (info.insertAt == 0)
                        info.isValidEOC = true;
                }
                 else
                {
                    info.isValidEOC = true;
                }
                break;

            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE :
            case XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE :
                info.results[0] = true;

                if ((fOp == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE)
                ||  (info.insertAt > 0))
                {
                    info.isValidEOC = true;
                }
                break;

            case XMLContentSpec.CONTENTSPECNODE_CHOICE :
                if (info.insertAt == 0)
                {
                    if (!fullyValid && (info.childCount == 0))
                    {
                        info.results[0] = true;
                        info.results[1] = true;
                    }
                }

                if (info.insertAt == 1)
                    info.isValidEOC = true;
                break;

            case XMLContentSpec.CONTENTSPECNODE_SEQ :
                if (info.insertAt == 0)
                {
                    if (fullyValid)
                    {
                        if (info.childCount == 1)
                            info.results[0] = info.curChildren[0].uri == fSecondChild.uri &&
                                              info.curChildren[0].localpart == fSecondChild.localpart;
                    }
                     else
                    {
                        info.results[0] = true;
                    }
                }
                 else if (info.insertAt == 1)
                {
                    if (!fullyValid || (info.childCount == 1))
                        info.results[1] = true;
                }

                if (info.insertAt == 2)
                    info.isValidEOC = true;
                break;

            default :
                throw new CMException(ImplementationMessages.VAL_CST);
        }

        return -1;
    }

    public ContentLeafNameTypeVector getContentLeafNameTypeVector() {
          return null;
    }
