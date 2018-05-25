package org.apache.xalan.xsltc.runtime;

import org.xml.sax.SAXException;

import org.apache.xml.serializer.EmptySerializer;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
public final class StringValueHandler extends EmptySerializer {

    private StringBuffer _buffer = new StringBuffer();
    private String _str = null;
    private static final String EMPTY_STR = "";
    private boolean m_escaping = false;
    private int _nestedLevel = 0;
	
    public void characters(char[] ch, int off, int len) 
	throws SAXException 
    {
	if (_nestedLevel > 0)
	    return;
	
	if (_str != null) {
	    _buffer.append(_str);
	    _str = null;
	}
	_buffer.append(ch, off, len);
    }

    public String getValue() {
	if (_buffer.length() != 0) {
	    String result = _buffer.toString();
	    _buffer.setLength(0);
	    return result;
	}
	else {
	    String result = _str;
	    _str = null;
	    return (result != null) ? result : EMPTY_STR;
	}
    }

    public void characters(String characters) throws SAXException {
	if (_nestedLevel > 0)
	    return;

	if (_str == null && _buffer.length() == 0) {
	    _str = characters;
	}
	else {
	    if (_str != null) {
	        _buffer.append(_str);
	        _str = null;
	    }
	    
	    _buffer.append(characters);
	}
    }
    
    public void startElement(String qname) throws SAXException {
        _nestedLevel++;
    }

    public void endElement(String qname) throws SAXException {
        _nestedLevel--;
    }

    public boolean setEscaping(boolean bool) {
        boolean oldEscaping = m_escaping;
        m_escaping = bool;

        return bool;
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