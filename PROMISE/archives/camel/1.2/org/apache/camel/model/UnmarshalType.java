package org.apache.camel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.camel.Processor;
import org.apache.camel.impl.RouteContext;
import org.apache.camel.model.dataformat.DataFormatType;
import org.apache.camel.processor.UnmarshalProcessor;
import org.apache.camel.spi.DataFormat;
import static org.apache.camel.util.ObjectHelper.notNull;

/**
 * Unmarshals the binary payload using the given {@link DataFormatType}
 * 
 * @version $Revision: 1.1 $
 */
@XmlRootElement(name = "unmarshal")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnmarshalType extends OutputType {
    @XmlAttribute(required = false)
    private String ref;
    @XmlElementRef
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
        }
        else {
            return "Marshal[ref:  " + ref + "]";
        }
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
        DataFormatType type = getDataFormatType();
        if (type == null) {
            notNull(ref, "ref or dataFormatType");
            type = routeContext.lookup(ref, DataFormatType.class);
        }
        DataFormat dataFormat = type.getDataFormat(routeContext);
        return new UnmarshalProcessor(dataFormat);
    }
}
