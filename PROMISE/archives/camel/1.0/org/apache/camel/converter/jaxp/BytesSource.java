package org.apache.camel.converter.jaxp;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * A helper class which provides a JAXP {@link Source} from a byte[]
 * which can be read as many times as required.
 *
 * @version $Revision: 523731 $
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
