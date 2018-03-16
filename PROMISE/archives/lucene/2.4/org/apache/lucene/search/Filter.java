import java.util.BitSet;
import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.DocIdBitSet;

/** Abstract base class providing a mechanism to use a subset of an index
 *  for restriction or permission of index search results.
 *  <p>
 *  <b>Note:</b> In Lucene 3.0 {@link #bits(IndexReader)} will be removed
 *  and {@link #getDocIdSet(IndexReader)} will be defined as abstract.
 *  All implementing classes must therefore implement {@link #getDocIdSet(IndexReader)}
 *  in order to work with Lucene 3.0.
 */
public abstract class Filter implements java.io.Serializable {
  /**
   * @return A BitSet with true for documents which should be permitted in
   * search results, and false for those that should not.
   * @deprecated Use {@link #getDocIdSet(IndexReader)} instead.
   */
  public BitSet bits(IndexReader reader) throws IOException {
    return null;
  }
	
  /**
   * @return a DocIdSet that provides the documents which should be
   * permitted or prohibited in search results.
   * @see DocIdBitSet
   */
  public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
    return new DocIdBitSet(bits(reader));
  }
}
