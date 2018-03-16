import java.io.IOException;

import java.util.Collection;
import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.Searcher;

/** Base class for span-based queries. */
public abstract class SpanQuery extends Query {
  /** Expert: Returns the matches for this query in an index.  Used internally
   * to search for spans. */
  public abstract Spans getSpans(IndexReader reader) throws IOException;

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

