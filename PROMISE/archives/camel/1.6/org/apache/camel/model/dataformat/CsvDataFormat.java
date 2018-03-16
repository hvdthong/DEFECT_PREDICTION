package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.spi.DataFormat;

/**
 * Represents a CSV (Comma Separated Values) {@link DataFormat}
 *
 * @version $Revision: 686463 $
 */
@XmlRootElement(name = "csv")
@XmlAccessorType(XmlAccessType.FIELD)
public class CsvDataFormat extends DataFormatType {

    public CsvDataFormat() {
        super("org.apache.camel.dataformat.csv.CsvDataFormat");
    }

}
