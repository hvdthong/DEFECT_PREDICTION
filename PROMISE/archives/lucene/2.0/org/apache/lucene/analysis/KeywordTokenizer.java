import java.io.IOException;
import java.io.Reader;

/**
 * Emits the entire input as a single token.
 */
public class KeywordTokenizer extends Tokenizer {
  
  private static final int DEFAULT_BUFFER_SIZE = 256;

  private boolean done;
  private final char[] buffer;

  public KeywordTokenizer(Reader input) {
    this(input, DEFAULT_BUFFER_SIZE);
  }

  public KeywordTokenizer(Reader input, int bufferSize) {
    super(input);
    this.buffer = new char[bufferSize];
    this.done = false;
  }

  public Token next() throws IOException {
    if (!done) {
      done = true;
      StringBuffer buffer = new StringBuffer();
      int length;
      while (true) {
        length = input.read(this.buffer);
        if (length == -1) break;

        buffer.append(this.buffer, 0, length);
      }
      String text = buffer.toString();
      return new Token(text, 0, text.length());
    }
    return null;
  }
}
