package org.apache.camel.component.log;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.converter.ObjectConverter;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.ProcessorEndpoint;
import org.apache.camel.model.LoggingLevel;
import org.apache.camel.processor.Logger;
import org.apache.camel.processor.ThroughputLogger;
import org.apache.camel.util.IntrospectionSupport;

/**
 * to log message exchanges to the underlying logging mechanism.
 *
 * @version $Revision: 710133 $
 */
public class LogComponent extends DefaultComponent<Exchange> {

    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        LoggingLevel level = getLoggingLevel(parameters);
        Integer groupSize = getAndRemoveParameter(parameters, "groupSize", Integer.class);

        Logger logger;
        if (groupSize != null) {
            logger = new ThroughputLogger(remaining, level, ObjectConverter.toInteger(groupSize));
        } else {
            LogFormatter formatter = new LogFormatter();
            IntrospectionSupport.setProperties(formatter, parameters);

            logger = new Logger(remaining);
            logger.setLevel(level);
            logger.setFormatter(formatter);
        }

        return new ProcessorEndpoint(uri, this, logger);
    }

    /**
     * Gets the logging level, will default to use INFO if no level parameter provided.
     */
    protected LoggingLevel getLoggingLevel(Map parameters) {
        String levelText = getAndRemoveParameter(parameters, "level", String.class, "INFO");
        return LoggingLevel.valueOf(levelText.toUpperCase());
    }

}
