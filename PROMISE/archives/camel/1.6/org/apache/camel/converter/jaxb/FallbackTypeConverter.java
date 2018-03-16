package org.apache.camel.converter.jaxb;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;

import org.apache.camel.Exchange;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.TypeConverter;
import org.apache.camel.converter.stream.StreamCache;
import org.apache.camel.spi.TypeConverterAware;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $Revision: 740308 $
 */
public class FallbackTypeConverter implements TypeConverter, TypeConverterAware {
    private static final transient Log LOG = LogFactory.getLog(FallbackTypeConverter.class);
    private TypeConverter parentTypeConverter;
    private boolean prettyPrint = true;

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public void setTypeConverter(TypeConverter parentTypeConverter) {
        this.parentTypeConverter = parentTypeConverter;
    }

    public <T> T convertTo(Class<T> type, Object value) {
        try {
            if (isJaxbType(type)) {
                return unmarshall(type, value);
            }
            if (value != null) {
                if (isJaxbType(value.getClass()) && isNotStreamCacheType(type)) {
                    return marshall(type, value);
                }
            }
            return null;
        } catch (JAXBException e) {
            throw new RuntimeCamelException(e);
        }
    }

    private <T> boolean isNotStreamCacheType(Class<T> type) {
        return !StreamCache.class.isAssignableFrom(type);
    }

    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) {
        return convertTo(type, value);
    }

    protected <T> boolean isJaxbType(Class<T> type) {
        XmlRootElement element = type.getAnnotation(XmlRootElement.class);
        boolean jaxbType = element != null;
        return jaxbType;
    }

    /**
     * Lets try parse via JAXB
     */
    protected <T> T unmarshall(Class<T> type, Object value) throws JAXBException {
        JAXBContext context = createContext(type);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        if (parentTypeConverter != null) {
            InputStream inputStream = parentTypeConverter.convertTo(InputStream.class, value);
            if (inputStream != null) {
                Object unmarshalled = unmarshal(unmarshaller, inputStream);
                return type.cast(unmarshalled);
            }
            Reader reader = parentTypeConverter.convertTo(Reader.class, value);
            if (reader != null) {
                Object unmarshalled = unmarshal(unmarshaller, reader);
                return type.cast(unmarshalled);
            }
            Source source = parentTypeConverter.convertTo(Source.class, value);
            if (source != null) {
                Object unmarshalled = unmarshal(unmarshaller, source);
                return type.cast(unmarshalled);
            }
        }

        if (value instanceof String) {
            value = new StringReader((String) value);
        }
        if (value instanceof InputStream || value instanceof Reader) {
            Object unmarshalled = unmarshal(unmarshaller, value);
            return type.cast(unmarshalled);
        }

        return null;
    }

    protected <T> T marshall(Class<T> type, Object value) throws JAXBException {
        if (parentTypeConverter != null) {
            JAXBContext context = createContext(value.getClass());
            JAXBSource source = new JAXBSource(context, value);
            try {
                return parentTypeConverter.convertTo(type, source);
            } catch (NoTypeConversionAvailableException e) {
                StringWriter buffer = new StringWriter();
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isPrettyPrint() ? Boolean.TRUE : Boolean.FALSE);
                marshaller.marshal(value, buffer);
                return parentTypeConverter.convertTo(type, buffer.toString());
            }
        }

        return null;
    }

    /**
     * Unmarshals the given value with the unmarshaller
     *
     * @param unmarshaller  the unmarshaller
     * @param value  the stream to unmarshal (will close it after use, also if exception is thrown)
     * @return  the value
     * @throws JAXBException is thrown if an exception occur while unmarshalling
     */
    protected Object unmarshal(Unmarshaller unmarshaller, Object value) throws JAXBException {
        try {
            if (value instanceof InputStream) {
                return unmarshaller.unmarshal((InputStream) value);
            } else if (value instanceof Reader) {
                return unmarshaller.unmarshal((Reader) value);
            } else if (value instanceof Source) {
                return unmarshaller.unmarshal((Source) value);
            }
        } finally {
            if (value instanceof Closeable) {
                ObjectHelper.close((Closeable) value, "Unmarshalling", LOG);
            }
        }
        return null;
    }

    protected <T> JAXBContext createContext(Class<T> type) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(type);
        return context;
    }
}
