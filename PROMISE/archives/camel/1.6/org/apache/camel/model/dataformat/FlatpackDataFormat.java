package org.apache.camel.model.dataformat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.spi.DataFormat;

/**
 * 
 * @version $Revision: 711235 $
 */
@XmlRootElement(name = "flatpack")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlatpackDataFormat extends DataFormatType {

    public FlatpackDataFormat() {
        super("org.apache.camel.component.flatpack.FlatpackDataFormat");
    }

}
