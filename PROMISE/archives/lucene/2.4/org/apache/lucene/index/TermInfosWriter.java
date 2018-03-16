import java.io.IOException;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.UnicodeUtil;

/** This stores a monotonically increasing set of <Term, TermInfo> pairs in a
  Directory.  A TermInfos can be written once, in order.  */

final class TermInfosWriter {
  /** The file format version, a negative number. */
  public static final int FORMAT = -3;

  public static final int FORMAT_VERSION_UTF8_LENGTH_IN_BYTES = -4;

  public static final int FORMAT_CURRENT = FORMAT_VERSION_UTF8_LENGTH_IN_BYTES;

  private FieldInfos fieldInfos;
  private IndexOutput output;
  private TermInfo lastTi = new TermInfo();
  private long size;


  /** Expert: The fraction of terms in the "dictionary" which should be stored
   * in RAM.  Smaller values use more memory, but make searching slightly
   * faster, while larger values use less memory and make searching slightly
   * slower.  Searching is typically not dominated by dictionary lookup, so
   * tweaking this is rarely useful.*/
  int indexInterval = 128;

  /** Expert: The fraction of {@link TermDocs} entries stored in skip tables,
   * used to accellerate {@link TermDocs#skipTo(int)}.  Larger values result in
   * smaller indexes, greater acceleration, but fewer accelerable cases, while
   * smaller values result in bigger indexes, less acceleration and more
   * accelerable cases. More detailed experiments would be useful here. */
  int skipInterval = 16;
  
  /** Expert: The maximum number of skip levels. Smaller values result in 
   * slightly smaller indexes, but slower skipping in big posting lists.
   */
  int maxSkipLevels = 10;

  private long lastIndexPointer;
  private boolean isIndex;
  private byte[] lastTermBytes = new byte[10];
  private int lastTermBytesLength = 0;
  private int lastFieldNumber = -1;

  private TermInfosWriter other;
  private UnicodeUtil.UTF8Result utf8Result = new UnicodeUtil.UTF8Result();

  TermInfosWriter(Directory directory, String segment, FieldInfos fis,
                  int interval)
       throws IOException {
    initialize(directory, segment, fis, interval, false);
    other = new TermInfosWriter(directory, segment, fis, interval, true);
    other.other = this;
  }

  private TermInfosWriter(Directory directory, String segment, FieldInfos fis,
                          int interval, boolean isIndex) throws IOException {
    initialize(directory, segment, fis, interval, isIndex);
  }

  private void initialize(Directory directory, String segment, FieldInfos fis,
                          int interval, boolean isi) throws IOException {
    indexInterval = interval;
    fieldInfos = fis;
    isIndex = isi;
    output = directory.createOutput(segment + (isIndex ? ".tii" : ".tis"));
    assert initUTF16Results();
  }

  void add(Term term, TermInfo ti) throws IOException {
    UnicodeUtil.UTF16toUTF8(term.text, 0, term.text.length(), utf8Result);
    add(fieldInfos.fieldNumber(term.field), utf8Result.result, utf8Result.length, ti);
  }

  UnicodeUtil.UTF16Result utf16Result1;
  UnicodeUtil.UTF16Result utf16Result2;

  private boolean initUTF16Results() {
    utf16Result1 = new UnicodeUtil.UTF16Result();
    utf16Result2 = new UnicodeUtil.UTF16Result();
    return true;
  }

  private int compareToLastTerm(int fieldNumber, byte[] termBytes, int termBytesLength) {

    if (lastFieldNumber != fieldNumber) {
      final int cmp = fieldInfos.fieldName(lastFieldNumber).compareTo(fieldInfos.fieldName(fieldNumber));
      if (cmp != 0 || lastFieldNumber != -1)
        return cmp;
    }

    UnicodeUtil.UTF8toUTF16(lastTermBytes, 0, lastTermBytesLength, utf16Result1);
    UnicodeUtil.UTF8toUTF16(termBytes, 0, termBytesLength, utf16Result2);
    final int len;
    if (utf16Result1.length < utf16Result2.length)
      len = utf16Result1.length;
    else
      len = utf16Result2.length;

    for(int i=0;i<len;i++) {
      final char ch1 = utf16Result1.result[i];
      final char ch2 = utf16Result2.result[i];
      if (ch1 != ch2)
        return ch1-ch2;
    }
    return utf16Result1.length - utf16Result2.length;
  }

  /** Adds a new <<fieldNumber, termBytes>, TermInfo> pair to the set.
    Term must be lexicographically greater than all previous Terms added.
    TermInfo pointers must be positive and greater than all previous.*/
  void add(int fieldNumber, byte[] termBytes, int termBytesLength, TermInfo ti)
    throws IOException {

    assert compareToLastTerm(fieldNumber, termBytes, termBytesLength) < 0 ||
      (isIndex && termBytesLength == 0 && lastTermBytesLength == 0) :
      "Terms are out of order: field=" + fieldInfos.fieldName(fieldNumber) + " (number " + fieldNumber + ")" +
        " lastField=" + fieldInfos.fieldName(lastFieldNumber) + " (number " + lastFieldNumber + ")" +
        " text=" + new String(termBytes, 0, termBytesLength, "UTF-8") + " lastText=" + new String(lastTermBytes, 0, lastTermBytesLength, "UTF-8");

    assert ti.freqPointer >= lastTi.freqPointer: "freqPointer out of order (" + ti.freqPointer + " < " + lastTi.freqPointer + ")";
    assert ti.proxPointer >= lastTi.proxPointer: "proxPointer out of order (" + ti.proxPointer + " < " + lastTi.proxPointer + ")";

    if (!isIndex && size % indexInterval == 0)


    output.writeVLong(ti.proxPointer - lastTi.proxPointer);

    if (ti.docFreq >= skipInterval) {
      output.writeVInt(ti.skipOffset);
    }

    if (isIndex) {
      output.writeVLong(other.output.getFilePointer() - lastIndexPointer);
    }

    lastFieldNumber = fieldNumber;
    lastTi.set(ti);
    size++;
  }

  private void writeTerm(int fieldNumber, byte[] termBytes, int termBytesLength)
       throws IOException {

    int start = 0;
    final int limit = termBytesLength < lastTermBytesLength ? termBytesLength : lastTermBytesLength;
    while(start < limit) {
      if (termBytes[start] != lastTermBytes[start])
        break;
      start++;
    }

    final int length = termBytesLength - start;
    if (lastTermBytes.length < termBytesLength) {
      byte[] newArray = new byte[(int) (termBytesLength*1.5)];
      System.arraycopy(lastTermBytes, 0, newArray, 0, start);
      lastTermBytes = newArray;
    }
    System.arraycopy(termBytes, start, lastTermBytes, start, length);
    lastTermBytesLength = termBytesLength;
  }

  /** Called to complete TermInfos creation. */
  void close() throws IOException {
    output.writeLong(size);
    output.close();

    if (!isIndex)
      other.close();
  }

}
