import java.io.Reader;
import java.io.IOException;

/** A Tokenizer is a TokenStream whose input is a Reader.
  <p>
  This is an abstract class.
  <p>
  NOTE: subclasses must override {@link #next(Token)}.  It's
  also OK to instead override {@link #next()} but that
  method is now deprecated in favor of {@link #next(Token)}.
  <p>
  NOTE: subclasses overriding {@link #next(Token)} must  
  call {@link Token#clear()}.
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

  /** Expert: Reset the tokenizer to a new reader.  Typically, an
   *  analyzer (in its reusableTokenStream method) will use
   *  this to re-use a previously created tokenizer. */
  public void reset(Reader input) throws IOException {
    this.input = input;
  }
}

