package org.apache.xalan.xsltc.dom;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.xml.dtm.DTMAxisIterator;


/**
 * @author Morten Jorgensen
 */
public class BitArray implements Externalizable {

    private int[] _bits;
    private int   _bitSize;
    private int   _intSize;
    private int   _mask;

    private final static int[] _masks = {
	0x80000000, 0x40000000, 0x20000000, 0x10000000,
	0x08000000, 0x04000000, 0x02000000, 0x01000000,
	0x00800000, 0x00400000, 0x00200000, 0x00100000,
	0x00080000, 0x00040000, 0x00020000, 0x00010000,
	0x00008000, 0x00004000, 0x00002000, 0x00001000,
	0x00000800, 0x00000400, 0x00000200, 0x00000100,
	0x00000080, 0x00000040, 0x00000020, 0x00000010,
	0x00000008, 0x00000004, 0x00000002, 0x00000001 };

    private final static boolean DEBUG_ASSERTIONS = false;
    
    /**
     * Constructor. Defines the initial size of the bit array (in bits).
     */
    public BitArray() {
	this(32);
    }

    public BitArray(int size) {        
        if (size < 32) size = 32;
        _bitSize = size;
        _intSize = (_bitSize >>> 5) + 1;
        _bits = new int[_intSize + 1];
    }

    public BitArray(int size, int[] bits) {
	if (size < 32) size = 32;
	_bitSize = size;
	_intSize = (_bitSize >>> 5) + 1;
	_bits = bits;
    }

    /**
     * Set the mask for this bit array. The upper 8 bits of this mask
     * indicate the DOM in which the nodes in this array belong.
     */    
    public void setMask(int mask) {
	_mask = mask;
    }

    /**
     * See setMask()
     */
    public int getMask() {
	return(_mask);
    }

    /**
     * Returns the size of this bit array (in bits).
     */
    public final int size() {
	return(_bitSize);
    }

    /**
     * Returns true if the given bit is set
     */
    public final boolean getBit(int bit) {
        if (DEBUG_ASSERTIONS) {
            if (bit >= _bitSize) {
                throw new Error(
                             "Programmer's assertion in  BitArray.getBit");
            }
        }

        return((_bits[bit>>>5] & _masks[bit%32]) != 0);
    }

    /**
     * Returns the next set bit from a given position
     */
    public final int getNextBit(int startBit) {
        for (int i = (startBit >>> 5) ; i<=_intSize; i++) {
            int bits = _bits[i];
            if (bits != 0) {
                for (int b = (startBit % 32); b<32; b++) {
                    if ((bits & _masks[b]) != 0) {
                        return((i << 5) + b);
                    }
                }
            }
            startBit = 0;
        }
        return(DTMAxisIterator.END);
    }

    /**
     * This method returns the Nth bit that is set in the bit array. The
     * current position is cached in the following 4 variables and will
     * help speed up a sequence of next() call in an index iterator. This
     * method is a mess, but it is fast and it works, so don't fuck with it.
     */
    private int _pos = Integer.MAX_VALUE;
    private int _node = 0;
    private int _int = 0;
    private int _bit = 0;

    public final int getBitNumber(int pos) {

	if (pos == _pos) return(_node);
	
	if (pos < _pos) {
	    _int = _bit = _pos = 0;
	}

	for ( ; _int <= _intSize; _int++) {
	    int bits = _bits[_int];
		for ( ; _bit < 32; _bit++) {
		    if ((bits & _masks[_bit]) != 0) {
			if (++_pos == pos) {
			    _node = ((_int << 5) + _bit) - 1;
			    return (_node);
			}
		    }
		}
		_bit = 0;
	    }
	}
	return(0);
    }

    /**
     * Returns the integer array in which the bit array is contained
     */
    public final int[] data() {
	return(_bits);
    }


    /**
     * Sets a given bit
     */
    public final void setBit(int bit) {
        if (DEBUG_ASSERTIONS) {
            if (bit >= _bitSize) {
                throw new Error(
                             "Programmer's assertion in  BitArray.getBit");
            }
        }

        if (bit >= _bitSize) return;
        final int i = (bit >>> 5);
        if (i < _first) _first = i;
        if (i > _last) _last = i;
        _bits[i] |= _masks[bit % 32];
    }

    /**
     * Merge two bit arrays. This currently only works for nodes from
     * a single DOM (because there is only one _mask per array).
     */
    public final BitArray merge(BitArray other) {
	if (_last == -1) {
	    _bits = other._bits;
	}
	else if (other._last != -1) {
	    int start = (_first < other._first) ? _first : other._first;
	    int stop  = (_last > other._last) ? _last : other._last;

	    if (other._intSize > _intSize) {
		if (stop > _intSize) stop = _intSize;
		for (int i=start; i<=stop; i++)
		    other._bits[i] |= _bits[i];
		_bits = other._bits;
	    }
	    else {
		if (stop > other._intSize) stop = other._intSize;
		for (int i=start; i<=stop; i++)
		    _bits[i] |= other._bits[i];
	    }
	}
	return(this);
    }

    /**
     * Resizes the bit array - try to avoid using this method!!!
     */
    public final void resize(int newSize) {
	if (newSize > _bitSize) {
	    _intSize = (newSize >>> 5) + 1;
	    final int[] newBits = new int[_intSize + 1];
	    System.arraycopy(_bits, 0, newBits, 0, (_bitSize>>>5) + 1);
	    _bits = newBits;
	    _bitSize = newSize;
	}
    }

    public BitArray cloneArray() {
	return(new BitArray(_intSize, _bits));
    }

    public void writeExternal(ObjectOutput out) throws IOException {
	out.writeInt(_bitSize);
	out.writeInt(_mask);
	out.writeObject(_bits);
	out.flush();
    }

    /**
     * Read the whole tree from a file (serialized)
     */
    public void readExternal(ObjectInput in)
	throws IOException, ClassNotFoundException {
	_bitSize = in.readInt();
	_intSize = (_bitSize >>> 5) + 1;
	_mask    = in.readInt();
	_bits    = (int[])in.readObject();
    }

}
