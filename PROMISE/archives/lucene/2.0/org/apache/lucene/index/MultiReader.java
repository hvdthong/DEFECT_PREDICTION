import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.*;

/** An IndexReader which reads multiple indexes, appending their content.
 *
 * @version $Id: MultiReader.java 387550 2006-03-21 15:36:32Z yonik $
 */
public class MultiReader extends IndexReader {
  private IndexReader[] subReaders;
  private Hashtable normsCache = new Hashtable();
  private int maxDoc = 0;
  private int numDocs = -1;
  private boolean hasDeletions = false;

 /**
  * <p>Construct a MultiReader aggregating the named set of (sub)readers.
  * Directory locking for delete, undeleteAll, and setNorm operations is
  * left to the subreaders. </p>
  * <p>Note that all subreaders are closed if this Multireader is closed.</p>
  * @param subReaders set of (sub)readers
  * @throws IOException
  */
  public MultiReader(IndexReader[] subReaders) throws IOException {
    super(subReaders.length == 0 ? null : subReaders[0].directory());
    initialize(subReaders);
  }

  /** Construct reading the named set of readers. */
  MultiReader(Directory directory, SegmentInfos sis, boolean closeDirectory, IndexReader[] subReaders) {
    super(directory, sis, closeDirectory);
    initialize(subReaders);
  }

  private void initialize(IndexReader[] subReaders) {
    this.subReaders = subReaders;
    for (int i = 0; i < subReaders.length; i++) {
      starts[i] = maxDoc;

      if (subReaders[i].hasDeletions())
        hasDeletions = true;
    }
    starts[subReaders.length] = maxDoc;
  }


  /** Return an array of term frequency vectors for the specified document.
   *  The array contains a vector for each vectorized field in the document.
   *  Each vector vector contains term numbers and frequencies for all terms
   *  in a given vectorized field.
   *  If no such fields existed, the method returns null.
   */
  public TermFreqVector[] getTermFreqVectors(int n) throws IOException {
  }

  public TermFreqVector getTermFreqVector(int n, String field)
      throws IOException {
    return subReaders[i].getTermFreqVector(n - starts[i], field);
  }

  public synchronized int numDocs() {
      for (int i = 0; i < subReaders.length; i++)
      numDocs = n;
    }
    return numDocs;
  }

  public int maxDoc() {
    return maxDoc;
  }

  public Document document(int n) throws IOException {
  }

  public boolean isDeleted(int n) {
  }

  public boolean hasDeletions() { return hasDeletions; }

  protected void doDelete(int n) throws IOException {
    hasDeletions = true;
  }

  protected void doUndeleteAll() throws IOException {
    for (int i = 0; i < subReaders.length; i++)
      subReaders[i].undeleteAll();
    hasDeletions = false;
  }


    while (hi >= lo) {
      int mid = (lo + hi) >> 1;
      int midValue = starts[mid];
      if (n < midValue)
        hi = mid - 1;
      else if (n > midValue)
        lo = mid + 1;
        while (mid+1 < subReaders.length && starts[mid+1] == midValue) {
        }
        return mid;
      }
    }
    return hi;
  }

  public boolean hasNorms(String field) throws IOException {
    for (int i = 0; i < subReaders.length; i++) {
      if (subReaders[i].hasNorms(field)) return true;
    }
    return false;
  }

  private byte[] ones;
  private byte[] fakeNorms() {
    if (ones==null) ones=SegmentReader.createFakeNorms(maxDoc());
    return ones;
  }

  public synchronized byte[] norms(String field) throws IOException {
    byte[] bytes = (byte[])normsCache.get(field);
    if (bytes != null)
    if (!hasNorms(field))
      return fakeNorms();

    bytes = new byte[maxDoc()];
    for (int i = 0; i < subReaders.length; i++)
      subReaders[i].norms(field, bytes, starts[i]);
    return bytes;
  }

  public synchronized void norms(String field, byte[] result, int offset)
    throws IOException {
    byte[] bytes = (byte[])normsCache.get(field);
    if (bytes==null && !hasNorms(field)) bytes=fakeNorms();
      System.arraycopy(bytes, 0, result, offset, maxDoc());

      subReaders[i].norms(field, result, offset + starts[i]);
  }

  protected void doSetNorm(int n, String field, byte value)
    throws IOException {
  }

  public TermEnum terms() throws IOException {
    return new MultiTermEnum(subReaders, starts, null);
  }

  public TermEnum terms(Term term) throws IOException {
    return new MultiTermEnum(subReaders, starts, term);
  }

  public int docFreq(Term t) throws IOException {
    for (int i = 0; i < subReaders.length; i++)
      total += subReaders[i].docFreq(t);
    return total;
  }

  public TermDocs termDocs() throws IOException {
    return new MultiTermDocs(subReaders, starts);
  }

  public TermPositions termPositions() throws IOException {
    return new MultiTermPositions(subReaders, starts);
  }

  protected void doCommit() throws IOException {
    for (int i = 0; i < subReaders.length; i++)
      subReaders[i].commit();
  }

  protected synchronized void doClose() throws IOException {
    for (int i = 0; i < subReaders.length; i++)
      subReaders[i].close();
  }

  /**
   * @see IndexReader#getFieldNames(IndexReader.FieldOption)
   */
  public Collection getFieldNames (IndexReader.FieldOption fieldNames) {
    Set fieldSet = new HashSet();
    for (int i = 0; i < subReaders.length; i++) {
      IndexReader reader = subReaders[i];
      Collection names = reader.getFieldNames(fieldNames);
      fieldSet.addAll(names);
    }
    return fieldSet;
  }
}

