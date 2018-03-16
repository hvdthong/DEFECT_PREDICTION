import java.io.IOException;

/**
 * Normalizes token text to lower case.
 *
 * @version $Id: LowerCaseFilter.java 472959 2006-11-09 16:21:50Z yonik $
 */
public final class LowerCaseFilter extends TokenFilter {
  public LowerCaseFilter(TokenStream in) {
    super(in);
  }

  public final Token next() throws IOException {
    Token t = input.next();

    if (t == null)
      return null;

    t.termText = t.termText.toLowerCase();

    return t;
  }
}
