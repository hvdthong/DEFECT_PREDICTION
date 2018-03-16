import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import java.io.IOException;

/** Implements the wildcard search query. Supported wildcards are <code>*</code>, which
 * matches any character sequence (including the empty one), and <code>?</code>,
 * which matches any single character. Note this query can be slow, as it
 * needs to iterate over many terms. In order to prevent extremely slow WildcardQueries,
 * a Wildcard term should not start with one of the wildcards <code>*</code> or
 * <code>?</code>.
 * 
 * @see WildcardTermEnum
 */
public class WildcardQuery extends MultiTermQuery {
  private boolean termContainsWildcard;
    
  public WildcardQuery(Term term) {
    super(term);
    this.termContainsWildcard = (term.text().indexOf('*') != -1) || (term.text().indexOf('?') != -1);
  }

  protected FilteredTermEnum getEnum(IndexReader reader) throws IOException {
    return new WildcardTermEnum(reader, getTerm());
  }

  public boolean equals(Object o) {
    if (o instanceof WildcardQuery)
      return super.equals(o);

    return false;
  }
  
  public Query rewrite(IndexReader reader) throws IOException {
      if (this.termContainsWildcard) {
          return super.rewrite(reader);
      }
      
      return new TermQuery(getTerm());
  }
}
