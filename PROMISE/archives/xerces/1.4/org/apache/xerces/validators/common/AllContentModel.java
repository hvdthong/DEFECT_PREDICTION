package org.apache.xerces.validators.common;

import org.apache.xerces.utils.ImplementationMessages;
import org.apache.xerces.utils.QName;
import org.apache.xerces.validators.schema.SubstitutionGroupComparator;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.validators.schema.SchemaGrammar;
import org.apache.xerces.framework.XMLContentSpec;

import java.util.Hashtable;
import java.util.Vector;

public class AllContentModel implements XMLContentModel {

    private QName   fAllElements[] = new QName[10];
    private boolean fIsOptionalElement[] = new boolean[10];
    private boolean fHasOptionalContent = false;
    private boolean fIsMixed = false;

    private int fNumElements = 0;
    private int fNumRequired = 0;

    private Hashtable fElementsHash;

    /* this is the SubstitutionGroupComparator object */
    private SubstitutionGroupComparator fComparator = null;

    /** Set to true to debug content model validation. */
    private static final boolean DEBUG_VALIDATE_CONTENT = false;

    /**
     *
     */
    public AllContentModel(boolean hasOptionalContent) {
        fHasOptionalContent = hasOptionalContent;

        if (DEBUG_VALIDATE_CONTENT) {
            System.out.println("Entering AllContentModel#AllContentModel");
            System.out.println("   this == "+this);
            System.out.println("   optionalContent == "+hasOptionalContent);
        }
    }

    public AllContentModel(boolean hasOptionalContent, boolean isMixed) {
        this(hasOptionalContent);
        fIsMixed = isMixed;

        if (DEBUG_VALIDATE_CONTENT) {
            System.out.println("   mixed == "+fIsMixed);
        }
    }

    void addElement(QName newElement, boolean isOptional) {
        if (DEBUG_VALIDATE_CONTENT) {
            System.out.println("Entering AllContentModel#addElement");
        }

        if (fNumElements >= fAllElements.length) {
            QName newAllElements[] = new QName[2*fAllElements.length];
            boolean newIsOptionalElements[] =
                                      new boolean[2*fIsOptionalElement.length];

            System.arraycopy(fAllElements, 0, newAllElements, 0,
                             fAllElements.length);
            System.arraycopy(fIsOptionalElement, 0, newIsOptionalElements, 0,
                             fIsOptionalElement.length);

            fAllElements       = newAllElements;
            fIsOptionalElement = newIsOptionalElements;
        }

        fAllElements[fNumElements] = newElement;
        fIsOptionalElement[fNumElements] = isOptional;

        fNumElements++;

        if (!isOptional) {
            fNumRequired++;
        }

        if (DEBUG_VALIDATE_CONTENT) {
            showAllElements();
            System.out.println("Leaving AllContentModel#addElement");
        }
    }

    public void checkUniqueParticleAttribution(SchemaGrammar gram) throws Exception {
        for (int i = 0; i < fNumElements; i++)
            fAllElements[i].uri = gram.getContentSpecOrgUri(fAllElements[i].uri);

        for (int j = 0; j < fNumElements; j++) {
            for (int k = j+1; k < fNumElements; k++) {
                ElementWildcard.conflict(XMLContentSpec.CONTENTSPECNODE_LEAF,
                                         fAllElements[j].localpart,
                                         fAllElements[j].uri,
                                         XMLContentSpec.CONTENTSPECNODE_LEAF,
                                         fAllElements[k].localpart,
                                         fAllElements[k].uri,
                                         fComparator);
            }
        }
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

        if (DEBUG_VALIDATE_CONTENT)
            System.out.println("Entering AllContentModel#validateContent");

        if (fHasOptionalContent && length == 0) {
            if (DEBUG_VALIDATE_CONTENT) {
                System.out.println("Empty content");
                System.out.println("Leaving AllContentModel#validateContent");
            }
            return -1;
        }

        final int numElements = fNumElements;

        if (fElementsHash == null)
            createElementsHash();

        boolean elementSeen[] = new boolean[numElements];
        int numRequiredSeen = 0;

        for (int childIndex = 0; childIndex < length; childIndex++) {
            QName currChild = children[offset + childIndex];

            if (fIsMixed && currChild.localpart == -1)
                continue;

            Integer foundIdx = (Integer)fElementsHash.get(currChild);

            if (foundIdx == null) {
                if (DEBUG_VALIDATE_CONTENT) {
                    System.out.println("Unexpected element seen - idx == "+
                                       childIndex+" ("+currChild+")");
                    System.out.println("Leaving AllContentModel#validateContent");
                }

                return childIndex;
            }

            int foundIdxVal = foundIdx.intValue();

            if (elementSeen[foundIdxVal]) {
                if (DEBUG_VALIDATE_CONTENT) {
                    System.out.println("Duplicate element seen - idx == "+
                                       childIndex+" ("+currChild+")");
                    System.out.println("Leaving AllContentModel#validateContent");
                }

                return childIndex;
            }

            elementSeen[foundIdxVal] = true;

            if (!fIsOptionalElement[foundIdxVal]) {
                numRequiredSeen++;
            }
        }

        if (numRequiredSeen != fNumRequired) {
            if (DEBUG_VALIDATE_CONTENT) {
                System.out.println("Required element missing");
                System.out.println("Leaving AllContentModel#validateContent");
            }

            return length;
        }

        if (DEBUG_VALIDATE_CONTENT) {
            System.out.println("Successful validation");
            System.out.println("Leaving AllContentModel#validateContent");
        }

        return -1;
    }

