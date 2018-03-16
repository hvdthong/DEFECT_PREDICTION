import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

import java.io.IOException;

/** Subclass of FilteredTermEnum for enumerating all terms that are similiar
 * to the specified filter term.
 *
 * <p>Term enumerations are always ordered by Term.compareTo().  Each term in
 * the enumeration is greater than all that precede it.
 */
public final class FuzzyTermEnum extends FilteredTermEnum {

  /* This should be somewhere around the average long word.
   * If it is longer, we waste time and space. If it is shorter, we waste a
   * little bit of time growing the array as we encounter longer words.
   */
  private static final int TYPICAL_LONGEST_WORD_IN_INDEX = 19;

  /* Allows us save time required to create a new array
   * everytime similarity is called.
   */
  private int[][] d;

  private float similarity;
  private boolean endEnum = false;

  private Term searchTerm = null;
  private final String field;
  private final String text;
  private final String prefix;

  private final float minimumSimilarity;
  private final float scale_factor;
  private final int[] maxDistances = new int[TYPICAL_LONGEST_WORD_IN_INDEX];

  /**
   * Creates a FuzzyTermEnum with an empty prefix and a minSimilarity of 0.5f.
   * <p>
   * After calling the constructor the enumeration is already pointing to the first 
   * valid term if such a term exists. 
   * 
   * @param reader
   * @param term
   * @throws IOException
   * @see #FuzzyTermEnum(IndexReader, Term, float, int)
   */
  public FuzzyTermEnum(IndexReader reader, Term term) throws IOException {
    this(reader, term, FuzzyQuery.defaultMinSimilarity, FuzzyQuery.defaultPrefixLength);
  }
    
  /**
   * Creates a FuzzyTermEnum with an empty prefix.
   * <p>
   * After calling the constructor the enumeration is already pointing to the first 
   * valid term if such a term exists. 
   * 
   * @param reader
   * @param term
   * @param minSimilarity
   * @throws IOException
   * @see #FuzzyTermEnum(IndexReader, Term, float, int)
   */
  public FuzzyTermEnum(IndexReader reader, Term term, float minSimilarity) throws IOException {
    this(reader, term, minSimilarity, FuzzyQuery.defaultPrefixLength);
  }
    
  /**
   * Constructor for enumeration of all terms from specified <code>reader</code> which share a prefix of
   * length <code>prefixLength</code> with <code>term</code> and which have a fuzzy similarity &gt;
   * <code>minSimilarity</code>.
   * <p>
   * After calling the constructor the enumeration is already pointing to the first 
   * valid term if such a term exists. 
   * 
   * @param reader Delivers terms.
   * @param term Pattern term.
   * @param minSimilarity Minimum required similarity for terms from the reader. Default value is 0.5f.
   * @param prefixLength Length of required common prefix. Default value is 0.
   * @throws IOException
   */
  public FuzzyTermEnum(IndexReader reader, Term term, final float minSimilarity, final int prefixLength) throws IOException {
    super();
    
    if (minSimilarity >= 1.0f)
      throw new IllegalArgumentException("minimumSimilarity cannot be greater than or equal to 1");
    else if (minSimilarity < 0.0f)
      throw new IllegalArgumentException("minimumSimilarity cannot be less than 0");
    if(prefixLength < 0)
      throw new IllegalArgumentException("prefixLength cannot be less than 0");

    this.minimumSimilarity = minSimilarity;
    this.scale_factor = 1.0f / (1.0f - minimumSimilarity);
    this.searchTerm = term;
    this.field = searchTerm.field();

    final int fullSearchTermLength = searchTerm.text().length();
    final int realPrefixLength = prefixLength > fullSearchTermLength ? fullSearchTermLength : prefixLength;

    this.text = searchTerm.text().substring(realPrefixLength);
    this.prefix = searchTerm.text().substring(0, realPrefixLength);

    initializeMaxDistances();
    this.d = initDistanceArray();

    setEnum(reader.terms(new Term(searchTerm.field(), prefix)));
  }

  /**
   * The termCompare method in FuzzyTermEnum uses Levenshtein distance to 
   * calculate the distance between the given term and the comparing term. 
   */
  protected final boolean termCompare(Term term) {
    if (field == term.field() && term.text().startsWith(prefix)) {
        final String target = term.text().substring(prefix.length());
        this.similarity = similarity(target);
        return (similarity > minimumSimilarity);
    }
    endEnum = true;
    return false;
  }
  
  public final float difference() {
    return (float)((similarity - minimumSimilarity) * scale_factor);
  }
  
