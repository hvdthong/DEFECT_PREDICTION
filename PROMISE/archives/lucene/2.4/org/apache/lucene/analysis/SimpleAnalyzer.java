import java.io.Reader;
import java.io.IOException;

/** An Analyzer that filters LetterTokenizer with LowerCaseFilter. */

public final class SimpleAnalyzer extends Analyzer {
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new LowerCaseTokenizer(reader);
  }

  public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
    Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream();
    if (tokenizer == null) {
      tokenizer = new LowerCaseTokenizer(reader);
      setPreviousTokenStream(tokenizer);
    } else
      tokenizer.reset(reader);
    return tokenizer;
  }
}
