import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.util.cache.Cache;
import org.apache.lucene.util.cache.SimpleLRUCache;
import org.apache.lucene.util.CloseableThreadLocal;

/** This stores a monotonically increasing set of <Term, TermInfo> pairs in a
 * Directory.  Pairs are accessed either by Term or by ordinal position the
 * set.  */

final class TermInfosReader {
  private Directory directory;
  private String segment;
  private FieldInfos fieldInfos;

  private CloseableThreadLocal threadResources = new CloseableThreadLocal();
  private SegmentTermEnum origEnum;
  private long size;

  private Term[] indexTerms = null;
  private TermInfo[] indexInfos;
  private long[] indexPointers;
  
  private SegmentTermEnum indexEnum;
  
  private int indexDivisor = 1;
  private int totalIndexInterval;

  private final static int DEFAULT_CACHE_SIZE = 1024;
  
  /**
   * Per-thread resources managed by ThreadLocal
   */
  private static final class ThreadResources {
    SegmentTermEnum termEnum;
    
    Cache termInfoCache;
  }
  
  TermInfosReader(Directory dir, String seg, FieldInfos fis)
       throws CorruptIndexException, IOException {
    this(dir, seg, fis, BufferedIndexInput.BUFFER_SIZE);
  }

  TermInfosReader(Directory dir, String seg, FieldInfos fis, int readBufferSize)
       throws CorruptIndexException, IOException {
    boolean success = false;

    try {
      directory = dir;
      segment = seg;
      fieldInfos = fis;

      origEnum = new SegmentTermEnum(directory.openInput(segment + "." + IndexFileNames.TERMS_EXTENSION,
          readBufferSize), fieldInfos, false);
      size = origEnum.size;
      totalIndexInterval = origEnum.indexInterval;

      indexEnum = new SegmentTermEnum(directory.openInput(segment + "." + IndexFileNames.TERMS_INDEX_EXTENSION,
          readBufferSize), fieldInfos, true);

      success = true;
    } finally {
      if (!success) {
        close();
      }
    }
  }

  public int getSkipInterval() {
    return origEnum.skipInterval;
  }
  
  public int getMaxSkipLevels() {
    return origEnum.maxSkipLevels;
  }

  /**
   * <p>Sets the indexDivisor, which subsamples the number
   * of indexed terms loaded into memory.  This has a
   * similar effect as {@link
   * IndexWriter#setTermIndexInterval} except that setting
   * must be done at indexing time while this setting can be
   * set per reader.  When set to N, then one in every
   * N*termIndexInterval terms in the index is loaded into
   * memory.  By setting this to a value > 1 you can reduce
   * memory usage, at the expense of higher latency when
   * loading a TermInfo.  The default value is 1.</p>
   *
   * <b>NOTE:</b> you must call this before the term
   * index is loaded.  If the index is already loaded,
   * an IllegalStateException is thrown.
   *
   + @throws IllegalStateException if the term index has
   * already been loaded into memory.
   */
  public void setIndexDivisor(int indexDivisor) throws IllegalStateException {
    if (indexDivisor < 1)
      throw new IllegalArgumentException("indexDivisor must be > 0: got " + indexDivisor);

    if (indexTerms != null)
      throw new IllegalStateException("index terms are already loaded");

    this.indexDivisor = indexDivisor;
    totalIndexInterval = origEnum.indexInterval * indexDivisor;
  }

  /** Returns the indexDivisor.
   * @see #setIndexDivisor
   */
  public int getIndexDivisor() {
    return indexDivisor;
  }
  
  final void close() throws IOException {
    if (origEnum != null)
      origEnum.close();
    if (indexEnum != null)
      indexEnum.close();
    threadResources.close();
  }

  /** Returns the number of term/value pairs in the set. */
  final long size() {
    return size;
  }

  private ThreadResources getThreadResources() {
    ThreadResources resources = (ThreadResources)threadResources.get();
    if (resources == null) {
      resources = new ThreadResources();
      resources.termEnum = terms();
      resources.termInfoCache = new SimpleLRUCache(DEFAULT_CACHE_SIZE);
      threadResources.set(resources);
    }
    return resources;
  }

