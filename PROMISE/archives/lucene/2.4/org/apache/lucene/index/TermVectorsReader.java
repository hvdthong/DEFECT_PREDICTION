import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;

import java.io.IOException;
import java.util.Arrays;

/**
 * @version $Id: TermVectorsReader.java 687046 2008-08-19 13:01:11Z mikemccand $
 */
class TermVectorsReader implements Cloneable {

  static final int FORMAT_VERSION = 2;

  static final int FORMAT_VERSION2 = 3;

  static final int FORMAT_UTF8_LENGTH_IN_BYTES = 4;

  static final int FORMAT_CURRENT = FORMAT_UTF8_LENGTH_IN_BYTES;

  static final int FORMAT_SIZE = 4;

  static final byte STORE_POSITIONS_WITH_TERMVECTOR = 0x1;
  static final byte STORE_OFFSET_WITH_TERMVECTOR = 0x2;
  
  private FieldInfos fieldInfos;

  private IndexInput tvx;
  private IndexInput tvd;
  private IndexInput tvf;
  private int size;
  private int numTotalDocs;

  private int docStoreOffset;
  
  private final int format;

  TermVectorsReader(Directory d, String segment, FieldInfos fieldInfos)
    throws CorruptIndexException, IOException {
    this(d, segment, fieldInfos, BufferedIndexInput.BUFFER_SIZE);
  }

  TermVectorsReader(Directory d, String segment, FieldInfos fieldInfos, int readBufferSize)
    throws CorruptIndexException, IOException {
    this(d, segment, fieldInfos, readBufferSize, -1, 0);
  }
    
  TermVectorsReader(Directory d, String segment, FieldInfos fieldInfos, int readBufferSize, int docStoreOffset, int size)
    throws CorruptIndexException, IOException {
    boolean success = false;

    try {
      if (d.fileExists(segment + "." + IndexFileNames.VECTORS_INDEX_EXTENSION)) {
        tvx = d.openInput(segment + "." + IndexFileNames.VECTORS_INDEX_EXTENSION, readBufferSize);
        format = checkValidFormat(tvx);
        tvd = d.openInput(segment + "." + IndexFileNames.VECTORS_DOCUMENTS_EXTENSION, readBufferSize);
        final int tvdFormat = checkValidFormat(tvd);
        tvf = d.openInput(segment + "." + IndexFileNames.VECTORS_FIELDS_EXTENSION, readBufferSize);
        final int tvfFormat = checkValidFormat(tvf);

        assert format == tvdFormat;
        assert format == tvfFormat;

        if (format >= FORMAT_VERSION2) {
          assert (tvx.length()-FORMAT_SIZE) % 16 == 0;
          numTotalDocs = (int) (tvx.length() >> 4);
        } else {
          assert (tvx.length()-FORMAT_SIZE) % 8 == 0;
          numTotalDocs = (int) (tvx.length() >> 3);
        }

        if (-1 == docStoreOffset) {
          this.docStoreOffset = 0;
          this.size = numTotalDocs;
          assert size == 0 || numTotalDocs == size;
        } else {
          this.docStoreOffset = docStoreOffset;
          this.size = size;
          assert numTotalDocs >= size + docStoreOffset: "numTotalDocs=" + numTotalDocs + " size=" + size + " docStoreOffset=" + docStoreOffset;
        }
      } else
        format = 0;

      this.fieldInfos = fieldInfos;
      success = true;
    } finally {
      if (!success) {
        close();
      }
    }
  }

  IndexInput getTvdStream() {
    return tvd;
  }

  IndexInput getTvfStream() {
    return tvf;
  }

  final private void seekTvx(final int docNum) throws IOException {
    if (format < FORMAT_VERSION2)
      tvx.seek((docNum + docStoreOffset) * 8L + FORMAT_SIZE);
    else
      tvx.seek((docNum + docStoreOffset) * 16L + FORMAT_SIZE);
  }

