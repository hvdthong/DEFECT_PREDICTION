import java.io.Reader;
import java.io.IOException;

/** A Tokenizer is a TokenStream whose input is a Reader.
  <p>
  This is an abstract class.
 */

public abstract class Tokenizer extends TokenStream {
  /** The text source for this Tokenizer. */
  protected Reader input;

  /** Construct a tokenizer with null input. */
  protected Tokenizer() {}

  /** Construct a token stream processing the given input. */
  protected Tokenizer(Reader input) {
    this.input = input;
  }

  /** By default, closes the input Reader. */
  public void close() throws IOException {
    input.close();
  }
}