  public final boolean endEnum() {
    return endEnum;
  }
  
  /******************************
   * Compute Levenshtein distance
   ******************************/
  
  /**
   * Finds and returns the smallest of three integers 
   */
  private static final int min(int a, int b, int c) {
    final int t = (a < b) ? a : b;
    return (t < c) ? t : c;
  }

  private final int[][] initDistanceArray(){
    return new int[this.text.length() + 1][TYPICAL_LONGEST_WORD_IN_INDEX];
  }

  /**
   * <p>Similarity returns a number that is 1.0f or less (including negative numbers)
   * based on how similar the Term is compared to a target term.  It returns
   * exactly 0.0f when
   * <pre>
   *    editDistance &lt; maximumEditDistance</pre>
   * Otherwise it returns:
   * <pre>
   *    1 - (editDistance / length)</pre>
   * where length is the length of the shortest term (text or target) including a
   * prefix that are identical and editDistance is the Levenshtein distance for
   * the two words.</p>
   *
   * <p>Embedded within this algorithm is a fail-fast Levenshtein distance
   * algorithm.  The fail-fast algorithm differs from the standard Levenshtein
   * distance algorithm in that it is aborted if it is discovered that the
   * mimimum distance between the words is greater than some threshold.
   *
   * <p>To calculate the maximum distance threshold we use the following formula:
   * <pre>
   *     (1 - minimumSimilarity) * length</pre>
   * where length is the shortest term including any prefix that is not part of the
   * similarity comparision.  This formula was derived by solving for what maximum value
   * of distance returns false for the following statements:
   * <pre>
   *   similarity = 1 - ((float)distance / (float) (prefixLength + Math.min(textlen, targetlen)));
   *   return (similarity > minimumSimilarity);</pre>
   * where distance is the Levenshtein distance for the two words.
   * </p>
   * <p>Levenshtein distance (also known as edit distance) is a measure of similiarity
   * between two strings where the distance is measured as the number of character
   * deletions, insertions or substitutions required to transform one string to
   * the other string.
   * @param target the target word or phrase
   * @return the similarity,  0.0 or less indicates that it matches less than the required
   * threshold and 1.0 indicates that the text and target are identical
   */
  private synchronized final float similarity(final String target) {
    final int m = target.length();
    final int n = text.length();
    if (n == 0)  {
      return prefix.length() == 0 ? 0.0f : 1.0f - ((float) m / prefix.length());
    }
    if (m == 0) {
      return prefix.length() == 0 ? 0.0f : 1.0f - ((float) n / prefix.length());
    }

    final int maxDistance = getMaxDistance(m);

    if (maxDistance < Math.abs(m-n)) {
      return 0.0f;
    }

    if (d[0].length <= m) {
      growDistanceArray(m);
    }

    for (int i = 0; i <= n; i++) d[i][0] = i;
    for (int j = 0; j <= m; j++) d[0][j] = j;
    
    for (int i = 1; i <= n; i++) {
      int bestPossibleEditDistance = m;
      final char s_i = text.charAt(i - 1);
      for (int j = 1; j <= m; j++) {
        if (s_i != target.charAt(j-1)) {
            d[i][j] = min(d[i-1][j], d[i][j-1], d[i-1][j-1])+1;
        }
        else {
          d[i][j] = min(d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1]);
        }
        bestPossibleEditDistance = Math.min(bestPossibleEditDistance, d[i][j]);
      }


        return 0.0f;
      }
    }

    return 1.0f - ((float)d[n][m] / (float) (prefix.length() + Math.min(n, m)));
  }

  /**
   * Grow the second dimension of the array, so that we can calculate the
   * Levenshtein difference.
   */
  private void growDistanceArray(int m) {
    for (int i = 0; i < d.length; i++) {
      d[i] = new int[m+1];
    }
  }

  /**
   * The max Distance is the maximum Levenshtein distance for the text
   * compared to some other value that results in score that is
   * better than the minimum similarity.
   * @param m the length of the "other value"
   * @return the maximum levenshtein distance that we care about
   */
  private final int getMaxDistance(int m) {
    return (m < maxDistances.length) ? maxDistances[m] : calculateMaxDistance(m);
  }

  private void initializeMaxDistances() {
    for (int i = 0; i < maxDistances.length; i++) {
      maxDistances[i] = calculateMaxDistance(i);
    }
  }
  
  private int calculateMaxDistance(int m) {
    return (int) ((1-minimumSimilarity) * (Math.min(text.length(), m) + prefix.length()));
  }

  public void close() throws IOException {
  }
  
}
