package org.apache.camel.model.dataformat;


import java.util.zip.Deflater;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.RouteContext;

@XmlRootElement(name = "zip")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZipDataFormat extends DataFormatType {

    @XmlAttribute(required = false)
    private int compressionLevel = Deflater.BEST_SPEED;
    
    public ZipDataFormat() {
    }

    public ZipDataFormat(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    @Override
    protected DataFormat createDataFormat(RouteContext routeContext) {
        return new org.apache.camel.impl.ZipDataFormat(compressionLevel);
    }

    public int getCompressionLevel() {
        return compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }
}
