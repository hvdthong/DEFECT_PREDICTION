import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.ToStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/** Removes matches which overlap with another SpanQuery. */
public class SpanNotQuery extends SpanQuery {
  private SpanQuery include;
  private SpanQuery exclude;

  /** Construct a SpanNotQuery matching spans from <code>include</code> which
   * have no overlap with spans from <code>exclude</code>.*/
  public SpanNotQuery(SpanQuery include, SpanQuery exclude) {
    this.include = include;
    this.exclude = exclude;

    if (!include.getField().equals(exclude.getField()))
      throw new IllegalArgumentException("Clauses must have same field.");
  }

  /** Return the SpanQuery whose matches are filtered. */
  public SpanQuery getInclude() { return include; }

  /** Return the SpanQuery whose matches must not overlap those returned. */
  public SpanQuery getExclude() { return exclude; }

  public String getField() { return include.getField(); }

  /** Returns a collection of all terms matched by this query.
   * @deprecated use extractTerms instead
   * @see #extractTerms(Set)
   */
  public Collection getTerms() { return include.getTerms(); }
  
  public void extractTerms(Set terms) { include.extractTerms(terms); }

  public String toString(String field) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("spanNot(");
    buffer.append(include.toString(field));
    buffer.append(", ");
    buffer.append(exclude.toString(field));
    buffer.append(")");
    buffer.append(ToStringUtils.boost(getBoost()));
    return buffer.toString();
  }


  public Spans getSpans(final IndexReader reader) throws IOException {
    return new PayloadSpans() {
        private PayloadSpans includeSpans = include.getPayloadSpans(reader);
        private boolean moreInclude = true;

        private Spans excludeSpans = exclude.getSpans(reader);
        private boolean moreExclude = excludeSpans.next();

        public boolean next() throws IOException {
            moreInclude = includeSpans.next();

          while (moreInclude && moreExclude) {

              moreExclude = excludeSpans.skipTo(includeSpans.doc());

                   && includeSpans.doc() == excludeSpans.doc()
                   && excludeSpans.end() <= includeSpans.start()) {
            }

                || includeSpans.doc() != excludeSpans.doc()
                || includeSpans.end() <= excludeSpans.start())

          }
          return moreInclude;
        }

        public boolean skipTo(int target) throws IOException {
            moreInclude = includeSpans.skipTo(target);

          if (!moreInclude)
            return false;

              && includeSpans.doc() > excludeSpans.doc())
            moreExclude = excludeSpans.skipTo(includeSpans.doc());

                 && includeSpans.doc() == excludeSpans.doc()
                 && excludeSpans.end() <= includeSpans.start()) {
          }

                || includeSpans.doc() != excludeSpans.doc()
                || includeSpans.end() <= excludeSpans.start())

        }

        public int doc() { return includeSpans.doc(); }
        public int start() { return includeSpans.start(); }
        public int end() { return includeSpans.end(); }

      public Collection/*<byte[]>*/ getPayload() throws IOException {
        ArrayList result = null;
        if (includeSpans.isPayloadAvailable()) {
          result = new ArrayList(includeSpans.getPayload());
        }
        return result;
      }

     public boolean isPayloadAvailable() {
        return includeSpans.isPayloadAvailable();
      }

      public String toString() {
          return "spans(" + SpanNotQuery.this.toString() + ")";
        }

      };
  }

  public PayloadSpans getPayloadSpans(IndexReader reader) throws IOException {
    return (PayloadSpans) getSpans(reader);
  }

  public Query rewrite(IndexReader reader) throws IOException {
    SpanNotQuery clone = null;

    SpanQuery rewrittenInclude = (SpanQuery) include.rewrite(reader);
    if (rewrittenInclude != include) {
      clone = (SpanNotQuery) this.clone();
      clone.include = rewrittenInclude;
    }
    SpanQuery rewrittenExclude = (SpanQuery) exclude.rewrite(reader);
    if (rewrittenExclude != exclude) {
      if (clone == null) clone = (SpanNotQuery) this.clone();
      clone.exclude = rewrittenExclude;
    }

    if (clone != null) {
    } else {
    }
  }

    /** Returns true iff <code>o</code> is equal to this. */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SpanNotQuery)) return false;

    SpanNotQuery other = (SpanNotQuery)o;
    return this.include.equals(other.include)
            && this.exclude.equals(other.exclude)
            && this.getBoost() == other.getBoost();
  }

  public int hashCode() {
    int h = include.hashCode();
    h ^= exclude.hashCode();
    h ^= Float.floatToRawIntBits(getBoost());
    return h;
  }

}
