import java.io.Reader;

/** An Analyzer that uses WhitespaceTokenizer. */

public final class WhitespaceAnalyzer extends Analyzer {
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new WhitespaceTokenizer(reader);
  }
}
