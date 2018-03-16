import java.io.Reader;
import java.io.IOException;

/** An Analyzer that uses WhitespaceTokenizer. */

public final class WhitespaceAnalyzer extends Analyzer {
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new WhitespaceTokenizer(reader);
  }

  public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
    Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream();
    if (tokenizer == null) {
      tokenizer = new WhitespaceTokenizer(reader);
      setPreviousTokenStream(tokenizer);
    } else
      tokenizer.reset(reader);
    return tokenizer;
  }
}
