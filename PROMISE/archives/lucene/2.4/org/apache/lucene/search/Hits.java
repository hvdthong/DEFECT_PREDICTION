import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Vector;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;

/** A ranked list of documents, used to hold search results.
 * <p>
 * <b>Caution:</b> Iterate only over the hits needed.  Iterating over all
 * hits is generally not desirable and may be the source of
 * performance issues. If you need to iterate over many or all hits, consider
 * using the search method that takes a {@link HitCollector}.
 * </p>
 * <p><b>Note:</b> Deleting matching documents concurrently with traversing 
 * the hits, might, when deleting hits that were not yet retrieved, decrease
 * {@link #length()}. In such case, 
 * {@link java.util.ConcurrentModificationException ConcurrentModificationException}
 * is thrown when accessing hit <code>n</code> &ge; current_{@link #length()} 
 * (but <code>n</code> &lt; {@link #length()}_at_start). 
 * 
 * @deprecated Hits will be removed in Lucene 3.0. <p>
 * Instead e. g. {@link TopDocCollector} and {@link TopDocs} can be used:<br>
 * <pre>
 *   TopDocCollector collector = new TopDocCollector(hitsPerPage);
 *   searcher.search(query, collector);
 *   ScoreDoc[] hits = collector.topDocs().scoreDocs;
 *   for (int i = 0; i < hits.length; i++) {
 *     int docId = hits[i].doc;
 *     Document d = searcher.doc(docId);
 *     ...
 * </pre>
 */
public final class Hits {
  private Weight weight;
  private Searcher searcher;
  private Filter filter = null;
  private Sort sort = null;


  


  Hits(Searcher s, Query q, Filter f) throws IOException {
    weight = q.weight(s);
    searcher = s;
    filter = f;
    nDeletions = countDeletions(s);
    lengthAtStart = length;
  }

  Hits(Searcher s, Query q, Filter f, Sort o) throws IOException {
    weight = q.weight(s);
    searcher = s;
    filter = f;
    sort = o;
    nDeletions = countDeletions(s);
    lengthAtStart = length;
  }

  private int countDeletions(Searcher s) throws IOException {
    int cnt = -1;
    if (s instanceof IndexSearcher) {
      cnt = s.maxDoc() - ((IndexSearcher) s).getIndexReader().numDocs(); 
    } 
    return cnt;
  }

  /**
   * Tries to add new documents to hitDocs.
   * Ensures that the hit numbered <code>min</code> has been retrieved.
   */
  private final void getMoreDocs(int min) throws IOException {
    if (hitDocs.size() > min) {
      min = hitDocs.size();
    }

    TopDocs topDocs = (sort == null) ? searcher.search(weight, filter, n) : searcher.search(weight, filter, n, sort);
    
    length = topDocs.totalHits;
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;

    float scoreNorm = 1.0f;
    
    if (length > 0 && topDocs.getMaxScore() > 1.0f) {
      scoreNorm = 1.0f / topDocs.getMaxScore();
    }

    int start = hitDocs.size() - nDeletedHits;

    int nDels2 = countDeletions(searcher);
    debugCheckedForDeletions = false;
    if (nDeletions < 0 || nDels2 > nDeletions) { 
      nDeletedHits = 0;
      debugCheckedForDeletions = true;
      int i2 = 0;
      for (int i1=0; i1<hitDocs.size() && i2<scoreDocs.length; i1++) {
        int id1 = ((HitDoc)hitDocs.get(i1)).id;
        int id2 = scoreDocs[i2].doc;
        if (id1 == id2) {
          i2++;
        } else {
          nDeletedHits ++;
        }
      }
      start = i2;
    }

    int end = scoreDocs.length < length ? scoreDocs.length : length;
    length += nDeletedHits;
    for (int i = start; i < end; i++) {
      hitDocs.addElement(new HitDoc(scoreDocs[i].score * scoreNorm,
                                    scoreDocs[i].doc));
    }
    
    nDeletions = nDels2;
  }

  /** Returns the total number of hits available in this set. */
  public final int length() {
    return length;
  }

  /** Returns the stored fields of the n<sup>th</sup> document in this set.
   * <p>Documents are cached, so that repeated requests for the same element may
   * return the same Document object.
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  public final Document doc(int n) throws CorruptIndexException, IOException {
    HitDoc hitDoc = hitDoc(n);

      HitDoc oldLast = last;
    }

    if (hitDoc.doc == null) {
    }

    return hitDoc.doc;
  }

  /** Returns the score for the n<sup>th</sup> document in this set. */
  public final float score(int n) throws IOException {
    return hitDoc(n).score;
  }

  /** Returns the id for the n<sup>th</sup> document in this set.
   * Note that ids may change when the index changes, so you cannot
   * rely on the id to be stable.
   */
  public final int id(int n) throws IOException {
    return hitDoc(n).id;
  }

  /**
   * Returns a {@link HitIterator} to navigate the Hits.  Each item returned
   * from {@link Iterator#next()} is a {@link Hit}.
   * <p>
   * <b>Caution:</b> Iterate only over the hits needed.  Iterating over all
   * hits is generally not desirable and may be the source of
   * performance issues. If you need to iterate over many or all hits, consider
   * using a search method that takes a {@link HitCollector}.
   * </p>
   */
  public Iterator iterator() {
    return new HitIterator(this);
  }

  private final HitDoc hitDoc(int n) throws IOException {
    if (n >= lengthAtStart) {
      throw new IndexOutOfBoundsException("Not a valid hit number: " + n);
    }

    if (n >= hitDocs.size()) {
      getMoreDocs(n);
    }

    if (n >= length) {
      throw new ConcurrentModificationException("Not a valid hit number: " + n);
    }
    
    return (HitDoc) hitDocs.elementAt(n);
  }

    if (first == null) {
      last = hitDoc;
    } else {
      first.prev = hitDoc;
    }

    hitDoc.next = first;
    first = hitDoc;
    hitDoc.prev = null;

    numDocs++;
  }

    }

    if (hitDoc.next == null) {
      last = hitDoc.prev;
    } else {
      hitDoc.next.prev = hitDoc.prev;
    }

    if (hitDoc.prev == null) {
      first = hitDoc.next;
    } else {
      hitDoc.prev.next = hitDoc.next;
    }

    numDocs--;
  }
}

final class HitDoc {
  float score;
  int id;
  Document doc = null;


  HitDoc(float s, int i) {
    score = s;
    id = i;
  }
}
