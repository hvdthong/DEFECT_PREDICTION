import java.io.IOException;

/**
 * Normalizes token text to lower case.
 *
 * @version $Id: LowerCaseFilter.java 687357 2008-08-20 14:38:07Z mikemccand $
 */
public final class LowerCaseFilter extends TokenFilter {
  public LowerCaseFilter(TokenStream in) {
    super(in);
  }

  public final Token next(final Token reusableToken) throws IOException {
    assert reusableToken != null;
    Token nextToken = input.next(reusableToken);
    if (nextToken != null) {

      final char[] buffer = nextToken.termBuffer();
      final int length = nextToken.termLength();
      for(int i=0;i<length;i++)
        buffer[i] = Character.toLowerCase(buffer[i]);

      return nextToken;
    } else
      return null;
  }
}
