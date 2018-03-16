import org.apache.lucene.index.IndexReader;
import java.util.BitSet;
import java.util.WeakHashMap;
import java.util.Map;
import java.io.IOException;

/**
 * Wraps another filter's result and caches it.  The purpose is to allow
 * filters to simply filter, and then wrap with this class to add caching.
 */
public class CachingWrapperFilter extends Filter {
  protected Filter filter;

  /**
   * A transient Filter cache.  To cache Filters even when using {@link RemoteSearchable} use
   * {@link RemoteCachingWrapperFilter} instead.
   */
  protected transient Map cache;

  /**
   * @param filter Filter to cache results of
   */
  public CachingWrapperFilter(Filter filter) {
    this.filter = filter;
  }

  /**
   * @deprecated Use {@link #getDocIdSet(IndexReader)} instead.
   */
  public BitSet bits(IndexReader reader) throws IOException {
    if (cache == null) {
      cache = new WeakHashMap();
    }

      BitSet cached = (BitSet) cache.get(reader);
      if (cached != null) {
        return cached;
      }
    }

    final BitSet bits = filter.bits(reader);

      cache.put(reader, bits);
    }

    return bits;
  }
  
  public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
    if (cache == null) {
      cache = new WeakHashMap();
    }

      DocIdSet cached = (DocIdSet) cache.get(reader);
      if (cached != null) {
        return cached;
      }
    }

    final DocIdSet docIdSet = filter.getDocIdSet(reader);

      cache.put(reader, docIdSet);
    }

    return docIdSet;
    
  }

  public String toString() {
    return "CachingWrapperFilter("+filter+")";
  }

  public boolean equals(Object o) {
    if (!(o instanceof CachingWrapperFilter)) return false;
    return this.filter.equals(((CachingWrapperFilter)o).filter);
  }

  public int hashCode() {
    return filter.hashCode() ^ 0x1117BF25;  
  }
}
