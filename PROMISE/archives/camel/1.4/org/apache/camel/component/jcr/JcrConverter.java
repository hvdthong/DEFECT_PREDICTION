package org.apache.camel.component.jcr;

import java.io.InputStream;
import java.util.Calendar;

import javax.jcr.Value;

import org.apache.camel.Converter;
import org.apache.jackrabbit.value.BinaryValue;
import org.apache.jackrabbit.value.BooleanValue;
import org.apache.jackrabbit.value.DateValue;
import org.apache.jackrabbit.value.StringValue;

/**
 * A helper class to transform Object into JCR {@link Value} implementations 
 */
@Converter
public class JcrConverter {

    /**
     * Converts a {@link Boolean} into a {@link Value}
     * @param bool the boolean
     * @return the value
     */
    @Converter
    public Value toValue(Boolean bool) {
        return new BooleanValue(bool);
    }

    /**
     * Converts an {@link InputStream} into a {@link Value}
     * @param stream the input stream
     * @return the value
     */
    @Converter
    public Value toValue(InputStream stream) {
        return new BinaryValue(stream);
    }

    /**
     * Converts a {@link Calendar} into a {@link Value}
     * @param calendar the calendar
     * @return the value
     */
    @Converter
    public Value toValue(Calendar calendar) {
        return new DateValue(calendar);
    }

    /**
     * Converts a {@link String} into a {@link Value}
     * @param value the string
     * @return the value
     */
    @Converter
    public Value toValue(String value) {
        return new StringValue(value);
    }

}
