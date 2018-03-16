import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;

/** Optimized implementation of a vector of bits.  This is more-or-less like
  java.util.BitSet, but also includes the following:
  <ul>
  <li>a count() method, which efficiently computes the number of one bits;</li>
  <li>optimized read from and write to disk;</li>
  <li>inlinable get() method;</li>
  <li>store and load, as bit set or d-gaps, depending on sparseness;</li> 
  </ul>

  @author Doug Cutting
  @version $Id: BitVector.java 494136 2007-01-08 18:11:08Z mikemccand $
  */
public final class BitVector {

  private byte[] bits;
  private int size;
  private int count = -1;

  /** Constructs a vector capable of holding <code>n</code> bits. */
  public BitVector(int n) {
    size = n;
    bits = new byte[(size >> 3) + 1];
  }

  /** Sets the value of <code>bit</code> to one. */
  public final void set(int bit) {
    if (bit >= size) {
      throw new ArrayIndexOutOfBoundsException(bit);
    }
    bits[bit >> 3] |= 1 << (bit & 7);
    count = -1;
  }

  /** Sets the value of <code>bit</code> to zero. */
  public final void clear(int bit) {
    if (bit >= size) {
      throw new ArrayIndexOutOfBoundsException(bit);
    }
    bits[bit >> 3] &= ~(1 << (bit & 7));
    count = -1;
  }

  /** Returns <code>true</code> if <code>bit</code> is one and
    <code>false</code> if it is zero. */
  public final boolean get(int bit) {
    if (bit >= size) {
      throw new ArrayIndexOutOfBoundsException(bit);
    }
    return (bits[bit >> 3] & (1 << (bit & 7))) != 0;
  }

  /** Returns the number of bits in this vector.  This is also one greater than
    the number of the largest valid bit number. */
  public final int size() {
    return size;
  }

  /** Returns the total number of one bits in this vector.  This is efficiently
    computed and cached, so that, if the vector is not changed, no
    recomputation is done for repeated calls. */
  public final int count() {
    if (count == -1) {
      int c = 0;
      int end = bits.length;
      for (int i = 0; i < end; i++)
      count = c;
    }
    return count;
  }

    0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4,
    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
    4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8
  };


  /** Writes this vector to the file <code>name</code> in Directory
    <code>d</code>, in a format that can be read by the constructor {@link
    #BitVector(Directory, String)}.  */
  public final void write(Directory d, String name) throws IOException {
    IndexOutput output = d.createOutput(name);
    try {
      if (isSparse()) { 
      } else {
        writeBits(output);
      }
    } finally {
      output.close();
    }
  }
     
  /** Write as a bit set */
  private void writeBits(IndexOutput output) throws IOException {
    output.writeBytes(bits, bits.length);
  }
  
  /** Write as a d-gaps list */
  private void writeDgaps(IndexOutput output) throws IOException {
    int last=0;
    int n = count();
    int m = bits.length;
    for (int i=0; i<m && n>0; i++) {
      if (bits[i]!=0) {
        output.writeVInt(i-last);
        output.writeByte(bits[i]);
        last = i;
        n -= BYTE_COUNTS[bits[i] & 0xFF];
      }
    }
  }

  /** Indicates if the bit vector is sparse and should be saved as a d-gaps list, or dense, and should be saved as a bit set. */
  private boolean isSparse() {
    int factor = 10;  
    if (bits.length < (1<< 7)) return factor * (4 + (8+ 8)*count()) < size();
    if (bits.length < (1<<14)) return factor * (4 + (8+16)*count()) < size();
    if (bits.length < (1<<21)) return factor * (4 + (8+24)*count()) < size();
    if (bits.length < (1<<28)) return factor * (4 + (8+32)*count()) < size();
    return                            factor * (4 + (8+40)*count()) < size();
  }

  /** Constructs a bit vector from the file <code>name</code> in Directory
    <code>d</code>, as written by the {@link #write} method.
    */
  public BitVector(Directory d, String name) throws IOException {
    IndexInput input = d.openInput(name);
    try {
      if (size == -1) {
        readDgaps(input);
      } else {
        readBits(input);
      }
    } finally {
      input.close();
    }
  }

  /** Read as a bit set */
  private void readBits(IndexInput input) throws IOException {
    input.readBytes(bits, 0, bits.length);
  }

  /** read as a d-gaps list */ 
  private void readDgaps(IndexInput input) throws IOException {
    int last=0;
    int n = count();
    while (n>0) {
      last += input.readVInt();
      bits[last] = input.readByte();
      n -= BYTE_COUNTS[bits[last] & 0xFF];
    }          
  }
  
}
