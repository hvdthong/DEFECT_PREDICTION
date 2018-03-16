package org.apache.camel.util;

/**
 * A little helper class for converting a collection of values to a (usually comma separated) string.
 *
 * @version $Revision: 640438 $
 */
public class CollectionStringBuffer {
    private StringBuffer buffer = new StringBuffer();
    private String separator;
    private boolean first = true;

    public CollectionStringBuffer() {
        this(", ");
    }

    public CollectionStringBuffer(String separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

    public void append(Object value) {
        if (first) {
            first = false;
        } else {
            buffer.append(separator);
        }
        buffer.append(value);
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
