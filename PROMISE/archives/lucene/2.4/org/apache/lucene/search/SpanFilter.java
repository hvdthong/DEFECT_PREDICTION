import org.apache.lucene.index.IndexReader;

import java.io.IOException;

/** Abstract base class providing a mechanism to restrict searches to a subset
 of an index and also maintains and returns position information.

 This is useful if you want to compare the positions from a SpanQuery with the positions of items in
 a filter.  For instance, if you had a SpanFilter that marked all the occurrences of the word "foo" in documents,
 and then you entered a new SpanQuery containing bar, you could not only filter by the word foo, but you could
 then compare position information for post processing.
 */
public abstract class SpanFilter extends Filter{
  /** Returns a SpanFilterResult with true for documents which should be permitted in
    search results, and false for those that should not and Spans for where the true docs match.
   * @param reader The {@link org.apache.lucene.index.IndexReader} to load position and DocIdSet information from
   * @return A {@link SpanFilterResult}
   * @throws java.io.IOException if there was an issue accessing the necessary information
   * */
  public abstract SpanFilterResult bitSpans(IndexReader reader) throws IOException;
}
