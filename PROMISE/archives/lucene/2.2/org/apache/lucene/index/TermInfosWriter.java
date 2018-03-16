import java.io.IOException;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.StringHelper;

/** This stores a monotonically increasing set of <Term, TermInfo> pairs in a
  Directory.  A TermInfos can be written once, in order.  */

final class TermInfosWriter {
  /** The file format version, a negative number. */
  public static final int FORMAT = -3;

  private FieldInfos fieldInfos;
  private IndexOutput output;
  private Term lastTerm = new Term("", "");
  private TermInfo lastTi = new TermInfo();
  private long size = 0;


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

  private long lastIndexPointer = 0;
  private boolean isIndex = false;

  private TermInfosWriter other = null;

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
  }

  /** Adds a new <Term, TermInfo> pair to the set.
    Term must be lexicographically greater than all previous Terms added.
    TermInfo pointers must be positive and greater than all previous.*/
  final void add(Term term, TermInfo ti)
       throws CorruptIndexException, IOException {
    if (!isIndex && term.compareTo(lastTerm) <= 0)
      throw new CorruptIndexException("term out of order (\"" + term + 
          "\".compareTo(\"" + lastTerm + "\") <= 0)");
    if (ti.freqPointer < lastTi.freqPointer)
      throw new CorruptIndexException("freqPointer out of order (" + ti.freqPointer +
          " < " + lastTi.freqPointer + ")");
    if (ti.proxPointer < lastTi.proxPointer)
      throw new CorruptIndexException("proxPointer out of order (" + ti.proxPointer + 
          " < " + lastTi.proxPointer + ")");

    if (!isIndex && size % indexInterval == 0)

    output.writeVLong(ti.proxPointer - lastTi.proxPointer);

    if (ti.docFreq >= skipInterval) {
      output.writeVInt(ti.skipOffset);
    }

    if (isIndex) {
      output.writeVLong(other.output.getFilePointer() - lastIndexPointer);
    }

    lastTi.set(ti);
    size++;
  }

  private final void writeTerm(Term term)
       throws IOException {
    int start = StringHelper.stringDifference(lastTerm.text, term.text);
    int length = term.text.length() - start;



    lastTerm = term;
  }



  /** Called to complete TermInfos creation. */
  final void close() throws IOException {
    output.writeLong(size);
    output.close();

    if (!isIndex)
      other.close();
  }

}
