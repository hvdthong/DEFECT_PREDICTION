import java.io.Reader;

/** An Analyzer that filters LetterTokenizer with LowerCaseFilter. */

public final class SimpleAnalyzer extends Analyzer {
  public TokenStream tokenStream(String fieldName, Reader reader) {
    return new LowerCaseTokenizer(reader);
  }
}
