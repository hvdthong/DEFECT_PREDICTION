import java.io.IOException;

/** A TokenFilter is a TokenStream whose input is another token stream.
  <p>
  This is an abstract class.
  NOTE: subclasses must override {@link #next(Token)}.  It's
  also OK to instead override {@link #next()} but that
  method is now deprecated in favor of {@link #next(Token)}.
  */
public abstract class TokenFilter extends TokenStream {
  /** The source of tokens for this filter. */
  protected TokenStream input;

  /** Construct a token stream filtering the given input. */
  protected TokenFilter(TokenStream input) {
    this.input = input;
  }

  /** Close the input TokenStream. */
  public void close() throws IOException {
    input.close();
  }

  /** Reset the filter as well as the input TokenStream. */
  public void reset() throws IOException {
    super.reset();
    input.reset();
  }
}
