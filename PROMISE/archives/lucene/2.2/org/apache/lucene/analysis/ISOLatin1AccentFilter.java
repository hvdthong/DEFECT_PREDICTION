public class ISOLatin1AccentFilter extends TokenFilter {
	public ISOLatin1AccentFilter(TokenStream input) {
		super(input);
	}

	public final Token next() throws java.io.IOException {
		final Token t = input.next();
    if (t != null)
      t.setTermText(removeAccents(t.termText()));
    return t;
	}

	/**
	 * To replace accented characters in a String by unaccented equivalents.
	 */
	public final static String removeAccents(String input) {
		final StringBuffer output = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			switch (input.charAt(i)) {
					output.append("A");
					break;
					output.append("AE");
					break;
					output.append("C");
					break;
					output.append("E");
					break;
					output.append("I");
					break;
					output.append("D");
					break;
					output.append("N");
					break;
					output.append("O");
					break;
					output.append("OE");
					break;
					output.append("TH");
					break;
					output.append("U");
					break;
					output.append("Y");
					break;
					output.append("a");
					break;
					output.append("ae");
					break;
					output.append("c");
					break;
					output.append("e");
					break;
					output.append("i");
					break;
					output.append("d");
					break;
					output.append("n");
					break;
					output.append("o");
					break;
					output.append("oe");
					break;
					output.append("ss");
					break;
					output.append("th");
					break;
					output.append("u");
					break;
					output.append("y");
					break;
				default :
					output.append(input.charAt(i));
					break;
			}
		}
		return output.toString();
	}
}
