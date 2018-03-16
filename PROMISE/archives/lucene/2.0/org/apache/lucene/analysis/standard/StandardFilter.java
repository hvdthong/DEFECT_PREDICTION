public final class StandardFilter extends TokenFilter
  implements StandardTokenizerConstants  {


  /** Construct filtering <i>in</i>. */
  public StandardFilter(TokenStream in) {
    super(in);
  }

  private static final String APOSTROPHE_TYPE = tokenImage[APOSTROPHE];
  private static final String ACRONYM_TYPE = tokenImage[ACRONYM];
  
  /** Returns the next token in the stream, or null at EOS.
   * <p>Removes <tt>'s</tt> from the end of words.
   * <p>Removes dots from acronyms.
   */
  public final org.apache.lucene.analysis.Token next() throws java.io.IOException {
    org.apache.lucene.analysis.Token t = input.next();

    if (t == null)
      return null;

    String text = t.termText();
    String type = t.type();

	(text.endsWith("'s") || text.endsWith("'S"))) {
      return new org.apache.lucene.analysis.Token
	(text.substring(0,text.length()-2),
	 t.startOffset(), t.endOffset(), type);

      StringBuffer trimmed = new StringBuffer();
      for (int i = 0; i < text.length(); i++) {
	char c = text.charAt(i);
	if (c != '.')
	  trimmed.append(c);
      }
      return new org.apache.lucene.analysis.Token
	(trimmed.toString(), t.startOffset(), t.endOffset(), type);

    } else {
      return t;
    }
  }
}