  boolean canReadRawDocs() {
    return format >= FORMAT_UTF8_LENGTH_IN_BYTES;
  }

  /** Retrieve the length (in bytes) of the tvd and tvf
   *  entries for the next numDocs starting with
   *  startDocID.  This is used for bulk copying when
   *  merging segments, if the field numbers are
   *  congruent.  Once this returns, the tvf & tvd streams
   *  are seeked to the startDocID. */
  final void rawDocs(int[] tvdLengths, int[] tvfLengths, int startDocID, int numDocs) throws IOException {

    if (tvx == null) {
      Arrays.fill(tvdLengths, 0);
      Arrays.fill(tvfLengths, 0);
      return;
    }

    if (format < FORMAT_VERSION2)
      throw new IllegalStateException("cannot read raw docs with older term vector formats");

    seekTvx(startDocID);

    long tvdPosition = tvx.readLong();
    tvd.seek(tvdPosition);

    long tvfPosition = tvx.readLong();
    tvf.seek(tvfPosition);

    long lastTvdPosition = tvdPosition;
    long lastTvfPosition = tvfPosition;

    int count = 0;
    while (count < numDocs) {
      final int docID = docStoreOffset + startDocID + count + 1;
      assert docID <= numTotalDocs;
      if (docID < numTotalDocs)  {
        tvdPosition = tvx.readLong();
        tvfPosition = tvx.readLong();
      } else {
        tvdPosition = tvd.length();
        tvfPosition = tvf.length();
        assert count == numDocs-1;
      }
      tvdLengths[count] = (int) (tvdPosition-lastTvdPosition);
      tvfLengths[count] = (int) (tvfPosition-lastTvfPosition);
      count++;
      lastTvdPosition = tvdPosition;
      lastTvfPosition = tvfPosition;
    }
  }