  private synchronized void ensureIndexIsRead() throws IOException {
    try {

      indexTerms = new Term[indexSize];
      indexInfos = new TermInfo[indexSize];
      indexPointers = new long[indexSize];
        
      for (int i = 0; indexEnum.next(); i++) {
        indexTerms[i] = indexEnum.term();
        indexInfos[i] = indexEnum.termInfo();
        indexPointers[i] = indexEnum.indexPointer;
        
        for (int j = 1; j < indexDivisor; j++)
            if (!indexEnum.next())
                break;
      }
    } finally {
        indexEnum.close();
        indexEnum = null;
    }
  }

  /** Returns the offset of the greatest index entry which is less than or equal to term.*/
  private final int getIndexOffset(Term term) {
    int hi = indexTerms.length - 1;

    while (hi >= lo) {
      int mid = (lo + hi) >> 1;
      int delta = term.compareTo(indexTerms[mid]);
      if (delta < 0)
	hi = mid - 1;
      else if (delta > 0)
	lo = mid + 1;
      else
	return mid;
    }
    return hi;
  }

  private final void seekEnum(SegmentTermEnum enumerator, int indexOffset) throws IOException {
    enumerator.seek(indexPointers[indexOffset],
                   (indexOffset * totalIndexInterval) - 1,
                   indexTerms[indexOffset], indexInfos[indexOffset]);
  }

  /** Returns the TermInfo for a Term in the set, or null. */
  TermInfo get(Term term) throws IOException {
    return get(term, true);
  }
  
  /** Returns the TermInfo for a Term in the set, or null. */
  private TermInfo get(Term term, boolean useCache) throws IOException {
    if (size == 0) return null;

    ensureIndexIsRead();
    
    TermInfo ti;
    ThreadResources resources = getThreadResources();
    Cache cache = null;
    
    if (useCache) {
      cache = resources.termInfoCache;
      ti = (TermInfo) cache.get(term);
      if (ti != null) {
        return ti;
      }
    }
    
    SegmentTermEnum enumerator = resources.termEnum;
	&& ((enumerator.prev() != null && term.compareTo(enumerator.prev())> 0)
	    || term.compareTo(enumerator.term()) >= 0)) {
      int enumOffset = (int)(enumerator.position/totalIndexInterval)+1;
    || term.compareTo(indexTerms[enumOffset]) < 0) {

        int numScans = enumerator.scanTo(term);
        if (enumerator.term() != null && term.compareTo(enumerator.term()) == 0) {
          ti = enumerator.termInfo();
          if (cache != null && numScans > 1) {
            cache.put(term, ti);
          }
        } else {
          ti = null;
        }

        return ti;
      }  
    }

    seekEnum(enumerator, getIndexOffset(term));
    enumerator.scanTo(term);
    if (enumerator.term() != null && term.compareTo(enumerator.term()) == 0) {
      ti = enumerator.termInfo();
      if (cache != null) {
        cache.put(term, ti);
      }
    } else {
      ti = null;
    }
    return ti;
  }

  /** Returns the nth term in the set. */
  final Term get(int position) throws IOException {
    if (size == 0) return null;

    SegmentTermEnum enumerator = getThreadResources().termEnum;
    if (enumerator != null && enumerator.term() != null &&
        position >= enumerator.position &&
	position < (enumerator.position + totalIndexInterval))

    return scanEnum(enumerator, position);
  }

  private final Term scanEnum(SegmentTermEnum enumerator, int position) throws IOException {
    while(enumerator.position < position)
      if (!enumerator.next())
	return null;

    return enumerator.term();
  }

  /** Returns the position of a Term in the set or -1. */
  final long getPosition(Term term) throws IOException {
    if (size == 0) return -1;

    ensureIndexIsRead();
    int indexOffset = getIndexOffset(term);
    
    SegmentTermEnum enumerator = getThreadResources().termEnum;
    seekEnum(enumerator, indexOffset);

    while(term.compareTo(enumerator.term()) > 0 && enumerator.next()) {}

    if (term.compareTo(enumerator.term()) == 0)
      return enumerator.position;
    else
      return -1;
  }

  /** Returns an enumeration of all the Terms and TermInfos in the set. */
  public SegmentTermEnum terms() {
    return (SegmentTermEnum)origEnum.clone();
  }

  /** Returns an enumeration of terms starting at or after the named term. */
  public SegmentTermEnum terms(Term term) throws IOException {
    get(term, false);
    return (SegmentTermEnum)getThreadResources().termEnum.clone();
  }
}
