import java.io.Reader;

/** An Analyzer builds TokenStreams, which analyze text.  It thus represents a
 *  policy for extracting index terms from text.
 *  <p>
 *  Typical implementations first build a Tokenizer, which breaks the stream of
 *  characters from the Reader into raw Tokens.  One or more TokenFilters may
 *  then be applied to the output of the Tokenizer.
 *  <p>
 *  WARNING: You must override one of the methods defined by this class in your
 *  subclass or the Analyzer will enter an infinite loop.
 */
public abstract class Analyzer {
  /** Creates a TokenStream which tokenizes all the text in the provided
    Reader.  Default implementation forwards to tokenStream(Reader) for 
    compatibility with older version.  Override to allow Analyzer to choose 
    strategy based on document and/or field.  Must be able to handle null
    field name for backward compatibility. */
  public abstract TokenStream tokenStream(String fieldName, Reader reader);


  /**
   * Invoked before indexing a Field instance if
   * terms have already been added to that field.  This allows custom
   * analyzers to place an automatic position increment gap between
   * Field instances using the same field name.  The default value
   * position increment gap is 0.  With a 0 position increment gap and
   * the typical default token position increment of 1, all terms in a field,
   * including across Field instances, are in successive positions, allowing
   * exact PhraseQuery matches, for instance, across Field instance boundaries.
   *
   * @param fieldName Field name being indexed.
   * @return position increment gap, added to the next token emitted from {@link #tokenStream(String,Reader)}
   */
  public int getPositionIncrementGap(String fieldName)
  {
    return 0;
  }
}