  private int checkValidFormat(IndexInput in) throws CorruptIndexException, IOException
  {
    int format = in.readInt();
    if (format > FORMAT_CURRENT) {
      throw new CorruptIndexException("Incompatible format version: " + format + " expected " 
                                      + FORMAT_CURRENT + " or less");
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

  public void get(int docNum, String field, TermVectorMapper mapper) throws IOException {
    if (tvx != null) {
      int fieldNumber = fieldInfos.fieldNumber(field);
      seekTvx(docNum);
      long tvdPosition = tvx.readLong();

      tvd.seek(tvdPosition);
      int fieldCount = tvd.readVInt();
      int number = 0;
      int found = -1;
      for (int i = 0; i < fieldCount; i++) {
        if (format >= FORMAT_VERSION)
          number = tvd.readVInt();
        else
          number += tvd.readVInt();

        if (number == fieldNumber)
          found = i;
      }

      if (found != -1) {
        long position;
        if (format >= FORMAT_VERSION2)
          position = tvx.readLong();
        else
          position = tvd.readVLong();
        for (int i = 1; i <= found; i++)
          position += tvd.readVLong();

        mapper.setDocumentNumber(docNum);
        readTermVector(field, position, mapper);
      } else {
      }
    } else {
    }
  }



  /**
   * Retrieve the term vector for the given document and field
   * @param docNum The document number to retrieve the vector for
   * @param field The field within the document to retrieve
   * @return The TermFreqVector for the document and field or null if there is no termVector for this field.
   * @throws IOException if there is an error reading the term vector files
   */ 
  TermFreqVector get(int docNum, String field) throws IOException {
    ParallelArrayTermVectorMapper mapper = new ParallelArrayTermVectorMapper();
    get(docNum, field, mapper);

    return mapper.materializeVector();
  }

  final private String[] readFields(int fieldCount) throws IOException {
    int number = 0;
    String[] fields = new String[fieldCount];

    for (int i = 0; i < fieldCount; i++) {
      if (format >= FORMAT_VERSION)
        number = tvd.readVInt();
      else
        number += tvd.readVInt();

      fields[i] = fieldInfos.fieldName(number);
    }

    return fields;
  }

  final private long[] readTvfPointers(int fieldCount) throws IOException {
    long position;
    if (format >= FORMAT_VERSION2)
      position = tvx.readLong();
    else
      position = tvd.readVLong();

    long[] tvfPointers = new long[fieldCount];
    tvfPointers[0] = position;

    for (int i = 1; i < fieldCount; i++) {
      position += tvd.readVLong();
      tvfPointers[i] = position;
    }

    return tvfPointers;
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
      seekTvx(docNum);
      long tvdPosition = tvx.readLong();

      tvd.seek(tvdPosition);
      int fieldCount = tvd.readVInt();

      if (fieldCount != 0) {
        final String[] fields = readFields(fieldCount);
        final long[] tvfPointers = readTvfPointers(fieldCount);
        result = readTermVectors(docNum, fields, tvfPointers);
      }
    } else {
    }
    return result;
  }

  public void get(int docNumber, TermVectorMapper mapper) throws IOException {
    if (tvx != null) {

      seekTvx(docNumber);
      long tvdPosition = tvx.readLong();

      tvd.seek(tvdPosition);
      int fieldCount = tvd.readVInt();

      if (fieldCount != 0) {
        final String[] fields = readFields(fieldCount);
        final long[] tvfPointers = readTvfPointers(fieldCount);
        mapper.setDocumentNumber(docNumber);
        readTermVectors(fields, tvfPointers, mapper);
      }
    } else {
    }
  }


  private SegmentTermVector[] readTermVectors(int docNum, String fields[], long tvfPointers[])
          throws IOException {
    SegmentTermVector res[] = new SegmentTermVector[fields.length];
    for (int i = 0; i < fields.length; i++) {
      ParallelArrayTermVectorMapper mapper = new ParallelArrayTermVectorMapper();
      mapper.setDocumentNumber(docNum);
      readTermVector(fields[i], tvfPointers[i], mapper);
      res[i] = (SegmentTermVector) mapper.materializeVector();
    }
    return res;
  }

  private void readTermVectors(String fields[], long tvfPointers[], TermVectorMapper mapper)
          throws IOException {
    for (int i = 0; i < fields.length; i++) {
      readTermVector(fields[i], tvfPointers[i], mapper);
    }
  }


  /**
   * 
   * @param field The field to read in
   * @param tvfPointer The pointer within the tvf file where we should start reading
   * @param mapper The mapper used to map the TermVector
   * @throws IOException
   */ 
  private void readTermVector(String field, long tvfPointer, TermVectorMapper mapper)
          throws IOException {

    tvf.seek(tvfPointer);

    int numTerms = tvf.readVInt();
    if (numTerms == 0) 
      return;
    
    boolean storePositions;
    boolean storeOffsets;
    
    if (format >= FORMAT_VERSION){
      byte bits = tvf.readByte();
      storePositions = (bits & STORE_POSITIONS_WITH_TERMVECTOR) != 0;
      storeOffsets = (bits & STORE_OFFSET_WITH_TERMVECTOR) != 0;
    }
    else{
      tvf.readVInt();
      storePositions = false;
      storeOffsets = false;
    }
    mapper.setExpectations(field, numTerms, storeOffsets, storePositions);
    int start = 0;
    int deltaLength = 0;
    int totalLength = 0;
    byte[] byteBuffer;
    char[] charBuffer;
    final boolean preUTF8 = format < FORMAT_UTF8_LENGTH_IN_BYTES;

    if (preUTF8) {
      charBuffer = new char[10];
      byteBuffer = null;
    } else {
      charBuffer = null;
      byteBuffer = new byte[20];
    }

    for (int i = 0; i < numTerms; i++) {
      start = tvf.readVInt();
      deltaLength = tvf.readVInt();
      totalLength = start + deltaLength;

      final String term;
      
      if (preUTF8) {
        if (charBuffer.length < totalLength) {
          char[] newCharBuffer = new char[(int) (1.5*totalLength)];
          System.arraycopy(charBuffer, 0, newCharBuffer, 0, start);
          charBuffer = newCharBuffer;
        }
        tvf.readChars(charBuffer, start, deltaLength);
        term = new String(charBuffer, 0, totalLength);
      } else {
        if (byteBuffer.length < totalLength) {
          byte[] newByteBuffer = new byte[(int) (1.5*totalLength)];
          System.arraycopy(byteBuffer, 0, newByteBuffer, 0, start);
          byteBuffer = newByteBuffer;
        }
        tvf.readBytes(byteBuffer, start, deltaLength);
        term = new String(byteBuffer, 0, totalLength, "UTF-8");
      }
      int freq = tvf.readVInt();
      int [] positions = null;
        if (mapper.isIgnoringPositions() == false) {
          positions = new int[freq];
          int prevPosition = 0;
          for (int j = 0; j < freq; j++)
          {
            positions[j] = prevPosition + tvf.readVInt();
            prevPosition = positions[j];
          }
        } else {
          for (int j = 0; j < freq; j++)
          {
            tvf.readVInt();
          }
        }
      }
      TermVectorOffsetInfo[] offsets = null;
      if (storeOffsets) {
        if (mapper.isIgnoringOffsets() == false) {
          offsets = new TermVectorOffsetInfo[freq];
          int prevOffset = 0;
          for (int j = 0; j < freq; j++) {
            int startOffset = prevOffset + tvf.readVInt();
            int endOffset = startOffset + tvf.readVInt();
            offsets[j] = new TermVectorOffsetInfo(startOffset, endOffset);
            prevOffset = endOffset;
          }
        } else {
          for (int j = 0; j < freq; j++){
            tvf.readVInt();
            tvf.readVInt();
          }
        }
      }
      mapper.map(term, freq, offsets, positions);
    }
  }

