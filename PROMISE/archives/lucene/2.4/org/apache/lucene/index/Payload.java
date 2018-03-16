import java.io.Serializable;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.ArrayUtil;

/**
 *  A Payload is metadata that can be stored together with each occurrence 
 *  of a term. This metadata is stored inline in the posting list of the
 *  specific term.  
 *  <p>
 *  To store payloads in the index a {@link TokenStream} has to be used that
 *  produces {@link Token}s containing payload data.
 *  <p>
 *  Use {@link TermPositions#getPayloadLength()} and {@link TermPositions#getPayload(byte[], int)}
 *  to retrieve the payloads from the index.<br>
 *
 */
public class Payload implements Serializable, Cloneable {
  /** the byte array containing the payload data */
  protected byte[] data;
    
  /** the offset within the byte array */
  protected int offset;
    
  /** the length of the payload data */
  protected int length;
    
  /** Creates an empty payload and does not allocate a byte array. */
  public Payload() {
  }
    
  /**
   * Creates a new payload with the the given array as data.
   * A reference to the passed-in array is held, i. e. no 
   * copy is made.
   * 
   * @param data the data of this payload
   */
  public Payload(byte[] data) {
    this(data, 0, data.length);
  }

  /**
   * Creates a new payload with the the given array as data. 
   * A reference to the passed-in array is held, i. e. no 
   * copy is made.
   * 
   * @param data the data of this payload
   * @param offset the offset in the data byte array
   * @param length the length of the data
   */
  public Payload(byte[] data, int offset, int length) {
    if (offset < 0 || offset + length > data.length) {
      throw new IllegalArgumentException();
    }
    this.data = data;
    this.offset = offset;
    this.length = length;
  }
    
  /**
   * Sets this payloads data. 
   * A reference to the passed-in array is held, i. e. no 
   * copy is made.
   */
  public void setData(byte[] data) {
    setData(data, 0, data.length);
  }

  /**
   * Sets this payloads data. 
   * A reference to the passed-in array is held, i. e. no 
   * copy is made.
   */
  public void setData(byte[] data, int offset, int length) {
    this.data = data;
    this.offset = offset;
    this.length = length;
  }
    
  /**
   * Returns a reference to the underlying byte array
   * that holds this payloads data.
   */
  public byte[] getData() {
    return this.data;
  }
    
  /**
   * Returns the offset in the underlying byte array 
   */
  public int getOffset() {
    return this.offset;
  }
    
  /**
   * Returns the length of the payload data. 
   */
  public int length() {
    return this.length;
  }
    
  /**
   * Returns the byte at the given index.
   */
  public byte byteAt(int index) {
    if (0 <= index && index < this.length) {
      return this.data[this.offset + index];    
    }
    throw new ArrayIndexOutOfBoundsException(index);
  }
    
  /**
   * Allocates a new byte array, copies the payload data into it and returns it. 
   */
  public byte[] toByteArray() {
    byte[] retArray = new byte[this.length];
    System.arraycopy(this.data, this.offset, retArray, 0, this.length);
    return retArray;
  }
    
  /**
   * Copies the payload data to a byte array.
   * 
   * @param target the target byte array
   * @param targetOffset the offset in the target byte array
   */
  public void copyTo(byte[] target, int targetOffset) {
    if (this.length > target.length + targetOffset) {
      throw new ArrayIndexOutOfBoundsException();
    }
    System.arraycopy(this.data, this.offset, target, targetOffset, this.length);
  }

  /**
   * Clones this payload by creating a copy of the underlying
   * byte array.
   */
  public Object clone() {
    try {
      Payload clone = (Payload) super.clone();
      if (offset == 0 && length == data.length) {
        clone.data = (byte[]) data.clone();
      }
      else {
        clone.data = this.toByteArray();
        clone.offset = 0;
      }
      return clone;
    } catch (CloneNotSupportedException e) {
    }
  }

  public boolean equals(Object obj) {
    if (obj == this)
      return true;
    if (obj instanceof Payload) {
      Payload other = (Payload) obj;
      if (length == other.length) {
        for(int i=0;i<length;i++)
          if (data[offset+i] != other.data[other.offset+i])
            return false;
        return true;
      } else
        return false;
    } else
      return false;
  }

  public int hashCode() {
    return ArrayUtil.hashCode(data, offset, offset+length);
  }
}
