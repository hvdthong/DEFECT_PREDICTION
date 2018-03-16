import java.io.IOException;

/**
 * Removes words that are too long and too short from the stream.
 *
 * @author David Spencer
 * @version $Id: LengthFilter.java 472959 2006-11-09 16:21:50Z yonik $
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
   * Returns the next input Token whose termText() is the right len
   */
  public final Token next() throws IOException
  {
    for (Token token = input.next(); token != null; token = input.next())
    {
      int len = token.termText().length();
      if (len >= min && len <= max) {
          return token;
      }
    }
    return null;
  }
}
