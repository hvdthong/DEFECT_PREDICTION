import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.ToStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/** Matches spans containing a term. */
public class SpanTermQuery extends SpanQuery {
  protected Term term;

  /** Construct a SpanTermQuery matching the named term's spans. */
  public SpanTermQuery(Term term) { this.term = term; }

  /** Return the term whose spans are matched. */
  public Term getTerm() { return term; }

  public String getField() { return term.field(); }
  
  /** Returns a collection of all terms matched by this query.
   * @deprecated use extractTerms instead
   * @see #extractTerms(Set)
   */
  public Collection getTerms() {
    Collection terms = new ArrayList();
    terms.add(term);
    return terms;
  }
  public void extractTerms(Set terms) {
	  terms.add(term);
  }

  public String toString(String field) {
    StringBuffer buffer = new StringBuffer();
    if (term.field().equals(field))
      buffer.append(term.text());
    else
      buffer.append(term.toString());
    buffer.append(ToStringUtils.boost(getBoost()));
    return buffer.toString();
  }

  /** Returns true iff <code>o</code> is equal to this. */
  public boolean equals(Object o) {
    if (!(o instanceof SpanTermQuery))
      return false;
    SpanTermQuery other = (SpanTermQuery)o;
    return (this.getBoost() == other.getBoost())
      && this.term.equals(other.term);
  }

  /** Returns a hash code value for this object.*/
  public int hashCode() {
    return Float.floatToIntBits(getBoost()) ^ term.hashCode() ^ 0xD23FE494;
  }

  public Spans getSpans(final IndexReader reader) throws IOException {
    return new TermSpans(reader.termPositions(term), term);
  }

}
