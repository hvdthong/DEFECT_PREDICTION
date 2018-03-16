package org.apache.camel.component.flatpack;

import net.sf.flatpack.DefaultParserFactory;
import net.sf.flatpack.Parser;
import net.sf.flatpack.ParserFactory;
import net.sf.flatpack.DataSet;
import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ObjectHelper;
import org.springframework.core.io.Resource;

import java.io.*;

/**
 * @version $Revision: 1.1 $
 */
public class FlatpackDataFormat implements DataFormat {
    private char delimiter = ',';
    private char textQualifier = '"';
    private boolean ignoreFirstRecord = true;
    private Resource definition;
    private boolean fixed = false;
    private ParserFactory parserFactory = DefaultParserFactory.getInstance();

    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
        throw new UnsupportedOperationException("marshal() not implemented yet!");
    }

    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        InputStreamReader reader = new InputStreamReader(stream);
        Parser parser = createParser(exchange, reader);
        DataSet dataSet = parser.parse();
        return new DataSetList(dataSet);
    }


    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
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


    public Resource getDefinition() {
        return definition;
    }

    public void setDefinition(Resource definition) {
        this.definition = definition;
    }

    public ParserFactory getParserFactory() {
        return parserFactory;
    }

    public void setParserFactory(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }


    protected Parser createParser(Exchange exchange, Reader bodyReader) throws IOException {
        if (isFixed()) {
            Resource resource = getDefinition();
            ObjectHelper.notNull(resource, "resource property");
            return getParserFactory().newFixedLengthParser(new InputStreamReader(resource.getInputStream()), bodyReader);
        } else {
            Resource resource = getDefinition();
            if (resource == null) {
                return getParserFactory().newDelimitedParser(bodyReader, delimiter, textQualifier);
            } else {
                return getParserFactory().newDelimitedParser(new InputStreamReader(resource.getInputStream()), bodyReader, delimiter, textQualifier, ignoreFirstRecord);
            }
        }
    }

}
