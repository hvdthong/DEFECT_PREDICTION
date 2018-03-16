import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.ToStringUtils;

/**
 * A {@link Query} that matches documents containing a subset of terms provided
 * by a {@link FilteredTermEnum} enumeration.
 * <P>
 * <code>MultiTermQuery</code> is not designed to be used by itself.
 * <BR>
 * The reason being that it is not intialized with a {@link FilteredTermEnum}
 * enumeration. A {@link FilteredTermEnum} enumeration needs to be provided.
 * <P>
 * For example, {@link WildcardQuery} and {@link FuzzyQuery} extend
 * <code>MultiTermQuery</code> to provide {@link WildcardTermEnum} and
 * {@link FuzzyTermEnum}, respectively.
 */
public abstract class MultiTermQuery extends Query {
    private Term term;

    /** Constructs a query for terms matching <code>term</code>. */
    public MultiTermQuery(Term term) {
        this.term = term;
    }

    /** Returns the pattern term. */
    public Term getTerm() { return term; }

    /** Construct the enumeration to be used, expanding the pattern term. */
    protected abstract FilteredTermEnum getEnum(IndexReader reader)
      throws IOException;

    public Query rewrite(IndexReader reader) throws IOException {
      FilteredTermEnum enumerator = getEnum(reader);
      BooleanQuery query = new BooleanQuery(true);
      try {
        do {
          Term t = enumerator.term();
          if (t != null) {
          }
        } while (enumerator.next());
      } finally {
        enumerator.close();
      }
      return query;
    }

    /** Prints a user-readable version of this query. */
    public String toString(String field) {
        StringBuffer buffer = new StringBuffer();
        if (!term.field().equals(field)) {
            buffer.append(term.field());
            buffer.append(":");
        }
        buffer.append(term.text());
        buffer.append(ToStringUtils.boost(getBoost()));
        return buffer.toString();
    }

    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof MultiTermQuery)) return false;

      final MultiTermQuery multiTermQuery = (MultiTermQuery) o;

      if (!term.equals(multiTermQuery.term)) return false;

      return getBoost() == multiTermQuery.getBoost();
    }

    public int hashCode() {
      return term.hashCode() + Float.floatToRawIntBits(getBoost());
    }
}
