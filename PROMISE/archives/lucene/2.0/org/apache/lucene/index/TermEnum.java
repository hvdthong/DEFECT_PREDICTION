import java.io.IOException;

/** Abstract class for enumerating terms.

  <p>Term enumerations are always ordered by Term.compareTo().  Each term in
  the enumeration is greater than all that precede it.  */

public abstract class TermEnum {
  /** Increments the enumeration to the next element.  True if one exists.*/
  public abstract boolean next() throws IOException;

  /** Returns the current Term in the enumeration.*/
  public abstract Term term();

  /** Returns the docFreq of the current Term in the enumeration.*/
  public abstract int docFreq();

  /** Closes the enumeration to further activity, freeing resources. */
  public abstract void close() throws IOException;
  
  
  /** Skips terms to the first beyond the current whose value is
   * greater or equal to <i>target</i>. <p>Returns true iff there is such
   * an entry.  <p>Behaves as if written: <pre>
   *   public boolean skipTo(Term target) {
   *     do {
   *       if (!next())
   * 	     return false;
   *     } while (target > term());
   *     return true;
   *   }
   * </pre>
   * Some implementations are considerably more efficient than that.
   */
  public boolean skipTo(Term target) throws IOException {
     do {
        if (!next())
  	        return false;
     } while (target.compareTo(term()) > 0);
     return true;
  }
}
