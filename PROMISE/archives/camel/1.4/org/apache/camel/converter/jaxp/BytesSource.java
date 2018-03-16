package org.apache.camel.converter.jaxp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.transform.stream.StreamSource;

/**
 * A helper class which provides a JAXP {@link javax.xml.transform.Source Source} from a byte[]
 * which can be read as many times as required.
 *
 * @version $Revision: 642753 $
 */
public class BytesSource extends StreamSource {
    private byte[] data;

    public BytesSource(byte[] data) {
        this.data = data;
    }

    public BytesSource(byte[] data, String systemId) {
        this.data = data;
        setSystemId(systemId);
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    public Reader getReader() {
        return new InputStreamReader(getInputStream());
    }

    public byte[] getData() {
        return data;
    }

    public String toString() {
        return "BytesSource[" + new String(data) + "]";
    }

}
