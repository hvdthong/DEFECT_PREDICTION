package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.ArtixDSDataFormat;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.dataformat.DataFormatType;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.model.dataformat.SerializationDataFormat;
import org.apache.camel.model.dataformat.StringDataFormat;
import org.apache.camel.model.dataformat.XMLBeansDataFormat;
import org.apache.camel.processor.UnmarshalProcessor;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.RouteContext;

/**
 * Unmarshals the binary payload using the given {@link DataFormatType}
 *
 * @version $Revision: 671918 $
 */
@XmlRootElement(name = "unmarshal")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnmarshalType extends OutputType<ProcessorType> {
    @XmlAttribute(required = false)
    private String ref;
    @XmlElements({
    @XmlElement(required = false, name = "artixDS", type = ArtixDSDataFormat.class),
    @XmlElement(required = false, name = "csv", type = CsvDataFormat.class),
    @XmlElement(required = false, name = "jaxb", type = JaxbDataFormat.class),
    @XmlElement(required = false, name = "serialization", type = SerializationDataFormat.class),
    @XmlElement(required = false, name = "string", type = StringDataFormat.class),
    @XmlElement(required = false, name = "xmlBeans", type = XMLBeansDataFormat.class)}
    )
    private DataFormatType dataFormatType;

    public UnmarshalType() {
    }

    public UnmarshalType(DataFormatType dataFormatType) {
        this.dataFormatType = dataFormatType;
    }

    public UnmarshalType(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        if (dataFormatType != null) {
            return "Marshal[" + dataFormatType + "]";
        } else {
            return "Marshal[ref:  " + ref + "]";
        }
    }

    @Override
    public String getShortName() {
        return "unmarshal";
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public DataFormatType getDataFormatType() {
        return dataFormatType;
    }

    public void setDataFormatType(DataFormatType dataFormatType) {
        this.dataFormatType = dataFormatType;
    }

    @Override
    public Processor createProcessor(RouteContext routeContext) {
        DataFormat dataFormat = DataFormatType.getDataFormat(routeContext, getDataFormatType(), ref);
        return new UnmarshalProcessor(dataFormat);
    }
}
