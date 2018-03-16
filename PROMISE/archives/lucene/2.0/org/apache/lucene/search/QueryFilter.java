import java.io.IOException;
import java.util.WeakHashMap;
import java.util.BitSet;
import org.apache.lucene.index.IndexReader;

/** Constrains search results to only match those which also match a provided
 * query.  Results are cached, so that searches after the first on the same
 * index using this filter are much faster.
 *
 * <p> This could be used, for example, with a {@link RangeQuery} on a suitably
 * formatted date field to implement date filtering.  One could re-use a single
 * QueryFilter that matches, e.g., only documents modified within the last
 * week.  The QueryFilter and RangeQuery would only need to be reconstructed
 * once per day.
 *
 * @version $Id: QueryFilter.java 328729 2005-10-26 21:05:35Z yonik $
 */
public class QueryFilter extends Filter {
  private Query query;
  private transient WeakHashMap cache = null;

  /** Constructs a filter which only matches documents matching
   * <code>query</code>.
   */
  public QueryFilter(Query query) {
    this.query = query;
  }

  public BitSet bits(IndexReader reader) throws IOException {

    if (cache == null) {
      cache = new WeakHashMap();
    }

      BitSet cached = (BitSet) cache.get(reader);
      if (cached != null) {
        return cached;
      }
    }

    final BitSet bits = new BitSet(reader.maxDoc());

    new IndexSearcher(reader).search(query, new HitCollector() {
      public final void collect(int doc, float score) {
      }
    });

      cache.put(reader, bits);
    }

    return bits;
  }

  public String toString() {
    return "QueryFilter("+query+")";
  }

  public boolean equals(Object o) {
    if (!(o instanceof QueryFilter)) return false;
    return this.query.equals(((QueryFilter)o).query);
  }

  public int hashCode() {
    return query.hashCode() ^ 0x923F64B9;  
  }
}
