package org.apache.camel.testng;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.apache.camel.spring.SpringCamelContext;

/**
 * A helper base class for running Camel based test cases using TestNG which makes it easy to overload
 * system properties before the spring application context is initialised; to allow a single spring XML to be reused
 * with some properties being overloaded.
 *
 * @version $Revision: 667435 $
 */
public class SpringRunner {
    private Properties oldSystemProperties;
    private AbstractXmlApplicationContext applicationContext;
    private SpringCamelContext camelContext;

    protected void assertApplicationContextStarts(String applicationContextLocations, Properties properties) throws Exception {
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            System.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }

        applicationContext = new ClassPathXmlApplicationContext(applicationContextLocations);
        applicationContext.start();

        camelContext = SpringCamelContext.springCamelContext(applicationContext);
    }

    protected SpringCamelContext getCamelContext() {
        return camelContext;
    }

    /**
     * Creates a properties object with the given key and value 
     */
    protected Properties createProperties(String name, String value) {
        Properties properties = new Properties();
        properties.setProperty(name, value);
        return properties;
    }

    @BeforeTest
    protected void setUp() {
        oldSystemProperties = new Properties(System.getProperties());
    }

    @AfterTest
    protected void tearDown() {
        if (applicationContext != null) {
            applicationContext.close();
        }
        System.setProperties(oldSystemProperties);
    }

}
