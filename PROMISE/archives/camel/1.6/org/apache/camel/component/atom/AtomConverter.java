package org.apache.camel.component.atom;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.Converter;

/**
 * Date converters.
 */
@Converter
public final class AtomConverter {

    public static final String DATE_PATTERN_NO_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss";

    private AtomConverter() {
    }

    @Converter
    public static Date toDate(String text) throws ParseException {
        DateFormat sdf = new SimpleDateFormat(DATE_PATTERN_NO_TIMEZONE);
        return sdf.parse(text);
    }

}
