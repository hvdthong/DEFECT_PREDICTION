package org.apache.xerces.validators.common;

import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.validators.schema.SubstitutionGroupComparator;
import org.apache.xerces.validators.schema.SchemaGrammar;

/**
 * MixedContentModel is a derivative of the abstract content model base
 * class that handles the special case of mixed model elements. If an element
 * is mixed model, it has PCDATA as its first possible content, followed
 * by an alternation of the possible children. The children cannot have any
 * numeration or order, so it must look like this:
 * <pre>
 *   &lt;!ELEMENT Foo ((#PCDATA|a|b|c|)*)&gt;
 * </pre>
 * So, all we have to do is to keep an array of the possible children and
 * validate by just looking up each child being validated by looking it up
 * in the list.
 *
 * @version $Id: MixedContentModel.java 317357 2001-07-16 21:22:16Z sandygao $
 */
public class MixedContentModel
    implements XMLContentModel {



    /** The count of possible children that we have to deal with. */
    private int fCount;

    /** The list of possible children that we have to accept. */
    private QName fChildren[];

    /** The type of the children to support ANY. */
    private int fChildrenType[];

    /* this is the SubstitutionGroupComparator object */
    private SubstitutionGroupComparator comparator = null;

    /**
     * True if mixed content model is ordered. DTD mixed content models
     * are <em>always</em> unordered.
     */
    private boolean fOrdered;

    /** Boolean to allow DTDs to validate even with namespace support. */
    private boolean fDTD;


    /**
     * Constructs a mixed content model.
     *
     * @param count The child count.
     * @param childList The list of allowed children.
     *
     * @exception CMException Thrown if content model can't be built.
     */
    public MixedContentModel(QName childList[],
                             int childListType[],
                             int offset, int length) throws CMException {
        this(childList, childListType, offset, length, false, false);
    }

    /**
     * Constructs a mixed content model.
     *
     * @param count The child count.
     * @param childList The list of allowed children.
     * @param ordered True if content must be ordered.
     *
     * @exception CMException Thrown if content model can't be built.
     */
    public MixedContentModel(QName childList[],
                             int childListType[],
                             int offset, int length,
                             boolean ordered) throws CMException {
        this(childList, childListType, offset, length, ordered, false);
    }

    /**
     * Constructs a mixed content model.
     *
     * @param count The child count.
     * @param childList The list of allowed children.
     * @param ordered True if content must be ordered.
     *
     * @exception CMException Thrown if content model can't be built.
     */
    public MixedContentModel(QName childList[],
                             int childListType[],
                             int offset, int length,
                             boolean ordered,
                             boolean dtd) throws CMException {

        fCount = length;
        fChildren = new QName[fCount];
        fChildrenType = new int[fCount];
        for (int i = 0; i < fCount; i++) {
            fChildren[i] = new QName(childList[offset + i]);
            fChildrenType[i] = childListType[offset + i];
        }
        fOrdered = ordered;

        fDTD = dtd;


    public void checkUniqueParticleAttribution(SchemaGrammar gram) {
        for (int i = 0; i < fCount; i++)
            fChildren[i].uri = gram.getContentSpecOrgUri(fChildren[i].uri);

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
    public int validateContent(QName children[], int offset, int length)
        throws Exception {

        if (fOrdered) {
            int inIndex = 0;
            for (int outIndex = 0; outIndex < length; outIndex++) {

                final QName curChild = children[offset + outIndex];
                if (curChild.localpart == -1) {
                    continue;
                }

                int type = fChildrenType[inIndex];
                if (type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
                    if (fDTD) {
                        if (fChildren[inIndex].rawname != children[offset + outIndex].rawname) {
                            return outIndex;
                        }
                    }
                    else {
                        if (fChildren[inIndex].uri != children[offset + outIndex].uri &&
                            fChildren[inIndex].localpart != children[offset + outIndex].localpart) {
                            return outIndex;
                        }
                    }
                }

                inIndex++;
            }
        }

        else {
            for (int outIndex = 0; outIndex < length; outIndex++)
            {
                final QName curChild = children[offset + outIndex];

                if (curChild.localpart == -1)
                    continue;

                int inIndex = 0;
                for (; inIndex < fCount; inIndex++)
                {
                    int type = fChildrenType[inIndex];
                    if (type == XMLContentSpec.CONTENTSPECNODE_LEAF) {
                        if (fDTD) {
                            if (curChild.rawname == fChildren[inIndex].rawname) {
                                break;
                            }
                        }
                        else {
                            if (curChild.uri == fChildren[inIndex].uri &&
                                curChild.localpart == fChildren[inIndex].localpart)
                                break;
                        }
                    }
                }

                if (inIndex == fCount)
                    return outIndex;
            }
        }

        return -1;

    }

    public int validateContentSpecial(QName children[], int offset, int length) throws Exception{
            return validateContent(children,offset, length);
    }

    public void setSubstitutionGroupComparator(SubstitutionGroupComparator comparator) {
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
    public int whatCanGoHere(boolean                    fullyValid
                            , InsertableElementsInfo    info) throws Exception
    {
        for (int index = info.insertAt; index < info.childCount-1; index++)
            info.curChildren[index] = info.curChildren[index+1];
        info.childCount--;

        final int failedIndex = validateContent(info.curChildren, 0, info.childCount);
        if ((failedIndex != -1) && (failedIndex < info.insertAt))
            return failedIndex;

        info.canHoldPCData = true;
        info.isValidEOC = true;

        info.resultsCount = fCount;

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

        boolean bStatus = true;
        if (fullyValid && (failedIndex < info.childCount))
            bStatus = false;

        for (int index = 0; index < fCount; index++)
        {
            info.possibleChildren[index].setValues(fChildren[index]);
            info.results[index] = bStatus;
        }

        return -1;
    }


    public ContentLeafNameTypeVector getContentLeafNameTypeVector() {
        return null;
    }


