import java.io.IOException;
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
  public TokenStream reusableTokenStream(String fieldName,
                                         final Reader reader) throws IOException {
    Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream();
    if (tokenizer == null) {
      tokenizer = new KeywordTokenizer(reader);
      setPreviousTokenStream(tokenizer);
    } else
      	tokenizer.reset(reader);
    return tokenizer;
  }
}
