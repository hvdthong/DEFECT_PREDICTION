import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.ToStringUtils;

/** A Query that matches documents containing terms with a specified prefix. A PrefixQuery
 * is built by QueryParser for input like <code>app*</code>. */
public class PrefixQuery extends Query {
  private Term prefix;

  /** Constructs a query for terms starting with <code>prefix</code>. */
  public PrefixQuery(Term prefix) {
    this.prefix = prefix;
  }

  /** Returns the prefix of this query. */
  public Term getPrefix() { return prefix; }

  public Query rewrite(IndexReader reader) throws IOException {
    BooleanQuery query = new BooleanQuery(true);
    TermEnum enumerator = reader.terms(prefix);
    try {
      String prefixText = prefix.text();
      String prefixField = prefix.field();
      do {
        Term term = enumerator.term();
        if (term != null &&
            term.text().startsWith(prefixText) &&
        {
        } else {
          break;
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
    if (!prefix.field().equals(field)) {
      buffer.append(prefix.field());
      buffer.append(":");
    }
    buffer.append(prefix.text());
    buffer.append('*');
    buffer.append(ToStringUtils.boost(getBoost()));
    return buffer.toString();
  }

  /** Returns true iff <code>o</code> is equal to this. */
  public boolean equals(Object o) {
    if (!(o instanceof PrefixQuery))
      return false;
    PrefixQuery other = (PrefixQuery)o;
    return (this.getBoost() == other.getBoost())
      && this.prefix.equals(other.prefix);
  }

  /** Returns a hash code value for this object.*/
  public int hashCode() {
    return Float.floatToIntBits(getBoost()) ^ prefix.hashCode() ^ 0x6634D93C;
  }
}
