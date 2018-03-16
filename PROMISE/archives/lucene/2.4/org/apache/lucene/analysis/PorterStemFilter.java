import java.io.IOException;

/** Transforms the token stream as per the Porter stemming algorithm.
    Note: the input to the stemming filter must already be in lower case,
    so you will need to use LowerCaseFilter or LowerCaseTokenizer farther
    down the Tokenizer chain in order for this to work properly!
    <P>
    To use this filter with other analyzers, you'll want to write an
    Analyzer class that sets up the TokenStream chain as you want it.
    To use this with LowerCaseTokenizer, for example, you'd write an
    analyzer like this:
    <P>
    <PRE>
    class MyAnalyzer extends Analyzer {
      public final TokenStream tokenStream(String fieldName, Reader reader) {
        return new PorterStemFilter(new LowerCaseTokenizer(reader));
      }
    }
    </PRE>
*/
public final class PorterStemFilter extends TokenFilter {
  private PorterStemmer stemmer;

  public PorterStemFilter(TokenStream in) {
    super(in);
    stemmer = new PorterStemmer();
  }

  public final Token next(final Token reusableToken) throws IOException {
    assert reusableToken != null;
    Token nextToken = input.next(reusableToken);
    if (nextToken == null)
      return null;

    if (stemmer.stem(nextToken.termBuffer(), 0, nextToken.termLength()))
      nextToken.setTermBuffer(stemmer.getResultBuffer(), 0, stemmer.getResultLength());
    return nextToken;
  }
}
