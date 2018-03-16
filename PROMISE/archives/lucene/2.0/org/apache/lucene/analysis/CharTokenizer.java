import java.io.IOException;
import java.io.Reader;

/** An abstract base class for simple, character-oriented tokenizers.*/
public abstract class CharTokenizer extends Tokenizer {
  public CharTokenizer(Reader input) {
    super(input);
  }

  private int offset = 0, bufferIndex = 0, dataLen = 0;
  private static final int MAX_WORD_LEN = 255;
  private static final int IO_BUFFER_SIZE = 1024;
  private final char[] buffer = new char[MAX_WORD_LEN];
  private final char[] ioBuffer = new char[IO_BUFFER_SIZE];

  /** Returns true iff a character should be included in a token.  This
   * tokenizer generates as tokens adjacent sequences of characters which
   * satisfy this predicate.  Characters for which this is false are used to
   * define token boundaries and are not included in tokens. */
  protected abstract boolean isTokenChar(char c);

  /** Called on each token character to normalize it before it is added to the
   * token.  The default implementation does nothing.  Subclasses may use this
   * to, e.g., lowercase tokens. */
  protected char normalize(char c) {
    return c;
  }

  /** Returns the next token in the stream, or null at EOS. */
  public final Token next() throws IOException {
    int length = 0;
    int start = offset;
    while (true) {
      final char c;

      offset++;
      if (bufferIndex >= dataLen) {
        dataLen = input.read(ioBuffer);
        bufferIndex = 0;
      }
      ;
      if (dataLen == -1) {
        if (length > 0)
          break;
        else
          return null;
      } else
        c = ioBuffer[bufferIndex++];


          start = offset - 1;


          break;


    }

    return new Token(new String(buffer, 0, length), start, start + length);
  }
}