class MultiTermEnum extends TermEnum {
  private SegmentMergeQueue queue;

  private Term term;
  private int docFreq;

  public MultiTermEnum(IndexReader[] readers, int[] starts, Term t)
    throws IOException {
    queue = new SegmentMergeQueue(readers.length);
    for (int i = 0; i < readers.length; i++) {
      IndexReader reader = readers[i];
      TermEnum termEnum;

      if (t != null) {
        termEnum = reader.terms(t);
      } else
        termEnum = reader.terms();

      SegmentMergeInfo smi = new SegmentMergeInfo(starts[i], termEnum, reader);
      if (t == null ? smi.next() : termEnum.term() != null)
      else
        smi.close();
    }

    if (t != null && queue.size() > 0) {
      next();
    }
  }

  public boolean next() throws IOException {
    SegmentMergeInfo top = (SegmentMergeInfo)queue.top();
    if (top == null) {
      term = null;
      return false;
    }

    term = top.term;
    docFreq = 0;

    while (top != null && term.compareTo(top.term) == 0) {
      queue.pop();
      if (top.next())
      else
      top = (SegmentMergeInfo)queue.top();
    }
    return true;
  }

  public Term term() {
    return term;
  }

  public int docFreq() {
    return docFreq;
  }

  public void close() throws IOException {
    queue.close();
  }
}

class MultiTermDocs implements TermDocs {
  protected IndexReader[] readers;
  protected int[] starts;
  protected Term term;

  protected int base = 0;
  protected int pointer = 0;

  private TermDocs[] readerTermDocs;

  public MultiTermDocs(IndexReader[] r, int[] s) {
    readers = r;
    starts = s;

    readerTermDocs = new TermDocs[r.length];
  }

  public int doc() {
    return base + current.doc();
  }
  public int freq() {
    return current.freq();
  }

  public void seek(Term term) {
    this.term = term;
    this.base = 0;
    this.pointer = 0;
    this.current = null;
  }

  public void seek(TermEnum termEnum) throws IOException {
    seek(termEnum.term());
  }

  public boolean next() throws IOException {
    if (current != null && current.next()) {
      return true;
    } else if (pointer < readers.length) {
      base = starts[pointer];
      current = termDocs(pointer++);
      return next();
    } else
      return false;
  }

  /** Optimized implementation. */
  public int read(final int[] docs, final int[] freqs) throws IOException {
    while (true) {
      while (current == null) {
          base = starts[pointer];
          current = termDocs(pointer++);
        } else {
          return 0;
        }
      }
      int end = current.read(docs, freqs);
        current = null;
        for (int i = 0; i < end; i++)
         docs[i] += b;
        return end;
      }
    }
  }

  /** As yet unoptimized implementation. */
  public boolean skipTo(int target) throws IOException {
    do {
      if (!next())
        return false;
    } while (target > doc());
      return true;
  }

  private TermDocs termDocs(int i) throws IOException {
    if (term == null)
      return null;
    TermDocs result = readerTermDocs[i];
    if (result == null)
      result = readerTermDocs[i] = termDocs(readers[i]);
    result.seek(term);
    return result;
  }

  protected TermDocs termDocs(IndexReader reader)
    throws IOException {
    return reader.termDocs();
  }

  public void close() throws IOException {
    for (int i = 0; i < readerTermDocs.length; i++) {
      if (readerTermDocs[i] != null)
        readerTermDocs[i].close();
    }
  }
}

class MultiTermPositions extends MultiTermDocs implements TermPositions {
  public MultiTermPositions(IndexReader[] r, int[] s) {
    super(r,s);
  }

  protected TermDocs termDocs(IndexReader reader) throws IOException {
    return (TermDocs)reader.termPositions();
  }

  public int nextPosition() throws IOException {
    return ((TermPositions)current).nextPosition();
  }

}