    /**
     * This method is different from "validateContent" in that it will try to use
     * the SubstitutionGroupComparator to match children against the content model.
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
    public int validateContentSpecial(QName children[], int offset, int length)
        throws Exception {

        if (fComparator == null)
            return validateContent(children, offset, length);

        if (DEBUG_VALIDATE_CONTENT)
            System.out.println("Entering AllContentModel#validateContentSpecial");

        if (fHasOptionalContent && length == 0) {
            if (DEBUG_VALIDATE_CONTENT) {
                System.out.println("Empty content");
                System.out.println("Leaving AllContentModel#validateContentSpecial");
            }
            return -1;
        }

        final int numElements = fNumElements;

        boolean elementSeen[] = new boolean[numElements];
        int numRequiredSeen = 0;

        childLoop: for (int childIndex = 0; childIndex < length; childIndex++) {
            QName currChild = children[offset + childIndex];

            if (fIsMixed && currChild.localpart == -1)
                continue;

            int compareIdx;

            for (compareIdx = 0; compareIdx < numElements; compareIdx++) {
                if (fComparator.isEquivalentTo(currChild,
                                               fAllElements[compareIdx])) {
                    if (elementSeen[compareIdx]) {
                        if (DEBUG_VALIDATE_CONTENT) {
                            System.out.println("Duplicate element seen - idx == "+
                                               childIndex+" ("+currChild+")");
                            System.out.println("Leaving AllContentModel#validateContentSpecial");
                        }

                        return childIndex;
                    }

                    elementSeen[compareIdx] = true;

                    if (!fIsOptionalElement[compareIdx]) {
                        numRequiredSeen++;
                    }

                    continue childLoop;
                }
            }

            if (DEBUG_VALIDATE_CONTENT) {
                System.out.println("Unexpected element seen - idx == "+
                                   childIndex+" ("+currChild+")");
                System.out.println("Leaving AllContentModel#validateContentSpecial");
            }

            return childIndex;
        }

        if (numRequiredSeen != fNumRequired) {
            if (DEBUG_VALIDATE_CONTENT) {
                System.out.println("Required element missing");
                System.out.println("Leaving AllContentModel#validateContentSpecial");
            }

            return length;
        }

        if (DEBUG_VALIDATE_CONTENT) {
            System.out.println("Successful validation");
            System.out.println("Leaving AllContentModel#validateContentSpecial");
        }

        return -1;
    }

    /**
     * The setter method to pass in the SubstitutionGroupComparator.
     *
     * @param comparator a SubstitutionGroupComparator object.
     * @return 
     * @exception 
     */
    public void setSubstitutionGroupComparator(SubstitutionGroupComparator comparator) {
        fComparator = comparator;
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
    public int whatCanGoHere(boolean fullyValid, 
                             InsertableElementsInfo info) throws Exception {

        if (DEBUG_VALIDATE_CONTENT)
            System.out.println("Entering AllContentModel#whatCanGoHere");


        if (fElementsHash == null)
            createElementsHash();

        final int numElements = fNumElements;

        boolean elementSeen[] = new boolean[numElements];

        final int numChildren = info.curChildren.length;
        final int insertAt = info.insertAt;
        final QName curChildren[] = info.curChildren;

        for (int childIndex = 0; childIndex < insertAt; childIndex++) {
            QName currChild = curChildren[childIndex];

            Integer foundIdx = (Integer)fElementsHash.get(currChild);

            if (foundIdx == null)
                return childIndex;

            int foundIdxVal = foundIdx.intValue();

            if (elementSeen[foundIdxVal])
                return childIndex;

            elementSeen[foundIdxVal] = true;
        }

        info.canHoldPCData = fIsMixed;

        final int resultsCount = numElements - insertAt;
        info.resultsCount = resultsCount;

        if ((info.results == null) || (info.results.length < resultsCount))
            info.results = new boolean[resultsCount];

        if ((info.possibleChildren == null)
        ||  (info.possibleChildren.length < resultsCount))
        {
            info.possibleChildren = new QName[resultsCount];

            QName possibleChildren[] = info.possibleChildren;
            final int possibleChildrenLen = info.possibleChildren.length;

            for (int i = 0; i < possibleChildrenLen; i++) {
                possibleChildren[i] = new QName();
            }
        }

        int possibleChildIdx = 0;

        for (int elemIdx = 0; elemIdx < numElements; elemIdx++) {
            if (!elementSeen[elemIdx]) {
                info.possibleChildren[possibleChildIdx].
                           setValues(fAllElements[elemIdx]);
                info.results[possibleChildIdx] = true;
                possibleChildIdx++;
            }
        }

        info.isValidEOC = (resultsCount == 0);

        if (DEBUG_VALIDATE_CONTENT)
            System.out.println("Leaving AllContentModel#whatCanGoHere");

        if (resultsCount == 0)
          return -1;

        return info.childCount;
    }

    public ContentLeafNameTypeVector getContentLeafNameTypeVector() {
        if (DEBUG_VALIDATE_CONTENT)
            System.out.println("Entering AllContentModel#getContentLeafNameTypeVector");

        if (DEBUG_VALIDATE_CONTENT)
            System.out.println("Leaving AllContentModel#getContentLeafNameTypeVector");
        return null;
    }

    private void createElementsHash() {
        int numElements = fNumElements;
        fElementsHash = new Hashtable(numElements);

        for (int elementIdx = 0; elementIdx < numElements; elementIdx++) {
           fElementsHash.put(fAllElements[elementIdx],
                             new Integer(elementIdx));
        }
    }

    private void showAllElements() {
        for (int elementIdx = 0;
             elementIdx < fNumElements;
             elementIdx++) {
           System.out.print("fAllElements["+elementIdx+"] == " +
                            fAllElements[elementIdx].toString());

           if (fIsOptionalElement[elementIdx]) {
               System.out.print(" (optional)");
           }

           System.out.println();
        }
    }
}
