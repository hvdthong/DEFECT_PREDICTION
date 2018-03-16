import java.io.IOException;

/**
 * Expert: Common scoring functionality for different types of queries.
 *
 * <p>
 * A <code>Scorer</code> either iterates over documents matching a
 * query in increasing order of doc Id, or provides an explanation of
 * the score for a query for a given document.
 * </p>
 * <p>
 * Document scores are computed using a given <code>Similarity</code>
 * implementation.
 * </p>
 * @see BooleanQuery#setAllowDocsOutOfOrder
 */
public abstract class Scorer extends DocIdSetIterator {
  private Similarity similarity;

  /** Constructs a Scorer.
   * @param similarity The <code>Similarity</code> implementation used by this scorer.
   */
  protected Scorer(Similarity similarity) {
    this.similarity = similarity;
  }

  /** Returns the Similarity implementation used by this scorer. */
  public Similarity getSimilarity() {
    return this.similarity;
  }

  /** Scores and collects all matching documents.
   * @param hc The collector to which all matching documents are passed through
   * {@link HitCollector#collect(int, float)}.
   * <br>When this method is used the {@link #explain(int)} method should not be used.
   */
  public void score(HitCollector hc) throws IOException {
    while (next()) {
      hc.collect(doc(), score());
    }
  }

  /** Expert: Collects matching documents in a range.  Hook for optimization.
   * Note that {@link #next()} must be called once before this method is called
   * for the first time.
   * @param hc The collector to which all matching documents are passed through
   * {@link HitCollector#collect(int, float)}.
   * @param max Do not score documents past this.
   * @return true if more matching documents may remain.
   */
  protected boolean score(HitCollector hc, int max) throws IOException {
    while (doc() < max) {
      hc.collect(doc(), score());
      if (!next())
        return false;
    }
    return true;
  }

  /** Returns the score of the current document matching the query.
   * Initially invalid, until {@link #next()} or {@link #skipTo(int)}
   * is called the first time.
   */
  public abstract float score() throws IOException;

  /** Returns an explanation of the score for a document.
   * <br>When this method is used, the {@link #next()}, {@link #skipTo(int)} and
   * {@link #score(HitCollector)} methods should not be used.
   * @param doc The document number for the explanation.
   */
  public abstract Explanation explain(int doc) throws IOException;

}
