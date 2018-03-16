import org.apache.lucene.index.IndexReader;
import java.io.IOException;
import java.io.Serializable;

/**
 * Expert: returns a comparator for sorting ScoreDocs.
 *
 * <p>Created: Apr 21, 2004 3:49:28 PM
 * 
 *
 * @version $Id: SortComparatorSource.java 564236 2007-08-09 15:21:19Z gsingers $
 * @since   1.4
 */
public interface SortComparatorSource
extends Serializable {

  /**
   * Creates a comparator for the field in the given index.
   * @param reader Index to create comparator for.
   * @param fieldname  Name of the field to create comparator for.
   * @return Comparator of ScoreDoc objects.
   * @throws IOException If an error occurs reading the index.
   */
  ScoreDocComparator newComparator (IndexReader reader, String fieldname)
  throws IOException;
}
