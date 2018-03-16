package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.spi.DataFormat;

/**
 *
 * @version $Revision: 711235 $
 */
@XmlRootElement(name = "hl7")
@XmlAccessorType(XmlAccessType.FIELD)
public class HL7DataFormat extends DataFormatType {

    public HL7DataFormat() {
        super("org.apache.camel.dataformat.hl7.HL7DataFormat");
    }

}
