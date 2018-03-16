import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.MultiSegmentReader.MultiTermDocs;
import org.apache.lucene.index.MultiSegmentReader.MultiTermEnum;
import org.apache.lucene.index.MultiSegmentReader.MultiTermPositions;

/** An IndexReader which reads multiple indexes, appending their content.
 *
 * @version $Id: MultiReader.java 692921 2008-09-07 19:22:40Z mikemccand $
 */
public class MultiReader extends IndexReader {
  protected IndexReader[] subReaders;
  private Map normsCache = new HashMap();
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
  public MultiReader(IndexReader[] subReaders) {
    initialize(subReaders, true);
  }

  /**
   * <p>Construct a MultiReader aggregating the named set of (sub)readers.
   * Directory locking for delete, undeleteAll, and setNorm operations is
   * left to the subreaders. </p>
   * @param closeSubReaders indicates whether the subreaders should be closed
   * when this MultiReader is closed
   * @param subReaders set of (sub)readers
   * @throws IOException
   */
  public MultiReader(IndexReader[] subReaders, boolean closeSubReaders) {
    initialize(subReaders, closeSubReaders);
  }
  
  private void initialize(IndexReader[] subReaders, boolean closeSubReaders) {
    this.subReaders = (IndexReader[]) subReaders.clone();
    decrefOnClose = new boolean[subReaders.length];
    for (int i = 0; i < subReaders.length; i++) {
      starts[i] = maxDoc;

      if (!closeSubReaders) {
        subReaders[i].incRef();
        decrefOnClose[i] = true;
      } else {
        decrefOnClose[i] = false;
      }
      
      if (subReaders[i].hasDeletions())
        hasDeletions = true;
    }
    starts[subReaders.length] = maxDoc;
  }

  /**
   * Tries to reopen the subreaders.
   * <br>
   * If one or more subreaders could be re-opened (i. e. subReader.reopen() 
   * returned a new instance != subReader), then a new MultiReader instance 
   * is returned, otherwise this instance is returned.
   * <p>
   * A re-opened instance might share one or more subreaders with the old 
   * instance. Index modification operations result in undefined behavior
   * when performed before the old instance is closed.
   * (see {@link IndexReader#reopen()}).
   * <p>
   * If subreaders are shared, then the reference count of those
   * readers is increased to ensure that the subreaders remain open
   * until the last referring reader is closed.
   * 
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error 
   */
  public IndexReader reopen() throws CorruptIndexException, IOException {
    ensureOpen();
    
    boolean reopened = false;
    IndexReader[] newSubReaders = new IndexReader[subReaders.length];
    boolean[] newDecrefOnClose = new boolean[subReaders.length];
    
    boolean success = false;
    try {
      for (int i = 0; i < subReaders.length; i++) {
        newSubReaders[i] = subReaders[i].reopen();
        if (newSubReaders[i] != subReaders[i]) {
          reopened = true;
          newDecrefOnClose[i] = false;
        }
      }

      if (reopened) {
        for (int i = 0; i < subReaders.length; i++) {
          if (newSubReaders[i] == subReaders[i]) {
            newSubReaders[i].incRef();
            newDecrefOnClose[i] = true;
          }
        }
        
        MultiReader mr = new MultiReader(newSubReaders);
        mr.decrefOnClose = newDecrefOnClose;
        success = true;
        return mr;
      } else {
        success = true;
        return this;
      }
    } finally {
      if (!success && reopened) {
        for (int i = 0; i < newSubReaders.length; i++) {
          if (newSubReaders[i] != null) {
            try {
              if (newDecrefOnClose[i]) {
                newSubReaders[i].decRef();
              } else {
                newSubReaders[i].close();
              }
            } catch (IOException ignore) {
            }
          }
        }
      }
    }
  }

  public TermFreqVector[] getTermFreqVectors(int n) throws IOException {
    ensureOpen();
  }

  public TermFreqVector getTermFreqVector(int n, String field)
      throws IOException {
    ensureOpen();
    return subReaders[i].getTermFreqVector(n - starts[i], field);
  }


  public void getTermFreqVector(int docNumber, String field, TermVectorMapper mapper) throws IOException {
    ensureOpen();
    subReaders[i].getTermFreqVector(docNumber - starts[i], field, mapper);
  }

  public void getTermFreqVector(int docNumber, TermVectorMapper mapper) throws IOException {
    ensureOpen();
    subReaders[i].getTermFreqVector(docNumber - starts[i], mapper);
  }

  public boolean isOptimized() {
    return false;
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

  public Document document(int n, FieldSelector fieldSelector) throws CorruptIndexException, IOException {
    ensureOpen();
  }

  public boolean isDeleted(int n) {
  }

  public boolean hasDeletions() {
    return hasDeletions;
  }

  protected void doDelete(int n) throws CorruptIndexException, IOException {
    hasDeletions = true;
  }

  protected void doUndeleteAll() throws CorruptIndexException, IOException {
    for (int i = 0; i < subReaders.length; i++)
      subReaders[i].undeleteAll();

    hasDeletions = false;
  }

    return MultiSegmentReader.readerIndex(n, this.starts, this.subReaders.length);
  }
  
  public boolean hasNorms(String field) throws IOException {
    ensureOpen();
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
    ensureOpen();
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
    ensureOpen();
    byte[] bytes = (byte[])normsCache.get(field);
    if (bytes==null && !hasNorms(field)) bytes=fakeNorms();
      System.arraycopy(bytes, 0, result, offset, maxDoc());

      subReaders[i].norms(field, result, offset + starts[i]);
  }

  protected void doSetNorm(int n, String field, byte value)
    throws CorruptIndexException, IOException {
    synchronized (normsCache) {
    }
  }

  public TermEnum terms() throws IOException {
    ensureOpen();
    return new MultiTermEnum(subReaders, starts, null);
  }

  public TermEnum terms(Term term) throws IOException {
    ensureOpen();
    return new MultiTermEnum(subReaders, starts, term);
  }

  public int docFreq(Term t) throws IOException {
    ensureOpen();
    for (int i = 0; i < subReaders.length; i++)
      total += subReaders[i].docFreq(t);
    return total;
  }

  public TermDocs termDocs() throws IOException {
    ensureOpen();
    return new MultiTermDocs(subReaders, starts);
  }

  public TermPositions termPositions() throws IOException {
    ensureOpen();
    return new MultiTermPositions(subReaders, starts);
  }

  protected void doCommit() throws IOException {
    for (int i = 0; i < subReaders.length; i++)
      subReaders[i].commit();
  }

  protected synchronized void doClose() throws IOException {
    for (int i = 0; i < subReaders.length; i++) {
      if (decrefOnClose[i]) {
        subReaders[i].decRef();
      } else {
        subReaders[i].close();
      }
    }
  }
  
  public Collection getFieldNames (IndexReader.FieldOption fieldNames) {
    ensureOpen();
    return MultiSegmentReader.getFieldNames(fieldNames, this.subReaders);
  }  
  
  /**
   * Checks recursively if all subreaders are up to date. 
   */
  public boolean isCurrent() throws CorruptIndexException, IOException {
    for (int i = 0; i < subReaders.length; i++) {
      if (!subReaders[i].isCurrent()) {
        return false;
      }
    }
    
    return true;
  }
  
  /** Not implemented.
   * @throws UnsupportedOperationException
   */
  public long getVersion() {
    throw new UnsupportedOperationException("MultiReader does not support this method.");
  }
  
  IndexReader[] getSubReaders() {
    return subReaders;
  }
}
