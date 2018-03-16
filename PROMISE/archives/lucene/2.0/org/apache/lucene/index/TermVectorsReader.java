import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;

import java.io.IOException;

/**
 * @version $Id: TermVectorsReader.java 170226 2005-05-15 15:04:39Z bmesser $
 */
class TermVectorsReader implements Cloneable {
  private FieldInfos fieldInfos;

  private IndexInput tvx;
  private IndexInput tvd;
  private IndexInput tvf;
  private int size;
  
  private int tvdFormat;
  private int tvfFormat;

  TermVectorsReader(Directory d, String segment, FieldInfos fieldInfos)
    throws IOException {
    if (d.fileExists(segment + TermVectorsWriter.TVX_EXTENSION)) {
      tvx = d.openInput(segment + TermVectorsWriter.TVX_EXTENSION);
      checkValidFormat(tvx);
      tvd = d.openInput(segment + TermVectorsWriter.TVD_EXTENSION);
      tvdFormat = checkValidFormat(tvd);
      tvf = d.openInput(segment + TermVectorsWriter.TVF_EXTENSION);
      tvfFormat = checkValidFormat(tvf);
      size = (int) tvx.length() / 8;
    }

    this.fieldInfos = fieldInfos;
  }
  
  private int checkValidFormat(IndexInput in) throws IOException
  {
    int format = in.readInt();
    if (format > TermVectorsWriter.FORMAT_VERSION)
    {
      throw new IOException("Incompatible format version: " + format + " expected " 
              + TermVectorsWriter.FORMAT_VERSION + " or less");
    }
    return format;
  }

  void close() throws IOException {
  	IOException keep = null;
  	if (tvx != null) try { tvx.close(); } catch (IOException e) { if (keep == null) keep = e; }
  	if (tvd != null) try { tvd.close(); } catch (IOException e) { if (keep == null) keep = e; }
  	if (tvf  != null) try {  tvf.close(); } catch (IOException e) { if (keep == null) keep = e; }
  	if (keep != null) throw (IOException) keep.fillInStackTrace();
  }

  /**
   * 
   * @return The number of documents in the reader
   */
  int size() {
    return size;
  }

  /**
   * Retrieve the term vector for the given document and field
   * @param docNum The document number to retrieve the vector for
   * @param field The field within the document to retrieve
   * @return The TermFreqVector for the document and field or null if there is no termVector for this field.
   * @throws IOException if there is an error reading the term vector files
   */ 
  TermFreqVector get(int docNum, String field) throws IOException {
    int fieldNumber = fieldInfos.fieldNumber(field);
    TermFreqVector result = null;
    if (tvx != null) {
      tvx.seek((docNum * 8L) + TermVectorsWriter.FORMAT_SIZE);
      long position = tvx.readLong();

      tvd.seek(position);
      int fieldCount = tvd.readVInt();
      int number = 0;
      int found = -1;
      for (int i = 0; i < fieldCount; i++) {
        if(tvdFormat == TermVectorsWriter.FORMAT_VERSION)
          number = tvd.readVInt();
        else
          number += tvd.readVInt();
        
        if (number == fieldNumber)
          found = i;
      }

      if (found != -1) {
        position = 0;
        for (int i = 0; i <= found; i++)
          position += tvd.readVLong();

        result = readTermVector(field, position);
      } else {
      }
    } else {
    }
    return result;
  }

  /**
   * Return all term vectors stored for this document or null if the could not be read in.
   * 
   * @param docNum The document number to retrieve the vector for
   * @return All term frequency vectors
   * @throws IOException if there is an error reading the term vector files 
   */
  TermFreqVector[] get(int docNum) throws IOException {
    TermFreqVector[] result = null;
    if (tvx != null) {
      tvx.seek((docNum * 8L) + TermVectorsWriter.FORMAT_SIZE);
      long position = tvx.readLong();

      tvd.seek(position);
      int fieldCount = tvd.readVInt();

      if (fieldCount != 0) {
        int number = 0;
        String[] fields = new String[fieldCount];
        
        for (int i = 0; i < fieldCount; i++) {
          if(tvdFormat == TermVectorsWriter.FORMAT_VERSION)
            number = tvd.readVInt();
          else
            number += tvd.readVInt();

          fields[i] = fieldInfos.fieldName(number);
        }

        position = 0;
        long[] tvfPointers = new long[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
          position += tvd.readVLong();
          tvfPointers[i] = position;
        }

        result = readTermVectors(fields, tvfPointers);
      }
    } else {
    }
    return result;
  }


