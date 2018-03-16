package org.apache.synapse.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.impl.EmptyOMLocation;
import org.apache.axiom.om.impl.llom.util.NamespaceContextImpl;
import org.apache.commons.io.IOUtils;

/**
 * {@link javax.xml.stream.XMLStreamException XMLInputStreamReader} implementation that
 * represents a text node wrapped inside an element. The text data is provided by a
 * {@link java.io.Reader Reader}.
 * <p>
 * It will produce the following sequence of XML events:
 * <ul>
 *   <li>START_DOCUMENT</li>
 *   <li>START_ELEMENT</li>
 *   <li>(CHARACTER)*</li>
 *   <li>END_ELEMENT</li>
 *   <li>END_DOCMENT</li>
 * </ul>
 * The class is implemented as a simple state machine, where the state is identified
 * by the current event type. The initial state is <tt>START_DOCUMENT</tt> and the
 * following transitions are triggered by {@link #next()}:
 * <ul>
 *   <li>START_DOCUMENT &rarr; START_ELEMENT</li>
 *   <li>START_ELEMENT &rarr; END_ELEMENT (if character stream is empty)</li>
 *   <li>START_ELEMENT &rarr; CHARACTERS (if character stream is not empty)</li>
 *   <li>CHARACTERS &rarr; CHARACTERS (if data available in stream)</li>
 *   <li>CHARACTERS &rarr; END_ELEMENT (if end of stream reached)</li>
 *   <li>END_ELEMENT &rarr; END_DOCUMENT</li>
 * </ul>
 * Additionally, {@link #getElementText()} triggers the following transition:
 * <ul>
 *   <li>START_ELEMENT &rarr; END_ELEMENT</li>
 * </ul>
 * Note that since multiple consecutive CHARACTERS events may be returned, this
 * "parser" is not coalescing.
 * 
 */
public class WrappedTextNodeStreamReader implements XMLStreamReader {
    /**
     * Location object returned by {@link #getLocation()}.
     * It always returns -1 for the location and null for the publicId and systemId. 
     */
    private final static Location EMPTY_LOCATION = new EmptyOMLocation();
    
    /**
     * The qualified name of the wrapper element.
     */
    private final QName wrapperElementName;
    
    /**
     * The Reader object that represents the text data.
     */
    private final Reader reader;
    
    /**
     * The maximum number of characters to return for each CHARACTER event.
     */
    private final int chunkSize;
    
    /**
     * The type of the current XML event.
     */
    private int eventType = START_DOCUMENT;
    
    /**
     * The character data for the current event. This is only set if the current
     * event is a CHARACTER event. The size of the array is determined by
     * {@link #chunkSize}
     */
    private char[] charData;
    
    /**
     * The length of the character data in {@link #charData}.
     */
    private int charDataLength;
    
    /**
     * The namespace context applicable in the scope of the wrapper element.
     * Beside the default mappings for xml and xmlns, it only contains the
     * mapping for the namespace of the wrapper element.
     * This attribute is initialized lazily by {@link #getNamespaceContext()}.
     */
    private NamespaceContext namespaceContext;
    
    /**
     * Create a new instance.
     * 
     * @param wrapperElementName the qualified name of the wrapper element
     * @param reader the Reader object holding the character data to be wrapped
     * @param chunkSize the maximum number of characters that are returned for each CHARACTER event
     */
    public WrappedTextNodeStreamReader(QName wrapperElementName, Reader reader, int chunkSize) {
        this.wrapperElementName = wrapperElementName;
        this.reader = reader;
        this.chunkSize = chunkSize;
    }
    
    /**
     * Create a new instance with chunk size 4096.
     * 
     * @param wrapperElementName the qualified name of the wrapper element
     * @param reader the Reader object holding the character data to be wrapped
     */
    public WrappedTextNodeStreamReader(QName wrapperElementName, Reader reader) {
        this(wrapperElementName, reader, 4096);
    }

    public Object getProperty(String name) throws IllegalArgumentException {
        return null;
    }
    
    
    public boolean hasNext() throws XMLStreamException {
        return eventType != END_DOCUMENT;
    }
    
    public int next() throws XMLStreamException {
        switch (eventType) {
            case START_DOCUMENT:
                eventType = START_ELEMENT;
                break;
            case START_ELEMENT:
                charData = new char[chunkSize];
            case CHARACTERS:
                try {
                    charDataLength = reader.read(charData);
                }
                catch (IOException ex) {
                    throw new XMLStreamException(ex);
                }
                if (charDataLength == -1) {
                    charData = null;
                    eventType = END_ELEMENT;
                } else {
                    eventType = CHARACTERS;
                }
                break;
            case END_ELEMENT:
                eventType = END_DOCUMENT;
                break;
            default:
                throw new IllegalStateException();
        }
        return eventType;
    }
    
    public int nextTag() throws XMLStreamException {
        throw new XMLStreamException("Current event is not white space");
    }

    public int getEventType() {
        return eventType;
    }

