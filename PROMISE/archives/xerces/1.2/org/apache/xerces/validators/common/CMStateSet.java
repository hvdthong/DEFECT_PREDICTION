package org.apache.xerces.validators.common;

import org.apache.xerces.utils.ImplementationMessages;

/**
 * This class is a very simple bitset class. The DFA content model code needs
 * to support a bit set, but the java BitSet class is way, way overkill. Our
 * bitset never needs to be expanded after creation, hash itself, etc...
 *
 * Since the vast majority of content models will never require more than 64
 * bits, and since allocation of anything in Java is expensive, this class
 * provides a hybrid implementation that uses two ints for instances that use
 * 64 bits or fewer. It has a byte array reference member which will only be
 * used if more than 64 bits are required.
 *
 * Note that the code that uses this class will never perform operations
 * on sets of different sizes, so that check does not have to be made here.
 *
 * @version
 */
class CMStateSet
{
    CMStateSet(int bitCount) throws CMException
    {
        fBitCount = bitCount;
        if (fBitCount < 0)
            throw new CMException(ImplementationMessages.VAL_CMSI);

        if (fBitCount > 64)
        {
            fByteCount = fBitCount / 8;
            if (fBitCount % 8 != 0)
                fByteCount++;
            fByteArray = new byte[fByteCount];
        }

        zeroBits();
    }


    public String toString()
    {
        StringBuffer strRet = new StringBuffer();
        try
        {
            strRet.append("{");
            for (int index = 0; index < fBitCount; index++)
            {
                if (getBit(index))
                    strRet.append(" " + index);
            }
            strRet.append(" }");
        }

        catch(CMException exToCatch)
        {
        }
        return strRet.toString();
    }


    final void intersection(CMStateSet setToAnd)
    {
        if (fBitCount < 65)
        {
            fBits1 &= setToAnd.fBits1;
            fBits2 &= setToAnd.fBits2;
        }
         else
        {
            for (int index = fByteCount - 1; index >= 0; index--)
                fByteArray[index] &= setToAnd.fByteArray[index];
        }
    }

    final boolean getBit(int bitToGet) throws CMException
    {
        if (bitToGet >= fBitCount)
            throw new CMException(ImplementationMessages.VAL_CMSI);

        if (fBitCount < 65)
        {
            final int mask = (0x1 << (bitToGet % 32));
            if (bitToGet < 32)
                return (fBits1 & mask) != 0;
            else
                return (fBits2 & mask) != 0;
        }
         else
        {
            final byte mask = (byte)(0x1 << (bitToGet % 8));
            final int ofs = bitToGet >> 3;

            return ((fByteArray[ofs] & mask) != 0);
        }
    }

    final boolean isEmpty()
    {
        if (fBitCount < 65)
        {
            return ((fBits1 == 0) && (fBits2 == 0));
        }
         else
        {
            for (int index = fByteCount - 1; index >= 0; index--)
            {
                if (fByteArray[index] != 0)
                    return false;
            }
        }
        return true;
    }

    final boolean isSameSet(CMStateSet setToCompare)
    {
        if (fBitCount != setToCompare.fBitCount)
            return false;

        if (fBitCount < 65)
        {
            return ((fBits1 == setToCompare.fBits1)
            &&      (fBits2 == setToCompare.fBits2));
        }

        for (int index = fByteCount - 1; index >= 0; index--)
        {
            if (fByteArray[index] != setToCompare.fByteArray[index])
                return false;
        }
        return true;
    }

    final void union(CMStateSet setToOr)
    {
        if (fBitCount < 65)
        {
            fBits1 |= setToOr.fBits1;
            fBits2 |= setToOr.fBits2;
        }
         else
        {
            for (int index = fByteCount - 1; index >= 0; index--)
                fByteArray[index] |= setToOr.fByteArray[index];
        }
    }

    final void setBit(int bitToSet) throws CMException
    {
        if (bitToSet >= fBitCount)
            throw new CMException(ImplementationMessages.VAL_CMSI);

        if (fBitCount < 65)
        {
            final int mask = (0x1 << (bitToSet % 32));
            if (bitToSet < 32)
            {
                fBits1 &= ~mask;
                fBits1 |= mask;
            }
             else
            {
                fBits2 &= ~mask;
                fBits2 |= mask;
            }
        }
         else
        {
            final byte mask = (byte)(0x1 << (bitToSet % 8));
            final int ofs = bitToSet >> 3;

            fByteArray[ofs] &= ~mask;
            fByteArray[ofs] |= mask;
        }
    }

    final void setTo(CMStateSet srcSet) throws CMException
    {
        if (fBitCount != srcSet.fBitCount)
            throw new CMException(ImplementationMessages.VAL_CMSI);
        
        if (fBitCount < 65)
        {
            fBits1 = srcSet.fBits1;
            fBits2 = srcSet.fBits2;
        }
         else
        {
            for (int index = fByteCount - 1; index >= 0; index--)
                fByteArray[index] = srcSet.fByteArray[index];
        }
    }

    final void zeroBits()
    {
        if (fBitCount < 65)
        {
            fBits1 = 0;
            fBits2 = 0;
        }
         else
        {
            for (int index = fByteCount - 1; index >= 0; index--)
                fByteArray[index] = 0;
        }
    }


    int         fBitCount;
    int         fByteCount;
    int         fBits1;
    int         fBits2;
    byte[]      fByteArray;
};
