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
  </ul>

  @author Doug Cutting
  @version $Id: BitVector.java 150536 2004-09-28 18:15:52Z cutting $
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
    bits[bit >> 3] |= 1 << (bit & 7);
    count = -1;
  }

  /** Sets the value of <code>bit</code> to zero. */
  public final void clear(int bit) {
    bits[bit >> 3] &= ~(1 << (bit & 7));
    count = -1;
  }

  /** Returns <code>true</code> if <code>bit</code> is one and
    <code>false</code> if it is zero. */
  public final boolean get(int bit) {
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
    } finally {
      output.close();
    }
  }

  /** Constructs a bit vector from the file <code>name</code> in Directory
    <code>d</code>, as written by the {@link #write} method.
    */
  public BitVector(Directory d, String name) throws IOException {
    IndexInput input = d.openInput(name);
    try {
    } finally {
      input.close();
    }
  }

}
