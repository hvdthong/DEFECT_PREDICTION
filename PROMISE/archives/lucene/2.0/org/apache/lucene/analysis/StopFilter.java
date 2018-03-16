import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Removes stop words from a token stream.
 */

public final class StopFilter extends TokenFilter {

  private final Set stopWords;
  private final boolean ignoreCase;

    /**
     * Construct a token stream filtering the given input.
     */
    public StopFilter(TokenStream input, String [] stopWords)
    {
        this(input, stopWords, false);
    }

  /**
   * Constructs a filter which removes words from the input
   * TokenStream that are named in the array of words.
   */
  public StopFilter(TokenStream in, String[] stopWords, boolean ignoreCase) {
    super(in);
    this.ignoreCase = ignoreCase;
    this.stopWords = makeStopSet(stopWords, ignoreCase);
  }


    /**
     * Construct a token stream filtering the given input.
     * @param input
     * @param stopWords The set of Stop Words, as Strings.  If ignoreCase is true, all strings should be lower cased
     * @param ignoreCase -Ignore case when stopping.  The stopWords set must be setup to contain only lower case words 
     */
    public StopFilter(TokenStream input, Set stopWords, boolean ignoreCase)
    {
        super(input);
        this.ignoreCase = ignoreCase;
        this.stopWords = stopWords;
    }

  /**
   * Constructs a filter which removes words from the input
   * TokenStream that are named in the Set.
   * It is crucial that an efficient Set implementation is used
   * for maximum performance.
   *
   * @see #makeStopSet(java.lang.String[])
   */
  public StopFilter(TokenStream in, Set stopWords) {
    this(in, stopWords, false);
  }

  /**
   * Builds a Set from an array of stop words,
   * appropriate for passing into the StopFilter constructor.
   * This permits this stopWords construction to be cached once when
   * an Analyzer is constructed.
   * 
   * @see #makeStopSet(java.lang.String[], boolean) passing false to ignoreCase
   */
  public static final Set makeStopSet(String[] stopWords) {
    return makeStopSet(stopWords, false);
  }
    
  /**
   * 
    * @param stopWords
   * @param ignoreCase If true, all words are lower cased first.  
   * @return a Set containing the words
   */    
  public static final Set makeStopSet(String[] stopWords, boolean ignoreCase) {
    HashSet stopTable = new HashSet(stopWords.length);
    for (int i = 0; i < stopWords.length; i++)
      stopTable.add(ignoreCase ? stopWords[i].toLowerCase() : stopWords[i]);
    return stopTable;
  }    

  /**
   * Returns the next input Token whose termText() is not a stop word.
   */
  public final Token next() throws IOException {
    for (Token token = input.next(); token != null; token = input.next())
    {
        String termText = ignoreCase ? token.termText.toLowerCase() : token.termText;
        if (!stopWords.contains(termText))
          return token;
    }
    return null;
  }
}
