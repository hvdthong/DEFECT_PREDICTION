package org.apache.camel.builder;

import org.apache.camel.model.ProcessorType;
import org.apache.camel.model.dataformat.ArtixDSContentType;
import org.apache.camel.model.dataformat.ArtixDSDataFormat;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.dataformat.DataFormatType;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.model.dataformat.SerializationDataFormat;
import org.apache.camel.model.dataformat.StringDataFormat;
import org.apache.camel.model.dataformat.XMLBeansDataFormat;
import org.apache.camel.model.dataformat.XStreamDataFormat;
import org.apache.camel.spi.DataFormat;

/**
 * An expression for constructing the different possible {@link DataFormat}
 * options.
 *
 * @version $Revision: 659760 $
 */
public class DataFormatClause<T extends ProcessorType> {
    private final T processorType;
    private final Operation operation;

    /**
     * {@link DataFormat} operations.
     */
    public enum Operation {
        Marshal, Unmarshal
    }

    public DataFormatClause(T processorType, Operation operation) {
        this.processorType = processorType;
        this.operation = operation;
    }

    /**
     * Uses the
     * data format for dealing with lots of different message formats such as SWIFT etc.
     */
    public T artixDS() {
        return dataFormat(new ArtixDSDataFormat());
    }

    /**
     * Uses the
     * data format with the specified type of ComplexDataObject
     * for marshalling and unmarshalling messages using the dataObject's default Source and Sink.
     */
    public T artixDS(Class<?> dataObjectType) {
        return dataFormat(new ArtixDSDataFormat(dataObjectType));
    }


    /**
     * Uses the
     * data format with the specified type of ComplexDataObject
     * for marshalling and unmarshalling messages using the dataObject's default Source and Sink.
     */
    public T artixDS(Class<?> elementType, ArtixDSContentType contentType) {
        return dataFormat(new ArtixDSDataFormat(elementType, contentType));
    }

    /**
     * Uses the
     * data format with the specified content type
     * for marshalling and unmarshalling messages
     */
    public T artixDS(ArtixDSContentType contentType) {
        return dataFormat(new ArtixDSDataFormat(contentType));
    }

    /**
     * Uses the CSV data format
     */
    public T csv() {
        return dataFormat(new CsvDataFormat());
    }

    /**
     * Uses the JAXB data format
     */
    public T jaxb() {
        return dataFormat(new JaxbDataFormat());
    }

    /**
     * Uses the JAXB data format turning pretty printing on or off
     */
    public T jaxb(boolean prettyPrint) {
        return dataFormat(new JaxbDataFormat(prettyPrint));
    }

    /**
     * Uses the Java Serialization data format
     */
    public T serialization() {
        return dataFormat(new SerializationDataFormat());
    }

    /**
     * Uses the String data format
     */
    public T string() {
        return string(null);
    }

    /**
     * Uses the String data format supporting encoding using given charset
     */
    public T string(String charset) {
        StringDataFormat sdf = new StringDataFormat();
        sdf.setCharset(charset);
        return dataFormat(sdf);
    }

    /**
     * Uses the JAXB data format
     */
    public T xmlBeans() {
        return dataFormat(new XMLBeansDataFormat());
    }

    /**
     * Uses the XStream data format
     */
    public T xstream() {
        return dataFormat(new XStreamDataFormat());
    }

    private T dataFormat(DataFormatType dataFormatType) {
        switch (operation) {
        case Unmarshal:
            return (T)processorType.unmarshal(dataFormatType);
        case Marshal:
            return (T)processorType.marshal(dataFormatType);
        default:
            throw new IllegalArgumentException("Unknown DataFormat operation: " + operation);
        }
    }

}
