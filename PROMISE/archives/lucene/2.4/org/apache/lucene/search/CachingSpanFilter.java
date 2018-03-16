import org.apache.lucene.index.IndexReader;

import java.io.IOException;
import java.util.BitSet;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Wraps another SpanFilter's result and caches it.  The purpose is to allow
 * filters to simply filter, and then wrap with this class to add caching.
 */
public class CachingSpanFilter extends SpanFilter {
  protected SpanFilter filter;

  /**
   * A transient Filter cache.  To cache Filters even when using {@link org.apache.lucene.search.RemoteSearchable} use
   * {@link org.apache.lucene.search.RemoteCachingWrapperFilter} instead.
   */
  protected transient Map cache;

  /**
   * @param filter Filter to cache results of
   */
  public CachingSpanFilter(SpanFilter filter) {
    this.filter = filter;
  }

  /**
   * @deprecated Use {@link #getDocIdSet(IndexReader)} instead.
   */
  public BitSet bits(IndexReader reader) throws IOException {
    SpanFilterResult result = getCachedResult(reader);
    return result != null ? result.getBits() : null;
  }
  
  public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
    SpanFilterResult result = getCachedResult(reader);
    return result != null ? result.getDocIdSet() : null;
  }
  
  private SpanFilterResult getCachedResult(IndexReader reader) throws IOException {
    SpanFilterResult result = null;
    if (cache == null) {
      cache = new WeakHashMap();
    }

      result = (SpanFilterResult) cache.get(reader);
      if (result == null) {
        result = filter.bitSpans(reader);
        cache.put(reader, result);
      }
    }
    return result;
  }


  public SpanFilterResult bitSpans(IndexReader reader) throws IOException {
    return getCachedResult(reader);
  }

  public String toString() {
    return "CachingSpanFilter("+filter+")";
  }

  public boolean equals(Object o) {
    if (!(o instanceof CachingSpanFilter)) return false;
    return this.filter.equals(((CachingSpanFilter)o).filter);
  }

  public int hashCode() {
    return filter.hashCode() ^ 0x1117BF25;
  }
}
