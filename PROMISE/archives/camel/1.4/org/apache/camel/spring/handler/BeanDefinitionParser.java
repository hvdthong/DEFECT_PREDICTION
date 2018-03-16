package org.apache.camel.spring.handler;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.core.Conventions;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


/**
 * A base class for a parser for a bean.
 *
 * @version $Revision: 641676 $
 */
public class BeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private Class type;

    protected BeanDefinitionParser() {
    }

    public BeanDefinitionParser(Class type) {
        this.type = type;
    }

    protected Class getBeanClass(Element element) {
        if (type == null) {
            type = loadType();
        }
        return type;
    }

    protected Class loadType() {
        throw new IllegalArgumentException("No type specified!");
    }

    protected boolean isEligibleAttribute(String attributeName) {
        return attributeName != null && !ID_ATTRIBUTE.equals(attributeName)
                && !attributeName.equals("xmlns") && !attributeName.startsWith("xmlns:");
    }


    /**
     * Parse the supplied {@link Element} and populate the supplied
     * {@link BeanDefinitionBuilder} as required.
     * <p>This implementation maps any attributes present on the
     * supplied element to {@link org.springframework.beans.PropertyValue}
     * instances, and
     * {@link BeanDefinitionBuilder#addPropertyValue(String, Object) adds them}
     * to the
     * {@link org.springframework.beans.factory.config.BeanDefinition builder}.
     * <p>The {@link #extractPropertyName(String)} method is used to
     * reconcile the name of an attribute with the name of a JavaBean
     * property.
     *
     * @param element the XML element being parsed
     * @param builder used to define the <code>BeanDefinition</code>
     * @see #extractPropertyName(String)
     */
    protected final void doParse(Element element, BeanDefinitionBuilder builder) {
        NamedNodeMap attributes = element.getAttributes();
        for (int x = 0; x < attributes.getLength(); x++) {
            Attr attribute = (Attr) attributes.item(x);
            String name = attribute.getLocalName();
            String fullName = attribute.getName();
            if (!fullName.startsWith("xmlns:") && !fullName.equals("xmlns") && isEligibleAttribute(name)) {
                String propertyName = extractPropertyName(name);
                Assert.state(StringUtils.hasText(propertyName),
                        "Illegal property name returned from 'extractPropertyName(String)': cannot be null or empty.");
                builder.addPropertyValue(propertyName, attribute.getValue());
            }
        }
        postProcess(builder, element);
    }


    /**
     * Extract a JavaBean property name from the supplied attribute name.
     * <p>The default implementation uses the
     * {@link Conventions#attributeNameToPropertyName(String)}
     * method to perform the extraction.
     * <p>The name returned must obey the standard JavaBean property name
     * conventions. For example for a class with a setter method
     * '<code>setBingoHallFavourite(String)</code>', the name returned had
     * better be '<code>bingoHallFavourite</code>' (with that exact casing).
     *
     * @param attributeName the attribute name taken straight from the
     *                      XML element being parsed (never <code>null</code>)
     * @return the extracted JavaBean property name (must never be <code>null</code>)
     */
    protected String extractPropertyName(String attributeName) {
        return Conventions.attributeNameToPropertyName(attributeName);
    }

    /**
     * Hook method that derived classes can implement to inspect/change a
     * bean definition after parsing is complete.
     * <p>The default implementation does nothing.
     *
     * @param beanDefinition the parsed (and probably totally defined) bean definition being built
     * @param element        the XML element that was the source of the bean definition's metadata
     */
    protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {

    }

}
