package org.apache.camel.dataformat.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

import org.apache.camel.Converter;

/**
 * HL7 converters.
 */
@Converter
public final class HL7Converter {

    private HL7Converter() {
    }

    @Converter
    public static String toString(Message message) throws HL7Exception {
        Parser parser = new PipeParser();
        String encoded = parser.encode(message);
        return encoded;
    }

    @Converter
    public static Message toMessage(String body) throws HL7Exception {
        body = body.replace('\n', '\r');

        Parser parser = new PipeParser();
        Message message = parser.parse(body);
        return message;
    }

}
