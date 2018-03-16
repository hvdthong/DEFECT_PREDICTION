package org.apache.xerces.validators.common;

/**
 *
 * @version
 */
public abstract class CMNode
{
    CMNode(int type) throws CMException
    {
        fType = type;
    }


    abstract boolean isNullable() throws CMException;


    final int type()
    {
        return fType;
    }

    final CMStateSet firstPos() throws CMException
    {
        if (fFirstPos == null)
        {
            fFirstPos = new CMStateSet(fMaxStates);
            calcFirstPos(fFirstPos);
        }
        return fFirstPos;
    }

    final CMStateSet lastPos() throws CMException
    {
        if (fLastPos == null)
        {
            fLastPos = new CMStateSet(fMaxStates);
            calcLastPos(fLastPos);
        }
        return fLastPos;
    }

    final void setFollowPos(CMStateSet setToAdopt)
    {
        fFollowPos = setToAdopt;
    }

    final void setMaxStates(int maxStates)
    {
        fMaxStates = maxStates;
    }


    protected abstract void calcFirstPos(CMStateSet toSet) throws CMException;

    protected abstract void calcLastPos(CMStateSet toSet) throws CMException;


    private int         fType;
    private CMStateSet  fFirstPos   = null;
    private CMStateSet  fFollowPos  = null;
    private CMStateSet  fLastPos    = null;
    private int         fMaxStates  = -1;
};
