import java.io.IOException;
import org.apache.lucene.store.IndexInput;

final class SegmentTermEnum extends TermEnum implements Cloneable {
  private IndexInput input;
  FieldInfos fieldInfos;
  long size;
  long position = -1;

  private TermBuffer termBuffer = new TermBuffer();
  private TermBuffer prevBuffer = new TermBuffer();

  private TermInfo termInfo = new TermInfo();

  private int format;
  private boolean isIndex = false;
  long indexPointer = 0;
  int indexInterval;
  int skipInterval;
  int maxSkipLevels;
  private int formatM1SkipInterval;

  SegmentTermEnum(IndexInput i, FieldInfos fis, boolean isi)
          throws CorruptIndexException, IOException {
    input = i;
    fieldInfos = fis;
    isIndex = isi;
    
    int firstInt = input.readInt();
    if (firstInt >= 0) {
      format = 0;
      size = firstInt;

      indexInterval = 128;
    } else {
      format = firstInt;

      if (format < TermInfosWriter.FORMAT_CURRENT)
        throw new CorruptIndexException("Unknown format version:" + format + " expected " + TermInfosWriter.FORMAT_CURRENT + " or higher");

      
      if(format == -1){
        if (!isIndex) {
          indexInterval = input.readInt();
          formatM1SkipInterval = input.readInt();
        }
        skipInterval = Integer.MAX_VALUE;
      } else {
        indexInterval = input.readInt();
        skipInterval = input.readInt();
        if (format <= TermInfosWriter.FORMAT) {
          maxSkipLevels = input.readInt();
        }
      }
    }
    if (format > TermInfosWriter.FORMAT_VERSION_UTF8_LENGTH_IN_BYTES) {
      termBuffer.setPreUTF8Strings();
      scanBuffer.setPreUTF8Strings();
      prevBuffer.setPreUTF8Strings();
    }
  }

  protected Object clone() {
    SegmentTermEnum clone = null;
    try {
      clone = (SegmentTermEnum) super.clone();
    } catch (CloneNotSupportedException e) {}

    clone.input = (IndexInput) input.clone();
    clone.termInfo = new TermInfo(termInfo);

    clone.termBuffer = (TermBuffer)termBuffer.clone();
    clone.prevBuffer = (TermBuffer)prevBuffer.clone();
    clone.scanBuffer = new TermBuffer();

    return clone;
  }

  final void seek(long pointer, int p, Term t, TermInfo ti)
          throws IOException {
    input.seek(pointer);
    position = p;
    termBuffer.set(t);
    prevBuffer.reset();
    termInfo.set(ti);
  }

  /** Increments the enumeration to the next element.  True if one exists.*/
  public final boolean next() throws IOException {
    if (position++ >= size - 1) {
      prevBuffer.set(termBuffer);
      termBuffer.reset();
      return false;
    }

    prevBuffer.set(termBuffer);
    termBuffer.read(input, fieldInfos);

    
    if(format == -1){
      if (!isIndex) {
        if (termInfo.docFreq > formatM1SkipInterval) {
          termInfo.skipOffset = input.readVInt(); 
        }
      }
    }
    else{
      if (termInfo.docFreq >= skipInterval) 
        termInfo.skipOffset = input.readVInt();
    }
    
    if (isIndex)

    return true;
  }

  /** Optimized scan, without allocating new terms. 
   *  Return number of invocations to next(). */
  final int scanTo(Term term) throws IOException {
    scanBuffer.set(term);
    int count = 0;
    while (scanBuffer.compareTo(termBuffer) > 0 && next()) {
      count++;
    }
    return count;
  }

  /** Returns the current Term in the enumeration.
   Initially invalid, valid after next() called for the first time.*/
  public final Term term() {
    return termBuffer.toTerm();
  }

  /** Returns the previous Term enumerated. Initially null.*/
  final Term prev() {
    return prevBuffer.toTerm();
  }

  /** Returns the current TermInfo in the enumeration.
   Initially invalid, valid after next() called for the first time.*/
  final TermInfo termInfo() {
    return new TermInfo(termInfo);
  }

  /** Sets the argument to the current TermInfo in the enumeration.
   Initially invalid, valid after next() called for the first time.*/
  final void termInfo(TermInfo ti) {
    ti.set(termInfo);
  }

  /** Returns the docFreq from the current TermInfo in the enumeration.
   Initially invalid, valid after next() called for the first time.*/
  public final int docFreq() {
    return termInfo.docFreq;
  }

  /* Returns the freqPointer from the current TermInfo in the enumeration.
    Initially invalid, valid after next() called for the first time.*/
  final long freqPointer() {
    return termInfo.freqPointer;
  }

  /* Returns the proxPointer from the current TermInfo in the enumeration.
    Initially invalid, valid after next() called for the first time.*/
  final long proxPointer() {
    return termInfo.proxPointer;
  }

  /** Closes the enumeration to further activity, freeing resources. */
  public final void close() throws IOException {
    input.close();
  }
}
