public class ISOLatin1AccentFilter extends TokenFilter {
  public ISOLatin1AccentFilter(TokenStream input) {
    super(input);
  }

  private char[] output = new char[256];
  private int outputPos;

  public final Token next(final Token reusableToken) throws java.io.IOException {
    assert reusableToken != null;
    Token nextToken = input.next(reusableToken);
    if (nextToken != null) {
      final char[] buffer = nextToken.termBuffer();
      final int length = nextToken.termLength();
      for(int i=0;i<length;i++) {
        final char c = buffer[i];
        if (c >= '\u00c0' && c <= '\uFB06') {
          removeAccents(buffer, length);
          nextToken.setTermBuffer(output, 0, outputPos);
          break;
        }
      }
      return nextToken;
    } else
      return null;
  }

  /**
   * To replace accented characters in a String by unaccented equivalents.
   */
  public final void removeAccents(char[] input, int length) {

    final int maxSizeNeeded = 2*length;

    int size = output.length;
    while (size < maxSizeNeeded)
      size *= 2;

    if (size != output.length)
      output = new char[size];

    outputPos = 0;

    int pos = 0;

    for (int i=0; i<length; i++, pos++) {
      final char c = input[pos];

      if (c < '\u00c0' || c > '\uFB06')
        output[outputPos++] = c;
      else {
        switch (c) {
          output[outputPos++] = 'A';
          break;
          output[outputPos++] = 'A';
          output[outputPos++] = 'E';
          break;
          output[outputPos++] = 'C';
          break;
          output[outputPos++] = 'E';
          break;
          output[outputPos++] = 'I';
          break;
            output[outputPos++] = 'I';
            output[outputPos++] = 'J';
            break;
          output[outputPos++] = 'D';
          break;
          output[outputPos++] = 'N';
          break;
          output[outputPos++] = 'O';
          break;
          output[outputPos++] = 'O';
          output[outputPos++] = 'E';
          break;
          output[outputPos++] = 'T';
          output[outputPos++] = 'H';
          break;
          output[outputPos++] = 'U';
          break;
          output[outputPos++] = 'Y';
          break;
          output[outputPos++] = 'a';
          break;
          output[outputPos++] = 'a';
          output[outputPos++] = 'e';
          break;
          output[outputPos++] = 'c';
          break;
          output[outputPos++] = 'e';
          break;
          output[outputPos++] = 'i';
          break;
            output[outputPos++] = 'i';
            output[outputPos++] = 'j';
            break;
          output[outputPos++] = 'd';
          break;
          output[outputPos++] = 'n';
          break;
          output[outputPos++] = 'o';
          break;
          output[outputPos++] = 'o';
          output[outputPos++] = 'e';
          break;
          output[outputPos++] = 's';
          output[outputPos++] = 's';
          break;
          output[outputPos++] = 't';
          output[outputPos++] = 'h';
          break;
          output[outputPos++] = 'u';
          break;
          output[outputPos++] = 'y';
          break;
            output[outputPos++] = 'f';
            output[outputPos++] = 'f';
            break;
            output[outputPos++] = 'f';
            output[outputPos++] = 'i';
            break;
            output[outputPos++] = 'f';
            output[outputPos++] = 'l';
            break;
            output[outputPos++] = 'f';
            output[outputPos++] = 't';
            break;
            output[outputPos++] = 's';
            output[outputPos++] = 't';
        	break;
        default :
          output[outputPos++] = c;
          break;
        }
      }
    }
  }
}
