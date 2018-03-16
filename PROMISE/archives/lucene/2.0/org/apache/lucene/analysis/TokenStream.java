import java.io.IOException;

/** A TokenStream enumerates the sequence of tokens, either from
  fields of a document or from query text.
  <p>
  This is an abstract class.  Concrete subclasses are:
  <ul>
  <li>{@link Tokenizer}, a TokenStream
  whose input is a Reader; and
  <li>{@link TokenFilter}, a TokenStream
  whose input is another TokenStream.
  </ul>
  */

public abstract class TokenStream {
  /** Returns the next token in the stream, or null at EOS. */
  public abstract Token next() throws IOException;

  /** Releases resources associated with this stream. */
  public void close() throws IOException {}
}
