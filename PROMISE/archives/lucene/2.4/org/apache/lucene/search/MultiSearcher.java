import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Implements search over a set of <code>Searchables</code>.
 *
 * <p>Applications usually need only call the inherited {@link #search(Query)}
 * or {@link #search(Query,Filter)} methods.
 */
public class MultiSearcher extends Searcher {
    /**
     * Document Frequency cache acting as a Dummy-Searcher.
     * This class is no full-fledged Searcher, but only supports
     * the methods necessary to initialize Weights.
     */
  private static class CachedDfSource extends Searcher {

    public CachedDfSource(Map dfMap, int maxDoc, Similarity similarity) {
      this.dfMap = dfMap;
      this.maxDoc = maxDoc;
      setSimilarity(similarity);
    }

    public int docFreq(Term term) {
      int df;
      try {
        df = ((Integer) dfMap.get(term)).intValue();
      } catch (NullPointerException e) {
        throw new IllegalArgumentException("df for term " + term.text()
            + " not available");
      }
      return df;
    }

    public int[] docFreqs(Term[] terms) {
      int[] result = new int[terms.length];
      for (int i = 0; i < terms.length; i++) {
        result[i] = docFreq(terms[i]);
      }
      return result;
    }

    public int maxDoc() {
      return maxDoc;
    }

    public Query rewrite(Query query) {
      return query;
    }

    public void close() {
      throw new UnsupportedOperationException();
    }

    public Document doc(int i) {
      throw new UnsupportedOperationException();
    }
    
    public Document doc(int i, FieldSelector fieldSelector) {
        throw new UnsupportedOperationException();
    }

    public Explanation explain(Weight weight,int doc) {
      throw new UnsupportedOperationException();
    }

    public void search(Weight weight, Filter filter, HitCollector results) {
      throw new UnsupportedOperationException();
    }

    public TopDocs search(Weight weight,Filter filter,int n) {
      throw new UnsupportedOperationException();
    }

    public TopFieldDocs search(Weight weight,Filter filter,int n,Sort sort) {
      throw new UnsupportedOperationException();
    }
  }


  private Searchable[] searchables;
  private int[] starts;
  private int maxDoc = 0;

  /** Creates a searcher which searches <i>searchables</i>. */
  public MultiSearcher(Searchable[] searchables) throws IOException {
    this.searchables = searchables;

    for (int i = 0; i < searchables.length; i++) {
      starts[i] = maxDoc;
    }
    starts[searchables.length] = maxDoc;
  }
  
  /** Return the array of {@link Searchable}s this searches. */
  public Searchable[] getSearchables() {
    return searchables;
  }

  protected int[] getStarts() {
  	return starts;
  }

  public void close() throws IOException {
    for (int i = 0; i < searchables.length; i++)
      searchables[i].close();
  }

  public int docFreq(Term term) throws IOException {
    int docFreq = 0;
    for (int i = 0; i < searchables.length; i++)
      docFreq += searchables[i].docFreq(term);
    return docFreq;
  }

  public Document doc(int n) throws CorruptIndexException, IOException {
  }

  public Document doc(int n, FieldSelector fieldSelector) throws CorruptIndexException, IOException {
  }
  
  /** Returns index of the searcher for document <code>n</code> in the array
   * used to construct this searcher. */
    while (hi >= lo) {
      int mid = (lo + hi) >> 1;
      int midValue = starts[mid];
      if (n < midValue)
	hi = mid - 1;
      else if (n > midValue)
	lo = mid + 1;
        while (mid+1 < searchables.length && starts[mid+1] == midValue) {
        }
	return mid;
      }
    }
    return hi;
  }

  /** Returns the document number of document <code>n</code> within its
   * sub-index. */
  public int subDoc(int n) {
    return n - starts[subSearcher(n)];
  }

  public int maxDoc() throws IOException {
    return maxDoc;
  }

  public TopDocs search(Weight weight, Filter filter, int nDocs)
  throws IOException {

    HitQueue hq = new HitQueue(nDocs);
    int totalHits = 0;

      TopDocs docs = searchables[i].search(weight, filter, nDocs);
      ScoreDoc[] scoreDocs = docs.scoreDocs;
	ScoreDoc scoreDoc = scoreDocs[j];
        if(!hq.insert(scoreDoc))
      }
    }

    ScoreDoc[] scoreDocs = new ScoreDoc[hq.size()];
      scoreDocs[i] = (ScoreDoc)hq.pop();
    
    float maxScore = (totalHits==0) ? Float.NEGATIVE_INFINITY : scoreDocs[0].score;
    
    return new TopDocs(totalHits, scoreDocs, maxScore);
  }

  public TopFieldDocs search (Weight weight, Filter filter, int n, Sort sort)
  throws IOException {
    FieldDocSortedHitQueue hq = null;
    int totalHits = 0;

    float maxScore=Float.NEGATIVE_INFINITY;
    
      TopFieldDocs docs = searchables[i].search (weight, filter, n, sort);
      
      if (hq == null) hq = new FieldDocSortedHitQueue (docs.fields, n);
      maxScore = Math.max(maxScore, docs.getMaxScore());
      ScoreDoc[] scoreDocs = docs.scoreDocs;
        ScoreDoc scoreDoc = scoreDocs[j];
        if (!hq.insert (scoreDoc))
      }
    }

    ScoreDoc[] scoreDocs = new ScoreDoc[hq.size()];
      scoreDocs[i] = (ScoreDoc) hq.pop();

    return new TopFieldDocs (totalHits, scoreDocs, hq.getFields(), maxScore);
  }


  public void search(Weight weight, Filter filter, final HitCollector results)
    throws IOException {
    for (int i = 0; i < searchables.length; i++) {

      final int start = starts[i];

      searchables[i].search(weight, filter, new HitCollector() {
	  public void collect(int doc, float score) {
	    results.collect(doc + start, score);
	  }
	});

    }
  }

  public Query rewrite(Query original) throws IOException {
    Query[] queries = new Query[searchables.length];
    for (int i = 0; i < searchables.length; i++) {
      queries[i] = searchables[i].rewrite(original);
    }
    return queries[0].combine(queries);
  }

  public Explanation explain(Weight weight, int doc) throws IOException {
  }

  /**
   * Create weight in multiple index scenario.
   * 
   * Distributed query processing is done in the following steps:
   * 1. rewrite query
   * 2. extract necessary terms
   * 3. collect dfs for these terms from the Searchables
   * 4. create query weight using aggregate dfs.
   * 5. distribute that weight to Searchables
   * 6. merge results
   *
   * Steps 1-4 are done here, 5+6 in the search() methods
   *
   * @return rewritten queries
   */
  protected Weight createWeight(Query original) throws IOException {
    Query rewrittenQuery = rewrite(original);

    Set terms = new HashSet();
    rewrittenQuery.extractTerms(terms);

    Term[] allTermsArray = new Term[terms.size()];
    terms.toArray(allTermsArray);
    int[] aggregatedDfs = new int[terms.size()];
    for (int i = 0; i < searchables.length; i++) {
      int[] dfs = searchables[i].docFreqs(allTermsArray);
      for(int j=0; j<aggregatedDfs.length; j++){
        aggregatedDfs[j] += dfs[j];
      }
    }

    HashMap dfMap = new HashMap();
    for(int i=0; i<allTermsArray.length; i++) {
      dfMap.put(allTermsArray[i], new Integer(aggregatedDfs[i]));
    }

    int numDocs = maxDoc();
    CachedDfSource cacheSim = new CachedDfSource(dfMap, numDocs, getSimilarity());

    return rewrittenQuery.weight(cacheSim);
  }

}
