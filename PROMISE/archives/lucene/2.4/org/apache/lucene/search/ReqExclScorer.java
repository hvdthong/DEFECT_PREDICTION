import java.io.IOException;


/** A Scorer for queries with a required subscorer and an excluding (prohibited) subscorer.
 * <br>
 * This <code>Scorer</code> implements {@link Scorer#skipTo(int)},
 * and it uses the skipTo() on the given scorers.
 */
public class ReqExclScorer extends Scorer {
  private Scorer reqScorer, exclScorer;

  /** Construct a <code>ReqExclScorer</code>.
   * @param reqScorer The scorer that must match, except where
   * @param exclScorer indicates exclusion.
   */
  public ReqExclScorer(
      Scorer reqScorer,
      Scorer exclScorer) {
    this.reqScorer = reqScorer;
    this.exclScorer = exclScorer;
  }

  private boolean firstTime = true;
  
  public boolean next() throws IOException {
    if (firstTime) {
      if (! exclScorer.next()) {
      }
      firstTime = false;
    }
    if (reqScorer == null) {
      return false;
    }
    if (! reqScorer.next()) {
      return false;
    }
    if (exclScorer == null) {
    }
    return toNonExcluded();
  }
  
  /** Advance to non excluded doc.
   * <br>On entry:
   * <ul>
   * <li>reqScorer != null,
   * <li>exclScorer != null,
   * <li>reqScorer was advanced once via next() or skipTo()
   *      and reqScorer.doc() may still be excluded.
   * </ul>
   * Advances reqScorer a non excluded required doc, if any.
   * @return true iff there is a non excluded required doc.
   */
  private boolean toNonExcluded() throws IOException {
    int exclDoc = exclScorer.doc();
    do {  
      if (reqDoc < exclDoc) {
      } else if (reqDoc > exclDoc) {
        if (! exclScorer.skipTo(reqDoc)) {
          return true;
        }
        exclDoc = exclScorer.doc();
        if (exclDoc > reqDoc) {
        }
      }
    } while (reqScorer.next());
    return false;
  }

  public int doc() {
  }

  /** Returns the score of the current document matching the query.
   * Initially invalid, until {@link #next()} is called the first time.
   * @return The score of the required scorer.
   */
  public float score() throws IOException {
  }
  
  /** Skips to the first match beyond the current whose document number is
   * greater than or equal to a given target.
   * <br>When this method is used the {@link #explain(int)} method should not be used.
   * @param target The target document number.
   * @return true iff there is such a match.
   */
  public boolean skipTo(int target) throws IOException {
    if (firstTime) {
      firstTime = false;
      if (! exclScorer.skipTo(target)) {
      }
    }
    if (reqScorer == null) {
      return false;
    }
    if (exclScorer == null) {
      return reqScorer.skipTo(target);
    }
    if (! reqScorer.skipTo(target)) {
      reqScorer = null;
      return false;
    }
    return toNonExcluded();
  }

  public Explanation explain(int doc) throws IOException {
    Explanation res = new Explanation();
    if (exclScorer.skipTo(doc) && (exclScorer.doc() == doc)) {
      res.setDescription("excluded");
    } else {
      res.setDescription("not excluded");
      res.addDetail(reqScorer.explain(doc));
    }
    return res;
  }
}
