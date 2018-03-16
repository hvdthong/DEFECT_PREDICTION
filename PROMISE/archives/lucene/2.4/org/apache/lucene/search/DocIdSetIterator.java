import java.io.IOException;

/**
 * This abstract class defines methods to iterate over a set of
 * non-decreasing doc ids.
 */
public abstract class DocIdSetIterator {
    /** Returns the current document number.  <p> This is invalid until {@link
    #next()} is called for the first time.*/
    public abstract int doc();
    
    /** Moves to the next docId in the set. Returns true, iff
     * there is such a docId. */
    public abstract boolean next() throws IOException;
    
    /** Skips entries to the first beyond the current whose document number is
     * greater than or equal to <i>target</i>. <p>Returns true iff there is such
     * an entry.  <p>Behaves as if written: <pre>
     *   boolean skipTo(int target) {
     *     do {
     *       if (!next())
     *         return false;
     *     } while (target > doc());
     *     return true;
     *   }
     * </pre>
     * Some implementations are considerably more efficient than that.
     */
    public abstract boolean skipTo(int target) throws IOException;
}
