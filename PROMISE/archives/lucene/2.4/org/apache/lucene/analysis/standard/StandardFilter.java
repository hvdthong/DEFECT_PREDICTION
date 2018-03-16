import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

/** Normalizes tokens extracted with {@link StandardTokenizer}. */

public final class StandardFilter extends TokenFilter {


  /** Construct filtering <i>in</i>. */
  public StandardFilter(TokenStream in) {
    super(in);
  }

  private static final String APOSTROPHE_TYPE = StandardTokenizerImpl.TOKEN_TYPES[StandardTokenizerImpl.APOSTROPHE];
  private static final String ACRONYM_TYPE = StandardTokenizerImpl.TOKEN_TYPES[StandardTokenizerImpl.ACRONYM];

  /** Returns the next token in the stream, or null at EOS.
   * <p>Removes <tt>'s</tt> from the end of words.
   * <p>Removes dots from acronyms.
   */
  public final Token next(final Token reusableToken) throws java.io.IOException {
    assert reusableToken != null;
    Token nextToken = input.next(reusableToken);

    if (nextToken == null)
      return null;

    char[] buffer = nextToken.termBuffer();
    final int bufferLength = nextToken.termLength();
    final String type = nextToken.type();

	bufferLength >= 2 &&
        buffer[bufferLength-2] == '\'' &&
        (buffer[bufferLength-1] == 's' || buffer[bufferLength-1] == 'S')) {
      nextToken.setTermLength(bufferLength - 2);
      int upto = 0;
      for(int i=0;i<bufferLength;i++) {
        char c = buffer[i];
        if (c != '.')
          buffer[upto++] = c;
      }
      nextToken.setTermLength(upto);
    }

    return nextToken;
  }
}
