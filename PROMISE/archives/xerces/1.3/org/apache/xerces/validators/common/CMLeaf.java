package org.apache.xerces.validators.common;

import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.utils.ImplementationMessages;
import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;

/**
 * Content model leaf node.
 *
 * @version $Id: CMLeaf.java 315647 2000-05-17 18:33:29Z jeffreyr $
 */
public class CMLeaf 
    extends CMNode {


    /** This is the element that this leaf represents. */
    private QName fElement = new QName();

    /**
     * Part of the algorithm to convert a regex directly to a DFA
     * numbers each leaf sequentially. If its -1, that means its an
     * epsilon node. Zero and greater are non-epsilon positions.
     */
    private int fPosition = -1;


    /** Constructs a content model leaf. */
    public CMLeaf(QName element, int position) throws CMException {
        super(XMLContentSpec.CONTENTSPECNODE_LEAF);

        fElement.setValues(element);
        fPosition = position;
    }

    /** Constructs a content model leaf. */
    public CMLeaf(QName element) throws CMException {
        super(XMLContentSpec.CONTENTSPECNODE_LEAF);

        fElement.setValues(element);
    }


    final QName getElement()
    {
        return fElement;
    }

    final int getPosition()
    {
        return fPosition;
    }

    final void setPosition(int newPosition)
    {
        fPosition = newPosition;
    }



    boolean isNullable() throws CMException
    {
        return (fPosition == -1);
    }

    String toString(StringPool stringPool)
    {
        StringBuffer strRet = new StringBuffer(fElement.toString());
        strRet.append(" (");
        strRet.append(stringPool.toString(fElement.uri));
        strRet.append(',');
        strRet.append(stringPool.toString(fElement.localpart));
        strRet.append(')');
        if (fPosition >= 0)
        {
            strRet.append
            (
                " (Pos:"
                + new Integer(fPosition).toString()
                + ")"
            );
        }
        return strRet.toString();
    }


    protected void calcFirstPos(CMStateSet toSet) throws CMException
    {
        if (fPosition == -1)
            toSet.zeroBits();

        else
            toSet.setBit(fPosition);
    }

    protected void calcLastPos(CMStateSet toSet) throws CMException
    {
        if (fPosition == -1)
            toSet.zeroBits();

        else
            toSet.setBit(fPosition);
    }

