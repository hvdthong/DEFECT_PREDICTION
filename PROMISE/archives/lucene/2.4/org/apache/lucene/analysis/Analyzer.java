import java.io.Reader;
import java.io.IOException;

/** An Analyzer builds TokenStreams, which analyze text.  It thus represents a
 *  policy for extracting index terms from text.
 *  <p>
 *  Typical implementations first build a Tokenizer, which breaks the stream of
 *  characters from the Reader into raw Tokens.  One or more TokenFilters may
 *  then be applied to the output of the Tokenizer.
 */
public abstract class Analyzer {
  /** Creates a TokenStream which tokenizes all the text in the provided
   * Reader.  Must be able to handle null field name for backward compatibility.
   */
  public abstract TokenStream tokenStream(String fieldName, Reader reader);

  /** Creates a TokenStream that is allowed to be re-used
   *  from the previous time that the same thread called
   *  this method.  Callers that do not need to use more
   *  than one TokenStream at the same time from this
   *  analyzer should use this method for better
   *  performance.
   */
  public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
    return tokenStream(fieldName, reader);
  }

  private ThreadLocal tokenStreams = new ThreadLocal();

  /** Used by Analyzers that implement reusableTokenStream
   *  to retrieve previously saved TokenStreams for re-use
   *  by the same thread. */
  protected Object getPreviousTokenStream() {
    return tokenStreams.get();
  }

  /** Used by Analyzers that implement reusableTokenStream
   *  to save a TokenStream for later re-use by the same
   *  thread. */
  protected void setPreviousTokenStream(Object obj) {
    tokenStreams.set(obj);
  }


  /**
   * Invoked before indexing a Fieldable instance if
   * terms have already been added to that field.  This allows custom
   * analyzers to place an automatic position increment gap between
   * Fieldable instances using the same field name.  The default value
   * position increment gap is 0.  With a 0 position increment gap and
   * the typical default token position increment of 1, all terms in a field,
   * including across Fieldable instances, are in successive positions, allowing
   * exact PhraseQuery matches, for instance, across Fieldable instance boundaries.
   *
   * @param fieldName Fieldable name being indexed.
   * @return position increment gap, added to the next token emitted from {@link #tokenStream(String,Reader)}
   */
  public int getPositionIncrementGap(String fieldName)
  {
    return 0;
  }
}
