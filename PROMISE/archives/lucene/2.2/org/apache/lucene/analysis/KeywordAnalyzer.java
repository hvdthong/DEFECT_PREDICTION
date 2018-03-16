import java.io.Reader;

/**
 * "Tokenizes" the entire stream as a single token. This is useful
 * for data like zip codes, ids, and some product names.
 */
public class KeywordAnalyzer extends Analyzer {
  public TokenStream tokenStream(String fieldName,
                                 final Reader reader) {
    return new KeywordTokenizer(reader);
  }
}
