import java.io.Reader;

/** A WhitespaceTokenizer is a tokenizer that divides text at whitespace.
 * Adjacent sequences of non-Whitespace characters form tokens. */

public class WhitespaceTokenizer extends CharTokenizer {
  /** Construct a new WhitespaceTokenizer. */
  public WhitespaceTokenizer(Reader in) {
    super(in);
  }

  /** Collects only characters which do not satisfy
   * {@link Character#isWhitespace(char)}.*/
  protected boolean isTokenChar(char c) {
    return !Character.isWhitespace(c);
  }
}
