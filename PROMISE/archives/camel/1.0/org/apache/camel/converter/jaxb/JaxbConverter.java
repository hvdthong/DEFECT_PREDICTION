package org.apache.camel.converter.jaxb;

import org.apache.camel.converter.HasAnnotation;
import org.apache.camel.converter.jaxp.XmlConverter;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.util.JAXBSource;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @version $Revision: 525892 $
 */
public class JaxbConverter {
    private XmlConverter jaxbConverter;

    public XmlConverter getJaxbConverter() {
        if (jaxbConverter == null) {
            jaxbConverter = new XmlConverter();
        }
        return jaxbConverter;
    }

    public void setJaxbConverter(XmlConverter jaxbConverter) {
        this.jaxbConverter = jaxbConverter;
    }

    public static JAXBSource toSource(@HasAnnotation(XmlRootElement.class)Object value) throws JAXBException {
        JAXBContext context = createJaxbContext(value);
        return new JAXBSource(context, value);
    }

    public Document toDocument(@HasAnnotation(XmlRootElement.class)Object value) throws JAXBException, ParserConfigurationException {
        JAXBContext context = createJaxbContext(value);
        Marshaller marshaller = context.createMarshaller();

        Document doc = getJaxbConverter().createDocument();
        marshaller.marshal(value, doc);
        return doc;
    }

    protected static JAXBContext createJaxbContext(Object value) throws JAXBException {
        if (value == null) {
            throw new IllegalArgumentException("Cannot convert from null value to JAXBSource");
        }
        JAXBContext context = JAXBContext.newInstance(value.getClass());
        return context;
    }

/*
    public void write(OutputStream out, Object value) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(value.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(value, out);
    }
*/
}