    public boolean isStartElement() { return eventType == START_ELEMENT; }
    public boolean isEndElement() { return eventType == END_ELEMENT; }
    public boolean isCharacters() { return eventType == CHARACTERS; }
    public boolean isWhiteSpace() { return false; }
    public boolean hasText() { return eventType == CHARACTERS; }
    public boolean hasName() { return eventType == START_ELEMENT || eventType == END_ELEMENT; }
    
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        if (type != eventType
             || (namespaceURI != null && !namespaceURI.equals(getNamespaceURI()))
             || (localName != null && !namespaceURI.equals(getLocalName()))) {
            throw new XMLStreamException("Unexpected event type");
        }
    }
    
    public Location getLocation() {
        return EMPTY_LOCATION;
    }
    
    public void close() throws XMLStreamException {
        try {
            reader.close();
        }
        catch (IOException ex) {
            throw new XMLStreamException(ex);
        }
    }

    
    public String getEncoding() {
        return null;
    }

    public String getCharacterEncodingScheme() {
        return null;
    }

    public String getVersion() {
        return null;
    }

    public boolean standaloneSet() {
        return false;
    }

    public boolean isStandalone() {
        return true;
    }
    
    
    public NamespaceContext getNamespaceContext() {
        if (namespaceContext == null) {
            namespaceContext = new NamespaceContextImpl(Collections.singletonMap(wrapperElementName.getPrefix(), wrapperElementName.getNamespaceURI()));
        }
        return namespaceContext;
    }
    
    public String getNamespaceURI(String prefix) {
        String namespaceURI = getNamespaceContext().getNamespaceURI(prefix);
        return namespaceURI.equals(XMLConstants.NULL_NS_URI) ? null : prefix;
    }
    
    
    private void checkStartElement() {
        if (eventType != START_ELEMENT) {
            throw new IllegalStateException();
        }
    }
    
    public String getAttributeValue(String namespaceURI, String localName) {
        checkStartElement();
        return null;
    }

    public int getAttributeCount() {
        checkStartElement();
        return 0;
    }
    
    public QName getAttributeName(int index) {
        checkStartElement();
        throw new ArrayIndexOutOfBoundsException();
    }

    public String getAttributeLocalName(int index) {
        checkStartElement();
        throw new ArrayIndexOutOfBoundsException();
    }

    public String getAttributePrefix(int index) {
        checkStartElement();
        throw new ArrayIndexOutOfBoundsException();
    }

    public String getAttributeNamespace(int index) {
        checkStartElement();
        throw new ArrayIndexOutOfBoundsException();
    }

    public String getAttributeType(int index) {
        checkStartElement();
        throw new ArrayIndexOutOfBoundsException();
    }

    public String getAttributeValue(int index) {
        checkStartElement();
        throw new ArrayIndexOutOfBoundsException();
    }

    public boolean isAttributeSpecified(int index) {
        checkStartElement();
        throw new ArrayIndexOutOfBoundsException();
    }
    
    private void checkElement() {
        if (eventType != START_ELEMENT && eventType != END_ELEMENT) {
            throw new IllegalStateException();
        }
    }
    
    public QName getName() {
        return null;
    }

    public String getLocalName() {
        checkElement();
        return wrapperElementName.getLocalPart();
    }

    public String getPrefix() {
        return wrapperElementName.getPrefix();
    }

    public String getNamespaceURI() {
        checkElement();
        return wrapperElementName.getNamespaceURI();
    }
    
    public int getNamespaceCount() {
        checkElement();
        return 1;
    }

    public String getNamespacePrefix(int index) {
        checkElement();
        if (index == 0) {
            return wrapperElementName.getPrefix();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public String getNamespaceURI(int index) {
        checkElement();
        if (index == 0) {
            return wrapperElementName.getNamespaceURI();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
    
    public String getElementText() throws XMLStreamException {
        if (eventType == START_ELEMENT) {
            try {
                String result = IOUtils.toString(reader);
                eventType = END_ELEMENT;
                return result;
            }
            catch (IOException ex) {
                throw new XMLStreamException(ex);
            }
        } else {
            throw new XMLStreamException("Current event is not a START_ELEMENT");
        }
    }

    private void checkCharacters() {
        if (eventType != CHARACTERS) {
            throw new IllegalStateException();
        }
    }
    
    public String getText() {
        checkCharacters();
        return new String(charData, 0, charDataLength);
    }

    public char[] getTextCharacters() {
        checkCharacters();
        return charData;
    }

    public int getTextStart() {
        checkCharacters();
        return 0;
    }

    public int getTextLength() {
        checkCharacters();
        return charDataLength;
    }

    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        checkCharacters();
        int c = Math.min(charDataLength-sourceStart, length);
        System.arraycopy(charData, sourceStart, target, targetStart, c);
        return c;
    }
    
    
    public String getPIData() {
        throw new IllegalStateException();
    }

    public String getPITarget() {
        throw new IllegalStateException();
    }
}
