package org.apache.xml.dtm.ref;
 
import org.w3c.dom.*;
import org.apache.xml.dtm.*;

import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xalan.res.XSLMessages;

/**
 * <code>ChunkedIntArray</code> is an extensible array of blocks of integers.
 * (I'd consider Vector, but it's unable to handle integers except by
 * turning them into Objects.)

 * <p>Making this a separate class means some call-and-return overhead. But
 * doing it all inline tends to be fragile and expensive in coder time,
 * not to mention driving up code size. If you want to inline it, feel free.
 * The Java text suggest that private and Final methods may be inlined, 
 * and one can argue that this beast need not be made subclassable...</p>
 *
 * <p>%REVIEW% This has strong conceptual overlap with the IntVector class.
 * It would probably be a good thing to merge the two, when time permits.<p>
 */
final class ChunkedIntArray
{
  static final int chunkalloc=1<<lowbits;
  static final int lowmask=chunkalloc-1;
  
  ChunksVector chunks=new ChunksVector();
  final int fastArray[] = new int[chunkalloc];
  int lastUsed=0;

  /**
   * Create a new CIA with specified record size. Currently record size MUST
   * be a power of two... and in fact is hardcoded to 4.
   */
  ChunkedIntArray(int slotsize)
  {
    if(this.slotsize<slotsize)
    else if (this.slotsize>slotsize)
      System.out.println("*****WARNING: ChunkedIntArray("+slotsize+") wasting "+(this.slotsize-slotsize)+" words per slot");
    chunks.addElement(fastArray);
  }
  /**
   * Append a 4-integer record to the CIA, starting with record 1. (Since
   * arrays are initialized to all-0, 0 has been reserved as the "unknown"
   * value in DTM.)
   * @return the index at which this record was inserted.
   */
  int appendSlot(int w0, int w1, int w2, int w3)
  {
    /*
    try
    {
      int newoffset = (lastUsed+1)*slotsize;
      fastArray[newoffset] = w0;
      fastArray[newoffset+1] = w1;
      fastArray[newoffset+2] = w2;
      fastArray[newoffset+3] = w3;
      return ++lastUsed;
    }
    catch(ArrayIndexOutOfBoundsException aioobe)
    */
    {
      final int slotsize=4;
      int newoffset = (lastUsed+1)*slotsize;
      int chunkpos = newoffset >> lowbits;
      int slotpos = (newoffset & lowmask);

      if (chunkpos > chunks.size() - 1)
        chunks.addElement(new int[chunkalloc]);
      int[] chunk = chunks.elementAt(chunkpos);
      chunk[slotpos] = w0;
      chunk[slotpos+1] = w1;
      chunk[slotpos+2] = w2;
      chunk[slotpos+3] = w3;

      return ++lastUsed;
    }
  }
  /**
   * Retrieve an integer from the CIA by record number and column within
   * the record, both 0-based (though position 0 is reserved for special
   * purposes).
   * @param position int Record number
   * @param slotpos int Column number
   */
  int readEntry(int position, int offset) throws ArrayIndexOutOfBoundsException
  {
    /*
    try
    {
      return fastArray[(position*slotsize)+offset];
    }
    catch(ArrayIndexOutOfBoundsException aioobe)
    */
    {
      if (offset>=slotsize)
      position*=slotsize;
      int chunkpos = position >> lowbits;
      int slotpos = position & lowmask;
      int[] chunk = chunks.elementAt(chunkpos);
      return chunk[slotpos + offset];
    }
  }
  
  int specialFind(int startPos, int position)
  {
          int ancestor = startPos;
          while(ancestor > 0)
          {
                ancestor*=slotsize;
                int chunkpos = ancestor >> lowbits;
                int slotpos = ancestor & lowmask;
                int[] chunk = chunks.elementAt(chunkpos);
                                                        
                ancestor = chunk[slotpos + 1];

                if(ancestor == position)
                         break;
          }

          if (ancestor <= 0) 
          {
                  return position;
          }
          return -1;
  }
  
  /**
   * @return int index of highest-numbered record currently in use
   */
  int slotsUsed()
  {
    return lastUsed;
  }

  /** Disard the highest-numbered record. This is used in the string-buffer
   CIA; when only a single characters() chunk has been recieved, its index
   is moved into the Text node rather than being referenced by indirection
   into the text accumulator.
   */
  void discardLast()
  {
    --lastUsed;
  }

  /**
   * Overwrite the integer found at a specific record and column.
   * Used to back-patch existing records, most often changing their
   * "next sibling" reference from 0 (unknown) to something meaningful
   * @param position int Record number
   * @param offset int Column number
   * @param value int New contents
   */
  void writeEntry(int position, int offset, int value) throws ArrayIndexOutOfBoundsException
  {
    /*
    try
    {
      fastArray[( position*slotsize)+offset] = value;
    }
    catch(ArrayIndexOutOfBoundsException aioobe)
    */
    {
      if (offset >= slotsize)
      position*=slotsize;
      int chunkpos = position >> lowbits;
      int slotpos = position & lowmask;
      int[] chunk = chunks.elementAt(chunkpos);
    }
  }

  /**
   * Overwrite an entire (4-integer) record at the specified index.
   * Mostly used to create record 0, the Document node.
   * @param position integer Record number
   * @param w0 int 
   * @param w1 int
   * @param w2 int
   * @param w3 int
   */
  void writeSlot(int position, int w0, int w1, int w2, int w3)
  {
      position *= slotsize;
      int chunkpos = position >> lowbits;
      int slotpos = (position & lowmask);

    if (chunkpos > chunks.size() - 1)
      chunks.addElement(new int[chunkalloc]);
    int[] chunk = chunks.elementAt(chunkpos);
    chunk[slotpos] = w0;
    chunk[slotpos + 1] = w1;
    chunk[slotpos + 2] = w2;
    chunk[slotpos + 3] = w3;
  }

  /**
   * Retrieve the contents of a record into a user-supplied buffer array.
   * Used to reduce addressing overhead when code will access several
   * columns of the record.
   * @param position int Record number
   * @param buffer int[] Integer array provided by user, must be large enough
   * to hold a complete record.
   */
  void readSlot(int position, int[] buffer)
  {
    /*
    try
    {
      System.arraycopy(fastArray, position*slotsize, buffer, 0, slotsize);
    }
    catch(ArrayIndexOutOfBoundsException aioobe)
    */
    {
      position *= slotsize;
      int chunkpos = position >> lowbits;
      int slotpos = (position & lowmask);

      if (chunkpos > chunks.size() - 1)
        chunks.addElement(new int[chunkalloc]);
      int[] chunk = chunks.elementAt(chunkpos);
      System.arraycopy(chunk,slotpos,buffer,0,slotsize);
    }
  }

  class ChunksVector
  {
    final int BLOCKSIZE = 64;
    int[] m_map[] = new int[BLOCKSIZE][];
    int m_mapSize = BLOCKSIZE;
    int pos = 0;
    
    ChunksVector()
    {
    }
    
    final int size()
    {
      return pos;
    }
    
    void addElement(int[] value)
    {
      if(pos >= m_mapSize)
      {
        int orgMapSize = m_mapSize;
        while(pos >= m_mapSize)
          m_mapSize+=BLOCKSIZE;
        int[] newMap[] = new int[m_mapSize][];
        System.arraycopy(m_map, 0, newMap, 0, orgMapSize);
        m_map = newMap;
      }
      m_map[pos] = value;
      pos++;
    }
    
    final int[] elementAt(int pos)
    {
      return m_map[pos];
    }
  }
}
