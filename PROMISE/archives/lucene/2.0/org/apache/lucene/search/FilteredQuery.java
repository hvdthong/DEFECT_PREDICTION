import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.ToStringUtils;

import java.io.IOException;
import java.util.BitSet;
import java.util.Set;


/**
 * A query that applies a filter to the results of another query.
 *
 * <p>Note: the bits are retrieved from the filter each time this
 * query is used in a search - use a CachingWrapperFilter to avoid
 * regenerating the bits every time.
 *
 * <p>Created: Apr 20, 2004 8:58:29 AM
 *
 * @author  Tim Jones
 * @since   1.4
 * @version $Id: FilteredQuery.java 384072 2006-03-08 00:59:28Z ehatcher $
 * @see     CachingWrapperFilter
 */
public class FilteredQuery
extends Query {

  Query query;
  Filter filter;

  /**
   * Constructs a new query which applies a filter to the results of the original query.
   * Filter.bits() will be called every time this query is used in a search.
   * @param query  Query to be filtered, cannot be <code>null</code>.
   * @param filter Filter to apply to query results, cannot be <code>null</code>.
   */
  public FilteredQuery (Query query, Filter filter) {
    this.query = query;
    this.filter = filter;
  }



  /**
   * Returns a Weight that applies the filter to the enclosed query's Weight.
   * This is accomplished by overriding the Scorer returned by the Weight.
   */
  protected Weight createWeight (final Searcher searcher) throws IOException {
    final Weight weight = query.createWeight (searcher);
    final Similarity similarity = query.getSimilarity(searcher);
    return new Weight() {

      public float getValue() { return weight.getValue(); }
      public float sumOfSquaredWeights() throws IOException { return weight.sumOfSquaredWeights(); }
      public void normalize (float v) { weight.normalize(v); }
      public Explanation explain (IndexReader ir, int i) throws IOException { return weight.explain (ir, i); }

      public Query getQuery() { return FilteredQuery.this; }

       public Scorer scorer (IndexReader indexReader) throws IOException {
        final Scorer scorer = weight.scorer (indexReader);
        final BitSet bitset = filter.bits (indexReader);
        return new Scorer (similarity) {

          public boolean next() throws IOException {
            do {
              if (! scorer.next()) {
                return false;
              }
            } while (! bitset.get(scorer.doc()));
            /* When skipTo() is allowed on scorer it should be used here
             * in combination with bitset.nextSetBit(...)
             * See the while loop in skipTo() below.
             */
            return true;
          }
          public int doc() { return scorer.doc(); }

          public boolean skipTo(int i) throws IOException {
            if (! scorer.skipTo(i)) {
              return false;
            }
            while (! bitset.get(scorer.doc())) {
              int nextFiltered = bitset.nextSetBit(scorer.doc() + 1);
              if (nextFiltered == -1) {
                return false;
              } else if (! scorer.skipTo(nextFiltered)) {
                return false;
              }
            }
            return true;
           }

          public float score() throws IOException { return scorer.score(); }

          public Explanation explain (int i) throws IOException {
            Explanation exp = scorer.explain (i);
            if (bitset.get(i))
              exp.setDescription ("allowed by filter: "+exp.getDescription());
            else
              exp.setDescription ("removed by filter: "+exp.getDescription());
            return exp;
          }
        };
      }
    };
  }

  /** Rewrites the wrapped query. */
  public Query rewrite(IndexReader reader) throws IOException {
    Query rewritten = query.rewrite(reader);
    if (rewritten != query) {
      FilteredQuery clone = (FilteredQuery)this.clone();
      clone.query = rewritten;
      return clone;
    } else {
      return this;
    }
  }

  public Query getQuery() {
    return query;
  }

  public Filter getFilter() {
    return filter;
  }

  public void extractTerms(Set terms) {
      getQuery().extractTerms(terms);
  }

  /** Prints a user-readable version of this query. */
  public String toString (String s) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("filtered(");
    buffer.append(query.toString(s));
    buffer.append(")->");
    buffer.append(filter);
    buffer.append(ToStringUtils.boost(getBoost()));
    return buffer.toString();
  }

  /** Returns true iff <code>o</code> is equal to this. */
  public boolean equals(Object o) {
    if (o instanceof FilteredQuery) {
      FilteredQuery fq = (FilteredQuery) o;
      return (query.equals(fq.query) && filter.equals(fq.filter));
    }
    return false;
  }

  /** Returns a hash code value for this object. */
  public int hashCode() {
    return query.hashCode() ^ filter.hashCode();
  }
}
