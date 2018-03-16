import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.util.OpenBitSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Constrains search results to only match those which also match a provided
 * query. Also provides position information about where each document matches
 * at the cost of extra space compared with the QueryWrapperFilter.
 * There is an added cost to this above what is stored in a {@link QueryWrapperFilter}.  Namely,
 * the position information for each matching document is stored.
 * <p/>
 * This filter does not cache.  See the {@link org.apache.lucene.search.CachingSpanFilter} for a wrapper that
 * caches.
 *
 *
 * @version $Id:$
 */
public class SpanQueryFilter extends SpanFilter {
  protected SpanQuery query;

  protected SpanQueryFilter()
  {
    
  }

  /** Constructs a filter which only matches documents matching
   * <code>query</code>.
   * @param query The {@link org.apache.lucene.search.spans.SpanQuery} to use as the basis for the Filter.
   */
  public SpanQueryFilter(SpanQuery query) {
    this.query = query;
  }

  public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
    SpanFilterResult result = bitSpans(reader);
    return result.getDocIdSet();
  }

  public SpanFilterResult bitSpans(IndexReader reader) throws IOException {

    final OpenBitSet bits = new OpenBitSet(reader.maxDoc());
    Spans spans = query.getSpans(reader);
    List tmp = new ArrayList(20);
    int currentDoc = -1;
    SpanFilterResult.PositionInfo currentInfo = null;
    while (spans.next())
    {
      int doc = spans.doc();
      bits.set(doc);
      if (currentDoc != doc)
      {
        currentInfo = new SpanFilterResult.PositionInfo(doc);
        tmp.add(currentInfo);
        currentDoc = doc;
      }
      currentInfo.addPosition(spans.start(), spans.end());
    }
    return new SpanFilterResult(bits, tmp);
  }


  public SpanQuery getQuery() {
    return query;
  }

  public String toString() {
    return "QueryWrapperFilter(" + query + ")";
  }

  public boolean equals(Object o) {
    return o instanceof SpanQueryFilter && this.query.equals(((SpanQueryFilter) o).query);
  }

  public int hashCode() {
    return query.hashCode() ^ 0x923F64B9;
  }
}
