import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.store.Directory;

/** 
 * An IndexReader which reads indexes with multiple segments.
 */
class MultiSegmentReader extends DirectoryIndexReader {
  protected SegmentReader[] subReaders;
  private Map normsCache = new HashMap();
  private int maxDoc = 0;
  private int numDocs = -1;
  private boolean hasDeletions = false;

  /** Construct reading the named set of readers. */
  MultiSegmentReader(Directory directory, SegmentInfos sis, boolean closeDirectory, boolean readOnly) throws IOException {
    super(directory, sis, closeDirectory, readOnly);


    SegmentReader[] readers = new SegmentReader[sis.size()];
    for (int i = sis.size()-1; i >= 0; i--) {
      try {
        readers[i] = SegmentReader.get(readOnly, sis.info(i));
      } catch (IOException e) {
        for(i++;i<sis.size();i++) {
          try {
            readers[i].close();
          } catch (IOException ignore) {
          }
        }
        throw e;
      }
    }

    initialize(readers);
  }

  /** This contructor is only used for {@link #reopen()} */
  MultiSegmentReader(Directory directory, SegmentInfos infos, boolean closeDirectory, SegmentReader[] oldReaders, int[] oldStarts, Map oldNormsCache, boolean readOnly) throws IOException {
    super(directory, infos, closeDirectory, readOnly);

    Map segmentReaders = new HashMap();

    if (oldReaders != null) {
      for (int i = 0; i < oldReaders.length; i++) {
        segmentReaders.put(oldReaders[i].getSegmentName(), new Integer(i));
      }
    }
    
    SegmentReader[] newReaders = new SegmentReader[infos.size()];
    
    boolean[] readerShared = new boolean[infos.size()];
    
    for (int i = infos.size() - 1; i>=0; i--) {
      Integer oldReaderIndex = (Integer) segmentReaders.get(infos.info(i).name);
      if (oldReaderIndex == null) {
        newReaders[i] = null;
      } else {
        newReaders[i] = oldReaders[oldReaderIndex.intValue()];
      }

      boolean success = false;
      try {
        SegmentReader newReader;
        if (newReaders[i] == null || infos.info(i).getUseCompoundFile() != newReaders[i].getSegmentInfo().getUseCompoundFile()) {
          newReader = SegmentReader.get(readOnly, infos.info(i));
        } else {
          newReader = (SegmentReader) newReaders[i].reopenSegment(infos.info(i));
        }
        if (newReader == newReaders[i]) {
          readerShared[i] = true;
          newReader.incRef();
        } else {
          readerShared[i] = false;
          newReaders[i] = newReader;
        }
        success = true;
      } finally {
        if (!success) {
          for (i++; i < infos.size(); i++) {
            if (newReaders[i] != null) {
              try {
                if (!readerShared[i]) {
                  newReaders[i].close();
                } else {
                  newReaders[i].decRef();
                }
              } catch (IOException ignore) {
              }
            }
          }
        }
      }
    }    
    
    initialize(newReaders);
    
    if (oldNormsCache != null) {
      Iterator it = oldNormsCache.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        String field = (String) entry.getKey();
        if (!hasNorms(field)) {
          continue;
        }

        byte[] oldBytes = (byte[]) entry.getValue();

        byte[] bytes = new byte[maxDoc()];

        for (int i = 0; i < subReaders.length; i++) {
          Integer oldReaderIndex = ((Integer) segmentReaders.get(subReaders[i].getSegmentName()));

          if (oldReaderIndex != null &&
               (oldReaders[oldReaderIndex.intValue()] == subReaders[i] 
                 || oldReaders[oldReaderIndex.intValue()].norms.get(field) == subReaders[i].norms.get(field))) {
            System.arraycopy(oldBytes, oldStarts[oldReaderIndex.intValue()], bytes, starts[i], starts[i+1] - starts[i]);
          } else {
            subReaders[i].norms(field, bytes, starts[i]);
          }
        }

      }
    }
  }

  private void initialize(SegmentReader[] subReaders) {
    this.subReaders = subReaders;
    for (int i = 0; i < subReaders.length; i++) {
      starts[i] = maxDoc;

      if (subReaders[i].hasDeletions())
        hasDeletions = true;
    }
    starts[subReaders.length] = maxDoc;
  }

  protected synchronized DirectoryIndexReader doReopen(SegmentInfos infos) throws CorruptIndexException, IOException {
    if (infos.size() == 1) {
      return SegmentReader.get(readOnly, infos, infos.info(0), false);
    } else if (readOnly) {
      return new ReadOnlyMultiSegmentReader(directory, infos, closeDirectory, subReaders, starts, normsCache);
    } else {
      return new MultiSegmentReader(directory, infos, closeDirectory, subReaders, starts, normsCache, false);
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

    return readerIndex(n, this.starts, this.subReaders.length);
  }
  

    while (hi >= lo) {
      int mid = (lo + hi) >> 1;
      int midValue = starts[mid];
      if (n < midValue)
        hi = mid - 1;
      else if (n > midValue)
        lo = mid + 1;
        while (mid+1 < numSubReaders && starts[mid+1] == midValue) {
        }
        return mid;
      }
    }
    return hi;
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

  protected void commitChanges() throws IOException {
    for (int i = 0; i < subReaders.length; i++)
      subReaders[i].commit();
  }

  void startCommit() {
    super.startCommit();
    for (int i = 0; i < subReaders.length; i++) {
      subReaders[i].startCommit();
    }
  }

  void rollbackCommit() {
    super.rollbackCommit();
    for (int i = 0; i < subReaders.length; i++) {
      subReaders[i].rollbackCommit();
    }
  }

  protected synchronized void doClose() throws IOException {
    for (int i = 0; i < subReaders.length; i++)
      subReaders[i].decRef();
    
    super.doClose();
  }

  public Collection getFieldNames (IndexReader.FieldOption fieldNames) {
    ensureOpen();
    return getFieldNames(fieldNames, this.subReaders);
  }
  
  static Collection getFieldNames (IndexReader.FieldOption fieldNames, IndexReader[] subReaders) {
    Set fieldSet = new HashSet();
    for (int i = 0; i < subReaders.length; i++) {
      IndexReader reader = subReaders[i];
      Collection names = reader.getFieldNames(fieldNames);
      fieldSet.addAll(names);
    }
    return fieldSet;
  } 
  
  SegmentReader[] getSubReaders() {
    return subReaders;
  }

  public void setTermInfosIndexDivisor(int indexDivisor) throws IllegalStateException {
    for (int i = 0; i < subReaders.length; i++)
      subReaders[i].setTermInfosIndexDivisor(indexDivisor);
  }

  public int getTermInfosIndexDivisor() throws IllegalStateException {
    if (subReaders.length > 0)
      return subReaders[0].getTermInfosIndexDivisor();
    else
      throw new IllegalStateException("no readers");
  }

  static class MultiTermEnum extends TermEnum {
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

  static class MultiTermDocs implements TermDocs {
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
      for(;;) {
        if (current!=null && current.next()) {
          return true;
        }
        else if (pointer < readers.length) {
          base = starts[pointer];
          current = termDocs(pointer++);
        } else {
          return false;
        }
      }
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
  
   /* A Possible future optimization could skip entire segments */ 
    public boolean skipTo(int target) throws IOException {
      for(;;) {
        if (current != null && current.skipTo(target-base)) {
          return true;
        } else if (pointer < readers.length) {
          base = starts[pointer];
          current = termDocs(pointer++);
        } else
          return false;
      }
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

  static class MultiTermPositions extends MultiTermDocs implements TermPositions {
    public MultiTermPositions(IndexReader[] r, int[] s) {
      super(r,s);
    }
  
    protected TermDocs termDocs(IndexReader reader) throws IOException {
      return (TermDocs)reader.termPositions();
    }
  
    public int nextPosition() throws IOException {
      return ((TermPositions)current).nextPosition();
    }
    
    public int getPayloadLength() {
      return ((TermPositions)current).getPayloadLength();
    }
     
    public byte[] getPayload(byte[] data, int offset) throws IOException {
      return ((TermPositions)current).getPayload(data, offset);
    }
  
  
    public boolean isPayloadAvailable() {
      return ((TermPositions) current).isPayloadAvailable();
    }
  }
}
