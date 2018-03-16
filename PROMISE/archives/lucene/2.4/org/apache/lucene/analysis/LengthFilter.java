import java.io.IOException;

/**
 * Removes words that are too long and too short from the stream.
 *
 *
 * @version $Id: LengthFilter.java 687357 2008-08-20 14:38:07Z mikemccand $
 */
public final class LengthFilter extends TokenFilter {

  final int min;
  final int max;

  /**
   * Build a filter that removes words that are too long or too
   * short from the text.
   */
  public LengthFilter(TokenStream in, int min, int max)
  {
    super(in);
    this.min = min;
    this.max = max;
  }

  /**
   * Returns the next input Token whose term() is the right len
   */
  public final Token next(final Token reusableToken) throws IOException
  {
    assert reusableToken != null;
    for (Token nextToken = input.next(reusableToken); nextToken != null; nextToken = input.next(reusableToken))
    {
      int len = nextToken.termLength();
      if (len >= min && len <= max) {
          return nextToken;
      }
    }
    return null;
  }
}
