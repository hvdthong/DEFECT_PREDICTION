package org.apache.xalan.xsltc.runtime;

import org.apache.xalan.xsltc.*;

public final class StringValueHandler extends TransletOutputBase {

    private char[] _buffer = new char[32];
    private int _free = 0;
	
    public void characters(char[] ch, int off, int len) 
	throws TransletException 
    {
	if (_free + len >= _buffer.length) {
	    char[] newBuffer = new char[_free + len + 32];
	    System.arraycopy(_buffer, 0, newBuffer, 0, _free);
	    _buffer = newBuffer;
	}
	System.arraycopy(ch, off, _buffer, _free, len);
	_free += len;
    }

    public String getValue() {
	final int length = _free;
	return new String(_buffer, 0, length);
    }

    public void characters(String characters) throws TransletException {
	characters(characters.toCharArray(), 0, characters.length());
    }

    /**
     * The value of a PI must not contain the substring "?>". Should
     * that substring be present, replace it by "? >". 
     */
    public String getValueOfPI() {
	final String value = getValue();

	if (value.indexOf("?>") > 0) {
	    final int n = value.length(); 
	    final StringBuffer valueOfPI = new StringBuffer();

	    for (int i = 0; i < n;) {
		final char ch = value.charAt(i++);
		if (ch == '?' && value.charAt(i) == '>') {
		    valueOfPI.append("? >"); i++;
		}
		else {
		    valueOfPI.append(ch);
		}
	    } 
	    return valueOfPI.toString();
	}
	return value;
    }
}