  private SegmentTermVector[] readTermVectors(String fields[], long tvfPointers[])
          throws IOException {
    SegmentTermVector res[] = new SegmentTermVector[fields.length];
    for (int i = 0; i < fields.length; i++) {
      res[i] = readTermVector(fields[i], tvfPointers[i]);
    }
    return res;
  }

  /**
   * 
   * @param field The field to read in
   * @param tvfPointer The pointer within the tvf file where we should start reading
   * @return The TermVector located at that position
   * @throws IOException
   */ 
  private SegmentTermVector readTermVector(String field, long tvfPointer)
          throws IOException {

    tvf.seek(tvfPointer);

    int numTerms = tvf.readVInt();
    if (numTerms == 0) 
      return new SegmentTermVector(field, null, null);
    
    boolean storePositions;
    boolean storeOffsets;
    
    if(tvfFormat == TermVectorsWriter.FORMAT_VERSION){
      byte bits = tvf.readByte();
      storePositions = (bits & TermVectorsWriter.STORE_POSITIONS_WITH_TERMVECTOR) != 0;
      storeOffsets = (bits & TermVectorsWriter.STORE_OFFSET_WITH_TERMVECTOR) != 0;
    }
    else{
      tvf.readVInt();
      storePositions = false;
      storeOffsets = false;
    }

    String terms[] = new String[numTerms];
    int termFreqs[] = new int[numTerms];
    
    int positions[][] = null;
    TermVectorOffsetInfo offsets[][] = null;
    if(storePositions)
      positions = new int[numTerms][];
    if(storeOffsets)
      offsets = new TermVectorOffsetInfo[numTerms][];
    
    int start = 0;
    int deltaLength = 0;
    int totalLength = 0;
    char[] previousBuffer = {};
    
    for (int i = 0; i < numTerms; i++) {
      start = tvf.readVInt();
      deltaLength = tvf.readVInt();
      totalLength = start + deltaLength;
        buffer = new char[totalLength];
        
          System.arraycopy(previousBuffer, 0, buffer, 0, start);
      }
      
      tvf.readChars(buffer, start, deltaLength);
      terms[i] = new String(buffer, 0, totalLength);
      previousBuffer = buffer;
      int freq = tvf.readVInt();
      termFreqs[i] = freq;
      
        int [] pos = new int[freq];
        positions[i] = pos;
        int prevPosition = 0;
        for (int j = 0; j < freq; j++)
        {
          pos[j] = prevPosition + tvf.readVInt();
          prevPosition = pos[j];
        }
      }
      
      if (storeOffsets) {
        TermVectorOffsetInfo[] offs = new TermVectorOffsetInfo[freq];
        offsets[i] = offs;
        int prevOffset = 0;
        for (int j = 0; j < freq; j++) {
          int startOffset = prevOffset + tvf.readVInt();
          int endOffset = startOffset + tvf.readVInt();
          offs[j] = new TermVectorOffsetInfo(startOffset, endOffset);
          prevOffset = endOffset;
        }
      }
    }
    
    SegmentTermVector tv;
    if (storePositions || storeOffsets){
      tv = new SegmentTermPositionVector(field, terms, termFreqs, positions, offsets);
    }
    else {
      tv = new SegmentTermVector(field, terms, termFreqs);
    }
    return tv;
  }

  protected Object clone() {
    
    if (tvx == null || tvd == null || tvf == null)
      return null;
    
    TermVectorsReader clone = null;
    try {
      clone = (TermVectorsReader) super.clone();
    } catch (CloneNotSupportedException e) {}

    clone.tvx = (IndexInput) tvx.clone();
    clone.tvd = (IndexInput) tvd.clone();
    clone.tvf = (IndexInput) tvf.clone();
    
    return clone;
  }
}
