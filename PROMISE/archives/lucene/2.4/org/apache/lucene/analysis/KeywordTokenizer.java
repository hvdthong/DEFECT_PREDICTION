import java.io.IOException;
import java.io.Reader;

/**
 * Emits the entire input as a single token.
 */
public class KeywordTokenizer extends Tokenizer {
  
  private static final int DEFAULT_BUFFER_SIZE = 256;

  private boolean done;

  public KeywordTokenizer(Reader input) {
    this(input, DEFAULT_BUFFER_SIZE);
  }

  public KeywordTokenizer(Reader input, int bufferSize) {
    super(input);
    this.done = false;
  }

  public Token next(final Token reusableToken) throws IOException {
    assert reusableToken != null;
    if (!done) {
      done = true;
      int upto = 0;
      reusableToken.clear();
      char[] buffer = reusableToken.termBuffer();
      while (true) {
        final int length = input.read(buffer, upto, buffer.length-upto);
        if (length == -1) break;
        upto += length;
        if (upto == buffer.length)
          buffer = reusableToken.resizeTermBuffer(1+buffer.length);
      }
      reusableToken.setTermLength(upto);
      return reusableToken;
    }
    return null;
  }

  public void reset(Reader input) throws IOException {
    super.reset(input);
    this.done = false;
  }
}
