package org.apache.camel.component.flatpack;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import net.sf.flatpack.Parser;
import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.util.ExchangeHelper;
import org.springframework.core.io.Resource;

/**
 * @version $Revision: 1.1 $
 */
public class DelimitedEndpoint extends FixedLengthEndpoint {
    private char delimiter = ',';
    private char textQualifier = '"';
    private boolean ignoreFirstRecord = true;

    public DelimitedEndpoint(String uri, Resource resource) {
        super(uri, resource);
    }

    public Parser createParser(Exchange exchange) throws InvalidPayloadException, IOException {
        Reader bodyReader = ExchangeHelper.getMandatoryInBody(exchange, Reader.class);
        Resource resource = getResource();
        if (resource == null) {
            return getParserFactory().newDelimitedParser(bodyReader, delimiter, textQualifier);
        } else {
            return getParserFactory().newDelimitedParser(new InputStreamReader(resource.getInputStream()), bodyReader, delimiter, textQualifier, ignoreFirstRecord);
        }
    }



    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isIgnoreFirstRecord() {
        return ignoreFirstRecord;
    }

    public void setIgnoreFirstRecord(boolean ignoreFirstRecord) {
        this.ignoreFirstRecord = ignoreFirstRecord;
    }

    public char getTextQualifier() {
        return textQualifier;
    }

    public void setTextQualifier(char textQualifier) {
        this.textQualifier = textQualifier;
    }

}
