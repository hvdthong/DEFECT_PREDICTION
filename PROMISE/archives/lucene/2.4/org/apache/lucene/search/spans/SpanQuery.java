import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Weight;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/** Base class for span-based queries. */
public abstract class SpanQuery extends Query {
  /** Expert: Returns the matches for this query in an index.  Used internally
   * to search for spans. */
  public abstract Spans getSpans(IndexReader reader) throws IOException;

  /**
   * Returns the matches for this query in an index, including access to any {@link org.apache.lucene.index.Payload}s at those
   * positions.  Implementing classes that want access to the payloads will need to implement this.
   * @param reader  The {@link org.apache.lucene.index.IndexReader} to use to get spans/payloads
   * @return null
   * @throws IOException if there is an error accessing the payload
   *
   * <font color="#FF0000">
   * WARNING: The status of the <b>Payloads</b> feature is experimental.
   * The APIs introduced here might change in the future and will not be
   * supported anymore in such a case.</font>
   */
  public PayloadSpans getPayloadSpans(IndexReader reader) throws IOException{
    return null;
  };

  /** Returns the name of the field matched by this query.*/
  public abstract String getField();

  /** Returns a collection of all terms matched by this query.
   * @deprecated use extractTerms instead
   * @see Query#extractTerms(Set)
   */
  public abstract Collection getTerms();

  protected Weight createWeight(Searcher searcher) throws IOException {
    return new SpanWeight(this, searcher);
  }

}

