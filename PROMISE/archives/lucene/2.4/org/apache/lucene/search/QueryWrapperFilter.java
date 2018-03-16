import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.OpenBitSet;

/** 
 * Constrains search results to only match those which also match a provided
 * query.  
 *
 * <p> This could be used, for example, with a {@link RangeQuery} on a suitably
 * formatted date field to implement date filtering.  One could re-use a single
 * QueryFilter that matches, e.g., only documents modified within the last
 * week.  The QueryFilter and RangeQuery would only need to be reconstructed
 * once per day.
 *
 * @version $Id:$
 */
public class QueryWrapperFilter extends Filter {
  private Query query;

  /** Constructs a filter which only matches documents matching
   * <code>query</code>.
   */
  public QueryWrapperFilter(Query query) {
    this.query = query;
  }

  /**
   * @deprecated Use {@link #getDocIdSet(IndexReader)} instead.
   */
  public BitSet bits(IndexReader reader) throws IOException {
    final BitSet bits = new BitSet(reader.maxDoc());

    new IndexSearcher(reader).search(query, new HitCollector() {
      public final void collect(int doc, float score) {
      }
    });
    return bits;
  }
  
  public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
    final OpenBitSet bits = new OpenBitSet(reader.maxDoc());

    new IndexSearcher(reader).search(query, new HitCollector() {
      public final void collect(int doc, float score) {
      }
    });
    return bits;
  }

  public String toString() {
    return "QueryWrapperFilter(" + query + ")";
  }

  public boolean equals(Object o) {
    if (!(o instanceof QueryWrapperFilter))
      return false;
    return this.query.equals(((QueryWrapperFilter)o).query);
  }

  public int hashCode() {
    return query.hashCode() ^ 0x923F64B9;
  }
}
