package org.apache.xerces.validators.common;

/**
 * CMException is thrown during CMNode operations
 *
 * @version
 */
public class CMException extends Exception
{
    public CMException(int errCode)
    {
        fErrorCode = errCode;
    }

    public int getErrorCode()
    {
        return fErrorCode;
    }


    static final int       fUnused = -1000;


    private int     fErrorCode;
};
