import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/** Filters LetterTokenizer with LowerCaseFilter and StopFilter. */

public final class StopAnalyzer extends Analyzer {
  private Set stopWords;

  /** An array containing some common English words that are not usually useful
    for searching. */
  public static final String[] ENGLISH_STOP_WORDS = {
    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "if", "in", "into", "is", "it",
    "no", "not", "of", "on", "or", "s", "such",
    "t", "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with"
  };

  /** Builds an analyzer which removes words in ENGLISH_STOP_WORDS. */
  public StopAnalyzer() {
    stopWords = StopFilter.makeStopSet(ENGLISH_STOP_WORDS);
  }

  /** Builds an analyzer with the stop words from the given set.
   */
  public StopAnalyzer(Set stopWords) {
    this.stopWords = stopWords;
  }

  /** Builds an analyzer which removes words in the provided array. */
  public StopAnalyzer(String[] stopWords) {
    this.stopWords = StopFilter.makeStopSet(stopWords);
  }
  
  /** Builds an analyzer with the stop words from the given file.
   * @see WordlistLoader#getWordSet(File)
   */
  public StopAnalyzer(File stopwordsFile) throws IOException {
    stopWords = WordlistLoader.getWordSet(stopwordsFile);
  }

  /** Builds an analyzer with the stop words from the given reader.
   * @see WordlistLoader#getWordSet(Reader)
   */
  public StopAnalyzer(Reader stopwords) throws IOException {
    stopWords = WordlistLoader.getWordSet(stopwords);
  }

  /** Filters LowerCaseTokenizer with StopFilter. */
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new StopFilter(new LowerCaseTokenizer(reader), stopWords);
  }
}

