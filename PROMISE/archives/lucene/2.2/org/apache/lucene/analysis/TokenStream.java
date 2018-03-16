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

  /** Resets this stream to the beginning. This is an
   *  optional operation, so subclasses may or may not
   *  implement this method. Reset() is not needed for
   *  the standard indexing process. However, if the Tokens 
   *  of a TokenStream are intended to be consumed more than 
   *  once, it is neccessary to implement reset(). 
   */
  public void reset() throws IOException {}
  
  /** Releases resources associated with this stream. */
  public void close() throws IOException {}
}
