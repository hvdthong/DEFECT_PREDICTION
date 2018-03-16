package org.apache.xerces.validators.common;

import org.apache.xerces.framework.XMLContentSpec;
import org.apache.xerces.utils.ImplementationMessages;

/**
 *
 * @version
 */
public class CMUniOp extends CMNode
{
    public CMUniOp(int type, CMNode childNode) throws CMException
    {
        super(type);

        if ((type() != XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE)
        &&  (type() != XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE)
        &&  (type() != XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE))
        {
            throw new CMException(ImplementationMessages.VAL_UST);
        }

        fChild = childNode;
    }


    final CMNode getChild()
    {
        return fChild;
    }


    boolean isNullable() throws CMException
    {
        if ((type() == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE)
        ||  (type() == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE))
        {
            throw new CMException(ImplementationMessages.VAL_UST);
        }
        return true;
    }


    protected void calcFirstPos(CMStateSet toSet) throws CMException
    {
        toSet.setTo(fChild.firstPos());
    }

    protected void calcLastPos(CMStateSet toSet) throws CMException
    {
        toSet.setTo(fChild.lastPos());
    }


    private CMNode  fChild;
};
