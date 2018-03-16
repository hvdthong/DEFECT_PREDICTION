import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.index.IndexReader;

/**
 * Provides caching of {@link Filter}s themselves on the remote end of an RMI connection.
 * The cache is keyed on Filter's hashCode(), so if it sees the same filter twice
 * it will reuse the original version.
 * <p/>
 * NOTE: This does NOT cache the Filter bits, but rather the Filter itself.
 * Thus, this works hand-in-hand with {@link CachingWrapperFilter} to keep both
 * file Filter cache and the Filter bits on the remote end, close to the searcher.
 * <p/>
 * Usage:
 * <p/>
 * To cache a result you must do something like 
 * RemoteCachingWrapperFilter f = new RemoteCachingWrapperFilter(new CachingWrapperFilter(myFilter));
 * <p/>
 * @author Matt Ericson
 */
public class RemoteCachingWrapperFilter extends Filter {
  protected Filter filter;

  public RemoteCachingWrapperFilter(Filter filter) {
    this.filter = filter;
  }

  /**
   * Uses the {@link FilterManager} to keep the cache for a filter on the 
   * searcher side of a remote connection.
   * @param reader the index reader for the Filter
   * @return the bitset
   */
  public BitSet bits(IndexReader reader) throws IOException {
    Filter cachedFilter = FilterManager.getInstance().getFilter(filter);
    return cachedFilter.bits(reader);
  }
}