  protected Object clone() throws CloneNotSupportedException {
    
    final TermVectorsReader clone = (TermVectorsReader) super.clone();

    if (tvx != null && tvd != null && tvf != null) {
      clone.tvx = (IndexInput) tvx.clone();
      clone.tvd = (IndexInput) tvd.clone();
      clone.tvf = (IndexInput) tvf.clone();
    }
    
    return clone;
  }
}


/**
 * Models the existing parallel array structure
 */
class ParallelArrayTermVectorMapper extends TermVectorMapper
{

  private String[] terms;
  private int[] termFreqs;
  private int positions[][];
  private TermVectorOffsetInfo offsets[][];
  private int currentPosition;
  private boolean storingOffsets;
  private boolean storingPositions;
  private String field;

  public void setExpectations(String field, int numTerms, boolean storeOffsets, boolean storePositions) {
    this.field = field;
    terms = new String[numTerms];
    termFreqs = new int[numTerms];
    this.storingOffsets = storeOffsets;
    this.storingPositions = storePositions;
    if(storePositions)
      this.positions = new int[numTerms][];
    if(storeOffsets)
      this.offsets = new TermVectorOffsetInfo[numTerms][];
  }

  public void map(String term, int frequency, TermVectorOffsetInfo[] offsets, int[] positions) {
    terms[currentPosition] = term;
    termFreqs[currentPosition] = frequency;
    if (storingOffsets)
    {
      this.offsets[currentPosition] = offsets;
    }
    if (storingPositions)
    {
      this.positions[currentPosition] = positions; 
    }
    currentPosition++;
  }

  /**
   * Construct the vector
   * @return The {@link TermFreqVector} based on the mappings.
   */
  public TermFreqVector materializeVector() {
    SegmentTermVector tv = null;
    if (field != null && terms != null) {
      if (storingPositions || storingOffsets) {
        tv = new SegmentTermPositionVector(field, terms, termFreqs, positions, offsets);
      } else {
        tv = new SegmentTermVector(field, terms, termFreqs);
      }
    }
    return tv;
  }
}
