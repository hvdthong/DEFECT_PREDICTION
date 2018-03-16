package org.apache.camel.builder;

import org.apache.camel.model.ProcessorType;
import org.apache.camel.model.dataformat.ArtixDSContentType;
import org.apache.camel.model.dataformat.ArtixDSDataFormat;
import org.apache.camel.model.dataformat.DataFormatType;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.model.dataformat.SerializationDataFormat;
import org.apache.camel.model.dataformat.XMLBeansDataFormat;
import org.apache.camel.spi.DataFormat;

/**
 * An expression for constructing the different possible {@link DataFormat}
 * options.
 *
 * @version $Revision: 1.1 $
 */
public class DataTypeExpression<T extends ProcessorType> {
    private final ProcessorType<T> processorType;
    private final Operation operation;

    public enum Operation {
        Marshal, Unmarshal
    };

    public DataTypeExpression(ProcessorType<T> processorType, Operation operation) {
        this.processorType = processorType;
        this.operation = operation;
    }

    /**
     * Uses the Java Serialization data format
     */
    public T serialization() {
        return dataFormat(new SerializationDataFormat());
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
     * Uses the JAXB data format
     */
    public T xmlBeans() {
        return dataFormat(new XMLBeansDataFormat());
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

    private T dataFormat(DataFormatType dataFormatType) {
        switch (operation) {
            case Unmarshal:
                return processorType.unmarshal(dataFormatType);
            case Marshal:
                return processorType.marshal(dataFormatType);
            default:
                throw new IllegalArgumentException("Unknown value: " + operation);
        }
    }

}
