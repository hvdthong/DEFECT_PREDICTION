import java.io.IOException;
import java.util.Vector;
import java.util.Iterator;

import org.apache.lucene.document.Document;

/** A ranked list of documents, used to hold search results. */
public final class Hits {
  private Weight weight;
  private Searcher searcher;
  private Filter filter = null;
  private Sort sort = null;



  Hits(Searcher s, Query q, Filter f) throws IOException {
    weight = q.weight(s);
    searcher = s;
    filter = f;
  }

  Hits(Searcher s, Query q, Filter f, Sort o) throws IOException {
    weight = q.weight(s);
    searcher = s;
    filter = f;
    sort = o;
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

    int end = scoreDocs.length < length ? scoreDocs.length : length;
    for (int i = hitDocs.size(); i < end; i++) {
      hitDocs.addElement(new HitDoc(scoreDocs[i].score * scoreNorm,
                                    scoreDocs[i].doc));
    }
  }

  /** Returns the total number of hits available in this set. */
  public final int length() {
    return length;
  }

  /** Returns the stored fields of the n<sup>th</sup> document in this set.
   <p>Documents are cached, so that repeated requests for the same element may
   return the same Document object. */
  public final Document doc(int n) throws IOException {
    HitDoc hitDoc = hitDoc(n);

      HitDoc oldLast = last;
    }

    if (hitDoc.doc == null) {
    }

    return hitDoc.doc;
  }

  /** Returns the score for the nth document in this set. */
  public final float score(int n) throws IOException {
    return hitDoc(n).score;
  }

  /** Returns the id for the nth document in this set. */
  public final int id(int n) throws IOException {
    return hitDoc(n).id;
  }

  /**
   * Returns a {@link HitIterator} to navigate the Hits.  Each item returned
   * from {@link Iterator#next()} is a {@link Hit}.
   * <p>
   * <b>Caution:</b> Iterate only over the hits needed.  Iterating over all
   * hits is generally not desirable and may be the source of
   * performance issues.
   * </p>
   */
  public Iterator iterator() {
    return new HitIterator(this);
  }

  private final HitDoc hitDoc(int n) throws IOException {
    if (n >= length) {
      throw new IndexOutOfBoundsException("Not a valid hit number: " + n);
    }

    if (n >= hitDocs.size()) {
      getMoreDocs(n);
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
